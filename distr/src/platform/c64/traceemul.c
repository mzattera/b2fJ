#include <stdio.h>

#include "platform_config.h"
#include "trace.h"

void assert_hook (boolean aCond, int aCode)
{
  if (aCond)
    return;
  printf ("Assertion violation: %d\n", aCode);
  getc(stdout);
  exit (aCode);
}

