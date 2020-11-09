#include <stddef.h>
#include <stdbool.h>
#include "classes.h"
#include "constants.h"
#include "conversion.h"
#include "exceptions.h"
#include "interpreter.h"
#include "language.h"
#include "opcodes.h"
#include "platform_config.h"
#include "specialclasses.h"
#include "stack.h"
#include "threads.h"
#include "trace.h"
#include "types.h"

#define F_OFFSET_MASK  0x0F

#if DEBUG_BYTECODE
extern char *OPCODE_NAME[];
#endif

// Interpreter globals:

volatile bool gMakeRequest;
byte    gRequestCode;

byte *pc;
STACKWORD *localsBase;
STACKWORD *stackTop;

String* stringList = JNULL;

// Temporary globals:

byte tempByte;
byte *tempBytePtr;
JFLOAT tempFloat;
ConstantRecord *tempConstRec;
STACKWORD tempStackWord;
STACKWORD *tempWordPtr;
JSHORT tempInt;

/**
 * Assumes pc points to 2-byte offset, and jumps.
 */
void do_goto(bool aCond)
{
	if (aCond)
	{
		pc += (JSHORT)(((TWOBYTES)*pc << 8) | *(pc + 1));
		pc--;
	}
	else
	{
		pc += 2;
	}
}

void do_isub(void)
{
	STACKWORD poppedWord;

	poppedWord = pop_word();
	set_top_word(word2jint(get_top_word()) - word2jint(poppedWord));
}

#if FP_ARITHMETIC

void do_fcmp(JFLOAT f1, JFLOAT f2, STACKWORD def)
{
	if (f1 > f2)
		push_word(1);
	else if (f1 == f2)
		push_word(0);
	else if (f1 < f2)
		push_word(-1);
	else
		push_word(def);
}

#endif

/**
 * @return A String instance, or JNULL if an exception was thrown
 *         or the static initializer of String had to be executed.
 */
static __INLINED Object *create_string(ConstantRecord *constantRecord,
	byte *btAddr)
{
	Object *ref;
	Object *arr;
	TWOBYTES i;

	/* Maxi: We chcek first if there is already a String for this constant */
	String *sp = stringList;
	while (sp != JNULL) {
		if (sp->constantOffset == constantRecord->offset) return (Object*)sp;
		sp = (String*)ref2obj(sp->next); /* leJos code uses word2ptr() instead of ref2ptr() in code for Thread */
	}

	ref = new_object_checked(JAVA_LANG_STRING, btAddr);
	if (ref == JNULL)
		return JNULL;
	arr = new_primitive_array(T_CHAR, constantRecord->constantSize);
	if (arr == JNULL)
	{
		deallocate(obj2ptr(ref), class_size(JAVA_LANG_STRING));
		return JNULL;
	}

	//  printf ("char array at %d\n", (int) arr);

	store_word((byte *) &(((String *)ref)->characters), 4, obj2word(arr));

	for (i = 0; i < constantRecord->constantSize; i++)
	{
		jchar_array(arr)[i] = (JCHAR)get_constant_ptr(constantRecord)[i];

		// printf ("char %lx %d: %c\n", jchar_array(arr), i, (char) (jchar_array(arr)[i]));
	}

	sp = (String *)ref;
	sp->next = obj2ref(stringList);
	sp->constantOffset = constantRecord->offset;
	stringList = sp;

	return ref;
}

/**
 * Pops the array index off the stack, assigns
 * both tempInt and tempBytePtr, and checks
 * bounds and NULL reference. The array reference
 * is the top word on the stack after this operation.
 * @return True if successful, false if an exception has been scheduled.
 */
bool array_load_helper()
{
	tempInt = word2jshort(pop_word());
	tempBytePtr = word2ptr(get_top_ref());
	if (tempBytePtr == JNULL)
		throw_exception(nullPointerException);
	else if (tempInt < 0 || tempInt >= get_array_length((Object *)tempBytePtr))
		throw_exception(arrayIndexOutOfBoundsException);
	else
		return true;
	return false;
}

/**
 * Same as array_load_helper, except that it pops
 * the reference from the stack.
 */
bool array_store_helper()
{
	if (array_load_helper())
	{
		pop_ref();
		return true;
	}
	return false;
}

/**
 * Everything runs inside here, essentially.
 *
 * To be able use only a single fast test on each instruction
 * several assumptions are made:
 * - currentThread is initialized and non-NULL and
 *   it is not set to NULL by any bytecode instruction.
 * - Thus it is not allowed to call schedule_thread() in instructions,
 *   use schedule_request( REQUEST_SWITCH_THREAD) instead.
 * - Whenever gMakeRequest is false, gRequestCode is REQUEST_TICK.
 * - Thus anybody who sets gRequestCode must also set gMakeRequest to true
 *   (using schedule_request assures this).
 * - Only the request handler may set gMakeRequest to false.
 * - The millisecond timer interrupt must set gMakeRequest to true
 *   for time slices to work.
 *
 */
void engine()
{
	byte ticks_until_switch = TICKS_PER_TIME_SLICE;

	assert(currentThread != NULL, INTERPRETER0);

	schedule_request(REQUEST_SWITCH_THREAD);

LABEL_ENGINELOOP:
	instruction_hook();

	assert(currentThread != NULL, INTERPRETER1);

	while (gMakeRequest)
	{
		byte requestCode = gRequestCode;

		gMakeRequest = false;
		gRequestCode = REQUEST_TICK;

		tick_hook();

		if (requestCode == REQUEST_EXIT)
		{
			return;
		}

		if (requestCode == REQUEST_TICK)
			ticks_until_switch--;

		if (requestCode == REQUEST_SWITCH_THREAD
			|| ticks_until_switch == 0) {
			ticks_until_switch = TICKS_PER_TIME_SLICE;
#if DEBUG_THREADS
			printf("switching thread: %d\n", (int)ticks_until_switch);
#endif
			switch_thread();
#if DEBUG_THREADS
			printf("done switching thread\n");
#endif
			switch_thread_hook();
		}
		if (currentThread == NULL   /* no runnable thread */
			&& gRequestCode == REQUEST_TICK) { /* no important request */
			idle_hook();
			schedule_request(REQUEST_SWITCH_THREAD);
		}
	}

	assert(gRequestCode == REQUEST_TICK, INTERPRETER2);
	assert(currentThread != NULL, INTERPRETER3);

	//-----------------------------------------------
	// SWITCH BEGINS HERE
	//-----------------------------------------------

#if DEBUG_BYTECODE
//printf ("0x%X: \n", (int) pc);
//printf ("OPCODE (0x%X) %s\n", (int) *pc, OPCODE_NAME[*pc]);
	printf("0x%X: OPCODE (0x%X)\n", (int)pc, (int)*pc);
	//getc(stdin);
#endif

	switch (*pc++)
	{
	case OP_NOP:
		goto LABEL_ENGINELOOP;

#include "op_stack.hc"
#include "op_locals.hc"
#include "op_arrays.hc"
#include "op_objects.hc"
#include "op_control.hc"
#include "op_other.hc"
#include "op_conversions.hc"
#include "op_logical.hc"
#include "op_arithmetic.hc"
#include "op_methods.hc"

		/*
		#if ASSERTIONS_ENABLED
			default:
				assert(false, (TWOBYTES)(pc-1) % 10000);
				break;
		#endif
		*/
	}

	//-----------------------------------------------
	// SWITCH ENDS HERE
	//-----------------------------------------------

#if !FP_ARITHMETIC

	throw_exception(noSuchMethodError);

#else

// This point should never be reached

#if ASSERTIONS_ENABLED
	assert(false, 1000 + *pc);
#endif // ASSERTIONS_ENABLED

#endif // FP_ARITHMETIC
}





