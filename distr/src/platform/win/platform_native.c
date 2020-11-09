/**
* platform_native.c
* Native methods specific to a platform.
*/

#include <malloc.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "memory.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "trace.h"
#include "types.h"

/* Called before engine() is started and Java program executed */
void engine_start_hook()
{
	system("cls");
	printf("****   b2fJ v.%s   ****\n", VERSION);
	printf("  %5d Java bytes free\n\n", getHeapFree());
}


/*
* Called to exit the program.
* As last step, this should call exit() passing given exit code.
*/
void exit_tool(char* exitMessage, int exitCode)
{
	if (exitMessage)
		printf(exitMessage);
	//if (exitCode) {
		printf("Press any key...");
		getchar();
	//}
	exit(exitCode);
}


/*
* Called when thread is about to die due to an uncaught exception.
*/
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
	printf("Press any key...");
	getchar();
}


/* Called by assert(aCond,aCode) */
void assert_hook(bool aCond, int aCode)
{
	if (aCond)
		return;
	printf("Assertion violation: %d", aCode);
	exit_tool(NULL, aCode);
}