/**
 * Entry source file for TinyVM emulator.
 */

#include <conio.h>
#include <stdio.h>
#include <stdlib.h>
#include "constants.h"
#include "load.h"
#include "tvmemul.h"
#include "version.h"

/***************************************************************************
 * int main
 * Parses command line. Format is:
 *	argv[0] [-v] bin_file
 *
 * options:
 *	-v	Verbose mode. Prints text output rather than raw output.
 *
 *--------------------------------------------------------------------------
 * To go into man page:
 * Name:	emu-lejosrun - Emulate lejos RCX code in Unix
 *
 * Synosis:	emu-lejosrun [-v] bin_file
 *
 * Description:	Executes a binary file created by the lejos compiler within
 *		Unix rather than in the RCX environment. The Java byte-codes
 *		are executed here, and their actions are listed rather than
 *		executed as they would be on the real RCX device.
 *
 * Options:	-v	Verbose mode. Normally the output is printed in raw
 *			mode. The actual hex values are printed. Using this
 *			option displays more user-friendly output.
 *--------------------------------------------------------------------------
 ***************************************************************************/
int main (int argc, char *argv[])
{
	// Name of the file to use. Leave null to use code linked directly in memory.
	char *file = null;

	clrscr();
	printf("\n");
	printf("    ****   b2fJ JAVA v. %s   ****\n\n", VERSION);

	if (argc == 2) {
		file = argv[1];
		printf("-> %s\n\n", file);
	}

#if DEBUG_STARTUP
	printf ("Reading binary %s\n", file);
#endif
	readBinary (file);

#if DEBUG_STARTUP
	printf("Running...\n");
#endif

	run();
	getc(stdin);
	exit(0);
} 
