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
			exit_tool ("Unable to initialize threading module", -1);
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
