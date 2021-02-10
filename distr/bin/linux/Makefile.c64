LEJOS_HOME=../../
CURRENT_DIR=$(shell pwd)
VMSRC=$(LEJOS_HOME)/src/javavm
PLATFORMSRC=$(LEJOS_HOME)/src/platform/c64
BIN_TARGET=$(CURRENT_DIR)

LD=ld65

CC=cl65

CFLAGS=-I $(LEJOS_HOME)/src/javavm -I $(PLATFORMSRC) -I $(LEJOS_HOME)/src/util -t c64 -Oi -W -unused-param,-unused-var

VMEMUL_OBJ=platform_native.o interpreter.o exceptions.o language.o main.o \
	   memory.o nativeemul.o  \
           threads.o trace.o tvmemul.o

IMAGE_TARGET=$(BIN_TARGET)/b2fj.prg

default: programs

programs: ${IMAGE_TARGET}

platform_native.o: ${PLATFORMSRC}/platform_native.c
	${CC} ${CFLAGS} -o $(BIN_TARGET)/platform_native.o -c ${PLATFORMSRC}/platform_native.c

main.o: ${PLATFORMSRC}/main.c
	${CC} ${CFLAGS} -o $(BIN_TARGET)/main.o -c ${PLATFORMSRC}/main.c

%.o: $(VMSRC)/%.c
	${CC} ${CFLAGS} -o $(BIN_TARGET)/$*.o -c $(VMSRC)/$*.c

${IMAGE_TARGET}: ${VMEMUL_OBJ}
	${LD} -t c64 -o ${IMAGE_TARGET} ${VMEMUL_OBJ} c64.lib

clean:
	rm -rf ${VMSRC}/*.o *.o $(BIN_TARGET)/emu-*.exe core ${DUMP_TARGET} ${VMEMUL_TARGET}

