#ifndef _PLATFORM_CONFIG_H
#define _PLATFORM_CONFIG_H

#include <time.h>
#include "compiler_config.h"

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

#ifndef LITTLE_ENDIAN
#define LITTLE_ENDIAN 1
#endif

#define FP_ARITHMETIC			0
#define TICKS_PER_TIME_SLICE	16	/* Actually instructions per timeslice */
#define VERIFY					0	/* If 0 disables all assertions and debug checks */
#define RECORD_REFERENCES		1

/* Max size (in TWOBYTES words) for Java Heap. The JVM will try to allocate this much memory for heap at startup. */
#define MAX_HEAP_SIZE ((size_t)(65536 / sizeof(TWOBYTES)))

/* Returns current time in millis */
#define get_sys_time() ((FOURBYTES)(clock() * 1000 / CLOCKS_PER_SEC))

/* Returns size of maximum free memory heap block available (in TWOBYTES words) */
/* If your platform has no way to provide this, return MAX_HEAP_SIZE */
extern size_t get_max_block_size(void);

#endif // _PLATFORM_CONFIG_H
