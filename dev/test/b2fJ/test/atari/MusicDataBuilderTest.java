package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class ArchonSongTest {

    public static void main(String[] args) throws InterruptedException {
        // Start the sound loop
        SoundLoop soundLoop1 = (new SoundLoop((short) 8));
        // soundLoop1.printSong();
        soundLoop1.run();

    }

    public static class SoundLoop {

        static final int RETURN = '\uFC00';

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

                '\uFE74', // 120: loop(position: 113,
                '\u0002', // 121: times: 2)

                // End
                '\u6C01', // 122:
                '\u6601', // 123:
                '\u6002', // 124:
                '\u5B03', // 125:
                '\u0001', // 126: Silence, 2 ticks
                '\u5520', // 127:
                '\u0040'
                /*
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.D4 << 8 | 1,
                Notes.D$4 << 8 | 1,
                Notes.E4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F$4 << 8 | 24,
                Notes.S << 8 | 1,
                Notes.C$4 << 8 | 9,
                Notes.D$4 << 8 | 4,
                Notes.C$4 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.G$3 << 8 | 2,
                Notes.G$4 << 8 | 36,
                Notes.S << 8 | 1,
                Notes.F$4 << 8 | 18,
                Notes.C$4 << 8 | 9,
                Notes.D$4 << 8 | 4,
                Notes.C$4 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.G$3 << 8 | 2,
                Notes.G$4 << 8 | 38,
                Notes.F4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.F3 << 8 | 1,
                Notes.F3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.C4 << 8 | 3,
                Notes.F$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.C$5 << 8 | 1,
                Notes.C$5 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.C$4 << 8 | 1,
                Notes.C$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.F$4 << 8 | 1,
                Notes.F$3 << 8 | 1,
                Notes.F$3 << 8 | 1,
                Notes.C$4 << 8 | 1,
                Notes.C$4 << 8 | 3,
                Notes.B3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.G4 << 8 | 1,
                Notes.E4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.G4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.B4 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.F4 << 8 | 4,
                Notes.B3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.G4 << 8 | 1,
                Notes.E4 << 8 | 1,
                Notes.F4 << 8 | 1,
                Notes.G4 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.A$4 << 8 | 1,
                Notes.B4 << 8 | 1,
                Notes.C5 << 8 | 1,
                Notes.G$4 << 8 | 1,
                Notes.F4 << 8 | 2,
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.C4 << 8 | 1,
                Notes.B3 << 8 | 1,
                Notes.A$3 << 8 | 1,
                Notes.A3 << 8 | 1,
                Notes.D4 << 8 | 1,
                Notes.D$4 << 8 | 1,
                Notes.E4 << 8 | 2,
                Notes.F4 << 8 | 2,
                Notes.F$4 << 8 | 32,
                64 */
        };
        final short volume;

        SoundLoop(short volume) {

            this.volume = volume;

        }

        public void printSong() {
            for (int i = 0; i < song1.length; i++) {
                out.println(song1[i]);
            }
        }

        public void run() {
            try {
                int HPOS0=53248;
                int BITS0=53261;
                int COL0=704;
                int SIZE0=53256;

                // Set colors
                poke(712, 144 );
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

                // REM **** SET COLOR
                poke(COL0,0); // REM B,GRN,INTEN=10
                // REM **** SET SIZE
                poke(SIZE0,0);
                // REM **** SET HORIZ POSITION
                poke(HPOS0,120);
                // REM **** SET DATA
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

                    //out.println("note:"+(int)note);
                    //out.println("pitch1:"+(int)pitch1);
                    //out.println("elapse1:"+(int)elapse1);


                    //Atari.sound(0, 0, 0, 0);
                    //Atari.sound(1, 0, 0, 0);
                    // Pause at the end of a note and before start th next
                    //Atari.chorusSound(0, 0, 10, 1);

                    //Atari.chorusSound(0, 0, 10, 1);

                    if (pitch[i] > 0) {
                        Atari.doubleSound(pitch[i], pitch[i] + 1, 10, volume);

                        if (parameter[i] > 1) {
                            poke(HPOS0,32+ (120-pitch[i]/2));
                            poke(COL0, 32 + (pitch[i] / 15));
                            Thread.sleep(60 * (parameter[i] - 1));
                            Atari.doubleSound(pitch[i], pitch[i] + 1, 10, 0);
                            //color = 15 - ((pitch[i] + 1) / 15);
                            //poke(710, 144+color);
                            //poke(712, 144+color);
                        } else {
                            int color2 = 32 + (pitch[i] / 15);
                            poke(COL0, color2);
                            Atari.doubleSound(pitch[i], pitch[i] + 1, 10, 2);
                            poke(COL0, color2+2);
                        }

                    } else {
                        Atari.doubleSound(0, 0, 10, 1);
                        poke(HPOS0,0);
                    }

                    i += 1;

                }
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

        /*
        static final class Notes {

            public static char C3 = '\u00F3';
            public static char C$3 = '\u00E6';
            public static char D3 = '\u00D9';
            public static char D$3 = '\u00CC';
            public static char E3 = '\u00C1';
            public static char F3 = '\u00B6';
            public static char F$3 = '\u00AC';
            public static char G3 = '\u00A2';
            public static char G$3 = '\u0099';
            public static char A3 = '\u0090';
            public static char A$3 = '\u0088';
            public static char B3 = '\u0080';
            public static char C4 = '\u0079';
            public static char C$4 = '\u0072';
            public static char D4 = '\u006C';
            public static char D$4 = '\u0066';
            public static char E4 = '\u0060';
            public static char F4 = '\u005B';
            public static char F$4 = '\u0055';
            public static char G4 = '\u0051';
            public static char G$4 = '\u004C';
            public static char A4 = '\u0048';
            public static char A$4 = '\u0044';
            public static char B4 = '\u0040';
            public static char C5 = '\u003C';
            public static char C$5 = '\u0039';
            public static char D5 = '\u0035';
            public static char D$5 = '\u0032';
            public static char E5 = '\u002F';
            public static char F5 = '\u002D';
            public static char F$5 = '\u002A';
            public static char G5 = '\u0028';
            public static char G$5 = '\u0025';
            public static char A5 = '\u0023';
            public static char A$5 = '\u0021';
            public static char B5 = '\u001F';
            public static char C6 = '\u001E';
            public static char C$6 = '\u001C';
            public static char D6 = '\u001A';
            public static char D$6 = '\u0019';
            public static char E6 = '\u0017';
            public static short F6 = '\u0016';
            public static char F$6 = '\u0015';
            public static char G6 = '\u0013';
            public static char G$6 = '\u0012';
            public static char A6 = '\u0011';
            public static char A$6 = '\u0010';
            public static char B6 = '\u000F';
            public static char C7 = '\u000E';
            public static short S = '\u0000';
        }
         */
    }
}

