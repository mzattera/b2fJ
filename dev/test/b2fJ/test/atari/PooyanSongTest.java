package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class PooyanSongTest {

    public static void main(String[] args) throws InterruptedException {

    	// Start the sound loop
		SoundLoop soundLoop1 = (new SoundLoop((short)0,(short)8));
		soundLoop1.run();

    }

    void run() throws InterruptedException {

        // Declare here all variables trying to save stack in loops
        int i= 0;

        // Test kernel
        while (true) {
			out.print("\n\tThe ");
			Thread.sleep(1000);
			out.print("Music ");
			Thread.sleep(1000);
			out.print("Is ");
			Thread.sleep(1000);
			out.print("Playing ");
			Thread.sleep(1000);
			out.print("And ");
			Thread.sleep(1000);
			out.print("Text ");
			Thread.sleep(1000);
			out.print("Is ");
			Thread.sleep(1000);
			out.print("Displayed ");
			Thread.sleep(1000);
			out.print("In Other ");
			Thread.sleep(1000);
			out.print("Thread\n");
			Thread.sleep(10000);
		}

    }

    private static class Atari {

		static final int[] AUDF = {0xD200, 0xD202, 0xD204, 0xD206};
		static final int[] AUDC = {0xD201, 0xD203, 0xD205, 0xD207};

		static boolean emulator = false;

		static {
			// Initialize 8-bit sound
			poke(53768,0);
		}

        static void sound(int voice, int pitch, int distortion, int volume) {
            poke(AUDF[voice], pitch);
            poke(AUDC[voice], (16 * distortion) + volume);
        }

    }

    public static class SoundLoop extends Thread {

        final short voice;
        final short volume;

		static final class Notes {

			public static short C3 = '\u00F3';
			public static short C$3 = '\u00E6';
			public static short D3 = '\u00D9';
			public static short D$3 = '\u00CC';
			public static short E3 = '\u00C1';
			public static short F3 = '\u00B6';
			public static short F$3 = '\u00AC';
			public static short G3 = '\u00A2';
			public static short G$3 = '\u0099';
			public static short A3 = '\u0090';
			public static short A$3 = '\u0088';
			public static short B3 = '\u0080';
			public static short C4 = '\u0079';
			public static short C$4 = '\u0072';
			public static short D4 = '\u006C';
			public static short D$4 = '\u0066';
			public static short E4 = '\u0060';
			public static short F4 = '\u005B';
			public static short F$4 = '\u0055';
			public static short G4 = '\u0051';
			public static short G$4 = '\u004C';
			public static short A4 = '\u0048';
			public static short A$4 = '\u0044';
			public static short B4 = '\u0040';
			public static short C5 = '\u003C';
			public static short C$5 = '\u0039';
			public static short D5 = '\u0035';
			public static short D$5 = '\u0032';
			public static short E5 = '\u002F';
			public static short F5 = '\u002D';
			public static short F$5 = '\u002A';
			public static short G5 = '\u0028';
			public static short G$5 = '\u0025';
			public static short A5 = '\u0023';
			public static short A$5 = '\u0021';
			public static short B5 = '\u001F';
			public static short C6 = '\u001E';
			public static short C$6 = '\u001C';
			public static short D6 = '\u001A';
			public static short D$6 = '\u0019';
			public static short E6 = '\u0017';
			public static short F6 = '\u0016';
			public static short F$6 = '\u0015';
			public static short G6 = '\u0013';
			public static short G$6 = '\u0012';
			public static short A6 = '\u0011';
			public static short A$6 = '\u0010';
			public static short B6 = '\u000F';
			public static short C7 = '\u000E';
			public static short S = '\u0000';
		}

		static final short song1[] = {
				Notes.G6,1,
				Notes.A6,1,
				Notes.B6,1,
				Notes.C7,2,
				Notes.G6,2,
				Notes.E6,2,
				Notes.C6,2,
				Notes.A6,6,
				Notes.S,1,
				Notes.B6,1,
				Notes.A6,1,
				Notes.G6,2,
				Notes.F6,2,
				Notes.E6,2,
				Notes.D6,2,
				Notes.C6,5,
				Notes.S,4
		};

//		private static final short[] song2=song1;

        SoundLoop(short voice, short volume) {

            this.voice = voice;
            this.volume = volume;

        }

		@Override
        public void run() {
            try {
                short i=0, j = 0;
                short elapse1 = 0;
                short elapse2 = 0;
                short pitch1,pitch2;

                for(;;) {
                	if(elapse1==0) {
						// Pause at the end of a note and before start th next
                		Atari.sound(0,0,0,0);
						// Atari.sound(1,0,0,0);
                		// Thread.sleep(1);

						if(i>=song1.length) {
							i=0;
							j=0;
							elapse2=0;
						}

						pitch1 = song1[i];
						elapse1 = song1[i + 1];
						i+=2;

						if(pitch1>0) {
							Atari.sound(0, pitch1, 10, volume);
							// Atari.sound(1, pitch1+2, 10, volume-2);
						}

					}
/*
					if(elapse2==0) {
						// Pause at the end of a note and before start th next
						Atari.sound(2,0,0,0);
						// Thread.sleep(1);

						if(j>=song2.length) j=0;
						pitch2 = song2[j];
						elapse2 = song2[j + 1];

						j+=2;

						if(pitch2>0)
							Atari.sound(2, pitch2, 10, volume);

					}*/
					Thread.sleep(25);
                	--elapse1;
					--elapse2;
                }
            } catch (Exception e) {
                ; // DoNothing
            }
        }
    }
}

