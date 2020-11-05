/**
* platform_native.c
* Native methods specific to a platform.
*/

#include <malloc.h>
#include <stdio.h>
#include "constants.h"
#include "types.h"
#include "platform_config.h"
#include "platform_hooks.h"

/*
 * Execute platform-specific native methods.
 */
int dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase)
{
	return false;
}


/*
 * Returns size of maximum free memory heap block available (as TWOBYTES words).
 */
size_t get_max_block_size() {
	/* The below code is very Visual Studio specifc.
	   It walks through the heap to get size of biggest free block. 
	   
	   It seems to be walking only over allocated bloks thoug...

	_HEAPINFO hinfo;
	hinfo._pentry = NULL;
	size_t maxSize = 0;

	while (_heapwalk(&hinfo) == _HEAPOK)
	{
		if ((hinfo._useflag == _FREEENTRY) && (hinfo._size > maxSize))
			maxSize = hinfo._size;
	}
	
	return maxSize;
	*/

	return MAX_HEAP_SIZE;
}
