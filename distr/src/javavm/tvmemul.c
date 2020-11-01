/**
 * tvmemul.c
 * Entry source file for TinyVM emulator.
 */

#include <stdio.h>
#include <stdlib.h>
#include "classes.h"
#include "constants.h"
#include "exceptions.h"
#include "interpreter.h"
#include "memory.h"
#include "platform_config.h"
#include "specialclasses.h"
#include "threads.h"
#include "tvmemul.h"

byte *region;

Thread   *bootThread;


/* Maxi: Buffer to convert from Java String to char[] */
char *strBuffer = NULL;

void handle_uncaught_exception (Object *exception,
                                       const Thread *thread,
				       const MethodRecord *methodRecord,
				       const MethodRecord *rootMethod,
				       byte *pc)
{
    printf ("*** UNCAUGHT EXCEPTION/ERROR: \n");
	printf ("--  Exception class   : %u\n", (unsigned) get_class_index (exception));
    printf ("--  Thread            : %u\n", (unsigned) thread->threadId);
    printf ("--  Method signature  : %u\n", (unsigned) methodRecord->signatureId);
    printf ("--  Root method sig.  : %u\n", (unsigned) rootMethod->signatureId);
    printf ("--  Bytecode offset   : %u\n", (unsigned) pc - 
            (int) get_code_ptr(methodRecord));  	
	getc(stdout);
}

void run(void)
{
  // Initialize binary image state
  initialize_binary();

  // Initialize memory
  {
	TWOBYTES allocated, size = MEMORY_SIZE;

    memory_init ();

	strBuffer = malloc(STRING_BUF_SIZE + 1);

	/**
	 * Maxi: Allocates memeory for heap; tries to allocate the biggest chunk possible.
	 * Since this is one chunk, the JVM uses SEGMENTED_HEAP=0, maye we can do better in the future.
	 */
	do {
		region = (byte *)malloc(size * sizeof(TWOBYTES));
		allocated = size;
		size -= 256;
	} while ((region == null) && (size > 0));
	memory_add_region(region, region + allocated * sizeof(TWOBYTES));
  }
  printf("  64K RAM SYSTEM %5d JAVA BYTES FREE\n\n", getHeapFree());

  // Initialize exceptions
  init_exceptions();
  // Create the boot thread (bootThread is a special global)
  bootThread = (Thread *) new_object_for_class (JAVA_LANG_THREAD);
  
  #if DEBUG_THREADS
  printf ("Created bootThread: %d. Initializing...\n", (int) bootThread);
  #endif
  
  #if DEBUG_RUNS
  for (count = 0; count < 100; count++)
  {
  #endif // DEBUG_RUNS

  init_threads();
  if (!init_thread (bootThread))
  {
    printf ("Unable to initialize threading module.\n");
    exit (1);	  
  }
  // Execute the bytecode interpreter
  set_program_number (0);
  #if DEBUG_STARTUP
  printf ("Engine starting.\n");
  #endif
  engine();
  // Engine returns when all non-daemon threads are dead
  #if DEBUG_STARTUP
  printf ("Engine finished.\n");
  #endif

  #if DEBUG_RUNS
  }
  #endif // DEBUG_RUNS
}
