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

			public static short C3= 0xF3;
			public static short C$3= 0xE6;
			public static short D3= 0xD9;
			public static short D$3= 0xCC;
			public static short E3= 0xC1;
			public static short F3= 0xB6;
			public static short F$3= 0xAC;
			public static short G3= 0xA2;
			public static short G$3= 0x99;
			public static short A3= 0x90;
			public static short A$3=0x88;
			public static short B3=0x80;
			public static short C4=0x79;
			public static short C$4=0x72;
			public static short D4=0x6C;
			public static short D$4=0x66;
			public static short E4=0x60;
			public static short F4=0x5B;
			public static short F$4=0x55;
			public static short G4=0x51;
			public static short G$4=0x4C;
			public static short A4=0x48;
			public static short A$4=0x44;
			public static short B4=0x40;
			public static short C5=0x3C;
			public static short C$5=0x39;
			public static short D5=0x35;
			public static short D$5=0x32;
			public static short E5=0x2F;
			public static short F5=0x2D;
			public static short F$5=0x2A;
			public static short G5=0x28;
			public static short G$5=0x25;
			public static short A5=0x23;
			public static short A$5=0x21;
			public static short B5=0x1F;
			public static short C6=0x1E;
			public static short C$6=0x1C;
			public static short D6=0x1A;
			public static short D$6=0x19;
			public static short E6=0x17;
			public static short F6=0x16;
			public static short F$6=0x15;
			public static short G6=0x13;
			public static short G$6=0x12;
			public static short A6=0x11;
			public static short A$6=0x10;
			public static short B6=0x0F;
			public static short C7=0x0E;
			public static short S=0x00;
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

