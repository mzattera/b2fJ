#ifndef _PLATFORM_HOOKS_H
#define _PLATFORM_HOOKS_H

// Methods declared here must be implemented by
// each platform.

#include "types.h"
#include "classes.h"
#include "language.h"
#include "interpreter.h"

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
extern void dispatch_native (TWOBYTES signature, STACKWORD *paramBase);				      

#endif // _PLATFORM_HOOKS_H