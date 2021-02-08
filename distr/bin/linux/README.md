
# Utilities

Utilities to create B2JF binaries on linux and macosx.

The utilities can be used to create c64 binaries that run any platform that supports VICE 3.5 or real C64.

The linux generated binaries only can run on 32-bits platforms.

## b2fj-go-c64 <File>

Compile source, link, create binary native image and run on C64 VICE 3.5

## b2fj-go-linux <File>

Compiles, link, creates binary native image and run on Linux.

## b2fj-compiler <File.java>

Compiles the source to a .class using the B2JF classes.

## b2fj-linker <File>

Link the classes in a single byte array and generates a java_code on distr/src/javavm to be compiled in a single native image.

## b2fj-native-c64

Generates b2fj.prg binary native image that can run on C64.

## b2fj-native-linux

Generates a 32-bit binary native image that can run on linux and debbuged with gdb.

# Linux

It is possible to use the linux utilities to create a C64 native image and run it on the VICE emulator. 

If the linux OS 32-bit support, is possible run the native images for testing and debug previous to run on a real device. The VM requires 32-bit supports as it assumes that the host machine has 32-bit addresses, in a 64-bit machine the VM crash due the addresses are truncated.

## Prerrequisites

sudo apt install vice
sudo apt install cc65
sudo apt install build-essential gcc-multilib make

# OSX

It is possible to use the linux utilities to create a C64 native image and run it on the VICE emulator on Mac OSX (tested on Catalina).

OSX does not have 32-bit support, the is not possible run the native images on this platform, just in the VICE emulator.

1. Prerrequisites

brew install vice
brew install cc65

# Quick Start

1. Install and correctly configure the prerrequisites (the command x64sc should open the VICE emulator).
2. Clone the B2JF repository.
3. Execute the following commands:

cd ./distr/bin/linux
export PATH=$(pwd):$PATH
b2fj-go-c64 Iterate # Build the binary and run in the emulator
