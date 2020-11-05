/**
 * Maxi: This file hass been created for debug purposes.
 * Many conversion macros from platform_config.h & types.h have been transformed into calls to the below functions.
 * These functions verify that confersion does not result in loss of information.
 * In addition several debug functions added.
 */

#include <stdio.h>
#include "debug.h"
#include "platform_config.h"
#include "trace.h"

/*
#define ptr2word(PTR_) ((STACKWORD) (PTR_))
#define word2ptr(WRD_) ((void *) (WRD_))
*/
STACKWORD ptr2wordImpl(void *ptr, boolean check) {
	STACKWORD res = (STACKWORD)ptr;

#if VERIFY
	if (check) assert(word2ptrImpl(res, 0) == ptr, CAST00);
#endif

	return res;
}

void *word2ptrImpl(STACKWORD wrd, boolean check) {
	void *res = (void *)wrd;

//#if VERIFY
//	printf("%lx\n", wrd);
//	if (wrd == 0)
//		getc(stdin);
//	if (check) assert(ptr2wordImpl(res, 0) == wrd, CAST01);
//#endif

	return res;
}

/**
#define obj2ptr(OBJ_)       ((void *) (OBJ_))
#define ptr2ref(PTR_)       ((REFERENCE) ptr2word(PTR_))
#define ref2ptr(REF_)       word2ptr((STACKWORD) (REF_))
#define ref2obj(REF_)       ((Object *) ref2ptr(REF_))
*/
void *obj2ptrImpl(Object *obj, boolean check) {
	void *res = (void *)obj;

#if VERIFY
	if (check) assert(ref2objImpl(ptr2refImpl(res, 0), 0) == obj, CAST02);
#endif

	return res;
}

REFERENCE ptr2refImpl(void *ptr, boolean check) {
	REFERENCE res = (REFERENCE)ptr2word(ptr);

#if VERIFY
	if (check) assert(ref2ptrImpl(res, 0) == ptr, CAST03);
#endif

	return res;
}

void *ref2ptrImpl(REFERENCE ref, boolean check) {
	void *res = word2ptr((STACKWORD)ref);

#if VERIFY
	if (check) assert(ptr2refImpl(res, 0) == ref, CAST04);
#endif

	return res;
}

Object *ref2objImpl(REFERENCE ref, boolean check) {
	Object *res = (Object *)ref2ptr(ref);

#if VERIFY
	if (check) assert(obj2ref(res) == ref, CAST05);
#endif

	return res;
}

#if FP_ARITHMETIC
extern STACKWORD jfloat2wordImpl(JFLOAT f, boolean check) {
	AuxConvUnion1 res;
	res.fnum = f;

#if VERIFY
	if (check) assert(word2jfloatImpl(res.sword, 0) == f, CAST06);
#endif

	return res.sword;
}

extern JFLOAT word2jfloatImpl(STACKWORD wrd, boolean check) {
	AuxConvUnion1 res;
	res.sword = wrd;

#if VERIFY
	if (check) assert(jfloat2wordImpl(res.fnum, 0) == wrd, CAST07);
#endif

	return res.fnum;
}
#endif


/**
typedef struct S_MasterRecord
{
TWOBYTES magicNumber;
TWOBYTES constantTableOffset;

* Offset to STATICFIELD[].
TWOBYTES staticFieldsOffset;
TWOBYTES staticStateOffset;

* Size of all static state in 2-byte words.
TWOBYTES staticStateLength;
TWOBYTES numStaticFields;

* Offset to sequence of class indices (bytes).
TWOBYTES entryClassesOffset;
byte numEntryClasses;
byte lastClass;
} MasterRecord;
*/
void printMasterRecord() {
	MasterRecord *mrec = get_master_record();
	printf("Master Record at: %5d\n", (int)mrec);
	printf(".magicNumber: %5d\n", (int)mrec->magicNumber);
	printf(".constantTableOffset: %5d\n", (int)mrec->constantTableOffset);
	printf(".staticFieldsOffset: %5d\n", (int)mrec->staticFieldsOffset);
	printf(".staticStateOffset: %5d\n", (int)mrec->staticStateOffset);
	printf(".staticStateLength: %5d\n", (int)mrec->staticStateLength);
	printf(".numStaticFields: %5d\n", (int)mrec->numStaticFields);
	printf(".entryClassesOffset: %5d\n", (int)mrec->entryClassesOffset);
	printf(".numEntryClasses: %d\n", mrec->numEntryClasses);
	printf(".lastClass: %d\n", mrec->lastClass);
	getc(stdin);
}

/**
typedef struct S_ClassRecord
{
* Space occupied by instance in 2-byte words.
TWOBYTES classSize;

* Offset of method table (in bytes) starting from
* the beginning of the entire binary.
TWOBYTES methodTableOffset;

* Offset to table of bytes containing types of fields.
* Useful for initializing objects.
TWOBYTES instanceTableOffset;
byte numInstanceFields;
byte numMethods;
byte parentClass;
byte cflags;
} ClassRecord;
*/
void printClassRecord(ClassRecord *cls) {
	printf("Class Record at: %5d\n", (int)cls);
	printf(".classSize: %5d\n", (int)cls->classSize);
	printf(".methodTableOffset: %5d\n", cls->methodTableOffset);
	printf(".instanceTableOffset: %5d\n", cls->instanceTableOffset);
	printf(".numInstanceFields: %d\n", cls->numInstanceFields);
	printf(".numMethods: %d\n", cls->numMethods);
	printf(".parentClass: %d\n", cls->parentClass);
	printf(".cflags: %d\n", cls->cflags);
	getc(stdin);
}

/*
typedef struct S_MethodRecord
{
// Unique id for the signature of the method
TWOBYTES signatureId;

// Offset to table of exception information
TWOBYTES exceptionTable;
TWOBYTES codeOffset;

// Number of 32-bit locals (long is counted as 2 locals).
byte numLocals;

// Maximum size of local operand stack, in 32-bit words.
byte maxOperands;

// It should be such that stackTop-numParameters unwinds the stack.
// The receiver in non-static methods is counted as one word.
byte numParameters;

// Number of exception handlers
byte numExceptionHandlers;
byte mflags;
} MethodRecord; */
void printMethodRecord(MethodRecord *m) {
	int i;
	printf("MethodRecord at: %5d\n", (int)m);
	for (i = 0; i < sizeof(MethodRecord); i++) {
		printf("%2X ", ((byte*)m)[i]);
	}
	printf("\n");
	printf(".signatureId: %5d\n", (int)m->signatureId);
	printf(".exceptionTable: %5d\n", m->exceptionTable);
	printf(".codeOffset: %5d\n", m->codeOffset);
	printf(".numLocals: %d\n", m->numLocals);
	printf(".maxOperands: %d\n", m->maxOperands);
	printf(".numParameters: %d\n", m->numParameters);
	printf(".numExceptionHandlers: %d\n", m->numExceptionHandlers);
	printf(".mflags: %d\n", m->mflags);
	getc(stdin);
}