2020.10.04

# b2fJ License

Back to the Future Java (b2fJ) is developed by Massimiliano Zattera.

b2fJ is distributed under [MOZILLA PUBLIC LICENSE Version 1.0](http://website-archive.mozilla.org/www.mozilla.org/mpl/MPL/1.0/).

b2fJ is based on the [leJOS](http://www.lejos.org/) project  which is also distributed under MPL 1.0.
At present, b2fJ shares most of its code with leJOX RCX JVM and jtool library, being mainly a port of 
leJOS under 8-bit architectures (most notably the Commodore 64). Java class library is taken from
leJOS NXJ class library, with minor adaptation.

leJOS is a tiny Java Virtual Machine targeting different Lego Mindstorms (RCX, NXJ & EV3); the project has many contributors, among them, Brian Bagnall, Jürgen Stuber and Paul Andrews.

leJOS was originally based on [TinyVM](http://tinyvm.sourceforge.net/) by Jose H. Solorzano.


## Dependencies

Some of the classes in the standard library come from [OpenJDK](http://hg.openjdk.java.net/); their copyright belongs to Oracle and/or its affiliates. They are 
redistributed under [General Public License version 2](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html).

Please note that files in "`.\redistr`" folder are *neither* developed *nor* modified by the author of b2fJ and are only re-distributed accordingly to what is permitted by corresponding licenses.

* [cc65](https://cc65.github.io/) is a C cross-assembler for various 6502-based target systems, including the Commodore 64; it is  used to compile the JVM for the target platform.
Initially developed by [Ullrich von Bassewitz](http://www.cc65.org/), it is now maintained by Oliver Schmidt.

* [VICE](http://vice-emu.sourceforge.net) it is arguabely the best Commodore 64 emulator for Windows; it used here to run the Java programs compiled with b2fJ. It is distributed under the GNU General Public License (version 2 or later). 

* [Apache Commons BCEL](http://commons.apache.org/bcel/) and [Apache Commons CLI](https://commons.apache.org/cli/) libraries (`.jar`) are distributed under the [Apache license](http://www.apache.org/licenses/).

## Acknowledgments

*** WARM THANKS *** to all the people that contributed to the above mentioned projects, whithout whom b2fJ would not have been possible.

Please contact the author of b2fJ if the above does not contain correct informations or if you see any copyright issue.

Massimiliano Zattera