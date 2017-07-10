// ascii.cpp : Defines the entry point for the console application.
//

#include <stdio.h>


int main(int argc, char* argv[])
{
	int i;

	for (i = 0; i < 256; ++i) {
		printf("'%c', ", i);
		if ((i > 0) && ((i % 8) == 0)) printf("\n");
	}
	getc(stdin);
}

