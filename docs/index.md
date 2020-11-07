# What is b2fJ?

**Back to the Future Java (b2fJ)** is a Java Virtual Machine intended to run on the 8-bit home computers of the 80s.

It can be downloaded from [here](https://github.com/mzattera/b2fJ/releases/latest) (distributed un der MOZILLA PUBLIC LICENSE Version 1.0.).

It is based on the [leJOS](http://www.lejos.org) JVM for the LEGO Mindstorms RCX brick.

In its current state, it is basically a straight port of leJOS RCX to the popular Commodore 64. As such, it has all features of leJOS...

* Lets you develop using Java (fully-featured OO language).
* Preemptive threads with synchronization.
* Multi-dimensional arrays.
* Exceptions, String, etc.

...and all of its limitations:

* No garbage collection.
* `switch` statement not implemented.
* Very limited support for long (they are recognized but can only be assigned to or read from).
* Provided "standard" library does not cover the full JSE library.

In addition, b2fJ relies on a C language cross-compiler to compile the Java VM for the target platform (namely, the Commodore 64).
The cc65 compiler currently used unfortunatley does not support floating-point variables, therefore types float and double, 
even if implmented in b2fJ, are not available in the resulting Commodore 64 JVM.

Finally, being a straight port, b2fJ is basically an interpreted machine with a 32-bit architecture running on top of a 30 year
old 8-bit machine with limited RAM. This to say that much can (and hopefully will) be done to improve b2fJ speed and memory footprint.
However, the current implementation shows that running Java on an 8-bit machine is possible.

<iframe width="560" height="315" src="https://www.youtube.com/embed/4An1BrG2u_4" frameborder="0" allowfullscreen></iframe>

## A small demo

[Here](./Sprite.java) you can download the code for a tiny demo showing how multi-thread programming can be used to move C64 sprites. See below for instructions about how to compile and run it.

The result should be this:

<iframe width="560" height="315" src="https://www.youtube.com/embed/7iwj2B4PHE4" frameborder="0" allowfullscreen></iframe>

# Installation and Set Up

## JDK

In order to compile and run your Java programs, you need first of all to have a [Java Development Kit](https://www.google.ch/search?q=JDK+1.8+download) installed.
b2fJ uses 1.8 as target for the generated code, it has been tested with Java 15, but it shoudl work with JDK 9 or later (as it uses `--release` parameter for compilation).

Make sure that the JDK folder is in your path or that you have [set `JAVA_HOME`](https://www.google.ch/search?q=set+JAVA_HOME+Windows) properly.

## b2fJ

Download and unzip [latest b2fJ distribution](https://github.com/mzattera/b2fJ/releases/latest).
It's easier to work if you add the `bin` folder in the distribution to your [`PATH` variable](https://www.google.ch/search?q=set+PATH+windows);
the below instructions assume you did so. If not, you obviously must specify the full path for each command in the different compilation steps.

# Compile and run Java Programs

The below steps explain how to compile and run a simple Java program on your C64.

For our example, we will use a simple `HelloWorld.java` file. 
You can paste the code below into a file named `HelloWorld.java`, or create the file with your favorite Java editor.

```java
public class HelloWorld {
	
	public static void main(String[] args) throws InterruptedException {
		String s = "Hello World. ";
		while (true) {            
			System.out.print(s);
			Thread.sleep(1000);
	   }
	}
}
```


## The easy way

Unfortunately, in order to overcome the limitations of our target platform, several additional steps are required compared to standard Java development when compiling our Java programs.

The good news is that you can forget all of the details and just invoke the below command to compile your Java program and run it inside the [VICE emulator](http://vice-emu.sourceforge.net/) provided with b2fJ:

    b2fJGO HelloWorld.java

It will take some time to load, be patient ;-)

## The boring details

Below, you can find a step-by-step explanation of the build process, which also sheds some light over the development toolchain.
    
### From `.java` to `.class` (compilation)

The below command will compile `HelloWorld.java` into corresponding `.class` file. 

    b2fJc HelloWorld.java
    
It will simply invoke the JDK compiler and compile your classes using the b2fJ libraries, instead of using the standard ones.

Notice that you can provide a class-path to your own classes and libraries by setting `CLASSPATH` variable properly; in case you are not familiar with this,
please have a look at this [nicely written article](http://kevinboone.net/classpath.html).

* Make sure to set your `CLASSPATH` to the "root(s)" of your class packages.
* Go to the folder where your `main()` class reside.
* Launch `b2fJc` from there (just provide the `.java` file name without path).
* Same applies to to the "linking" step below.

Also, any Java 1.8 compiler can be used for compilation, as long as you make sure `lib\classes.jar` is used instead of standard Java class libraries.

### From `.class` to C (linking)

In order to make the JVM more compact, we will squeeze all `.class` files needed by our program 
into a C header that is then compiled directly together with the JVM.
This will then result in a single C64 executable containing both the actual Java interpreter and your java code.

Starting from the class that contains the `main()` method, all methods invoked by the program are retrieved and corresponding java 
bytecode is stored as a `byte[]` inside  a C header (`java_code.h`) which is then moved into folder `src\platform\c64`
where the JVM source code resides, for later compilation.

The commnad to perform this "linking" operation is as follows:

    b2fJl HelloWorld

The "linker" is a Java application (`lib\jtools.jar`); its source code can be found under `src\java\tools` folder.

Again, you can provide a class-path to your own classes and libraries by setting CLASSPATH variable properly.

### Building the JVM

Finally, we must re-build the b2fJ Virtual Machine that, as explained above, will embed your program which will be executed when the JVM is launched.

The below command is a batch file that uses cc65, a 6502 cross-assembler (re)distributed together with b2fJ, to compile the JVM for the C64.

    buildJVM
    
Do not be scared by the many "Warnings" you will see printed; the build will only stop on errors.

If the process is successful, a file called `b2fJ.prg` should have been created; this is your JVM.

### Running the program

At last! You can now run your Java program on your C64.

If you don't have a C64 at hand (and if you do, please send me a video of your Java running on it) you can use the WinVICE
emulator that is (re)distributed with b2fJ, under `redistr\WinVICE-3.1-x86`; you can launch `x64.exe` then under 
"File > Autostart Disk/Tape image" choose the `b2fJ.prg` file you created above.

# Expanding b2fJ

Under `src\java\classes` you can find the source code for the class library distributed together with b2fJ; this is made up of several classes 
that replace the default Java library and some custom classes to support features of the target platform (e.g. C64 sprites).

## Building the library

You can improve the library by adding your classes or extending the existing ones; afterwards, the classes need to be compiled and put in a file
`classes.jar` under `lib` folder. The file `build.xml` that you can find in the `src` folder is an Ant script that serves this purpose.

Install [Apache Ant](http://ant.apache.org/) and use it to run the build.

## Adding native methods

In the process of extending the class library, it might be necessary to add native methods to your classes; this can be done as follows:

* Add a native method to your Java class.

* The "linker" needs to know that a method is native.

	* Add the [signature](http://www.rgagnon.com/javadetails/java-0286.html) of the native method to file `src\javavm\signatures.db`.

	* Mark the linker with a new `MAGIC_MASK`; this is a numeric constant defined inside `src\java\tools\js\tinyvm\TinyVMConstants.java`.
	The linker stores this constant in `java_code.h`, so the JVM can check that it is aligned with current version of the linker.

	* Rebuild the class library using the provided Ant script; the script will also rebuild the linker, processing `signatures.db` file
	and making the linker aware of the new methods. In addition, it will update the file `src\javavm\version.h` with the new `MAGIC_MASK`
	value and update `src\javavm\specialsignatures.h` with C constants matching each native method signature found.

* In `src\platform\c64\nativeemul.c` there is a function `dispatch_native()`; there you can find a `switch` statement for each of the
constants corresponding to native methods. Add a `case:` statement for you new native method and implement it there.

  * In case the method is declared as `static`, `paramBase[0...n]` will contain the method parameters (as `STACKWORD`s; see the different 
  macros available to convert this type into useful Java objects and vice-versa).

  * If the method is an instance method, `paramBase[0]` will contain a reference to the calling object (`this`) while the
  method parameters are stored in `paramBase[1...n]`.
  

