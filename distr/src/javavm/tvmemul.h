#ifndef _TVMEMUL_H
#define _TVMEMUL_H

#include "classes.h"
#include "language.h"

/* Maxi: Buffer to convert from Java String to char[] */
#define STRING_BUF_SIZE 255
extern char *strBuffer;

extern void handle_uncaught_exception (Object *exception,
                                       const Thread *thread,
				       const MethodRecord *methodRecord,
				       const MethodRecord *rootMethod,
				       byte *pc);

extern void run(void);

#endif // _TVMEMUL_H
