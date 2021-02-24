/**
 * Conversion macros to convert between different data types.
 */
#ifndef _CONVERSION_H
#define _CONVERSION_H

#include <stdint.h>
#include "classes.h"
#include "language.h"
#include "platform_config.h"
#include "types.h"

#define ptr2word(PTR_)		((STACKWORD)(map(PTR_)))
#define word2ptr(WRD_)		((void *)(unmap(WRD_)))

#define byte2jint(BYTE_)    ((JINT) (int8_t) (BYTE_))
#define word2jint(WORD_)    ((JINT) (WORD_))
#define word2jshort(WORD_)  ((JSHORT) (WORD_))
#define word2obj(WORD_)     ((Object *) word2ptr(WORD_))
#define obj2word(OBJ_)      ptr2word(OBJ_)

#define jfloat2word(FLOAT_) (((AuxConvUnion1) (FLOAT_)).sword)
#define word2jfloat(WORD_)  (((AuxConvUnion1) (WORD_)).fnum)
#define obj2ptr(OBJ_)       ((void *) (OBJ_))
#define ptr2ref(PTR_)       ((REFERENCE) ptr2word(PTR_))
#define ref2ptr(REF_)       word2ptr((STACKWORD) (REF_))

#define ref2obj(REF_)       ((Object *) ref2ptr(REF_))
#define obj2ref(OBJ_)       ptr2ref(OBJ_)

#endif // _CONVERSION_H
