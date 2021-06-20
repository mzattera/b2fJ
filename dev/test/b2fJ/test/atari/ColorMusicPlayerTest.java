package b2fJ.test.atari;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class ColorMusicPlayerTest {

    public static void main(String[] args) throws InterruptedException {

        out.print("  B2FJ MUSIC PLAYER ");

        loadDisplayListInterruptRoutine();
        setupDisplayListInterrupt();
        enableDisplayListInterrupt();

        // Start the sound loop
        SoundLoop soundLoop1 = (new SoundLoop((short) 8));
        soundLoop1.run();

        Thread.sleep(5000);

    }

    static int dataAddress = 0x0664; // A random, not used address in page 6
    static int PAGE =6;
    static int assemblyRoutineAddress= PAGE*256;

    /**
     * Small Interrupt Routine that is triggered every time that a row ia drawn on screen
     * Details on:
     * https://www.atariarchives.org/dere/chapt02.php
     *
     * 6502 assembly code:
     *
     * PHA ; Store accumulator register on stack
     * STA $D40A  ; Wait for the beam complete his round
     * LDA $D40B ; Load the current vertical counter
     * ASL A ; Shift all bits to the right (Multiply * 2)
     * CLC ; Clear the carry flag
     * ADC $0664 ; Add the value from dataAddress (this allow move the color offset)
     * STA $D018 ; Store the value in the color playfield register (background)
     * PLA ; pull accumulator register from the stack, restoring it
     * RTS ; return from the Display List Interrupt
     */
    private static char[] assemblyRoutine = {
            // Assembled using Easy6502: https://skilldrick.github.io/easy6502/
            '\u488d','\u0ad4','\uad0b','\ud40a', '\ueaea','\u186d','\u6406','\u8d18','\ud068','\u4000'
};

    public static void arraycopy(char[] source,int target) {
        int len = source.length;
        for (int i = 0; i < len; i += 1) {
            int c = (int)(source[i] & 0xFFFF);
            int msb = (c >> 8);
            int lsb = c - msb*256;
            poke(target+i*2,msb);
            poke(target+i*2+1,lsb);
        }
    }

    private static void loadDisplayListInterruptRoutine() throws InterruptedException {
        arraycopy(assemblyRoutine,assemblyRoutineAddress);
    }

    private static void enableDisplayListInterrupt() {
        poke(512,0);
        poke(513, PAGE); // Poke in interrupt vector
        poke(54286, 192); // Enable DLI
    }

    /**
     * Build the display list interrupt
     * https://www.atariarchives.org/dere/chapt02.php
     */
    private static void setupDisplayListInterrupt() {
        // Setup the display list to call the DLI  routine for every line
        int displayListAddress=peek(560)+256*peek(561); //REM Find display list
        poke(displayListAddress+9,0x07); // Blank line
        for(int i=0;i<19;i++) {
            poke(displayListAddress+i+10,128+15); // Insert interrupt instruction
        }
    }


    public static class SoundLoop {

        static final int HPOS0=53248;
        static final int BITS0=53261;
        static final int COL0=704;
        static final int SIZE0=53256;

        static final int RETURN = '\uFC00';

        final short volume;

        SoundLoop(short volume) {
            this.volume = volume;
        }

        public void run() {
            try {

                // Set colors
                //poke(712, 144 );
                poke(710, 144 );
                poke(709, 15 );

                int i = 0, j = 0;
                short parameter[] = new short[song1.length];
                short pitch[] = new short[song1.length];
                int color=16;
                int start = 0;
                int count = 0;
                int returnPosition = 0;
                boolean inLoop = false;

                // SET COLOR
                poke(COL0,0);
                // SET SIZE
                poke(SIZE0,0);
                // SET HORIZ POSITION
                poke(HPOS0,120);
                // SET DATA
                poke(BITS0,127);

                for (i = 0; i < song1.length; i++) {
                    pitch[i] = (short) (song1[i] >> 8);
                    parameter[i] = (short) (song1[i] % '\uFF00');
                    poke(COL0, 32 + (15*i/song1.length));
                }

                i = 0;
                for (; ; ) {

                    if (i >= song1.length) {
                        i = 0;
                        poke(HPOS0,120);
                    }
                    // Handle control codes in song data
                    if (pitch[i] > 0xFA) {
                        if (pitch[i] == 0xFF) { // Jump (position)
                            i = parameter[i];
                            continue;
                        } else if (pitch[i] == 0xFE) { // Loop (position,times)
                            if (inLoop) {
                                if (--count == 0) {
                                    //out.println("end");
                                    inLoop = false;
                                    i += 2;
                                } else {
                                    // out.println("again");
                                    i = start;
                                    continue;
                                }
                            } else {
                                inLoop = true;
                                count = parameter[i + 1]-1;
                                i = start = parameter[i];
                                // out.println("first");
                                continue;
                            }
                        } else if (pitch[i] == 0xFD) { // Jump Sub-Rutine (position)
                            returnPosition = i + 1;
                            i = parameter[i];
                            continue;
                        } else if (pitch[i] == 0xFC) { // Return from sub-rutine
                            i = returnPosition;
                            continue;
                        }
                    }

                    if (pitch[i] > 0) {

                        poke(HPOS0,64+ (120-pitch[i]/2));

                        AtariSound.doubleSound(pitch[i], pitch[i] + 1, 10, volume);

                        if (parameter[i] > 1) {

                            poke(COL0, 32 + (pitch[i] / 15));
                            Thread.sleep(50 * (parameter[i] - 1));
                            AtariSound.doubleSound(pitch[i], pitch[i] + 1, 10, 0);
                            color = 15 - ((pitch[i] + 1) / 15);
                            poke(710, 144+color);
                            poke(712, 144+color);
                            poke(709, 15-color);

                        } else {
                            int color2 = 32 + (pitch[i] / 15);
                            poke(COL0, color2);
                            AtariSound.doubleSound(pitch[i], pitch[i] + 1, 10, 2);
                            poke(COL0, color2+2);
                        }

                    } else {
                        poke(HPOS0,0);
                        Thread.sleep(50 * (parameter[i] - 1));
                        AtariSound.doubleSound(0, 0, 10, 1);
                    }

                    i++;
                    poke(dataAddress,i);

                }
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

        // Note/Instruction + Duration/Parameter
        static final int[] song1 = {
                '\uFF71', // 000: Goto position 6, start
                '\u7901', // 001: Sub-rutine 1
                '\u8001', // 002
                '\u8801', // 003
                '\u9001', // 004
                RETURN,   // 005
                '\u6C01', // 006: Sub-routine 2
                '\u6601',
                '\u6001',
                '\u5B01',
                '\u5518',
                '\u0001',
                '\u7209',
                '\u6604',
                '\u7201',
                '\u7901',
                '\u9902',
                '\u4C24',
                '\u0001',
                '\u5512',
                '\u7209',
                '\u6604',
                '\u7201',
                '\u7901',
                '\u9902',
                '\u4C26',
                '\u5B01',
                '\u5B01',
                '\u4C01',
                '\u4C01',
                '\u4401',
                '\u4401',
                '\u3C01',
                '\u3C01',
                '\u4401',
                '\u4401',
                '\u4C01',
                '\u4C01',
                '\u5B01',
                '\u5B01',
                '\u4401',
                '\u4401',
                '\u5B01',
                '\u5B01',
                '\u4C01',
                '\u4C01',
                '\u5B01',
                '\u5B01',
                '\u7901',
                '\u7901',
                '\u5B01',
                '\u5B01',
                '\uB601',
                '\uB601',
                '\u7901',
                '\u7903',
                '\u5501',
                '\u5501',
                '\u4401',
                '\u4401',
                '\u3C01',
                '\u3C01',
                '\u3901',
                '\u3901',
                '\u3C01',
                '\u3C01',
                '\u4401',
                '\u4401',
                '\u5501',
                '\u5501',
                '\u3C01',
                '\u3C01',
                '\u5501',
                '\u5501',
                '\u4401',
                '\u4401',
                '\u5501',
                '\u5501',
                '\u7201',
                '\u7201',
                '\u5501',
                '\u5501',
                '\uAC01',
                '\uAC01',
                '\u7201',
                '\u7203',
                '\u8001',
                '\u7901',
                '\u4C01',
                '\u5101',
                '\u6001',
                '\u5B01',
                '\u5101',
                '\u4C01',
                '\u4401',
                '\u4001',
                '\u3C01',
                '\u4C01',
                '\u5B04',
                '\u8001',
                '\u7901',
                '\u4C01',
                '\u5101',
                '\u6001',
                '\u5B01',
                '\u5101',
                '\u4C01',
                '\u4401',
                '\u4001',
                '\u3C01',
                '\u4C01',
                '\u5B02',
                RETURN,

                '\uFD01', // 113: jumpSubrutine(1)
                '\uFD01', // 114: jumpSubrutine(1)
                '\uFD01', // 115: jumpSubrutine(1)
                '\uFD06', // 116: jumpSubrutine(6)
                '\uFD01', // 117: jumpSubrutine(1)
                '\uFD01', // 118: jumpSubrutine(1)
                '\uFD01', // 119: jumpSubrutine(1)

                '\uFE74', // 120: loop(position: 116,
                '\u0002', // 121: times: 2)

                // End
                '\u6C01', // 122:
                '\u6601', // 123:
                '\u6002', // 124:
                '\u5B02', // 125:
                '\u5520', // 127:
                '\u0040'
        };



    }
}

