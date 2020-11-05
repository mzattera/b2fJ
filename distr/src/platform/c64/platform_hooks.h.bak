#ifndef _PLATFORM_HOOKS_H
#define _PLATFORM_HOOKS_H

#include "classes.h"
#include "compiler_config.h"
#include "types.h"
#include "interpreter.h"
#include "language.h"
#include "platform_config.h"

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