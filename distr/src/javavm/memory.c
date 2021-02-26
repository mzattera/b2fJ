#include <stdbool.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "classes.h"
#include "constants.h"
#include "conversion.h"
#include "exceptions.h"
#include "specialsignatures.h"
#include "specialclasses.h"
#include "memory.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "threads.h"
#include "trace.h"
#include "stack.h"

#if ASSERTIONS_ENABLED
static bool memoryInitialized = false;
#endif

// Heap memory needs to be aligned to 4 bytes on ARM
// Value is in 2-byte units and must be a power of 2
// Should be 2 for ARM-32, 4 for ARM-64
#define MEMORY_ALIGNMENT 1

#define NULL_OFFSET 0xFFFF

// Size of stack frame in 2-byte words
#define NORM_SF_SIZE ((sizeof(StackFrame) + 1) / 2)

const byte typeSize[] = {
  4, // 0 == T_REFERENCE
  SF_SIZE, // 1 == T_STACKFRAME
  0, // 2
  0, // 3
  1, // 4 == T_BOOLEAN
  2, // 5 == T_CHAR
  4, // 6 == T_FLOAT
  8, // 7 == T_DOUBLE
  1, // 8 == T_BYTE
  2, // 9 == T_SHORT
  4, // 10 == T_INT
  8  // 11 == T_LONG
};

/**
 * Beginning of heap.
 */
#if SEGMENTED_HEAP
static MemoryRegion *memory_regions; /* list of regions */
#else
static MemoryRegion *region; /* list of regions */
#endif

static TWOBYTES memory_size;    /* total number of words in heap */
static TWOBYTES memory_free;    /* total number of free words in heap */

extern void deallocate(TWOBYTES *ptr, TWOBYTES size);
extern TWOBYTES *allocate(TWOBYTES size);
#if GARBAGE_COLLECTOR
Object *protectedRef[MAX_VM_REFS];
#endif

/**
 * @param numWords Number of 2-byte words used in allocating the object.
 */
#define initialize_state(OBJ_,NWORDS_) zero_mem(((TWOBYTES *) (OBJ_)) + NORM_OBJ_SIZE, (NWORDS_) - NORM_OBJ_SIZE)
#define get_object_size(OBJ_)          (get_class_record(get_na_class_index(OBJ_))->classSize)

#if GARBAGE_COLLECTOR
static void set_reference( TWOBYTES* ptr);
static void clr_reference( TWOBYTES* ptr);
#endif

 /**
  * Zeroes out memory.
  * @param ptr The starting address.
  * @param numWords Number of two-byte words to clear.
  */
void zero_mem(register TWOBYTES *ptr,register TWOBYTES numWords)
{
  TWOBYTES* end = ptr + numWords;

  while( ptr < end)
    *ptr++ = 0;
}

static __INLINED void set_array(Object *obj, const byte elemType, const TWOBYTES length)
{
#if ASSERTIONS_ENABLED
	assert(elemType <= (ELEM_TYPE_MASK >> ELEM_TYPE_SHIFT), MEMORY0);
	assert(length <= (ARRAY_LENGTH_MASK >> ARRAY_LENGTH_SHIFT), MEMORY1);
#endif
	obj->flags.all = IS_ALLOCATED_MASK | IS_ARRAY_MASK | ((TWOBYTES)elemType << ELEM_TYPE_SHIFT) | length;
#if ASSERTIONS_ENABLED
	/*
	union _flags
	{
	TWOBYTES all;
	struct _freeBlock
	{
	__TWOBYTE_BITFIELD size:15;
	__TWOBYTE_BITFIELD isAllocated:1;
	} freeBlock;
	struct _objects
	{
	__TWOBYTE_BITFIELD     class:9;
	__TWOBYTE_BITFIELD     padding:5;
	__TWOBYTE_BITFIELD     mark:1;
	__TWOBYTE_BITFIELD     isArray:1;
	__TWOBYTE_BITFIELD     isAllocated:1;
	} objects;
	struct _arrays
	{
	__TWOBYTE_BITFIELD length:9;
	__TWOBYTE_BITFIELD type:4;
	__TWOBYTE_BITFIELD mark:1;
	__TWOBYTE_BITFIELD isArray:1;
	__TWOBYTE_BITFIELD isAllocated:1;
	} arrays;
	} flags;
	 */
	 /*
	   printf("set_array()\n");
	   printf("sizeof(flags) %d\n", sizeof(obj->flags));
	   printf("sizeof(freeBlock) %d\n", sizeof(obj->flags.freeBlock));
	   printf("sizeof(objects) %d\n", sizeof(obj->flags.objects));
	   printf("sizeof(arrays) %d\n", sizeof(obj->flags.arrays));
	   printf("par elementType %d\n", elemType);
	   printf("par length %d\n", length);
	   printf("all: %d\n", obj->flags.all);
	   printf("isArray: %d\n", obj->flags.objects.isArray);
	   printf("length: %d\n", obj->flags.arrays.length);
	   printf("type: %d\n", obj->flags.arrays.type);
	   printf("mark: %d\n", obj->flags.arrays.mark);
	   printf("isArray: %d\n", obj->flags.arrays.isArray);
	   printf("isAllocated: %d\n", obj->flags.arrays.isAllocated);
	 */
	assert(is_array(obj), MEMORY3);
#endif
}

Object *memcheck_allocate(const TWOBYTES size)
{
	Object *ref;
	ref = (Object *)allocate(size);
	if (ref == JNULL)
	{
#if ASSERTIONS_ENABLED
		assert(outOfMemoryError != NULL, MEMORY5);
#endif
		throw_exception(outOfMemoryError);
		return JNULL;
	}
	ref->monitorCount = 0;
	ref->threadId = 0;
#if SAFE
	ref->flags.all = 0;
#endif
	return ref;
}

/**
 * Checks if the class needs to be initialized.
 * If so, the static initializer is dispatched.
 * Otherwise, an instance of the class is allocated.
 *
 * @param btAddr Back-track PC address, in case
 *               a static initializer needs to be invoked.
 * @return Object reference or <code>NULL</code> iff
 *         NullPointerException had to be thrown or
 *         static initializer had to be invoked.
 */
Object *new_object_checked(const byte classIndex, byte *btAddr)
{
#if 0
	trace(-1, classIndex, 0);
#endif
	if (dispatch_static_initializer(get_class_record(classIndex), btAddr))
	{
#if DEBUG_MEMORY
		printf("New object checked returning NULL\n");
#endif
		return JNULL;
	}
	return new_object_for_class(classIndex);
}

/**
 * Allocates and initializes the state of
 * an object. It does not dispatch static
 * initializers.
 */
Object *new_object_for_class(const byte classIndex)
{
	Object *ref;
	TWOBYTES instanceSize;

#if DEBUG_MEMORY
	printf("New object for class %d\n", classIndex);
#endif
	instanceSize = get_class_record(classIndex)->classSize;

	ref = memcheck_allocate(instanceSize);
	if (ref == NULL)
	{
#if DEBUG_MEMORY
		printf("New object for class returning NULL\n");
#endif
		return JNULL;
	}

	// Initialize default values

	ref->flags.all = IS_ALLOCATED_MASK | classIndex;
	initialize_state(ref, instanceSize);

#if DEBUG_OBJECTS || DEBUG_MEMORY
	printf("new_object_for_class: returning %d\n", (int)ref);
#endif

	return ref;
}

/**
 * Return the size in words of an array of the given type
 */
TWOBYTES comp_array_size(const TWOBYTES length, const byte elemType)
{
	return NORM_OBJ_SIZE + (((TWOBYTES)length * typeSize[elemType]) + 1) / 2;
}

/**
 * Allocates an array. The size of the array is NORM_OBJ_SIZE
 * plus the size necessary to contain <code>length</code> elements
 * of the given type.
 */
Object *new_primitive_array(const byte primitiveType, STACKWORD length)
{
	Object *ref;
	TWOBYTES allocSize;

	// Check if length is too large to be representable
	if (length > (ARRAY_LENGTH_MASK >> ARRAY_LENGTH_SHIFT))
	{
		throw_exception(outOfMemoryError);
		return JNULL;
	}
	allocSize = comp_array_size(length, primitiveType);
#if DEBUG_MEMORY
	printf("New array of type %d, length %ld\n", primitiveType, length);
#endif
	ref = memcheck_allocate(allocSize);
#if DEBUG_MEMORY
	printf("Array ptr=%d\n", (int)ref);
#endif
	if (ref == NULL)
		return JNULL;
	set_array(ref, primitiveType, length);
	initialize_state(ref, allocSize);
	return ref;
}

TWOBYTES get_array_size(Object *obj)
{
	return comp_array_size(get_array_length(obj),
		get_element_type(obj));
}

void free_array(Object *objectRef)
{
#if ASSERTIONS_ENABLED
	assert(is_array(objectRef), MEMORY7);
#endif // ASSERTIONS_ENABLED

	deallocate((TWOBYTES *)objectRef, get_array_size(objectRef));
}

#if !FIXED_STACK_SIZE
Object *reallocate_array(Object *obj, STACKWORD newlen)
{
	byte elemType = get_element_type(obj);
	Object *newArray = new_primitive_array(elemType, newlen);

	// If can't allocate new array, give in!
	if (newArray != JNULL)
	{
		// Copy old array to new
		memcpy(((byte *)newArray + HEADER_SIZE), ((byte *)obj + HEADER_SIZE), get_array_length(obj) * typeSize[elemType]);

		// Free old array
		free_array(obj);
	}

	return newArray;
}
#endif

/**
 * @param elemType Type of primitive element of multi-dimensional array.
 * @param totalDimensions Same as number of brackets in array class descriptor.
 * @param reqDimensions Number of requested dimensions for allocation.
 * @param numElemPtr Pointer to first dimension. Next dimension at numElemPtr+1.
 */
Object *new_multi_array(byte elemType, byte totalDimensions,
	byte reqDimensions, STACKWORD *numElemPtr)
{
	Object *ref;

#if WIMPY_MATH
	Object *aux;
	TWOBYTES ne;
#endif

#if ASSERTIONS_ENABLED
	assert(totalDimensions >= 1, MEMORY6);
	assert(reqDimensions <= totalDimensions, MEMORY8);
#endif

#if 0
	printf("new_multi_array (%d, %d, %d)\n", (int)elemType, (int)totalDimensions, (int)reqDimensions);
#endif

	if (reqDimensions == 0)
		return JNULL;

#if 0
	printf("num elements: %d\n", (int)*numElemPtr);
#endif

	if (totalDimensions == 1)
		return new_primitive_array(elemType, *numElemPtr);


	ref = new_primitive_array(T_REFERENCE, *numElemPtr);
	if (ref == JNULL)
		return JNULL;
#if GARBAGE_COLLECTOR
	// Make sure we protect each level from the gc. Once we have returned
	// the ref it will be protected by the level above.
	protectedRef[totalDimensions] = ref;
 #endif
	while ((*numElemPtr)--)
	{
#if WIMPY_MATH

		aux = new_multi_array(elemType, totalDimensions - 1, reqDimensions - 1,
			numElemPtr + 1);
		ne = *numElemPtr;
		ref_array(ref)[ne] = ptr2word(aux);

#else

		ref_array(ref)[*numElemPtr] = ptr2word(new_multi_array(elemType, totalDimensions - 1, reqDimensions - 1, numElemPtr + 1));

#endif // WIMPY_MATH
	}
#if GARBAGE_COLLECTOR
    protectedRef[totalDimensions] = JNULL;
#endif
	return ref;
}

/**
 * Native array copy method,
 * Copy the (partial) contents of one array to another
 * Placed here tp allow access to element size information.
 */
void arraycopy(Object *src, int srcOff, Object *dst, int dstOff, int len)
{
  int elemSize;
  int type;
  // printf("ArrayCopy!\n");
  // validate things
  if (src == null || dst == null)
  {
    throw_exception(nullPointerException);
    return;
  }
  if (!is_array(src) || !is_array(dst) || (get_element_type(src) != get_element_type(dst)))
  {
    throw_exception(illegalArgumentException);
    return;
  }
  if (srcOff < 0 || (srcOff + len > get_array_length(src)) ||
      dstOff < 0 || (dstOff + len > get_array_length(dst)))
  {
    throw_exception(arrayIndexOutOfBoundsException);
    return;
  }
  // and finally do the copy!
  type=get_element_type(src);
  elemSize = typeSize[type];
  memcpy(((byte *) dst + HEADER_SIZE) + dstOff*elemSize , ((byte *) src + HEADER_SIZE) + srcOff*elemSize, len*elemSize);
}


#if WIMPY_MATH

void store_word(byte *ptr, byte aSize, STACKWORD aWord)
{
	byte *wptr;
	byte ctr;

	wptr = &aWord;
	ctr = aSize;
	while (ctr--)
	{
#if LITTLE_ENDIAN
		ptr[ctr] = wptr[aSize - ctr - 1];
#else
		ptr[ctr] = wptr[ctr];
#endif // LITTLE_ENDIAN
	}
}

#else
/**
 * Problem here is bigendian v. littleendian. Java has its
 * words stored bigendian, intel is littleendian.
 * Now slightly optmized;
 */

STACKWORD get_word( byte *ptr, byte aSize)
{
  STACKWORD aWord = 0;
  byte *end = ptr + aSize;

  do
  {
    aWord = (aWord << 8) | (STACKWORD)(*ptr++);
  } while( ptr < end);
  
  return aWord;
}

void store_word( byte *ptr, byte aSize, STACKWORD aWord)
{
  byte* base = ptr;
  
  ptr += aSize;

  do
  {
    *--ptr = (byte) aWord;
    aWord >>= 8;
  }
  while( ptr > base);
}

#endif // WIMPY_MATH

typedef union
{
	struct
	{
		byte byte0;
		byte byte1;
		byte byte2;
		byte byte3;
	} st;
	STACKWORD word;
} AuxStackUnion;

void make_word(byte *ptr, byte aSize, STACKWORD *aWordPtr)
{
	// This switch statement is 
	// a workaround for a h8300-gcc bug.
	switch (aSize)
	{
	case 1:
		*aWordPtr = (JINT)(JBYTE)(ptr[0]);
		return;
	case 2:
		*aWordPtr = (JINT)(JSHORT)(((TWOBYTES)ptr[0] << 8) | (ptr[1]));
		return;
#if ASSERTIONS_ENABLED
	default:
		assert(aSize == 4, MEMORY9);
#endif // ASSERTIONS_ENABLED
	}
#if LITTLE_ENDIAN
	((AuxStackUnion *)aWordPtr)->st.byte3 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte2 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte1 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte0 = *ptr;
#else
	((AuxStackUnion *)aWordPtr)->st.byte0 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte1 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte2 = *ptr++;
	((AuxStackUnion *)aWordPtr)->st.byte3 = *ptr;
#endif
}


/*
* Initializes heap.
* Maxi: it also allocates memory fro the heap and calls a platform hook.
*/
void memory_init()
{
	/* size in words of next block to alocate */
	size_t size;

	byte *mem;

	memory_init_hook();

#if ASSERTIONS_ENABLED
	memoryInitialized = true;
#endif

#if SEGMENTED_HEAP
	memory_regions = NULL;
#endif
	memory_size = 0;
	memory_free = 0;

	size = get_max_block_size();
	if (size > MAX_HEAP_SIZE) size = MAX_HEAP_SIZE;

#if SEGMENTED_HEAP
	{
		/* Keeps allocating blocks until desired heap size is reached (or we ran out of memory) */
		size_t remaining = MAX_HEAP_SIZE;

		while ((remaining > 0) && (size > 0) && ((mem = (byte *)malloc(size * sizeof(TWOBYTES))) != NULL)) {
			memory_add_region(mem, mem + size * sizeof(TWOBYTES));
			remaining -= size;
			size = get_max_block_size();
			if (size > remaining) size = remaining;
		}

		if (remaining == MAX_HEAP_SIZE) {
			exit_tool ("Unable to allocate Java heap", -1);
		}
	}
#else
	/* Allocates biggest available block for Java heap*/
	size = get_max_block_size();
	if (size > MAX_HEAP_SIZE) size = MAX_HEAP_SIZE;
	mem = (size > 0) ? (byte *)malloc(size * sizeof(TWOBYTES)) : NULL;
	if (mem != NULL) {
		memory_add_region(mem, mem + size * sizeof(TWOBYTES));
	} else {
		exit_tool("Unable to allocate Java heap", -1);
	}
#endif
}

/**
 * @param region Beginning of region.
 * @param size Size of region in bytes.
 */
void memory_add_region(byte *start, byte *end)
{
#if SEGMENTED_HEAP
	MemoryRegion *region;
#endif
	TWOBYTES contents_size;

#if DEBUG_MEMORY
	printf("memory_add_region(%lu, %lu)\n", start, end);
#endif

	/* word align upwards */
	region = (MemoryRegion *)(((NATIVEWORD)start + 1) & ~1);

#if SEGMENTED_HEAP
	/* initialize region header */
	region->next = memory_regions;

	/* add to list */
	memory_regions = region;
#endif
  region->end = (TWOBYTES *) ((NATIVEWORD)end & ~1); /* 16-bit align
 downwards */
#if GARBAGE_COLLECTOR
  {
    /* To be able to quickly identify a reference like stack slot
       we use a dedicated referance bitmap. With alignment of 4 bytes
       the map is 32 times smaller then the heap. Let's allocate
       the map by lowering the region->end pointer by the map size.
       The map must be zeroed. */
    TWOBYTES bitmap_size;
    contents_size = region->end - &(region->contents);
	bitmap_size = (contents_size / (((MEMORY_ALIGNMENT * 2) * 8) + 1) + 2) & ~1;
    region->end -= bitmap_size;
    zero_mem( region->end, bitmap_size);
  }
#endif

	/* create free block in region */
	contents_size = region->end - &(region->contents);
	((Object*)&(region->contents))->flags.all = contents_size;

	/* memory accounting */
	memory_size += contents_size;
	memory_free += contents_size;

#if SEGMENTED_HEAP
#if DEBUG_MEMORY
	printf("Added memory region\n");
	printf("  start:          %5d\n", (int)start);
	printf("  end:            %5d\n", (int)end);
	printf("  region:         %5d\n", (int)region);
	printf("  region->next:   %5d\n", (int)region->next);
	printf("  region->end:    %5d\n", (int)region->end);
	printf("  memory_regions: %5d\n", (int)memory_regions);
	printf("  contents_size:  %5d\n", (int)contents_size);
#endif
#endif
#if DEBUG_MEMORY
	printf("Exit memory_add_region()\n");
	printf("  start:          %5d\n", (int)start);
	printf("  end:            %5d\n", (int)end);
	printf("  region:         %5d\n", (int)region);
	printf("  region->end:    %5d\n", (int)region->end);
	printf("  contents_size:  %5d\n", (int)contents_size);
#if SEGMENTED_HEAP
	printf("  memory_regions: %5d\n", (int)memory_regions);
	printf("  region->next:   %5d\n", (int)region->next);
#endif
	getc(stdin);
#endif
}


/**
 * @param size Size of block including header in 2-byte words.
 */
static TWOBYTES *try_allocate(TWOBYTES size)
{
#if SEGMENTED_HEAP
	MemoryRegion *region;
#endif
#if MEMORY_ALIGNMENT>1
  // Align memory to boundary appropriate for system  
  size = (size + (MEMORY_ALIGNMENT-1)) & ~(MEMORY_ALIGNMENT-1);
#endif
#if DEBUG_MEMORY
	printf("Allocate %d - free %d\n", size, memory_free - size);
#endif

#if SEGMENTED_HEAP
	for (region = memory_regions; region != NULL; region = region->next)
#endif
	{
		TWOBYTES *ptr = &(region->contents);
		TWOBYTES *regionTop = region->end;

		while (ptr < regionTop) {
			TWOBYTES blockHeader = *ptr;

			if (blockHeader & IS_ALLOCATED_MASK) {
				/* jump over allocated block */
				TWOBYTES s = (blockHeader & IS_ARRAY_MASK)
					? get_array_size((Object *)ptr)
					: get_object_size((Object *)ptr);
#if MEMORY_ALIGMENT>1
        		// Round up according to alignment (needed for ARM32)
        		s = (s + (MEMORY_ALIGNMENT-1)) & ~(MEMORY_ALIGNMENT-1);
#endif
				ptr += s;
			}
			else
			{
				if (size <= blockHeader) {
					/* allocate from this block */
#if GARBAGE_COLLECTOR == 0
#if COALESCE
					{
						TWOBYTES nextBlockHeader;

						/* NOTE: Theoretically there could be adjacent blocks
						   that are too small, so we never arrive here and fail.
						   However, in practice this should suffice as it keeps
						   the big block at the beginning in one piece.
						   Putting it here saves code space as we only have to
						   search through the heap once, and deallocat remains
						   simple.
						*/

						while (true) {
							TWOBYTES *next_ptr = ptr + blockHeader;
							nextBlockHeader = *next_ptr;
							if (next_ptr >= regionTop ||
								(nextBlockHeader & IS_ALLOCATED_MASK) != 0)
								break;
							blockHeader += nextBlockHeader;
							*ptr = blockHeader;
						}
					}
#endif
#endif
					if (size < blockHeader) {
						/* cut into two blocks */
						blockHeader -= size; /* first block gets remaining free space */
						*ptr = blockHeader;
						ptr += blockHeader; /* second block gets allocated */
#if SAFE
						*ptr = size;
#endif
						/* NOTE: allocating from the top downwards avoids most
								 searching through already allocated blocks */
					}
					memory_free -= size;
					
#if GARBAGE_COLLECTOR
					/* set the corresponding bit of the reference map */
                    set_reference( ptr);
#endif					
					return ptr;
				} else {
					/* continue searching */
					ptr += blockHeader;
				}
			}
		}
	}
	/* couldn't allocate block */
	return JNULL;
}

#if GARBAGE_COLLECTOR

TWOBYTES *allocate (TWOBYTES size)
{
  TWOBYTES *ptr = try_allocate( size);

  if( ptr == JNULL)
  {
    /* no memory left so run the garbage collector */
    garbage_collect();

    /* now try to allocate object again */
    ptr = try_allocate( size);
  }
 
  return ptr;
}

#else

TWOBYTES *allocate (TWOBYTES size)
{
	return try_allocate( size);
}

#endif

/**
 * @param size Must be exactly same size used in allocation.
 */
void deallocate(TWOBYTES *p, TWOBYTES size)
{
#if GARBAGE_COLLECTOR
  /* clear the corresponding bit of the reference map */
  clr_reference( p);
#endif
#if MEMORY_ALEGNMENT > 1
  // Align memory to boundary appropriate for system (ex. ARM needs to be 2 word aligned, and ARM64 4 words)
  size = (size + (MEMORY_ALIGNMENT-1)) & ~(MEMORY_ALIGNMENT-1);
#endif

#if ASSERTIONS_ENABLED
	assert(size <= (FREE_BLOCK_SIZE_MASK >> FREE_BLOCK_SIZE_SHIFT), MEMORY3);
#endif

	memory_free += size;

#if DEBUG_MEMORY
	printf("Deallocate %d at %d - free %d\n", size, (int)p, memory_free);
#endif

    ((Object*)p)->flags.all = size;

}


int getHeapSize() {
	return ((int)memory_size) << 1;
}


int getHeapFree() {
	return ((int)memory_free) << 1;
}

NATIVEWORD getRegionAddress()
{
#if SEGMENTED_HEAP
	return (NATIVEWORD)memory_regions->region;
#else
	return (NATIVEWORD)region;
#endif
}

#if GARBAGE_COLLECTOR
/**
 * The garbage collector implementation starts here.
 * It is a classic mark and sweep implementation.
 * The garbage collection triggers automaticly only
 * after runing out of memory or by a java gc method
 * invocation. Thus, if a program is a "well behaving" one,
 * the garbage collector remains dormant. It consumes 
 * about 1000 bytes of flash and about 1800 bytes of ram.
 * Although the algorithm used is simple, it's pretty fast.
 * A typical single gc run should take no more then 10 ms
 * to complete. Besides, the presence of garbage collector
 * does not impair the speed of program execution.
 * There is no reference tracing on writes and no stack
 * or object size increase.
 * Potential problems: long linked list may cause
 * processor stack overflow due to recursion.
 */

/**
 * Just a forward declaration.
 */
static void mark_object( Object *obj);

/**
 * "Mark" flag manipulation functions.
 */
static __INLINED void set_gc_marked( Object* obj)
{
  obj->flags.objects.mark = 1;
}

static __INLINED void clr_gc_marked( Object* obj)
{
  obj->flags.objects.mark = 0;
}

static __INLINED boolean is_gc_marked( Object* obj)
{
  return obj->flags.objects.mark != 0;
}

/**
 * Reference bitmap manipulation functions.
 * The bitmap is allocated at the end of the region.
 */
static void set_reference( TWOBYTES* ptr)
{
#if SEGMENTED_HEAP
  MemoryRegion *region;
  for (region = memory_regions; region != null; region = region->next)
  {
    TWOBYTES* regBottom = &(region->contents);
    TWOBYTES* regTop = region->end;

    if( ptr >= regBottom && ptr < regTop)
    {
#endif
      int refIndex = (int)(((byte*) ptr - (byte*)&(region->contents)) / (MEMORY_ALIGNMENT * 2));

      ((byte*) region->end)[ refIndex >> 3] |= 1 << (refIndex & 7);

#if SEGMENTED_HEAP
      return;
    }
  }
#endif
}

static void clr_reference( TWOBYTES* ptr)
{
#if SEGMENTED_HEAP
  MemoryRegion *region;
  for (region = memory_regions; region != null; region = region->next)
  {
    TWOBYTES* regBottom = &(region->contents);
    TWOBYTES* regTop = region->end;

    if( ptr >= regBottom && ptr < regTop)
    {
#endif
      int refIndex = (int)(((byte*) ptr - (byte*)&(region->contents)) / (MEMORY_ALIGNMENT * 2));

      ((byte*) region->end)[ refIndex >> 3] &= ~ (1 << (refIndex & 7));

#if SEGMENTED_HEAP
      return;
    }
  }
#endif
}

static boolean is_reference( TWOBYTES* ptr)
{
#if SEGMENTED_HEAP
  MemoryRegion *region;
  for (region = memory_regions; region != null; region = region->next)
#endif
  {
    /* The reference must belong to a memory region. */
    TWOBYTES* regBottom = &(region->contents);
    TWOBYTES* regTop = region->end;

    if( ptr >= regBottom && ptr < regTop)
    {
      /* It must be properly aligned */
      if( ((int)ptr & ((MEMORY_ALIGNMENT * 2) - 1)) == 0)
      {
        /* Now we can safely check the corresponding bit in the reference bitmap. */
        int refIndex = (int)(((byte*) ptr - (byte*)&(region->contents)) / (MEMORY_ALIGNMENT * 2));

        return (((byte*) region->end)[ refIndex >> 3] & (1 << (refIndex & 7))) != 0;
      }

      return false;
    }
  }

  return false;
}

/**
 * Scan static area of all classes. For every non-null reference field
 * call mark_object function.
 */
static void mark_static_objects( void)
{
  MasterRecord* mrec = get_master_record();
  STATICFIELD* staticFieldBase = (STATICFIELD*) get_static_fields_base();
  byte* staticStateBase = get_static_state_base();
  byte* staticState = staticStateBase;
  byte* staticEnd = staticStateBase + mrec->staticStateLength * 2 - 1;
  int idx = 0;

  while( staticState < staticEnd)
  {
    STATICFIELD fieldRecord = staticFieldBase[ idx ++];
    byte fieldType = (fieldRecord >> 12) & 0x0F;
    byte fieldSize = typeSize[ fieldType];

    if( fieldType == T_REFERENCE)
    {
      Object* obj = (Object*) word2obj(get_word( staticState, 4));
      if( obj != NULL)
        mark_object( obj);
    }

    staticState += fieldSize;
  }
}

/**
 * Scan slot stacks of threads (local variables and method params).
 * For every slot containing reference value call the mark_object
 * function. Additionally, call this function for the thread itself,
 * for both its stacks and optionly for monitor object. This allows
 * avoiding garbage-collecting them.
 */
static void mark_local_objects()
{
  int i;
  // Make sure the stack frame for the current thread is up to date.
  StackFrame *currentFrame = current_stackframe();
  if (currentFrame != null) update_stack_frame(currentFrame);
  // If needed make sure we protect the VM temporary references
  for(i=0; i < MAX_VM_REFS; i++)
    if (protectedRef[i] != JNULL) mark_object(protectedRef[i]);

  for( i = 0; i < MAX_PRIORITY; i ++)
  {
    Thread* th0 = threadQ[ i];
    Thread* th = th0;

    while( th != NULL)
    {
      byte arraySize;

      mark_object( (Object*) th);
      mark_object( (Object*) ref2obj(th->stackArray));
      mark_object( (Object*) ref2obj(th->stackFrameArray));

      if( th->waitingOn != 0)
        mark_object( (Object*) ref2obj(th->waitingOn));

      arraySize = th->stackFrameArraySize;
      if( arraySize != 0)
      {
        Object* sfObj = word2ptr( th->stackFrameArray);
        StackFrame* stackFrame = ((StackFrame*) array_start( sfObj)) + (arraySize - 1);
        Object* saObj = word2ptr( th->stackArray);
        STACKWORD* stackBottom = (STACKWORD*) jint_array( saObj);
        STACKWORD* stackTop = stackFrame->stackTop;
        STACKWORD* sp;
    
        for( sp = stackBottom; sp <= stackTop; sp ++)
        {
          TWOBYTES* ptr = word2ptr( *sp);

          if( is_reference( ptr))
          {
            /* Now we know that ptr points to a valid allocated object.
               It does not mean, that this slot contains a reference variable.
               It may be an integer or float variable, which has exactly the
               same value as a reference of one of the allocated objects.
               But it is no harm. We can safely "mark" it, In such a case
               we may just leave an unreachable object uncollected. */

            mark_object( (Object*) ptr);
          }
        }
      }

      th = word2ptr( th->nextThread);
      if( th == th0)
        break;
    }
  }
}

/**
 * Scan member fields of class instance, and for every
 * non-null reference field call the mark_object function.
 */
static void mark_reference_fields( Object* obj)
{
  byte classIndex = get_na_class_index( obj);
  ClassRecord* classRecord;
  byte classIndexTable[ 16];
  int classIndexTableIndex = 0;
  byte* statePtr;

  /* first we need to prepare a reversed order of inheritance */

  for(;;)
  {
    if( classIndex == JAVA_LANG_OBJECT)
      break;

    classRecord = get_class_record( classIndex);

    if( classRecord->numInstanceFields)
      classIndexTable[ classIndexTableIndex ++] = classIndex;

    classIndex = classRecord->parentClass;
  } 

  /* now we can scan the member fields */

  statePtr = (byte*) (((TWOBYTES *) (obj)) + NORM_OBJ_SIZE);

  while( -- classIndexTableIndex >= 0)
  {
    classIndex = classIndexTable[ classIndexTableIndex];
    classRecord = get_class_record (classIndex);

    if( classRecord->numInstanceFields)
    {
      int i;

      for( i = 0; i < classRecord->numInstanceFields; i++)
      {
        byte fieldType = get_field_type( classRecord, i);
        byte fieldSize = typeSize[ fieldType];

        if( fieldType == T_REFERENCE)
        {
          /* omit nextThread field of Thread class */

          if( ! (classIndex == JAVA_LANG_THREAD && i == 0))
          {
            STACKWORD word=get_word(statePtr, 4);

#if DEBUG_MAPPING
              if(word>(NATIVEWORD)0xFFFF) {
                  printf("\nInvalid reference beyond than heap limits, please debug check obj=%p 0x%0xd",obj,word);
                  word=0x0000;
              }
#endif
              
            Object* robj = (Object*) word2obj(word);
              
#if DEBUG_COLLECTOR
              // Aparently we found an uninitialized reference field
              // where the value pointed by statePtr has an incorrect value under some circumstances
              if(word!=NULL && !is_reference((void*)robj)) {
              printf("\nInvalid reference beyond the heap limits, please debug check obj=%p 0x%0xd robj=%p\n",obj,word,robj);
              robj=null;
            }
#endif

            if( robj != NULL && is_reference((void*)robj)) {
                mark_object( robj);
            }
            
          }
        }

        statePtr += fieldSize;
      }
    }
  }
}

/**
 * A function which performs a "mark" operation for an object.
 * If it is an array of references recursively call mark_object
 * for every non-null array element.
 * Otherwise "mark" every non-null reference field of that object.
 */
static void mark_object( Object *obj)
{
  if( is_gc_marked( obj))
    return;

  set_gc_marked( obj);

  if( is_array( obj))
  {
    if( get_element_type( obj) == T_REFERENCE)
    {
      REFERENCE* refarr = ref_array( obj);
      REFERENCE* refarrend = refarr + get_array_length( obj);
      
      while( refarr < refarrend)
      {
        Object* obj = (Object*) ref2obj((*refarr ++));
          if( obj != NULL) {
              mark_object( obj);
          }
      }
    }
  }
  else
    mark_reference_fields( obj);
}

/**
 * A function which performs a "sweep" operation for an object.
 * If it's "marked" clear the mark. Otherwise delete the object.
 * For safety omit objects with active monitor.
 */
static void sweep_object( Object *obj, TWOBYTES size)
{
  if( is_gc_marked( obj))
    clr_gc_marked( obj);
  else
  if( get_monitor_count( obj) == 0)
    deallocate( (TWOBYTES*) obj, size);
}

/**
 * Scan heap objects and for every allocated object call
 * the sweep_object function.
 */
static void sweep_heap_objects( void)
{
#if SEGMENTED_HEAP
  MemoryRegion *region;
  for (region = memory_regions; region != null; region = region->next)
#endif
  {
    TWOBYTES* ptr = &(region->contents);
    TWOBYTES* fptr = null;
    TWOBYTES* regionTop = region->end;
    while( ptr < regionTop)
    {
      TWOBYTES blockHeader = *ptr;
      TWOBYTES size;

      if( blockHeader & IS_ALLOCATED_MASK)
      {
        /* jump over allocated block */
        size = (blockHeader & IS_ARRAY_MASK) ? get_array_size ((Object *) ptr)
                                             : get_object_size ((Object *) ptr);
        // Round up according to alignment
#if MEMORY_ALIGNMENT>1
        size = (size + (MEMORY_ALIGNMENT-1)) & ~(MEMORY_ALIGNMENT-1);
#endif
        sweep_object( (Object*) ptr, size);
        blockHeader = *ptr;
      }
      else
      {
        /* continue searching */
        size = blockHeader;
      }
      if( !(blockHeader & IS_ALLOCATED_MASK))
      {
        // Got a free block can we merge?
        if (fptr != null)
          *fptr += size;
        else
          fptr = ptr;
      }
      else
          fptr = null;

      ptr += size;

    }
  }
}

/**
 * "Mark" preallocated instances of exception objects
 * to avoid garbage-collecting them.
 */
static void mark_exception_objects( void)
{
  mark_object( outOfMemoryError);
  mark_object( noSuchMethodError);
  mark_object( stackOverflowError);
  mark_object( nullPointerException);
  mark_object( classCastException);
  mark_object( arithmeticException);
  mark_object( arrayIndexOutOfBoundsException);
  mark_object( illegalArgumentException);
  mark_object( interruptedException);
  mark_object( illegalStateException);
  mark_object( illegalMonitorStateException);
  mark_object( error);
}

/**
 * Main garbage collecting function.
 * Perform "mark" operation for internal objects,
 * class static areas and thread local areas.
 * After that perform a "sweep" operation for
 * every "unmarked" heap object.
 */
void garbage_collect( void)
{

#if DEBUG_COLLECTOR
	printf("Heap Size:%5d\n",getHeapSize());
	printf("Initial Free:%5d\n",getHeapFree());
	printf("mark_exception_objects\n");
#endif

  mark_exception_objects();

#if DEBUG_COLLECTOR
	printf("mark_static_objects\n");
#endif

  mark_static_objects();

#if DEBUG_COLLECTOR
	printf("mark_local_objects\n");
#endif

  mark_local_objects();

#if DEBUG_COLLECTOR
	printf("sweep_heap_objects\n");
#endif

  sweep_heap_objects();

#if DEBUG_COLLECTOR
	printf("garbage_collect completed\n");
	printf("Final Free:%5d\n",getHeapFree());
#endif

}

#else

void garbage_collect( void)
{
}

#endif // GARBAGE_COLLECTOR

