#include "platform_config.h"
#include "platform_hooks.h"
#include "memory.h"
#include "types.h"
/**
 * Defines how 32-bit references are translated to native 64-bit pointers
 * Here we use a trick, as we use a 64K continuos region, we only substract
 * the start address of the region to obtain a 64K reference and viceversa
 * More complex translation could be made with hashing techniques.
 *
 * Another solution is have 2 tables to translate the values.
 *
 * This translation mapping should work with x86-64bit macos and linux
 *
 */

STACKWORD _map(void* x) {
    if(x==NULL) return (STACKWORD)0x0;
    NATIVEWORD offset = getRegionAddress();
    return (STACKWORD)(x-offset);
}

void* _unmap(STACKWORD x) {
    if(x==0x0) return NULL;
    NATIVEWORD offset = (NATIVEWORD)getRegionAddress();
    return (void*)((NATIVEWORD)x)+((NATIVEWORD)offset);
}
