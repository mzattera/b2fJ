#ifndef _TRACE_H
#define _TRACE_H

#include "platform_config.h"

#if ASSERTIONS_ENABLED

#define INTERPRETER0   10
#define INTERPRETER1   11
#define INTERPRETER2   12
#define INTERPRETER3   13
#define INTERPRETER4   14
#define INTERPRETER5   15
#define INTERPRETER6   16

#define THREADS0       20
#define THREADS1       21
#define THREADS2       22
#define THREADS3       23
#define THREADS4       24
#define THREADS5       25
#define THREADS6       26
#define THREADS7       27
#define THREADS8       28
#define THREADS9       29

#define MEMORY0        30
#define MEMORY1        31
#define MEMORY2        32
#define MEMORY3        33
#define MEMORY4        34
#define MEMORY5        35
#define MEMORY6        36
#define MEMORY7        37
#define MEMORY8        38
#define MEMORY9        39

#define EXCEPTIONS0    40
#define EXCEPTIONS1    41

#define LANGUAGE0    50
#define LANGUAGE1    51
#define LANGUAGE2    52
#define LANGUAGE3    53

#define BAD_MAGIC	 54

#define ASSERT_TOP	100

#define CAST00        66
#define CAST01        67
#define CAST02        68
#define CAST03        69
#define CAST04        70
#define CAST05        71
#define CAST06        72
#define CAST07        73

#define assert( cond, code)  assert_hook( cond, code);

extern void assert_hook (boolean aCond, int aCode);

#else

#define assert(x,y)  ;

#endif /* ASSERTIONS_ENABLED */

extern void printMasterRecord(void);
extern void printClassRecord(ClassRecord *cls);
extern void printMethodRecord(MethodRecord *m);

#endif // _TRACE_H







