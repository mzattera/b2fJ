/**
 * load.c
 * Loads binary into VM memory.
 */

#include "language.h"
#include "platform_config.h"

#include <stdio.h>
#include <stdlib.h>
#include "version.h"
#include "java_code.h"

void abort_tool (char *msg, char *arg)
{
  printf (msg, arg);
  exit (1);
}

void readBinary (char *fileName)
{
	#if DEBUG_STARTUP
		printf ("Installing binary %d\n");
	#endif
	install_binary (javaClassFileContent);

	#if DEBUG_STARTUP
		printf ("Checking validity of magic number\n");
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