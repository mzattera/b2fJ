/**
 * Maxi: Util to verify Object fields are properly aligned.
 */
#include <stdio.h>
#include <stdbool.h>
#include "assert.h"
#include "classes.h"
#include "platform_config.h"

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
int main() {
	Object o;

	// Check lenght of fields
	assertLength("Object", 4, sizeof(o));
	assertLength("Object.freeBlock", sizeof(TWOBYTES), sizeof(o.flags.freeBlock));
	assertLength("Object.objects", sizeof(TWOBYTES), sizeof(o.flags.objects));
	assertLength("Object.arrays", sizeof(TWOBYTES), sizeof(o.flags.arrays));

	o.flags.all = 0x4001;
	//
	assert0(o.flags.all == 0x4001, "A1");
	//
	assert0(o.flags.freeBlock.size == 0x4001, "A2");
	assert0(o.flags.freeBlock.isAllocated == 0x0, "A3");
	//
	assert0(o.flags.objects.class == 0x0001, "A4");
	assert0(o.flags.objects.padding == 0x00, "A5");
	assert0(o.flags.objects.mark == 0, "A6");
	assert0(o.flags.objects.isArray == 1, "A7");
	assert0(o.flags.objects.isAllocated == 0x0, "A8");
	//
	assert0(o.flags.arrays.length == 0x0001, "A9");
	assert0(o.flags.arrays.type == 0x00, "A10");
	assert0(o.flags.arrays.mark == 0, "A11");
	assert0(o.flags.arrays.isArray == 1, "A12");
	assert0(o.flags.arrays.isAllocated == 0x0, "A13");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x8000;
	//
	assert0(o.flags.all == 0x8000, "A14");
	//
	assert0(o.flags.freeBlock.size == 0x0000, "A15");
	assert0(o.flags.freeBlock.isAllocated == 0x1, "A16");
	//
	assert0(o.flags.objects.class == 0x0000, "A17");
	assert0(o.flags.objects.padding == 0x00, "A18");
	assert0(o.flags.objects.mark == 0, "A19");
	assert0(o.flags.objects.isArray == 0, "A20");
	assert0(o.flags.objects.isAllocated == 0x1, "A21");
	//
	assert0(o.flags.arrays.length == 0x0000, "A22");
	assert0(o.flags.arrays.type == 0x00, "A23");
	assert0(o.flags.arrays.mark == 0, "A24");
	assert0(o.flags.arrays.isArray == 0, "A25");
	assert0(o.flags.arrays.isAllocated == 0x1, "A26");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x0101;
	//
	assert0(o.flags.all == 0x0101, "A27");
	//
	assert0(o.flags.freeBlock.size == 0x0101, "A28");
	assert0(o.flags.freeBlock.isAllocated == 0, "A29");
	//
	assert0(o.flags.objects.class == 0x0101, "A30");
	assert0(o.flags.objects.padding == 0x00, "A31");
	assert0(o.flags.objects.mark == 0, "A32");
	assert0(o.flags.objects.isArray == 0, "A33");
	assert0(o.flags.objects.isAllocated == 0, "A34");
	//
	assert0(o.flags.arrays.length == 0x0101, "A35");
	assert0(o.flags.arrays.type == 0x00, "A36");
	assert0(o.flags.arrays.mark == 0, "A37");
	assert0(o.flags.arrays.isArray == 0, "A38");
	assert0(o.flags.arrays.isAllocated == 0, "A39");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x1200;
	//
	assert0(o.flags.all == 0x1200, "A40");
	//
	assert0(o.flags.freeBlock.size == 0x1200, "A41");
	assert0(o.flags.freeBlock.isAllocated == 0, "A42");
	//
	assert0(o.flags.objects.class == 0x0000, "A43");
	assert0(o.flags.objects.padding == 0x09, "A44");
	assert0(o.flags.objects.mark == 0, "A45");
	assert0(o.flags.objects.isArray == 0, "A46");
	assert0(o.flags.objects.isAllocated == 0, "A47");
	//
	assert0(o.flags.arrays.length == 0x0000, "A48");
	assert0(o.flags.arrays.type == 0x09, "A49");
	assert0(o.flags.arrays.mark == 0, "A50");
	assert0(o.flags.arrays.isArray == 0, "A51");
	assert0(o.flags.arrays.isAllocated == 0, "A52");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x2000;
	//
	assert0(o.flags.all == 0x2000, "A53");
	//
	assert0(o.flags.freeBlock.size == 0x2000, "A54");
	assert0(o.flags.freeBlock.isAllocated == 0, "A55");
	//
	assert0(o.flags.objects.class == 0x0000, "A56");
	assert0(o.flags.objects.padding == 0x00, "A57");
	assert0(o.flags.objects.mark == 1, "A58");
	assert0(o.flags.objects.isArray == 0, "A59");
	assert0(o.flags.objects.isAllocated == 0, "A60");
	//
	assert0(o.flags.arrays.length == 0x0000, "A61");
	assert0(o.flags.arrays.type == 0x00, "A62");
	assert0(o.flags.arrays.mark == 1, "A63");
	assert0(o.flags.arrays.isArray == 0, "A64");
	assert0(o.flags.arrays.isAllocated == 0, "A65");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x4000;
	//
	assert0(o.flags.all == 0x4000, "A66");
	//
	assert0(o.flags.freeBlock.size == 0x4000, "A67");
	assert0(o.flags.freeBlock.isAllocated == 0, "A68");
	//
	assert0(o.flags.objects.class == 0x0000, "A69");
	assert0(o.flags.objects.padding == 0x00, "A70");
	assert0(o.flags.objects.mark == 0, "A71");
	assert0(o.flags.objects.isArray == 1, "A72");
	assert0(o.flags.objects.isAllocated == 0, "A73");
	//
	assert0(o.flags.arrays.length == 0x0000, "A74");
	assert0(o.flags.arrays.type == 0x00, "A75");
	assert0(o.flags.arrays.mark == 0, "A76");
	assert0(o.flags.arrays.isArray == 1, "A77");
	assert0(o.flags.arrays.isAllocated == 0, "A78");

	////////////////////////////////////////////////////////////////////

	o.flags.all = 0x8000;
	//
	assert0(o.flags.all == 0x8000, "A79");
	//
	assert0(o.flags.freeBlock.size == 0x0000, "A80");
	assert0(o.flags.freeBlock.isAllocated == 1, "A81");
	//
	assert0(o.flags.objects.class == 0x0000, "A82");
	assert0(o.flags.objects.padding == 0x00, "A83");
	assert0(o.flags.objects.mark == 0, "A84");
	assert0(o.flags.objects.isArray == 0, "A85");
	assert0(o.flags.objects.isAllocated == 1, "A86");
	//
	assert0(o.flags.arrays.length == 0x0000, "A87");
	assert0(o.flags.arrays.type == 0x00, "A88");
	assert0(o.flags.arrays.mark == 0, "A89");
	assert0(o.flags.arrays.isArray == 0, "A90");
	assert0(o.flags.arrays.isAllocated == 1, "A91");

	printf("Press a key...");
	getchar();
	return 0;
}