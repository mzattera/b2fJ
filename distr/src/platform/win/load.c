/**
 * load.c
 * Loads binary into VM memory.
 */

#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#include <stdlib.h>
#include "constants.h"
#include "java_code.h"
#include "language.h"
#include "load.h"
#include "platform_config.h"
#include "platform_hooks.h"
#include "version.h"

#ifndef O_BINARY
#define O_BINARY 0
#endif

void readBinary (char *fileName)
{
	byte *pBinary;

	/* Maxi: Read it from a file, if provided, otherwise use linked java code */
	if (fileName == null) {
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
		printf ("Installing binary %d\n", (int) pBinary);
	#endif
	install_binary (pBinary);

	#if DEBUG_STARTUP
		printf ("Checking validity of magic number ^^^^\n");
	#endif
	if (get_master_record()->magicNumber != MAGIC_MASK)
	{
		printf ("Bad magic number: 0x%X", get_master_record()->magicNumber);
		exit_tool (NULL, -1); 
	}

	#if DEBUG_STARTUP
		printf ("Magic number OK!\n");
	#endif

}