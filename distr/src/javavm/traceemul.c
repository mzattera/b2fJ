#include <stdio.h>
#include <stdlib.h>
#include "trace.h"
#include "types.h"

void assert_hook (boolean aCond, int aCode)
{
  if (aCond)
    return;
  printf ("Assertion violation: %d\n", aCode);
  exit (aCode);
}

