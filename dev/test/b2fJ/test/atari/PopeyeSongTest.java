package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class PopeyeSongTest {

	public static void main(String[] args) throws InterruptedException {

		// Start the sound loop
		SoundLoop soundLoop1 = (new SoundLoop((short)8));
		soundLoop1.run();

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

	public static class SoundLoop {

		final short volume;

		static final class Notes {

			public static short C3= 0xF3*100;
			public static short C$3= 0xE6*100;
			public static short D3= 0xD9*100;
			public static short D$3= 0xCC*100;
			public static short E3= 0xC1*100;
			public static short F3= 0xB6*100;
			public static short F$3= 0xAC*100;
			public static short G3= 0xA2*100;
			public static short G$3= 0x99*100;
			public static short A3= 0x90*100;
			public static short A$3=0x88*100;
			public static short B3=0x80*100;
			public static short C4=0x79*100;
			public static short C$4=0x72*100;
			public static short D4=0x6C*100;
			public static short D$4=0x66*100;
			public static short E4=0x60*100;
			public static short F4=0x5B*100;
			public static short F$4=0x55*100;
			public static short G4=0x51*100;
			public static short G$4=0x4C*100;
			public static short A4=0x48*100;
			public static short A$4=0x44*100;
			public static short B4=0x40*100;
			public static short C5=0x3C*100;
			public static short C$5=0x39*100;
			public static short D5=0x35*100;
			public static short D$5=0x32*100;
			public static short E5=0x2F*100;
			public static short F5=0x2D*100;
			public static short F$5=0x2A*100;
			public static short G5=0x28*100;
			public static short G$5=0x25*100;
			public static short A5=0x23*100;
			public static short A$5=0x21*100;
			public static short B5=0x1F*100;
			public static short C6=0x1E*100;
			public static short C$6=0x1C*100;
			public static short D6=0x1A*100;
			public static short D$6=0x19*100;
			public static short E6=0x17*100;
			public static short F6=0x16*100;
			public static short F$6=0x15*100;
			public static short G6=0x13*100;
			public static short G$6=0x12*100;
			public static short A6=0x11*100;
			public static short A$6=0x10*100;
			public static short B6=0x0F*100;
			public static short C7=0x0E*100;
			public static short S=0x00*100;
		}

		static final int song1[] = {
				Notes.B3+1,
				Notes.C$4+1,
				Notes.D$4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.E4+1,
				Notes.D$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.C$4+1,
				Notes.B3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.A3+1,
				Notes.A$3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.F$3+1,
				Notes.F$3+1,
				Notes.F$3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.F$3+1,
				Notes.B3+1,
				Notes.C$4+1,
				Notes.D$4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.E4+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.E4+1,
				Notes.D$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.C$4+1,
				Notes.G$3+1,
				Notes.G$3+1,
				Notes.C$4+1,
				Notes.B3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.A3+1,
				Notes.E3+1,
				Notes.E3+1,
				Notes.A3+1,
				Notes.A$3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.F$3+1,
				Notes.F$3+1,
				Notes.F$3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.B3+1,
				Notes.F$3+1
		};

		SoundLoop(short volume) {

			this.volume = volume;

		}

		public void printSong() {
			for(int i=0;i<song1.length;i++) {
				out.println(song1[i]);
			}
		}

		public void run() {
			try {
				short i=0, j = 0;
				int elapse1 = 0;
				int pitch1;

				for(;;) {
					// Pause at the end of a note and before start th next
					Atari.sound(0,0,0,0);
					//Thread.sleep(1);

					if(i>=song1.length) {
						i=0;
					}

					pitch1 = (song1[i]/100);
					elapse1 = (song1[i]-pitch1*100);
					i+=1;

					if(pitch1>0) {
						Atari.sound(0, pitch1, 10, volume);
					}

					Thread.sleep(50*elapse1);
				}
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
	}
}

