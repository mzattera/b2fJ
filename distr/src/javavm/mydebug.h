/**
 * Maxi: This file has been created for debug purposes.
 * Many conversion macros from platform_config.h & types.h have been moved here. See also conversion.c.
 * These functions verify that confersion does not result in loss of information.
 */
#ifndef _MYDEBUG_H_
#define _MYDEBUG_H_

#include "classes.h"
#include "language.h"
#include "types.h"

// MAXI custom flags for various debug stuff

#define DEBUG_MY_INITBINARY 0
#define DEBUG_MY_ALLOCMEM 0
#define DEBUG_MY_MAIN 0

#ifndef DEBUG_MY_INITBINARY 
# define DEBUG_MY_INITBINARY 0
#endif
#ifndef DEBUG_MY_ALLOCMEM 
# define DEBUG_MY_ALLOCMEM 0
#endif
#ifndef DEBUG_MY_MAIN 
# define DEBUG_MY_MAIN 0
#endif

// Trace/debug functions

extern void printMasterRecord(void);
extern void printClassRecord(ClassRecord *cls);
extern void printMethodRecord(MethodRecord *m);

// Conversion functions with guards

#define ptr2word(PTR_) ptr2wordImpl((PTR_),1)
#define word2ptr(WRD_) word2ptrImpl((WRD_),1)

#define byte2jint(BYTE_)    ((JINT) (signed char) (BYTE_))
#define word2jint(WORD_)    ((JINT) (WORD_))
#define word2jshort(WORD_)  ((JSHORT) (WORD_))
#define word2obj(WORD_)     ((Object *) word2ptr(WORD_))
#define obj2word(OBJ_)      ptr2word(OBJ_)
#define obj2ref(OBJ_)       ptr2ref(OBJ_)

/** DEBUG: moved into functions that do conversion checks
#define jfloat2word(FLOAT_) (((AuxConvUnion1) (FLOAT_)).sword)
#define word2jfloat(WORD_)  (((AuxConvUnion1) (WORD_)).fnum)
#define obj2ptr(OBJ_)       ((void *) (OBJ_))
#define ptr2ref(PTR_)       ((REFERENCE) ptr2word(PTR_))
#define ref2ptr(REF_)       word2ptr((STACKWORD) (REF_))
#define ref2obj(REF_)       ((Object *) ref2ptr(REF_))
*/
#define jfloat2word(FLOAT_) jfloat2wordImpl(FLOAT_,1)
#define word2jfloat(WORD_)  word2jfloatImpl(WORD_,1)
#define obj2ptr(OBJ_)   obj2ptrImpl((OBJ_),1)
#define ptr2ref(PTR_)   ptr2refImpl((PTR_),1)
#define ref2ptr(REF_)   ref2ptrImpl((REF_),1)
#define ref2obj(REF_)   ref2objImpl((REF_),1)

extern STACKWORD ptr2wordImpl(void *ptr, boolean check);
extern void *word2ptrImpl(STACKWORD wrd, boolean check);
extern STACKWORD jfloat2wordImpl(JFLOAT f, boolean check);
extern JFLOAT word2jfloatImpl(STACKWORD wrd, boolean check);
extern void *obj2ptrImpl(Object *obj, boolean check);
extern REFERENCE ptr2refImpl(void *ptr, boolean check);
extern void *ref2ptrImpl(REFERENCE ref, boolean check);
extern Object *ref2objImpl(REFERENCE ref, boolean check);

#endif // _MYDEBUG_H_