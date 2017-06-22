#ifndef _PLATFORM_CONFIG_H
#define _PLATFORM_CONFIG_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include "compiler_config.h"

#if !USING_VS
/*
Using cc65

sizeof primitive types

char 1
short 2
int 2
long 4
*/
typedef unsigned char byte;
typedef signed char JBYTE;
typedef signed short JSHORT;
typedef signed long JINT;
typedef unsigned short TWOBYTES;
typedef unsigned long FOURBYTES;
#else
/*
Using Visual Studio

sizeof primitive types

Size of char 1
Size of short 2
Size of int 4
Size of long 4
Size of void* 4
*/
typedef unsigned char byte;
typedef signed char JBYTE;
typedef signed short JSHORT;
typedef signed long JINT;
typedef unsigned short TWOBYTES;
typedef unsigned long FOURBYTES;
#endif

/*
#define ptr2word(PTR_) ((STACKWORD) (PTR_))
#define word2ptr(WRD_) ((void *) (WRD_))
*/

// This returns current time in millis elapsed (mills since witch on)
#define get_sys_time() ((FOURBYTES)(clock() * 1000 / CLOCKS_PER_SEC))

#ifndef LITTLE_ENDIAN
#define LITTLE_ENDIAN 1
#endif

#define FP_ARITHMETIC 0
#define TICKS_PER_TIME_SLICE          16 // Actually instructions per timeslice
#define VERIFY
#define RECORD_REFERENCES 1

/*
Maxi: This is the size of the buffer used to convert from Java String
into char[]; strings longer than this will be truncated before printing,
but the smaller this is, the less memory is used.
*/
#define STRING_BUF_SIZE 200

#endif // _PLATFORM_CONFIG_H
