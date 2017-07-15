/**
 * tvmemul.c
 * Entry source file for TinyVM emulator.
 */

#include <conio.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <signal.h>
#include "types.h"
#include "constants.h"
#include "classes.h"
#include "threads.h"
#include "stack.h"
#include "specialclasses.h"
#include "specialsignatures.h"
#include "language.h"
#include "memory.h"
#include "interpreter.h"
#include "exceptions.h"
#include "load.h"
#include "trace.h"
#include "platform_hooks.h"
#include "mydebug.h"
#include "version.h"

// Max memory size (in WORDS) to allocate for Java Heap.
#define MEMORY_SIZE 26623

byte *region;

Thread   *bootThread;

// Maxi: Buffer to convert from Java String to char[]
char *strBuffer;

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

	// Maxi: Allocates memeory for heap
	// tries to allocate the biggest chunk possible
	// Since this is one chunk, the JVM uses SEGMENTED_HEAP=0, maye we can do better in the future.
	do {
		region = (byte *)malloc(size * sizeof(TWOBYTES));
		allocated = size;
		size -= 256;
	} while ((region == NULL) && (size > 0));
	memory_add_region(region, region + allocated * sizeof(TWOBYTES));
  }
  printf("%d JAVA BYTES FREE\n\n", getHeapFree());

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

/***************************************************************************
 * int main
 * Parses command line. Format is:
 *	argv[0] [-v] bin_file
 *
 * options:
 *	-v	Verbose mode. Prints text output rather than raw output.
 *
 *--------------------------------------------------------------------------
 * To go into man page:
 * Name:	emu-lejosrun - Emulate lejos RCX code in Unix
 *
 * Synosis:	emu-lejosrun [-v] bin_file
 *
 * Description:	Executes a binary file created by the lejos compiler within
 *		Unix rather than in the RCX environment. The Java byte-codes
 *		are executed here, and their actions are listed rather than
 *		executed as they would be on the real RCX device.
 *
 * Options:	-v	Verbose mode. Normally the output is printed in raw
 *			mode. The actual hex values are printed. Using this
 *			option displays more user-friendly output.
 *--------------------------------------------------------------------------
 ***************************************************************************/
int main (int argc, char *argv[])
{
	// Name of the file to use. Leave null to use code linked directly in memory.
	char *file = NULL;

#if USING_VS
	system("cls");
#else
	// switch to Upper case Set
	// putc(142, stdout);
	clrscr();
#endif

	printf("\n");
	printf("    ****   b2fJ JAVA VER.%s   ****\n\n", VERSION);

	if (argc == 2) {
		file = argv[1];
		printf("-> %s\n\n", file);
	}

#if DEBUG_STARTUP
	printf ("Reading binary %s\n", file);
#endif
	readBinary (file);

#if DEBUG_STARTUP
	printf("Running...\n");
#endif
	run();
	getc(stdin);
	exit(0);
} 
