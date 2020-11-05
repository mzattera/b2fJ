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
#include "platform_hooks.h"
#include "specialclasses.h"
#include "threads.h"
#include "trace.h"
#include "tvmemul.h"

byte *region;
Thread   *bootThread;

void handle_uncaught_exception(Object *exception,
	const Thread *thread,
	const MethodRecord *methodRecord,
	const MethodRecord *rootMethod,
	byte *pc)
{
	printf("*** UNCAUGHT EXCEPTION/ERROR: \n");
	printf("--  Exception class   : %u\n", (unsigned)get_class_index(exception));
	printf("--  Thread            : %u\n", (unsigned)thread->threadId);
	printf("--  Method signature  : %u\n", (unsigned)methodRecord->signatureId);
	printf("--  Root method sig.  : %u\n", (unsigned)rootMethod->signatureId);
	printf("--  Bytecode offset   : %u\n", (unsigned)pc -
		(int)get_code_ptr(methodRecord));
	getc(stdout);
}

void run(void)
{
	// Initialize binary image state
	initialize_binary();

	// Initialize memory
	memory_init();

	// Initialize exceptions
	init_exceptions();
	// Create the boot thread (bootThread is a special global)
	bootThread = (Thread *)new_object_for_class(JAVA_LANG_THREAD);

#if DEBUG_THREADS
	printf("Created bootThread: %d. Initializing...\n", (int)bootThread);
#endif

#if DEBUG_RUNS
	for (count = 0; count < 100; count++)
	{
#endif // DEBUG_RUNS

		init_threads();
		if (!init_thread(bootThread))
		{
			printf("Unable to initialize threading module.\n");
			exit(1);
		}
		// Execute the bytecode interpreter
		set_program_number(0);

#if DEBUG_STARTUP
		printf("Engine starting.\n");
#endif
		engine_start_hook();
		engine();
		// Engine returns when all non-daemon threads are dead
#if DEBUG_STARTUP
		printf("Engine finished.\n");
#endif

#if DEBUG_RUNS
	}
#endif // DEBUG_RUNS
}
