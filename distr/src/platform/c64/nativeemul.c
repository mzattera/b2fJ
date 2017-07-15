/**
 * nativeemul.c
 * Native method handling for unix_impl (emulation).
 */
#include <stdio.h>
#include <stdlib.h>
#include "types.h"
#include "trace.h"
#include "constants.h"
#include "specialsignatures.h"
#include "specialclasses.h"
#include "stack.h"
#include "memory.h"
#include "threads.h"
#include "classes.h"
#include "language.h"
#include "configure.h"
#include "interpreter.h"
#include "exceptions.h"
#include "platform_config.h"

extern byte *region;

#if USING_VS
int t = 33;
void *ptr;
#endif

extern char* strBuffer;

/*
Conversion Table.

http://www.ascii-code.com/

This is a trick in a trick in that cc65 will convert each ASCII character below into the 
corresponding CBM charstet character at compile time, therefore displying the right char at the end.

TODO: do conversion at compile time with jtools.
*/
char charMap[] = {
	'\0', 1, 2, 3, 4, 5, 6, 7, 8,
	'\t', '\n', 11, 12, 13, 14, 15, 16,
	17, 18, 19, 20, 21, 22, 23, 24,
	25, 26, 27, 28, 29, 30, 31, ' ',
	'!', '"', '#', '$', '%', '&', '\'', '(',
	')', '*', '+', ',', '-', '.', '/', '0',
	'1', '2', '3', '4', '5', '6', '7', '8',
	'9', ':', ';', '<', '=', '>', '?', '@',
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
	'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
	'Y', 'Z', '[', '\\', ']', '^', '_', '`',
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
	'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
	'y', 'z', '{', '|', '}', '~', 127
};

// Convert a Java String in a char[]
char* string2chp(String* s)
{
	if ((s != null) && (s->characters))
	{
		Object *obj;
		JCHAR *pA;
		int i;
		int length;

		obj = word2obj(get_word((byte*)(&(s->characters)), 4));
		pA = jchar_array(obj);
		length = get_array_length(obj);
		if (length > STRING_BUF_SIZE) length = STRING_BUF_SIZE;
		for (i = 0; i < length; i++)
		{
#if USING_VS
			strBuffer[i] = (char)pA[i];
#else
			if (pA[i] < 128)
				strBuffer[i] = charMap[(int)pA[i]];
			else
				strBuffer[i] = (char)pA[i];
#endif
		}
		strBuffer[i] = 0;
		return strBuffer;
	}

	return ("null");
}


/**
 * NOTE: The technique is not the same as that used in TinyVM.
 */
void dispatch_native(TWOBYTES signature, STACKWORD *paramBase)
{
	ClassRecord	*classRecord;

	switch (signature)
	{
	case wait_4_5V:
		monitor_wait((Object*)word2ptr(paramBase[0]), 0);
		return;
	case wait_4J_5V:
		monitor_wait((Object*)word2ptr(paramBase[0]), paramBase[2]);
		return;
	case notify_4_5V:
		monitor_notify((Object*)word2ptr(paramBase[0]), false);
		return;
	case notifyAll_4_5V:
		monitor_notify((Object*)word2ptr(paramBase[0]), true);
		return;
	case start_4_5V:
		init_thread((Thread *)word2ptr(paramBase[0]));
		return;
	case yield_4_5V:
		schedule_request(REQUEST_SWITCH_THREAD);
		return;
	case sleep_4J_5V:
		sleep_thread(paramBase[1]);
		schedule_request(REQUEST_SWITCH_THREAD);
		return;
	case getPriority_4_5I:
		push_word(get_thread_priority((Thread*)word2ptr(paramBase[0])));
		return;
	case setPriority_4I_5V:
	{
		STACKWORD p = (STACKWORD)paramBase[1];
		if (p > MAX_PRIORITY || p < MIN_PRIORITY)
			throw_exception(illegalArgumentException);
		else
			set_thread_priority((Thread*)word2ptr(paramBase[0]), p);
	}
	return;
	case currentThread_4_5Ljava_3lang_3Thread_2:
		push_ref(ptr2ref(currentThread));
		return;
	case interrupt_4_5V:
		interrupt_thread((Thread*)word2ptr(paramBase[0]));
		return;
	case interrupted_4_5Z:
	{
		JBYTE i = currentThread->interruptState != INTERRUPT_CLEARED;
		currentThread->interruptState = INTERRUPT_CLEARED;
		push_word(i);
	}
	return;
	case isInterrupted_4_5Z:
		push_word(((Thread*)word2ptr(paramBase[0]))->interruptState
			!= INTERRUPT_CLEARED);
		return;
	case setDaemon_4Z_5:
		((Thread*)word2ptr(paramBase[0]))->daemon = (JBYTE)paramBase[1];
		return;
	case isDaemon_4_5Z:
		push_word(((Thread*)word2ptr(paramBase[0]))->daemon);
		return;
	case join_4_5V:
		join_thread((Thread*)word2ptr(paramBase[0]));
		return;
	case join_4J_5V:
		join_thread((Thread*)word2obj(paramBase[0]));
		return;
	case exit_4I_5V:
		schedule_request(REQUEST_EXIT);
		return;
	case currentTimeMillis_4_5J:
		push_word(0);
		push_word(get_sys_time());
		return;
	case freeMemory_4_5J:
		push_word(0);
		push_word(getHeapFree());
		return;
	case totalMemory_4_5J:
		push_word(0);
		push_word(getHeapSize());
		return;
	case floatToRawIntBits_4F_5I: // Fall through
	case intBitsToFloat_4I_5F: // TODO: I don-t thik it works properly
		push_word(paramBase[0]);
		return;
	case putCharToStdout0_4I_5V:
	{
		int c = (int)paramBase[1];
#if !USING_VS
		if (c < 128)
			c = charMap[c];
#endif
		putc(c, stdout);
		return;
	}
	case peek_4I_5I:
#if USING_VS
		ptr = word2ptr(paramBase[0]);
		//printf("Peek %d = %d\n", (unsigned int)ptr - 53248, t);
		push_word(t);
#else
		push_word(*((byte*)word2ptr(paramBase[0])));
#endif
		return;
	case poke_4II_5V:
#if USING_VS
		t = (byte)paramBase[1];
		ptr = word2ptr(paramBase[0]);
		//printf("Poke %d, %d\n", (unsigned int)ptr-53248, t);
#else
		*((byte*)word2ptr(paramBase[0])) = (byte)paramBase[1];
#endif
		return;
	case putStringToStdout0_4Ljava_3lang_3String_2_5V:
		printf("%s", string2chp((String*)word2obj(paramBase[1])));
		return;
	default:
#ifdef DEBUG_METHODS
		printf("Received bad native method code: %d\n", signature);
#endif
		throw_exception(noSuchMethodError);
	}
}
