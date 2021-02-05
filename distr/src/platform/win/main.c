/**
 * main() for b2fJ; should handle command line parameters, including loading of Java bytecode.
 */

#include <fcntl.h>
#include <io.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "java_code.h"
#include "language.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "tvmemul.h"
#include "version.h"


/**
 * Read Java bytecode from given file.
 * If the file name is NULL, it means bytecode is linked directly in a byte[].
 */
void readBinary(char *fileName)
{
	byte *pBinary;

	if (fileName == NULL) {
		pBinary = javaClassFileContent;
	}
	else {
		int pDesc;
		int pLength;
		int pTotal;
		int pNumRead;

		pDesc = open(fileName, O_RDONLY | O_BINARY);
		if (pDesc == -1) {
			printf("Unable to open %s\n", fileName);
			exit_tool(NULL, -1);
		}
		pLength = lseek(pDesc, 0, SEEK_END);
		lseek(pDesc, 0, SEEK_SET);
		pBinary = (void *)malloc(pLength);
		pTotal = 0;
		while (pTotal < pLength)
		{
			pNumRead = read(pDesc, pBinary + pTotal, pLength - pTotal);
			if (pNumRead == -1)
			{
				exit_tool("Unexpected EOF", -1);
			}
			pTotal += pNumRead;
		}
	}

#if DEBUG_STARTUP
	printf("Installing binary %d\n", (int)pBinary);
#endif
	install_binary(pBinary);

#if DEBUG_STARTUP
	printf("Checking validity of magic number ^^^^\n");
#endif
	if (get_master_record()->magicNumber != MAGIC_MASK)
	{
		printf("Bad magic number: 0x%X", get_master_record()->magicNumber);
		exit_tool(NULL, -1);
	}
}


int main (int argc, char *argv[])
{
	// Name of the file to use. Leave NULL to use code linked directly in memory.
	char *file = NULL;

	if (argc == 2) {
		file = argv[1];
	}

#if DEBUG_STARTUP
	printf ("Reading binary %s\n", file);
#endif
	readBinary (file);

#if DEBUG_STARTUP
	printf("Running...\n");
#endif
	run();
	exit_tool (NULL, 0);
} 
