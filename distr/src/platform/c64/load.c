/**
 * load.c
 * Loads binary into VM memory.
 */

#include "language.h"
#include "platform_config.h"

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
//#include <unistd.h>
#include <sys/types.h>
//#include <sys/stat.h>
#include "version.h"
#include "java_code.h"

#ifndef O_BINARY
#define O_BINARY 0
#endif

void abort_tool (char *msg, char *arg)
{
  printf (msg, arg);
  exit (1);
}

void readBinary (char *fileName)
{
	byte *pBinary;

#if !USING_VS
	// if we are using cc65, we always use the linked java code
	pBinary = javaClassFileContent;
#else
	//otherwise, we read it from a file, if provided
	if (fileName == NULL) {
		pBinary = javaClassFileContent;
	}
	else {
		int pDesc;
		int pLength;
		int pTotal;
		int pNumRead;

		pDesc = open(fileName, O_RDONLY | O_BINARY);
		if (pDesc == -1)
			abort_tool("Unable to open %s\n", fileName);
		pLength = lseek(pDesc, 0, SEEK_END);
		lseek(pDesc, 0, SEEK_SET);
		pBinary = (void *)malloc(pLength);
		pTotal = 0;
		while (pTotal < pLength)
		{
			pNumRead = read(pDesc, pBinary + pTotal, pLength - pTotal);
			if (pNumRead == -1)
			{
				printf("Unexpected EOF\n");
				exit(1);
			}
			pTotal += pNumRead;
		}
	}
#endif // !USING_VS

  #if DEBUG_STARTUP
  printf ("Installing binary %d\n", (int) pBinary);
  #endif
  install_binary (pBinary);

  #if DEBUG_STARTUP
  printf ("Checking validity of magic number ^^^^\n");
  #endif
  if (get_master_record()->magicNumber != MAGIC_MASK)
  {
    printf ("Fatal: bad magic number: 0x%X.\n", get_master_record()->magicNumber);
    exit(1); 
  }

  #if DEBUG_STARTUP
  printf ("Magic number OK!\n");
  #endif
}