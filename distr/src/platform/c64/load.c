/**
 * load.c
 * Loads binary into VM memory.
 */

#include "language.h"
#include "platform_config.h"
#include "platform_hooks.h"

#include <stdio.h>
#include <stdlib.h>
#include "version.h"
#include "java_code.h"

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
		printf ("Bad magic number: 0x%X", get_master_record()->magicNumber);
		exit_tool (NULL, -1); 
	}

	#if DEBUG_STARTUP
		printf ("Magic number OK!\n");
	#endif
}