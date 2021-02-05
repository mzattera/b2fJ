/**
 * nativeemul.c
 * Native method handling for unix_impl (emulation).
 */
#include <stddef.h>
#include <stdio.h>
#include "classes.h"
#include "conversion.h"
#include "exceptions.h"
#include "interpreter.h"
#include "types.h"
#include "memory.h"
#include "nativeemul.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "specialsignatures.h"
#include "stack.h"
#include "threads.h"


 /**
  * NOTE: The technique is not the same as that used in TinyVM.
  */
void dispatch_native(TWOBYTES signature, STACKWORD *paramBase)
{
	/* ClassRecord	*classRecord; */

	/* Maxi: First check wether there is a specific platform implementation for a method. */
	if (dispatch_platform_native(signature, paramBase))
		return;

	/* If not, use standard code */
	switch (signature)
	{
	case wait_4_5V:
		monitor_wait((Object*)word2ptr(paramBase[0]), 0);
		return;
	case wait_4J_5V:
		monitor_wait((Object*)word2ptr(paramBase[0]), paramBase[2]);
		return;
	case getDataAddress_4Ljava_3lang_3Object_2_5I:
		push_word(ptr2word(((byte *)word2ptr(paramBase[0])) + HEADER_SIZE));
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
	case floatToRawIntBits_4F_5I:
		/* Fall through */
	case intBitsToFloat_4I_5F:
		push_word(paramBase[0]);
		return;
	case putCharToStdout0_4I_5V:
		putc((int)paramBase[0], stdout);
		return;
	case getCharFromStdin0_4_5I:
		push_word(getc(stdin));
		return;
	case putStringToStdout0_4Ljava_3lang_3String_2_5V:
	{
		String* s = (String*)word2obj(paramBase[0]);
		if ((s != NULL) && (s->characters)) {
			Object *obj = word2obj(get_word((byte*)(&(s->characters)), 4));
			JCHAR *pA = jchar_array(obj);
			int length = get_array_length(obj);
			int i = 0;
			for (; i < length; ++i) {
				putc((int)pA[i], stdout);
			}
		}
	}
	return;
#if DEBUG_JAVA
	case dump0_4Ljava_3lang_3Object_2_5V:
	{
		Object* o = (Object*)word2obj(paramBase[0]);
		printf("adr %p %u \n", o, ((unsigned int)o + HEADER_SIZE));
		printf("all %x \n", o->flags.all);
		printf("siz %x \n", o->flags.freeBlock.size);
		printf("alo %x \n", o->flags.freeBlock.isAllocated);
		printf("cls %x \n", o->flags.objects.class);
		printf("pad %x \n", o->flags.objects.padding);
		printf("mrk %x \n", o->flags.objects.mark);
		printf("arr %x \n", o->flags.objects.isArray);
		printf("alo %x \n", o->flags.objects.isAllocated);
		printf("len %x \n", o->flags.arrays.length);
		printf("typ %x \n", o->flags.arrays.type);
		printf("mrk %x \n", o->flags.arrays.mark);
		printf("arr %x \n", o->flags.arrays.isArray);
		printf("alo %x \n", o->flags.arrays.isAllocated);
	}
	return;
#endif
	default:
#if DEBUG_METHODS
		printf("Received bad native method code: %d\n", signature);
#endif
		throw_exception(noSuchMethodError);
	}
}
