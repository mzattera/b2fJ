/**
* platform_native.c
* Native methods specific to a platform.
*/
#include "constants.h"
#include "types.h"
#include "platform_config.h"
#include "platform_hooks.h"

int dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase)
{
	return false;
}
