#ifndef _PLATFORM_HOOKS_H
#define _PLATFORM_HOOKS_H

#include <conio.h>
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
	clrscr();
	printf("\n");	
	printf("    ****   b2fJ JAVA v. %s   ****\n\n", VERSION);
	printf("  64K RAM SYSTEM %5d JAVA BYTES FREE\n\n", getHeapFree());
}

extern void switch_thread_hook(void);

/**
 * Called when thread is about to die due to an uncaught exception.
 */
extern void handle_uncaught_exception (Object *exception,
                                       const Thread *thread,
				       const MethodRecord *methodRecord,
				       const MethodRecord *rootMethod,
				       byte *pc);

/**
 * Dispatches a native method.
 */
extern int dispatch_platform_native (TWOBYTES signature, STACKWORD *paramBase);				      
extern void dispatch_native (TWOBYTES signature, STACKWORD *paramBase);				      

#endif // _PLATFORM_HOOKS_H