#include <stdio.h>
#include <stdbool.h>

void assert0(bool aCond, char* msg) {
	if (aCond)
		return;
	printf("FAIL> %s\n", msg);
}

void assertLength(char* structName, int expLen, int len) {
	printf("%s size is %d (%d)\n", structName, len, expLen);
	assert0((expLen == len), "Bad length.");
}