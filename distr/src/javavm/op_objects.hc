/**
 * This is included inside a switch statement.
 */

case OP_NEW:
  // Stack: +1
  // Arguments: 2
  // Hi byte unused
  tempBytePtr = (byte *) new_object_checked (pc[1], pc - 1);
  if (tempBytePtr != JNULL)
  { 
    #if 0
    trace (-1, (short) pc[1], 1);
    trace (-1, (short) tempBytePtr, 2);
    trace (-1, get_class_index((Object *) tempBytePtr), 3);
    #endif
    push_ref (ptr2ref(tempBytePtr));
    pc += 2;
  }
  goto LABEL_ENGINELOOP;
case OP_GETSTATIC:
case OP_PUTSTATIC:
  // Stack: +1 or +2 for GETSTATIC, -1 or -2 for PUTSTATIC
  {
    STATICFIELD fieldRecord;
    byte *fbase1 = null;
    byte fieldType;
#if RECORD_REFERENCES
    byte isRef;
#endif
    byte fieldSize;
    boolean wideWord;

    #if DEBUG_FIELDS
    printf ("---  GET/PUTSTATIC --- (%d, %d)\n", (int) pc[0], (int) pc[1]);
    #endif

    if (dispatch_static_initializer (get_class_record (pc[0]), pc - 1))
      goto LABEL_ENGINELOOP;
    fieldRecord = ((STATICFIELD *) get_static_fields_base())[pc[1]];

    fieldType = (fieldRecord >> 12) & 0x0F;
#if RECORD_REFERENCES
    isRef = (fieldType == T_REFERENCE);
#endif
    fieldSize = typeSize[fieldType];
    wideWord = false;
    if (fieldSize > 4)
    {
       wideWord = true;
       fieldSize = 4;
    }

    fbase1 = get_static_state_base() + get_static_field_offset (fieldRecord);

    #if DEBUG_FIELDS
    printf ("fieldSize  = %d\n", (int) fieldSize);
    printf ("fbase1  = %d\n", (int) fbase1);
    #endif

    if (*(pc-1) == OP_GETSTATIC)
    {
      make_word (fbase1, fieldSize, &tempStackWord);
#if RECORD_REFERENCES
      if (isRef)
        push_ref(tempStackWord);
      else
#endif
      push_word (tempStackWord);
      if (wideWord)
      {
        make_word (fbase1 + 4, 4, &tempStackWord);
        push_word (tempStackWord);
      }
    }
    else
    {
      if (wideWord)
        store_word (fbase1 + 4, 4, pop_word());
#if RECORD_REFERENCES
      if (isRef)
        store_word (fbase1, fieldSize, pop_ref());
      else
#endif
      store_word (fbase1, fieldSize, pop_word());
    }
    pc += 2;
  }
  goto LABEL_ENGINELOOP;
case OP_GETFIELD:
  {
    byte *fbase2 = null;
    byte fieldType;
    byte fieldSize;
    boolean wideWord;

    tempStackWord = get_top_ref();
    if (tempStackWord == JNULL)
    {
      throw_exception (nullPointerException);
      goto LABEL_ENGINELOOP;
    }
    fieldType = get_pgfield_type(pc[0]);
    fieldSize = typeSize[fieldType];
    wideWord = fieldSize > 4;
    if (wideWord)
    {
      fieldSize = 4;
    }

    fbase2 = ((byte *) word2ptr (tempStackWord)) + 
                get_pgfield_offset(pc[0], pc[1]);

    #ifdef DEBUG_FIELDS
    printf ("--- GETFIELD ---\n");
    printf ("fieldType: %d\n", (int) fieldType);
    printf ("fieldSize: %d\n", (int) fieldSize);
    printf ("wideWord: %d\n", (int) wideWord);
    printf ("reference: %d\n", (int) tempStackWord);
    printf ("stackTop: %d\n", (int) stackTop);
    #endif

    #ifdef DEBUG_FIELDS
    printf ("### get_field base=%d size=%d pushed=%d\n", (int) fbase2, (int) fieldSize, (int) tempStackWord);
    #endif

    make_word (fbase2, fieldSize, &tempStackWord);
   
    #ifdef DEBUG_FIELDS
    printf ("### get_field base=%d size=%d pushed=%d\n", (int) fbase2, (int) fieldSize, (int) tempStackWord);
    #endif

#ifdef RECORD_REFERENCES
    if (fieldType == T_REFERENCE)
      set_top_ref (tempStackWord);
    else
#endif
    set_top_word (tempStackWord);
    #ifdef DEBUG_FIELDS
    printf("Set top word done\n");
    if (wideWord)
    	printf("Wide word\n");
    #endif
    if (wideWord)
    {
      make_word (fbase2 + 4, 4, &tempStackWord);
      push_word (tempStackWord);
    }
    pc += 2;
  }
#ifdef DEBUG_FIELDS
	printf("Going home\n");
#endif
  goto LABEL_ENGINELOOP;
case OP_PUTFIELD:
  {
    byte *fbase3;
    byte fieldType;
    byte fieldSize;
    boolean wideWord;

    fieldType = get_pgfield_type(pc[0]);
    fieldSize = typeSize[fieldType];
    wideWord = (fieldSize > 4);
    if (wideWord)
      fieldSize = 4;
    tempStackWord = get_ref_at (wideWord ? 2 : 1);

    #ifdef DEBUG_FIELDS
    printf ("--- PUTFIELD ---\n");
    printf ("fieldType: %d\n", (int) fieldType);
    printf ("fieldSize: %d\n", (int) fieldSize);
    printf ("wideWord: %d\n", (int) wideWord);
    printf ("reference: %d\n", (int) tempStackWord);
    #endif


    if (tempStackWord == JNULL)
    {
      throw_exception (nullPointerException);
      goto LABEL_ENGINELOOP;
    }
    fbase3 = ((byte *) word2ptr (tempStackWord)) +
                get_pgfield_offset (pc[0], pc[1]); 
    if (wideWord)
      store_word (fbase3 + 4, 4, pop_word());

    #if 0
    printf ("### put_field base=%d size=%d stored=%d\n", (int) fbase3, (int) fieldSize, (int) get_top_word());
    #endif

#ifdef RECORD_REFERENCES
    if (fieldType == T_REFERENCE)
      store_word (fbase3, fieldSize, pop_ref());
    else
#endif
    store_word (fbase3, fieldSize, pop_word());
    just_pop_ref();
    pc += 2;
  }
  goto LABEL_ENGINELOOP;
case OP_INSTANCEOF:
  // Stack: unchanged
  // Arguments: 2
  // Ignore hi byte
  set_top_word (instance_of (word2obj (get_top_ref()),  pc[1]));
  pc += 2;
  goto LABEL_ENGINELOOP;
case OP_CHECKCAST:
  // Stack: -1 +1 (same)
  // Arguments: 2
  // Ignore hi byte
  pc++;
  tempStackWord = get_top_ref();
  if (tempStackWord != JNULL && !instance_of (word2obj (tempStackWord), pc[0]))
    throw_exception (classCastException);
  pc++;
  goto LABEL_ENGINELOOP;

// Notes:
// - NEW, INSTANCEOF, CHECKCAST: 8 bits ignored, 8-bit class index
// - GETSTATIC and PUTSTATIC: 8-bit class index, 8-bit static field record
// - GETFIELD and PUTFIELD: 4-bit field type, 12-bit field data offset

/*end*/








