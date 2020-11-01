/**
 * platform_native.c
 * Native methods specific to a platform.
 */
#include <stdio.h>
#include "constants.h"
#include "debug.h"
#include "types.h"
#include "memory.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "specialsignatures.h"
#include "stack.h"
#include "tvmemul.h"

/**
 * Converts a Java char into corresponding platform-dependent char (PETSCII).
 */
char int2nativeChar(int c)
{

	/* must swap upper / lower case letters */
	if (c >= 65 && c <= 90)
		return (c + 32);
	if (c >= 97 && c <= 122)
		return (c - 32);

	/* Special chars; represented with graphical PETSCII chars */
	if (c == 10) // New line
		return 13;
	if (c == 92) // backslash
		return 191;
	if (c == 95) // _
		return 164;
	if (c == 96) // `
		return 173;
	if (c == 123) // {
		return 179;
	if (c == 124) // |
		return 221;
	if (c == 125) // }
		return 171;
	if (c == 126) // ~
		return 177;

	return c;
}


/* 
 * Converts a Java String in a char[]
 */
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
			strBuffer[i] = int2nativeChar((int)pA[i]);
		}
		strBuffer[i] = 0;
		return strBuffer;
	}

	return ("null");
}

int dispatch_platform_native(TWOBYTES signature, STACKWORD *paramBase)
{
	switch (signature)
	{
	case putCharToStdout0_4I_5V:
		putc(int2nativeChar((int)paramBase[1]), stdout);
		return true;
	case putStringToStdout0_4Ljava_3lang_3String_2_5V:
		printf("%s", string2chp((String*)word2obj(paramBase[1])));
		return true;
	case peek_4I_5I:
		push_word(*((byte*)word2ptr(paramBase[0])));
		return true;
	case poke_4II_5V:
		*((byte*)word2ptr(paramBase[0])) = (byte)paramBase[1];
		return true;
	}
	
	return false;
}
