package b2fJ.test.atari;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class DisplayListInterruptTest {

    private static int dataAddress = 0x0664; // A random not used address in page 6

    // Assembled using Easy6502: https://skilldrick.github.io/easy6502/
    private static final int data[] = {
            72,
            141,10,212, // STA WSYNC
            173,11,212, // LDA VCOUNT
            0x4a, // LSR A
            0x18, // CLC
            0x6d, 0x64, 0x06, // ADC DATA1
            141,24,208,
            104,
            64
    };

    private static int PAGE =6;
    private static int assemblyRoutineAddress= PAGE *256;

    public static void main(String[] args) throws InterruptedException {

        for(int j=0;j<18;j++) {
            out.println("Hello World.");
        }

        loadRoutine();
        setupDisplayListInterrupt();
        enableDisplayListInterrupt();

        int i=0;
        while(true) {
            i=i+1;
            poke(dataAddress,i);
            Thread.sleep(1);
        }

    }

    private static void enableDisplayListInterrupt() {
        poke(512,0);
        poke(513, PAGE); // Poke in interrupt vector
        poke(54286, 192); // Enable DLI
    }

    private static void setupDisplayListInterrupt() {
        // Setup the display list to call the DLI  routine for every line
        int displayList=peek(560)+256*peek(561); //REM Find display list
        for(int i=0;i<20;i++) {
            poke(displayList+i+8,130); // Insert interrupt instruction
        }
    }

    private static void loadRoutine() {
        // Load the 6502 assembly routine in page 6
        for(int i=0;i<data.length;i++) { // Loop for poking DLI service routine
            poke(assemblyRoutineAddress + i, data[i]);
        }
    }

}

