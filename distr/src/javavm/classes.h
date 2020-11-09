/**
 * classes.h
 * Contains conterparts of special classes as C structs.
 */

#ifndef _CLASSES_H
#define _CLASSES_H

#include "platform_config.h"
#include "types.h"

#define CLASS_MASK      0x00FF
#define CLASS_SHIFT     0

#define GC_MASK         0x2000
#define GC_SHIFT        13

#define IS_ARRAY_MASK   0x4000
#define IS_ARRAY_SHIFT  14

#define IS_ALLOCATED_MASK  0x8000
#define IS_ALLOCATED_SHIFT 15

#define ARRAY_LENGTH_MASK  0x01FF
#define ARRAY_LENGTH_SHIFT 0

#define ELEM_TYPE_MASK  0x1E00
#define ELEM_TYPE_SHIFT 9

#define FREE_BLOCK_SIZE_MASK 0x7FFF
#define FREE_BLOCK_SIZE_SHIFT 0

#define is_array(OBJ_)           ((OBJ_)->flags.objects.isArray)
#define is_allocated(OBJ_)       ((OBJ_)->flags.freeBlock.isAllocated)
#define get_monitor_count(OBJ_)  ((OBJ_)->monitorCount)
#define is_gc(OBJ_)              ((OBJ_)->flags.objects.mark)

 /**
  * Object class native structure
  */
typedef __PACKED(struct S_Object
{
	/**
	 * Object/block flags.
	 * Free block:
	 *  -- bits 0-14: Size of free block in words.
	 *  -- bit 15   : Zero (not allocated).
	 * Objects:
	 *  -- bits 0-7 : Class index.
	 *  -- bits 8-12: Unused.
	 *  -- bit 13   : Garbage collection mark.
	 *  -- bit 14   : Zero (not an array).
	 *  -- bit 15   : One (allocated).
	 * Arrays:
	 *  -- bits 0-8 : Array length (0-511).
	 *  -- bits 9-12: Element type.
	 *  -- bit 13   : Garbage collection mark.
	 *  -- bit 14   : One (is an array).
	 *  -- bit 15   : One (allocated).
	 */
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

	/**
	 * Synchronization state.
	 */
	byte monitorCount;
	byte threadId;

}) Object;

/**
 * Thread class native structure
 */
typedef __PACKED(struct S_Thread
{
	Object _super;	     // Superclass object storage

	REFERENCE nextThread;      // Intrinsic circular list of threads
	JINT waitingOn;            // Object who's monitor we want
	JINT sleepUntil;           // Time to wake up
	JINT stackFrameArray;      // Array of stack frames
	JINT stackArray;           // The stack itself
	JBYTE stackFrameArraySize; // Number of stack frames in use.
	JBYTE monitorCount;        // Saved monitor depth for context switches
	JBYTE threadId;            // Unique thread ID
	JBYTE state;               // RUNNING, DEAD, etc.
	JBYTE priority;            // The priority
	JBYTE interruptState;      // INTERRUPT_CLEARED, INTERRUPT_REQUESTED, ...
	JBYTE daemon;              // true == daemon thread
}) Thread;

/**
 * Runtime class native structure. Doesn't actually contain
 * any instance data. Maybe it ought to? Like ALL of the leJOS
 * specific runtime instance data?
 */
typedef __PACKED(struct S_Runtime
{
	Object _super;
}) Runtime;

/**
 * String class native structure
 */
typedef __PACKED(struct S_String
{
	Object _super;

	REFERENCE characters;

	/*
	Maxi: Original leJos code creates a new String each time a String constant is used (ldc bytecode).
	So, for example, a loop that prints always the same string will generate an OutOfMemoryException.
	To avoid this, we link allocated Java Strings in a list and we store a pointer to constants from which they were created.
	When we need to create a new String, we check in the list first, if a String for given constant is already allocated and re-use it.
	BTW this aligns b2fJ with Java behavior.
	*/
	/* !!! NOTE !!! To have these values accessible form Java we should use get_word() and set_word() */
	/* !!! NOTE !!! We don't do this for performance, as these values are useless from Java */
	REFERENCE next;
	JSHORT constantOffset;
}) String;

#if WIMPY_MATH

static __INLINED TWOBYTES get_array_length(Object *obj)
{
	TWOBYTES aux;
	aux = obj->flags.all & ARRAY_LENGTH_MASK;
#if (ARRAY_LENGTH_SHIFT == 0)
	return aux;
#else
	return (aux >> ARRAY_LENGTH_SHIFT);
#endif
}

static __INLINED TWOBYTES get_element_type(Object *obj)
{
	TWOBYTES aux;
	aux = obj->flags.all & ELEM_TYPE_MASK;
#if (ELEM_TYPE_SHIFT == 0)
	return aux;
#else
	return (aux >> ELEM_TYPE_SHIFT);
#endif
}

static __INLINED TWOBYTES get_na_class_index(Object *obj)
{
	TWOBYTES aux;
	aux = obj->flags.all & CLASS_MASK;
#if (CLASS_SHIFT == 0)
	return aux;
#else
	return (aux >> CLASS_SHIFT);
#endif
}

#else

#ifndef notdef
#define get_array_length(ARR_)   (((ARR_)->flags.all & ARRAY_LENGTH_MASK) >> ARRAY_LENGTH_SHIFT)
#define get_element_type(ARR_)   ((ARR_)->flags.all & ELEM_TYPE_MASK) >> ELEM_TYPE_SHIFT
#define get_na_class_index(OBJ_) (((OBJ_)->flags.all & CLASS_MASK) >> CLASS_SHIFT)
#define get_free_length(OBJ_)    (((OBJ_)->flags.all & FREE_BLOCK_SIZE_MASK) >> FREE_BLOCK_SIZE_SHIFT)
#else
#define get_array_length(ARR_)   ((ARR_)->flags.arrays.length)
#define get_element_type(ARR_)   ((ARR_)->flags.arrays.type)
#define get_na_class_index(OBJ_) ((OBJ_)->flags.objects.class)
#define get_free_length(OBJ_)    ((OBJ_)->flags.freeBlock.size)
#endif

#endif // WIMPY_MATH

#endif // _CLASSES_H









