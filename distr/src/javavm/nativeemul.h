#ifndef _NATIVEEMUL_H
#define _NATIVEEMUL_H

#include "platform_config.h"
#include "types.h"

/**
* Dispatches a native method.
*/
extern void dispatch_native(TWOBYTES signature, STACKWORD *paramBase);

#endif // _NATIVEEMUL_H
