/**
 * Maxi: Util to validate length and alignment of structures.
 */
#include <stdio.h>
#include "assert.h"
#include "language.h"
#include "memory.h"
#include "threads.h"
#include "platform_config.h"

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

int main()
{
	assertLength("MasterRecord", 16, sizeof(MasterRecord));
	assertLength("ClassRecord", 10, sizeof(ClassRecord));
	assertLength("MethodRecord", 12, sizeof(MethodRecord));
	assertLength("ExceptionRecord", 8, sizeof(ExceptionRecord));
	assertLength("ConstantRecord", 4, sizeof(ConstantRecord));
	assertLength("StackFrame", 20, sizeof(StackFrame));
#if SEGMENTED_HEAP
	assertLength("MemoryRegion", 12, sizeof(MemoryRegion));
#else
	assertLength("MemoryRegion", 8, sizeof(MemoryRegion));
#endif

	getchar();
	return 0;
}