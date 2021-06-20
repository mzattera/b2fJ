#include <stddef.h>
#include "classes.h"
#include "conversion.h"
#include "exceptions.h"
#include "interpreter.h"
#include "language.h"
#include "memory.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "specialclasses.h"
#include "stack.h"
#include "threads.h"
#include "trace.h"

Object *outOfMemoryError;
Object *noSuchMethodError;
Object *stackOverflowError;
Object *nullPointerException;
Object *classCastException;
Object *arithmeticException;
Object *arrayIndexOutOfBoundsException;
Object *illegalArgumentException;
Object *interruptedException;
Object *illegalStateException;
Object *illegalMonitorStateException;
Object *error;

// Temporary globals:

static TWOBYTES tempCurrentOffset;
static MethodRecord *tempMethodRecord = NULL;
static StackFrame *tempStackFrame;
static ExceptionRecord *gExceptionRecord;
static byte gNumExceptionHandlers;
static MethodRecord *gExcepMethodRec = NULL;
static byte *gExceptionPc;


void init_exceptions()
{
  outOfMemoryError = new_object_for_class (JAVA_LANG_OUTOFMEMORYERROR);
  noSuchMethodError = new_object_for_class (JAVA_LANG_NOSUCHMETHODERROR);
  stackOverflowError = new_object_for_class (JAVA_LANG_STACKOVERFLOWERROR);
  nullPointerException = new_object_for_class (JAVA_LANG_NULLPOINTEREXCEPTION);
  classCastException = new_object_for_class (JAVA_LANG_CLASSCASTEXCEPTION);
  arithmeticException = new_object_for_class (JAVA_LANG_ARITHMETICEXCEPTION);
  arrayIndexOutOfBoundsException = new_object_for_class (JAVA_LANG_ARRAYINDEXOUTOFBOUNDSEXCEPTION);
  illegalArgumentException = new_object_for_class (JAVA_LANG_ILLEGALARGUMENTEXCEPTION);
  interruptedException = new_object_for_class (JAVA_LANG_INTERRUPTEDEXCEPTION);
  illegalStateException = new_object_for_class (JAVA_LANG_ILLEGALSTATEEXCEPTION);
  illegalMonitorStateException = new_object_for_class (JAVA_LANG_ILLEGALMONITORSTATEEXCEPTION);
  error = new_object_for_class (JAVA_LANG_ERROR);
}

/**
 * @exception the exception to throw.
 */
void throw_exception (Object *exception)
{
  Thread *auxThread;
  
#if ASSERTIONS_ENABLED
  assert (exception != NULL, EXCEPTIONS0);
#endif // ASSERTIONS_ENABLED

#if DEBUG_EXCEPTIONS
  printf("Throw exception\n");
#endif
  if (currentThread == NULL)
  {
    // No threads have started probably
    return;
  }
  else if (exception == interruptedException)
  {
    // Throwing an interrupted exception clears the flag
    currentThread->interruptState = INTERRUPT_CLEARED;
  }
  
  #if ASSERTIONS_ENABLED
  assert (currentThread->state > DEAD, EXCEPTIONS1);
  #endif // ASSERTIONS_ENABLED
  
  gExceptionPc = pc;
  gExcepMethodRec = NULL;

  #if 0
  trace (-1, get_class_index(exception), 3);
  #endif

 LABEL_PROPAGATE:
  tempStackFrame = current_stackframe();
  tempMethodRecord = tempStackFrame->methodRecord;

  if (gExcepMethodRec == NULL)
    gExcepMethodRec = tempMethodRecord;
  gExceptionRecord = (ExceptionRecord *) (get_binary_base() + tempMethodRecord->exceptionTable);
  tempCurrentOffset = ptr2word(pc) - ptr2word(get_binary_base() + tempMethodRecord->codeOffset);

  #if 0
  trace (-1, tempCurrentOffset, 5);
  #endif

  gNumExceptionHandlers = tempMethodRecord->numExceptionHandlers;
#if DEBUG_EXCEPTIONS
  printf("Num exception handlers=%d\n",gNumExceptionHandlers);
#endif
  while (gNumExceptionHandlers--)
  {
    if (gExceptionRecord->start <= tempCurrentOffset /* off by one? < ? */
        && tempCurrentOffset <= gExceptionRecord->end)
    {
      // Check if exception class applies
      if (instance_of (exception, gExceptionRecord->classIndex))
      {
        // Clear operand stack
        init_sp (tempStackFrame, tempMethodRecord);
        // Push the exception object
        push_ref (ptr2word (exception));
        // Jump to handler:
        pc = get_binary_base() + tempMethodRecord->codeOffset + 
             gExceptionRecord->handler;
#if DEBUG_EXCEPTIONS
  printf("Found exception handler\n");
#endif
        return;
      }
    }
    gExceptionRecord++;
  }
  // No good handlers in current stack frame - go up.
  auxThread = currentThread;
  do_return (0);
  // Note: return takes care of synchronized methods.
  if (auxThread->state == DEAD)
  {
#if DEBUG_EXCEPTIONS
  printf("Thread is dead\n");
#endif
    if (get_class_index(exception) != JAVA_LANG_THREADDEATH)
    {
#if DEBUG_EXCEPTIONS
  printf("Handle uncaught exception\n");
#endif

      handle_uncaught_exception (exception, auxThread,
  			         gExcepMethodRec, tempMethodRecord,
			         gExceptionPc);
    }
    return;
  }
  goto LABEL_PROPAGATE; 


}







