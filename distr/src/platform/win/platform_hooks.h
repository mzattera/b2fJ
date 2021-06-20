#ifndef _PLATFORM_HOOKS_H
#define _PLATFORM_HOOKS_H

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>
#include "classes.h"
#include "interpreter.h"
#include "language.h"
#include "platform_config.h"
#include "types.h"
#include "version.h"

/***********************************************************************************************************
* Platform specific methods.
***********************************************************************************************************/

/* Called before engine() is started and Java program executed. */
extern void engine_start_hook(void);

/*
* Called to exit the program.
* As last step, this should call exit() passing given exit code.
*/
extern void exit_tool(char* exitMessage, int exitCode);

/* Called when thread is about to die due to an uncaught exception. */
extern void handle_uncaught_exception(Object *exception,
	const Thread *thread,
	const MethodRecord *methodRecord,
	const MethodRecord *rootMethod,
	byte *pc);

/*
* Dispatches a native method.
* Return false to use the default implementation (common across all platforms) for a method.
*/
/* extern bool dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase); */
#define dispatch_platform_native(signature, paramBase)	false

/**
 * Converts a Java char into corresponding platform-dependent char.
 */
#define int2nativeChar(c)  c

/* Returns current time in millis */
/* extern FOURBYTES get_sys_time(void); */
#define get_sys_time() ((FOURBYTES)(clock() * 1000 / CLOCKS_PER_SEC))

/* Returns size of maximum free memory heap block available (in TWOBYTES words) */
/* If your platform has no way to provide this, return MAX_HEAP_SIZE */
/* extern size_t get_max_block_size(void); */
#define get_max_block_size() MAX_HEAP_SIZE

/* Called before Java heap is initialized, in case the platform needs to reserve some memory */
/* static __INLINED void memory_init_hook() */
#define memory_init_hook()	;

/* Invoked by interpreted prior execution each bytecode */
/* static __INLINED void instruction_hook() */
#define instruction_hook()	(gMakeRequest = true)

/* static __INLINED void tick_hook() */
#define tick_hook()	;

/* static __INLINED void idle_hook() */
#define idle_hook()	;

/* static __INLINED void switch_thread_hook() */
#define switch_thread_hook()	;

#define map(x)      (x)
#define unmap(x)    (x)

#endif // _PLATFORM_HOOKS_H