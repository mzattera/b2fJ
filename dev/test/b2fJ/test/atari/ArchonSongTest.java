package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class ArchonSongTest {

    public static void main(String[] args) throws InterruptedException {
        // Start the sound loop
        SoundLoop soundLoop1 = (new SoundLoop((short) 8));
        soundLoop1.run();

    }

    private static class Atari {

        static final int[] AUDF = {0xD200, 0xD202, 0xD204, 0xD206};
        static final int[] AUDC = {0xD201, 0xD203, 0xD205, 0xD207};

        static boolean emulator = false;
        static int dv = 0;

        static {
            // Initialize 8-bit sound
            poke(53768, 0);
        }

        static void sound(int voice, int pitch, int distortion, int volume) {
            poke(AUDF[voice], pitch);
            poke(AUDC[voice], (16 * distortion) + volume);
        }

        static void chorusSound(int pitch, int pitch2,int distortion, int volume) {
            dv = (16 * distortion) + volume;
            poke(AUDF[0], pitch);
            poke(AUDF[1], pitch2);
            poke(AUDC[0], dv);
            poke(AUDC[1], dv);
        }

    }

    public static class SoundLoop {

        // Note + Duration
        static final int[] song1 = {
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
                64
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

                poke(712, 144 );
                poke(710, 144 );

                short i = 0, j = 0;
                short elapse[] = new short[song1.length];
                short pitch[] = new short[song1.length];
                int color;

                for(i=0;i<song1.length;i++) {
                    pitch[i]=(short)(song1[i] >> 8);
                    elapse[i]=(short)(song1[i] % '\uFF00');
                }

                i=0;
                for (; ; ) {
                    // Pause at the end of a note and before start th next
                    Atari.chorusSound(0, 0, 10, 1);
                    //Thread.sleep(1);

                    if (i >= song1.length) {
                        i = 0;
                    }
                    //out.println("note:"+(int)note);
                    //out.println("pitch1:"+(int)pitch1);
                    //out.println("elapse1:"+(int)elapse1);


                    //Atari.sound(0, 0, 0, 0);
                    //Atari.sound(1, 0, 0, 0);

                    if (pitch[i] > 0) {
                        Atari.chorusSound(pitch[i], pitch[i]+1, 10, volume);
                    } else {
                        Atari.chorusSound(0, 0, 10, 1);
                    }

                    color = 16 - ((pitch[i] + 1) / 16);
                    poke(709, 16 - color);

                    if(elapse[i]>1) Thread.sleep(60 * (elapse[i] - 1));

                    i += 1;

                }
            } catch (Exception e) {
                out.println(e.getMessage());
            }
        }

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
    }
}

