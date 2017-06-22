#ifndef _TYPES_H
#define _TYPES_H

#include "platform_config.h"

typedef byte boolean;

/*
 * The following types must be defined in platform_config.h:
 * JBYTE
 * JSHORT
 * JINT
 * TWOBYTES
 * FOURBYTES
 */


typedef float        JFLOAT;
typedef JBYTE        JBOOLEAN;
typedef JSHORT       JCHAR;
typedef FOURBYTES    REFERENCE;
typedef FOURBYTES    STACKWORD;

typedef union
{
  JFLOAT fnum;
  STACKWORD sword;
} AuxConvUnion1;

typedef struct
{
  STACKWORD hi;
  STACKWORD lo;
} JLONG;

#ifndef LITTLE_ENDIAN
#error LITTLE_ENDIAN not defined in platform_config.h
#endif

#endif // _TYPES_H


