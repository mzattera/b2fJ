/*
 * This defines some macro related to different compile behaviors.
 * 
 * __PACKED() macro that can be used to pack structs using both VS and GCC that use different approaces.
 * 
*/
#ifndef _COMPILER_CONFIG_H_
#define _COMPILER_CONFIG_H_

// If true, we are using VisualStudio to compile the C64 VM, sometime we do for easier debugging.
#define USING_VS (_MSC_VER && !__INTEL_COMPILER)

#if USING_VS

// VS
#define __INLINED inline
#define __BYTE_BITFIELD byte
#define __TWOBYTE_BITFIELD TWOBYTES

#else

// cc65
#define __INLINED 
#define __BYTE_BITFIELD int 
#define __TWOBYTE_BITFIELD int

#endif // USING_VS

// VS (when compiling a native Win app, so using fully fledged VS options)
// #define __INLINED inline
// #define __BYTE_BITFIELD byte
// #define __TWOBYTE_BITFIELD TWOBYTES

// GCC (original code)
// #define __INLINED inline
// #define __BITFIELD byte
// #define __TWOBYTE_BITFIELD TWOBYTES

#endif // _COMPILER_CONFIG_H_
