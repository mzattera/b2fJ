
#include "types.h"
#include "trace.h"
#include "platform_hooks.h"
#include "constants.h"
#include "specialsignatures.h"
#include "specialclasses.h"
#include "threads.h"
#include "classes.h"
#include "language.h"
#include "configure.h"
#include "interpreter.h"
#include "memory.h"
#include "exceptions.h"
#include "stack.h"

#if DEBUG_MY_MAIN
extern byte javaClassFileContent[];
#endif

#define NO_OWNER 0x00

#define get_stack_frame() ((StackFrame *) (currentThread->currentStackFrame))

/**
 * Thread currently being executed by engine(). Threads exist in an
 * intrinsic circular list.
 */
Thread* currentThread;

/**
 * Priority queue of threads. Entry points at the last thread in the queue.
 */
Thread *threadQ[10];

/**
 * Thread id generator, always increasing.
 */
byte gThreadCounter;

/**
 * Current program number, i.e. number of 'main()'s hanging around
 */
byte gProgramNumber;

StackFrame *current_stackframe()
{
  byte arraySize;

  arraySize = currentThread->stackFrameArraySize;
  if (arraySize == 0)
    return null;
  return stackframe_array() + (arraySize - 1);
}

void update_stack_frame (StackFrame *stackFrame)
{
  stackFrame->stackTop = stackTop;
  stackFrame->pc = pc;
}  

void update_registers (StackFrame *stackFrame)
{
  pc = stackFrame->pc;
  stackTop = stackFrame->stackTop;
  localsBase = stackFrame->localsBase;
}

/* Turns out inlines aren't really inlined.
__INLINED byte get_thread_id (Object *obj)
{
  return obj->threadId;
}

__INLINED void set_thread_id (Object *obj, byte threadId)
{
  obj->threadId = threadId;
}

__INLINED void inc_monitor_count (Object *obj)
{
  obj->monitorCount++;
}

__INLINED void set_monitor_count (Object *obj, byte count)
{
  obj->monitorCount = count;
}
*/

#define get_thread_id(obj) ((obj)->threadId)
#define set_thread_id(obj,_threadId) ((obj)->threadId = (_threadId))
#define inc_monitor_count(obj) ((obj)->monitorCount++)
#define set_monitor_count(obj,count) ((obj)->monitorCount = (count))

/**
 * Allocate stack frames
 * Allocate ID
 * Insert into run list
 * Mark thread as STARTED
 */
boolean init_thread (Thread *thread)
{
  thread->threadId = gThreadCounter + 1;
  
  // Catch the primordial thread
  if (currentThread == null)
    thread->priority = NORM_PRIORITY;
  
  #if DEBUG_THREADS
  printf("Setting intial priority to %d\n", thread->priority);
  #endif

  if (thread->state != NEW)
  {
    throw_exception(illegalStateException);
    return false;
  }
  
  // Allocate space for stack frames.
  thread->stackFrameArray = ptr2word (new_primitive_array (T_STACKFRAME, INITIAL_STACK_FRAMES));
  if (thread->stackFrameArray == JNULL)
    return false;
    
  // Allocate actual stack storage (INITIAL_STACK_SIZE * 4 bytes)
  thread->stackArray = ptr2word (new_primitive_array (T_INT, INITIAL_STACK_SIZE));
  if (thread->stackArray == JNULL)
  {
    free_array (ref2obj(thread->stackFrameArray));
    thread->stackFrameArray = JNULL;
    return false;    
  }
  
  gThreadCounter++;
  
  #ifdef VERIFY
  assert (is_array (word2obj (thread->stackFrameArray)), THREADS0);
  assert (is_array (word2obj (thread->stackArray)), THREADS1);
  #endif

  thread->stackFrameArraySize = 0;
  thread->state = STARTED;
  if (currentThread == null)
    currentThread = thread;
    
  enqueue_thread(thread);

  return true;
}

/**
 * Switches to next thread:
 *
 * do
 *   get next thread
 *   if waiting, grab monitor and run
 *   if sleeping and timer expired, run
 *   if DEAD, clean up and use current thread
 *   if started, initialize and run
 * until selected thread can run
 *  
 * @return false iff there are no live threads
 *         to switch to.
 */
 
boolean switch_thread()
{
  Thread *anchorThread, *previousThread, *candidate;
  Thread **pThreadQ;
  boolean nonDaemonRunnable = false;
  StackFrame *stackFrame = null;
  short i;

  #if DEBUG_THREADS || DEBUG_BYTECODE
  printf ("------ switch_thread: currentThread at %d\n", (int) currentThread);
  #endif

  if (currentThread != null)
  {
    // Only current threads can die. Tidy up dead threads
    if (currentThread->state == DEAD)
    {
      #if DEBUG_THREADS
      printf ("Tidying up DEAD thread %d: %d\n", (int) currentThread, (int)currentThread->threadId);
      #endif
  
      #if REMOVE_DEAD_THREADS
      // This order of deallocation is actually crucial to avoid leaks
      free_array ((Object *) word2ptr (currentThread->stackArray));
      free_array ((Object *) word2ptr (currentThread->stackFrameArray));

      #ifdef SAFE
      currentThread->stackFrameArray = JNULL;
      currentThread->stackArray = JNULL;
      #endif // SAFE
      #endif // REMOVE_DEAD_THREADS
    
      // Remove thread from queue.
      dequeue_thread(currentThread);
    }
    else { // Save context information
      stackFrame = current_stackframe();

#if DEBUG_THREADS
      printf ("switchThread: current stack frame: %d\n", (int) stackFrame);
#endif
  
      if (stackFrame != null) {
        update_stack_frame (stackFrame);
      }
    }
  }

  currentThread = null;
  
  // Loop until a frame is found that can be made to run.
  for (i=MAX_PRIORITY-1; i >= 0; i--) {
    pThreadQ = &threadQ[i];

    previousThread = anchorThread = *pThreadQ;
    if (!previousThread)
      continue;

    do
    {
      candidate = word2ptr(previousThread->nextThread);

      #if DEBUG_THREADS
      printf ("Checking state of thread %d(%d)(s=%d,p=%d,i=%d,d=%d)\n",
      	(int)candidate,
      	(int)candidate->threadId,
      	(int)candidate->state,
      	(int)candidate->priority,
      	(int)candidate->interruptState,
      	(int)candidate->daemon
             );
      #endif
      
      // See if we can move a thread to the running state. Used to not do this if we
      // had already found one, but turns out to be easiest if we are avoiding
      // priority inversion.
      switch (candidate->state)
      {
        case CONDVAR_WAITING:
          // We are waiting to be notified
          if ((candidate->sleepUntil > 0) && (get_sys_time() >= (FOURBYTES) candidate->sleepUntil))
          {
#if DEBUG_MONITOR
      printf ("Waking up waiting thread %d: %d\n", (int) candidate, candidate->threadId);
#endif
            // We will drop through to mon waiting.
          }
          else if (candidate->interruptState == INTERRUPT_CLEARED)
            break;
          else
            candidate->interruptState = INTERRUPT_GRANTED;

          // candidate->state = MON_WAITING;
          // drop through
        case MON_WAITING:
          {
            Object *pObj = word2obj(candidate->waitingOn);
            byte threadId = get_thread_id(pObj);

            // We are waiting to enter a synchronized block
            #ifdef VERIFY
            assert (pObj != JNULL, THREADS6);
            #endif
             
            if (threadId == NO_OWNER)
            {
              // NOW enter the monitor (guaranteed to succeed)
              enter_monitor(candidate, pObj);
              
              // Set the monitor depth to whatever was saved.
              set_monitor_count(pObj, candidate->monitorCount);
              
              // Let the thread run.
              candidate->state = RUNNING;
  
              #ifdef SAFE
              candidate->waitingOn = JNULL;
              #endif
            }
#if PI_AVOIDANCE
            // Only avoid priority inversion if we don't already have a thread to run.
            else if (currentThread == null)
            {
            	Thread *pOwner;
            	int j;
            	
            	// Track down who owns this monitor and run them instead.            	
            	// Could be 'waiting' in a native method, or we could be deadlocked!
find_next:
            	if (candidate->threadId != threadId)
            	{
                    for (j=MAX_PRIORITY-1; j >= 0; j--)
                    {
                      pOwner = threadQ[j];
                      if (!pOwner)
                        continue;
                        
                      do {
                        // Remember threadQ[j] is the last thread on the queue
                        pOwner = word2ptr(pOwner->nextThread);
                        if (pOwner->threadId == threadId)
                        {
            		      if (pOwner->state == RUNNING)
            		      {
            			    currentThread = pOwner;
            			    goto done_pi;
            			  }
            			  
            		      // if owner is waiting too, iterate down.
            		      if (pOwner->state == MON_WAITING)
            		      {
            			    threadId = get_thread_id(word2obj(pOwner->waitingOn));
            			    if (threadId != NO_OWNER)
            				  goto find_next;
            		      }
                        }
                      } while (pOwner != threadQ[j]);
                    }                   
                    // If we got here, we're in trouble, just drop through.
            	}
            }
done_pi:
		break;
	    ;
#endif // PI_AVOIDANCE
          
          }
          break;
        case SLEEPING:
          if (candidate->interruptState != INTERRUPT_CLEARED
              || (get_sys_time() >= (FOURBYTES) candidate->sleepUntil))
          {
      #if DEBUG_THREADS
      printf ("Waking up sleeping thread %d: %d\n", (int) candidate, candidate->threadId);
      #endif
            candidate->state = RUNNING;
            if (candidate->interruptState != INTERRUPT_CLEARED)
          	candidate->interruptState = INTERRUPT_GRANTED;
            #ifdef SAFE
  	    candidate->sleepUntil = JNULL;
            #endif // SAFE
          }
          break;
        case STARTED:
          if (currentThread == null)
          {      
            // Put stack ptr at the beginning of the stack so we can push arguments
            // to entry methods. This assumes set_top_word or set_top_ref will
            // be called immediately below.
        #if DEBUG_THREADS
        printf ("Starting thread %d: %d\n", (int) candidate, candidate->threadId);
        #endif
            currentThread = candidate;	// Its just easier this way.
            init_sp_pv();
            candidate->state = RUNNING;
            if (candidate == bootThread)
            {
              MethodRecord *mRec;
              ClassRecord *classRecord;

#if DEBUG_MY_MAIN
			  printf("javaClassFileContent %5d\n", (int)javaClassFileContent);
			  printf("installedBinary %5d\n", (int)installedBinary);
			  printf("get_binary_base() %5d\n", (int)get_binary_base());
			  printf("get_master_record()->entryClassesOffset %5d\n", (int)get_master_record()->entryClassesOffset);
			  printf("get_entry_classes_base() %5d\n", (int)get_entry_classes_base());
			  printf("get_entry_class(%d) %5d\n", gProgramNumber, (int)get_entry_class(gProgramNumber));
			  printf("sizeof(MasterRecord) %5d\n", (int)sizeof(MasterRecord));
			  printf("get_class_base() %5d\n", (int)get_class_base());
			  printf("get_class_record(%d) %5d\n", get_entry_class(gProgramNumber), (int)get_class_record(get_entry_class(gProgramNumber)));
			  getc(stdin);
#endif

              classRecord = get_class_record (get_entry_class (gProgramNumber));
              // Initialize top word with fake parameter for main():
              set_top_ref (JNULL);
              // Push stack frame for main method:
              mRec= find_method (classRecord, main_4_1Ljava_3lang_3String_2_5V);

			  #if DEBUG_MY_MAIN
			  printMethodRecord(mRec);
			  #endif

              dispatch_special (mRec, null);
              // Push another if necessary for the static initializer:
              dispatch_static_initializer (classRecord, pc);
            }
            else
            {
              set_top_ref (ptr2word (candidate));
              dispatch_virtual ((Object *) candidate, run_4_5V, null);
            }
            // The following is needed because the current stack frame
            // was just created
            stackFrame = current_stackframe();
            update_stack_frame (stackFrame);
          }
          break;
        case RUNNING:
          // Its running already
        case DEAD:
          // Dead threads should be handled earlier
        default:
          // ???
          break;
      }

      // Do we now have a thread we want to run?
      // Note we may later decide not to if all non-daemon threads have died        
      if (currentThread == null && candidate->state == RUNNING)
      {
        currentThread = candidate;
        // Move thread to end of queue
        *pThreadQ = candidate;
      }
      
      if (!candidate->daemon)
      {
      	// May or may not be running but it could do at some point
#if DEBUG_THREADS
printf ("Found a non-daemon thread %d: %d(%d)\n", (int) candidate, (int)candidate->threadId, (int) candidate->state);
#endif
           nonDaemonRunnable = true;
      }
      
      #if DEBUG_THREADS
      printf ("switch_thread: done processing thread %d: %d\n", (int) candidate,
              (int) (candidate->state == RUNNING));
      #endif

      // Always use the first running thread as the thread
      // Keep looping: cull dead threads, check there's at least one non-daemon thread
      previousThread = candidate;
    } while (candidate != anchorThread);
  } // end for
  
#if DEBUG_THREADS
printf ("currentThread=%d, ndr=%d\n", (int) currentThread, (int)nonDaemonRunnable);
#endif

#if DEBUG_THREADS
  printf ("Leaving switch_thread()\n");
#endif
  if (nonDaemonRunnable)
  {
    // There is at least one non-daemon thread left alive
    if (currentThread != null)
    {
      // If we found a running thread and there is at least one
      // non-daemon thread left somewhere in the queue...
      #if DEBUG_THREADS
      printf ("Current thread is %d: %d(%d)\n", (int) currentThread, (int)currentThread->threadId, (int) currentThread->state);
      printf ("getting current stack frame...\n");
      #endif
    
      stackFrame = current_stackframe();
    
      #if DEBUG_THREADS
      printf ("updating registers...\n");
      #endif
    
      update_registers (stackFrame);
    
      #if DEBUG_THREADS
      printf ("done updating registers\n");
      #endif
    
      if (currentThread->interruptState == INTERRUPT_GRANTED)
        throw_exception(interruptedException);
    }
      
    return true;
  }

  schedule_request(REQUEST_EXIT);
  currentThread = null;
  
  return false;
}

/*
 * Current thread owns object's monitor (we hope) and wishes to relinquish
 * it temporarily (by calling Object.wait()).
 */
void monitor_wait(Object *obj, const FOURBYTES time)
{
#if DEBUG_MONITOR
  printf("monitor_wait of %d, thread %d(%d)\n",(int)obj, (int)currentThread, currentThread->threadId);
#endif
  if (currentThread->threadId != get_thread_id (obj))
  {
    throw_exception(illegalMonitorStateException);
    return;
  }
  
  // Great. We own the monitor which means we can give it up, but
  // indicate that we are listening for notify's.
  currentThread->state = CONDVAR_WAITING;
  
  // Save monitor depth
  currentThread->monitorCount = get_monitor_count(obj);
  
  // Save the object who's monitor we will want back
  currentThread->waitingOn = ptr2word (obj);
  
  // Might be an alarm set too.
  if (time > 0)
    currentThread->sleepUntil = get_sys_time() + time; 	
  else
    currentThread->sleepUntil = 0;
  
#if DEBUG_MONITOR
  printf("monitor_wait of %d, thread %d(%d) until %ld\n",(int)obj, (int)currentThread, currentThread->threadId, time);
#endif

  // Indicate that the object's monitor is now free.
  set_thread_id (obj, NO_OWNER);
  set_monitor_count(obj, 0);
  
  // Gotta yield
  schedule_request( REQUEST_SWITCH_THREAD);
}

/*
 * Current thread owns object's monitor (we hope) and wishes to wake up
 * any other threads waiting on it. (by calling Object.notify()).
 */
void monitor_notify(Object *obj, const boolean all)
{
#if DEBUG_MONITOR
  printf("monitor_notify of %d, thread %d(%d)\n",(int)obj, (int)currentThread, currentThread->threadId);
#endif
  if (currentThread->threadId != get_thread_id (obj))
  {
    throw_exception(illegalMonitorStateException);
    return;
  }
  
  monitor_notify_unchecked(obj, all);
}

/*
 * wake up any objects waiting on the passed object.
 */
void monitor_notify_unchecked(Object *obj, const boolean all)
{
  short i;
  Thread *pThread;
  
#if DEBUG_MONITOR
  printf("monitor_notify_unchecked of %d, thread %d(%d)\n",(int)obj, (int)currentThread, currentThread->threadId);
#endif
  // Find a thread waiting on us and move to WAIT state.
  for (i=MAX_PRIORITY-1; i >= 0; i--)
  {
    pThread = threadQ[i];
    if (!pThread)
      continue;
      
    do {
      // Remember threadQ[i] is the last thread on the queue
      pThread = word2ptr(pThread->nextThread);
      if (pThread->state == CONDVAR_WAITING && pThread->waitingOn == ptr2word (obj))
      {
        // might have been interrupted while waiting
        if (pThread->interruptState != INTERRUPT_CLEARED)
          pThread->interruptState = INTERRUPT_GRANTED;

        pThread->state = MON_WAITING;
        if (!all)
          return;
      }
    } while (pThread != threadQ[i]);
  }
}

/**
 * currentThread enters obj's monitor:
 *
 * if monitor is in use, save object in thread and re-schedule
 * else grab monitor and increment its count.
 * 
 * Note that this operation is atomic as far as the program is concerned.
 */
void enter_monitor (Thread *pThread, Object* obj)
{
#if DEBUG_MONITOR
  printf("enter_monitor of %d\n",(int)obj);
#endif

  if (obj == JNULL)
  {
    throw_exception (nullPointerException);
    return;
  }

  if (get_monitor_count (obj) != NO_OWNER && pThread->threadId != get_thread_id (obj))
  {
    // There is an owner, but its not us.
    // Make thread wait until the monitor is relinquished.
    pThread->state = MON_WAITING;
    pThread->waitingOn = ptr2word (obj);
    pThread->monitorCount = 1;
    // Gotta yield
    schedule_request (REQUEST_SWITCH_THREAD);    
    return;
  }
  set_thread_id (obj, pThread->threadId);
  inc_monitor_count (obj);
}

/**
 * Decrement monitor count
 * Release monitor if count reaches zero
 */
void exit_monitor (Thread *pThread, Object* obj)
{
  byte newMonitorCount;

#if DEBUG_MONITOR
  printf("exit_monitor of %d\n",(int)obj);
#endif

  if (obj == JNULL)
  {
    // Exiting due to a NPE on monitor_enter [FIX THIS]
    return;
  }

  #ifdef VERIFY
  assert (get_thread_id(obj) == pThread->threadId, THREADS7);
  assert (get_monitor_count(obj) > 0, THREADS8);
  #endif

  newMonitorCount = get_monitor_count(obj)-1;
  if (newMonitorCount == 0)
    set_thread_id (obj, NO_OWNER);
  set_monitor_count (obj, newMonitorCount);
}

/**
 * Current thread waits for thread to die.
 *
 * throws InterruptedException
 */
void join_thread(Thread *thread)
{
}

void dequeue_thread(Thread *thread)
{
  // First take it out of its current queue
  byte cIndex = thread->priority-1;
  Thread **pThreadQ = &threadQ[cIndex];
  
  // Find the previous thread at the old priority
  Thread *previous = *pThreadQ;
  #if DEBUG_THREADS
  printf("Previous thread %ld\n", ptr2word(previous));
  #endif
  while (word2ptr(previous->nextThread) != thread)
    previous = word2ptr(previous->nextThread);

  #if DEBUG_THREADS
  printf("Previous thread %ld\n", ptr2word(previous));
  #endif
  if (previous == thread)
  {
  #if DEBUG_THREADS
  printf("No more threads of priority %d\n", thread->priority);
  #endif
    *pThreadQ = null;
  }
  else
  {
    previous->nextThread = thread->nextThread;
    *pThreadQ = previous;
  }  
}

void enqueue_thread(Thread *thread)
{
  // Could insert it anywhere. Just insert it at the end.
  byte cIndex = thread->priority-1;
  Thread *previous = threadQ[cIndex];
  threadQ[cIndex] = thread;
  if (previous == null)
    thread->nextThread = ptr2word(thread);
  else {
    Thread *pNext = word2ptr(previous->nextThread);
    thread->nextThread = ptr2word(pNext);
    previous->nextThread = ptr2word(thread);
  }
}

/**
 * Set the priority of the passed thread. Insert into new queue, remove
 * from old queue. Overload to remove from all queues if passed priority
 * is zero.
 *
 * Returns the 'previous' thread.
 */
void set_thread_priority(Thread *thread, const FOURBYTES priority)
{
  #if DEBUG_THREADS
  printf("Thread priority set to %ld was %d\n", priority, thread->priority);
  #endif
  if (thread->priority == priority)
    return;

  if (thread->state == NEW)
  {
  	// Not fully initialized
  	thread->priority = priority;
  	return;
  }

  dequeue_thread(thread);
  thread->priority = priority;
  enqueue_thread(thread);      
}

