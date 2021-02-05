/**
 * Maxi: Util to validate length and alignment of classes.
 */
#include <stdio.h>
#include "assert.h"
#include "classes.h"
#include "platform_config.h"

int main(int argc, char *argv[])
{
	assertLength("byte", 1, sizeof(byte));
	assertLength("JBYTE", 1, sizeof(JBYTE));
	assertLength("JSHORT", 2, sizeof(JSHORT));
	assertLength("JINT", 4, sizeof(JINT));
	assertLength("JLONG", 8, sizeof(JLONG));
	assertLength("TWOBYTES", 2, sizeof(TWOBYTES));
	assertLength("FOURBYTES", 4, sizeof(FOURBYTES));
	assertLength("REFERENCE", 4, sizeof(REFERENCE));

	assertLength("Object", 4, sizeof(Object));
	assertLength("Thread", 31, sizeof(Thread));
	assertLength("Runtime", 4, sizeof(Runtime));
	assertLength("String", 14, sizeof(String));

	getchar();
	return 0;
}