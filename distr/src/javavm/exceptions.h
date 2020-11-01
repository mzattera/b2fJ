#ifndef _EXCEPTIONS_H
#define _EXCEPTIONS_H

#include "classes.h"

extern Object *outOfMemoryError;
extern Object *noSuchMethodError;
extern Object *stackOverflowError;
extern Object *nullPointerException;
extern Object *classCastException;
extern Object *arithmeticException;
extern Object *arrayIndexOutOfBoundsException;
extern Object *illegalArgumentException;
extern Object *interruptedException;
extern Object *illegalStateException;
extern Object *illegalMonitorStateException;
extern Object *error;

extern void init_exceptions(void);
extern void throw_exception (Object *throwable);
extern void throw_exception_checked (Object *throwable);

#endif


