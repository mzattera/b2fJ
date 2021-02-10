/**
 * platform_native.c
 * Native methods specific to a platform.
 */

#include <conio.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "constants.h"
#include "conversion.h"
#include "types.h"
#include "memory.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "specialsignatures.h"
#include "stack.h"
#include "trace.h"


/* Called before engine() is started and Java program executed */
void engine_start_hook()
{
	// clrscr();
	printf("\n      ****   b2fJ v.%s   ****\n", VERSION);
	printf("\n  64K RAM system %5d Java bytes free\n\n", getHeapFree());
	printf( " Heap Size: %5d\n", getHeapSize()); 

}


void exit_tool(char* exitMessage, int exitCode)
{
	if (exitMessage) {
		printf(exitMessage);
		getchar();
	}
	exit(exitCode);
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
	printf("--  Bytecode offset   : %u\n", (unsigned)pc - (int)get_code_ptr(methodRecord));
	getchar();
}


/**
 * Converts a Java char into corresponding platform-dependent char (ATASCII).
 */
char int2nativeChar(int c)
{

	/* must swap upper / lower case letters */

	/* Special chars; represented with graphical ATASCII chars */
	if (c == 10) // New line
		return 155;

	return c;
}


bool dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase)
{
	switch (signature)
	{
	case putCharToStdout0_4I_5V:
		putc(int2nativeChar((int)paramBase[0]), stdout);
		return true;
	case putStringToStdout0_4Ljava_3lang_3String_2_5V:
		{
			int length = 0;
			int i = 0;
			String* s = (String*)word2obj(paramBase[0]);
			if ((s != NULL) && (s->characters)) {
				Object *obj = word2obj(get_word((byte*)(&(s->characters)), 4));
				JCHAR *pA = jchar_array(obj);
				length = get_array_length(obj);
				for (i=0; i<length; ++i) {
					putc(int2nativeChar((int)pA[i]), stdout);
				}
			}
		}		
		return true;
	case peek_4I_5I:
		push_word(*((byte*)word2ptr(paramBase[0])));
		return true;
	case poke_4II_5V:
		*((byte*)word2ptr(paramBase[0])) = (byte)paramBase[1];
		return true;
	}
	
	/* Fallback to default native implementation */
	return false;
}


/* Called by assert(aCond,aCode) */
void assert_hook(bool aCond, int aCode)
{
	if (aCond)
		return;
	printf("Assertion violation: %d", aCode);
	exit_tool(NULL, aCode);
}