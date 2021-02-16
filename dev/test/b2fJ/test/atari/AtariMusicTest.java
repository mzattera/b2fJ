package b2fJ.test.atari;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class AtariMusicTest {

    public static void main(String[] args) throws InterruptedException {

    	// Start the sound loop
		SoundLoop soundLoop1 = (new SoundLoop((short)0,(short)8));
		soundLoop1.run();

		// Start the main thread
		// AtariMusicTest playerMissile = new AtariMusicTest();
		// playerMissile.run();

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

		static final class n {
			public static short C3= 0xF3;
			public static short C3$= 0xE6;
			public static short D3= 0xD9;
			public static short D3$= 0xCC;
			public static short E3= 0xC1;
			public static short F3= 0xB6;
			public static short F3$= 0xAC;
			public static short G3= 0xA2;
			public static short G3$= 0x99;
			public static short A3= 0x90;
			public static short A3$=0x88;
			public static short B3=0x80;
			public static short C4=0x79;
			public static short C4$=0x72;
			public static short D4=0x6C;
			public static short D4$=0x66;
			public static short E4=0x60;
			public static short F4=0x5B;
			public static short F4$=0x55;
			public static short G4=0x51;
			public static short G4$=0x4C;
			public static short A4=0x48;
			public static short A4$=0x44;
			public static short B4=0x40;
			public static short C5=0x3C;
			public static short C5$=0x39;
			public static short D5=0x35;
			public static short D5$=0x32;
			public static short E5=0x2F;
			public static short F5=0x2D;
			public static short F5$=0x2A;
			public static short G5=0x28;
			public static short G5$=0x25;
			public static short A5=0x23;
			public static short A5$=0x21;
			public static short B5=0x1F;
			public static short C6=0x1E;
			public static short C6$=0x1C;
			public static short D6=0x1A;
			public static short D6$=0x19;
			public static short E6=0x17;
			public static short F6=0x16;
			public static short F6$=0x15;
			public static short G6=0x13;
			public static short G6$=0x12;
			public static short A6=0x11;
			public static short A6$=0x10;
			public static short B6=0x0F;
			public static short C7=0x0E;
			public static short S=0x00;
		}


		static final short song1[] = {
				n.S  , 4, // 0
				n.C4,  1, n.B3,  1,  n.A3$, 1, n.A3,  1, // 1
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 2
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 3
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 4
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 5
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 6
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 7
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 8
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 9
				n.C4$, 1, n.C4$, 1,  n.G4$, 1, n.G4,  1, // 10
				n.C5$, 1, n.C5$, 1,  n.G4$, 1, n.G4,  1, // 11
				n.C4$, 1, n.C4$, 1,  n.G4$, 1, n.G4,  1, // 12
				n.C5$, 1, n.C5$, 1,  n.G4$, 1, n.G4,  1, // 13
				n.F3$, 1, n.F3$, 1,  n.C4$, 1, n.C4$, 1, // 14
				n.F4$, 1, n.F4$, 1,  n.C4$, 1, n.C4$, 1, // 15
				n.F3$, 1, n.F3$, 1,  n.C4$, 1, n.C4$, 1, // 16
				n.F4$, 1, n.F4$, 1,  n.C4$, 1, n.C4$, 1, // 17
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 18
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 19
				n.G3$, 1, n.G3$, 1,  n.D4$, 1, n.D4$, 1, // 20
				n.G4$, 1, n.G4$, 1,  n.D4$, 1, n.D4$, 1, // 21
				n.G3$, 32,// 22,23, 24, 25
				n.S  , 4 // 26
		};

		static final short song2[] = {
				n.S  , 4, // 0
				n.D5 ,  1, n.D5$, 1, n.E5,  1, n.F5,  1, // 1
				n.F5$, 16,  // 2
				n.C5$, 8,  // 6
				n.D5$, 4,  // 8
				n.C5$, 1, n.C5 , 1, n.G4$, 1, n.S, 1, // 9
				n.G5$, 32, // 10
				n.F5$, 16, // 18
				n.D5$, 32,
				n.S, 4,	   // 25
		};

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

                while(i<song1.length) {
                	if(elapse1==0) {
						// Pause at the end of a note and before start th next
                		Atari.sound(0,0,0,0);
						Atari.sound(1,0,0,0);
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
							Atari.sound(1, pitch1+2, 10, volume-2);
						}

					}

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

					}
					Thread.sleep(50);
                	--elapse1;
					--elapse2;
                }
            } catch (Exception e) {
                ; // DoNothing
            }
        }
    }
}

