/**
* platform_native.c
* Native methods specific to a platform.
*/

#include <malloc.h>
#include <stdio.h>
#include <stdlib.h>
#include "constants.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "trace.h"
#include "types.h"


/*
 * Execute platform-specific native methods.
 */
int dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase)
{
	/* Fallback to default native implementation */
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


void handle_uncaught_exception(Object *exception,
	const Thread *thread,
	const MethodRecord *methodRecord,
	const MethodRecord *rootMethod,
	byte *pc)
{
	printf("*** UNCAUGHT EXCEPTION/ERROR: \n");
	printf("--  Exception class   : %u\n", (unsigned)get_class_index(exception));
	printf("--  Thread            : %u\n", (unsigned)thread->threadId);
	printf("--  Method signature  : %u\n", (unsigned)methodRecord->signatureId);
	printf("--  Root method sig.  : %u\n", (unsigned)rootMethod->signatureId);
	printf("--  Bytecode offset   : %u\n", (unsigned)pc -
		(int)get_code_ptr(methodRecord));
}


void exit_tool(char* exitMessage, int exitCode)
{
	if (exitMessage)
		printf(exitMessage);
//	if (exitCode)
	printf("Press any key...");
		getchar();
	exit(exitCode);
}


/* Called by assert(aCond,aCode) */
void assert_hook(boolean aCond, int aCode)
{
	if (aCond)
		return;
	printf("Assertion violation: %d", aCode);
	exit_tool(NULL, aCode);
}