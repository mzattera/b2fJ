/**
* main() for b2fJ; should handle command line parameters, including loading of Java bytecode.
*/

#include <stdio.h>
#include <stdlib.h>
#include "constants.h"
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
void readBinary()
{
#if DEBUG_STARTUP
	printf("Installing binary %d\n");
#endif
	install_binary(javaClassFileContent);

#if DEBUG_STARTUP
	printf("Checking validity of magic number\n");
#endif
	if (get_master_record()->magicNumber != MAGIC_MASK)
	{
		printf("Bad magic number: 0x%X", get_master_record()->magicNumber);
		exit_tool(NULL, -1);
	}

#if DEBUG_STARTUP
	printf("Magic number OK!\n");
#endif
}


int main ()
{
#if DEBUG_STARTUP
	printf ("Reading binary %s\n", file);
#endif
	readBinary ();

#if DEBUG_STARTUP
	printf("Running...\n");
#endif

	run();
	exit_tool (NULL, 0);
	
	return 0;
} 
