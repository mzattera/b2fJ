#ifndef _PLATFORM_HOOKS_H
#define _PLATFORM_HOOKS_H

#include <stdio.h>
#include <stdlib.h>
#include "classes.h"
#include "compiler_config.h"
#include "interpreter.h"
#include "language.h"
#include "memory.h"
#include "platform_config.h"
#include "types.h"
#include "version.h"

static __INLINED void instruction_hook()
{
  gMakeRequest = true;
}

static __INLINED void tick_hook()
{
}

static __INLINED void idle_hook()
{
}

static __INLINED void switch_thread_hook()
{
}

/* Called before Java heap is initialized, in case the platform needs to reserve some memory */
static __INLINED void memory_init_hook()
{
}

/* Called before engine() is started and Java program executed */
static __INLINED void engine_start_hook()
{
	system("cls");
	printf("****   b2fJ v.%s   ****\n", VERSION);
	printf("  %5d Java bytes free\n\n", getHeapFree());
} 

extern void switch_thread_hook(void);

/*
 * Called when thread is about to die due to an uncaught exception.
 */
extern void handle_uncaught_exception (Object *exception,
                                       const Thread *thread,
				       const MethodRecord *methodRecord,
				       const MethodRecord *rootMethod,
				       byte *pc);

/*
 * Called to exit the program.
 * As last step, this should call exit() passing given exit code.
 */
extern void exit_tool(char* exitMessage, int exitCode);

/**
 * Dispatches a native method.
 */
extern int dispatch_platform_native (TWOBYTES signature, STACKWORD *paramBase);				      
extern void dispatch_native (TWOBYTES signature, STACKWORD *paramBase);				      

#endif // _PLATFORM_HOOKS_H