/**
 * Maxi: Util to validate length and alignment of structures.
 */
#include <stdbool.h>
#include <stdio.h>
#include "classes.h"
#include "configure.h"
#include "language.h"
#include "memory.h"
#include "types.h"
#include "threads.h"

int determine_little_endian()
{
	unsigned char carr[] = { 0x21, 0x34 };
	unsigned short *s = (void *)carr;

	if ((s[0] >> 8) == 0x21 && (s[0] & 0xFF) == 0x34)
		return 0; // Big endian, like Java
	if ((s[0] >> 8) == 0x34 && (s[0] & 0xFF) == 0x21)
		return 1; // Little endian, like gcc on Intel
				  // Unexpected:
	return -1;
}

void assert0(bool aCond, char* msg) {
		if (aCond)
			return;
		printf("Assertion failed: %s\n", msg);
		//exit(aCode);
	}

void assertLength(char* structName, int expLen, int len) {
	printf("sizeof(%s) is %d; expected %d.\n", structName, len, expLen);
	assert0((expLen == len), "Bad length.");
}

/**
* Object class native structure

typedef struct S_Object
{
	union _flags
	{
		TWOBYTES all;
		struct _freeBlock
		{
			__TWOBYTE_BITFIELD size : 15;
			__TWOBYTE_BITFIELD isAllocated : 1;
		} freeBlock;
		struct _objects
		{
			__TWOBYTE_BITFIELD     class :9;
			__TWOBYTE_BITFIELD     padding : 4;
			__TWOBYTE_BITFIELD     mark : 1;
			__TWOBYTE_BITFIELD     isArray : 1;
			__TWOBYTE_BITFIELD     isAllocated : 1;
		} objects;
		struct _arrays
		{
			__TWOBYTE_BITFIELD length : 9;
			__TWOBYTE_BITFIELD type : 4;
			__TWOBYTE_BITFIELD mark : 1;
			__TWOBYTE_BITFIELD isArray : 1;
			__TWOBYTE_BITFIELD isAllocated : 1;
		} arrays;
	} flags;

	byte monitorCount;
	byte threadId;

} Object;
*/
void checkObject() {
	Object o;
	// assertLength("Object", 4, sizeof(o));
	assertLength("Object.freeBlock", sizeof(TWOBYTES), sizeof(o.flags.freeBlock));
	assertLength("Object.objects", sizeof(TWOBYTES), sizeof(o.flags.objects));
	assertLength("Object.arrays", sizeof(TWOBYTES), sizeof(o.flags.arrays));

	// Set a byte mask
	o.flags.arrays.length = 0x01ff;
	o.flags.arrays.type = 0x00;
	o.flags.arrays.mark = 0x01;
	o.flags.arrays.isArray = 0x00;
	o.flags.arrays.isAllocated = 0x01;

	// Checks all
	assert0(o.flags.all == 0xa1ff, "Object.flags.all aligned.");

	//printf("%x\n", o.flags.freeBlock.size);
	assert0(o.flags.freeBlock.size == 0x21ff, "Object.flags.freeBlock.size aligned.");
	assert0(o.flags.freeBlock.isAllocated == o.flags.arrays.isAllocated, "Object.flags.freeBlock.isAllocated aligned.");

	//printf("%x\n", o.flags.objects.class);
	//printf("%x\n", o.flags.objects.padding);
	//printf("%x\n", o.flags.objects.mark);
	//printf("%x\n", o.flags.objects.isArray);
	//printf("%x\n", o.flags.objects.isAllocated);
	assert0(o.flags.objects.class == o.flags.arrays.length, "Object.flags.objects.class aligned.");
	assert0(o.flags.objects.mark == o.flags.arrays.mark, "Object.flags.objects.mark aligned.");
	assert0(o.flags.objects.isArray == o.flags.arrays.isArray, "Object.flags.objects.isArray aligned.");
	assert0(o.flags.objects.isAllocated == o.flags.arrays.isAllocated, "Object.flags.objects.isAllocated aligned.");
}

int main(int argc, char *argv[])
{
	// TODO see how it works for big-endian machines.
	assert0(determine_little_endian() == 1, "Machine is little endian.");

	assertLength("int16_t", 2, sizeof(int16_t));

	assertLength("byte", 1, sizeof(byte));
	assertLength("JBYTE", 1, sizeof(JBYTE));
	assertLength("JSHORT", 2, sizeof(JSHORT));
	assertLength("JINT", 4, sizeof(JINT));
	assertLength("TWOBYTES", 2, sizeof(TWOBYTES));
	assertLength("FOURBYTES", 4, sizeof(FOURBYTES));
	assertLength("REFERENCE", 4, sizeof(REFERENCE));

	getchar();

	checkObject();

	getchar();

	assertLength("MasterRecord", 16, sizeof(MasterRecord));
	assertLength("ClassRecord", 10, sizeof(ClassRecord));
	assertLength("MethodRecord", 12, sizeof(MethodRecord));
	assertLength("ExceptionRecord", 8, sizeof(ExceptionRecord));
	assertLength("ConstantRecord", 4, sizeof(ConstantRecord));

	getchar();

	assertLength("Thread", 31, sizeof(Thread));
	assertLength("Runtime", 4, sizeof(Runtime));
	assertLength("String", 14, sizeof(String));
	assertLength("StackFrame",20, sizeof(StackFrame));
	assertLength("JLONG", 8, sizeof(JLONG));

	getchar();
#if SEGMENTED_HEAP
		assertLength("MemoryRegion", 12, sizeof(MemoryRegion));
#else
		assertLength("MemoryRegion", 8, sizeof(MemoryRegion));
#endif

		// TODO: dai un occhio a AuxStackUnion e altre conversion unions/structs


		getchar();
}