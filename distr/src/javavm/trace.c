#include <stdbool.h>
#include <stdio.h>
#include "language.h"
#include "trace.h"

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