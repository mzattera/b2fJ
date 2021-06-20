package b2fJ.test.atari;

import java.util.Random;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class SpriteMusicTest {

	static final Random random = new Random(2304);

    private static short bitmap1[] = {
            0x42, 0x24, 0x7E, 0x5A,
            0xFF, 0xFF, 0xBD, 0x99,
            0x5A, 0x18, 0x3C, 0x66,
            0x42, 0x24, 0x24};

    private static int PMPAGE = 228;

    private final PlayerMissile[] player = new PlayerMissile[4];

    SpriteMusicTest() {

        SoundLoop soundLoop1 = (new SoundLoop((short) 0, (short) 8));
        soundLoop1.start();

        PlayerMissile.initClass();
        player[0] = new PlayerMissile(0, 24, 64, 2);
        player[1] = new PlayerMissile(1, 32, 32, 3);
        player[2] = new PlayerMissile(2, 218, 72, -4);
        player[3] = new PlayerMissile(3, 34, 48, 5);

        // Thread dedicated to animations and effects
        player[0].start();
        player[0].reactToCollitions = true;

        player[2].doDoubleWidth();


    }

    public static void main(String[] args) {

        SpriteMusicTest playerMissile = new SpriteMusicTest();

        playerMissile.run();

    }

    void run() {

        // Declare here all variables trying to save stack in loops
        int i = 0;

        // Test kernel
        while (true) {
            // Update location data
            PlayerMissile.clearCollisionsFlag();
            for (i = 0; i < 4; i++) {
                player[i].doStep();
            }
        }

    }

    private static class Atari {

        static final int[] AUDF = {0xD200, 0xD202, 0xD204, 0xD206};
        static final int[] AUDC = {0xD201, 0xD203, 0xD205, 0xD207};

        static {
            // Initialize 8-bit sound
            poke(53768, 0);
        }

        static void sound(int voice, int pitch, int distortion, int volume) {
            poke(AUDF[voice], pitch);
            poke(AUDC[voice], (16 * distortion) + volume);
        }

    }

    public static class PlayerMissile extends Thread {

        static final int SIZEP[] = {53256, 53257, 53258, 53259};
        static final int PXPL[] = {0xD00C, 0xD00D, 0xD00E, 0xD00F};
        static final int COLRP[] = {704, 705, 706, 707};
        static final int HPOSP[] = {53248, 53249, 53250, 53251};
        private static final int PM_OFFSET[] = {512, 640, 768, 896};
        private static int PMPAGE = 228; // CC65 left this page for applications
        private static int PMMEM = 0;

		private static final Object semaphore = new Object();

        private boolean reactToCollitions;

        private boolean collisionRecord = false; // We record a collision detection to know we are overlaping
        private int x, y, speed = 0;
        private int index = 0;

        PlayerMissile(int index, int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.index = index;

            // Initialize the player memory
			drawBitmap();
            updateScreen();
        }

        void drawBitmap() {
			int addr = 0;
			for (int l = 0; l < bitmap1.length; l++) {
				addr = PMMEM + PM_OFFSET[index] + (y + l);
				poke(addr, bitmap1[l]);
			};
		}

		void drawNoise() {
			int addr = 0;
			for (int l = 0; l < bitmap1.length; l++) {
				addr = PMMEM + PM_OFFSET[index] + (y + l);
                poke(addr, random.nextInt(255));
			};
		}

		static void initClass() {
            poke(623, 1);
            poke(559, 46);

            // Set colors
            poke(704, 58);
            poke(705, 88);
            poke(706, 0x34);
            poke(707, 196);
            poke( 709, 15);
            poke( 710,148);
            poke( 712, 148);

            poke(54279, PMPAGE); // Set the page that contains the Sprite memory
            poke(53277, 3); // Activates direct memory access to the memory
            // poke(53256,3);
            PMMEM = PMPAGE * 256;
            //Clean the Players memory
            out.println("Initializing...");
            for (int l = 0; l < 1024; l++) {
                poke(PMMEM + l, 0x00);
            }
            out.println("Done.");
        }

        // Clear the hardware collision flags for all the players
        static void clearCollisionsFlag() {
            poke(53278, 1);
        }

        void doDoubleWidth() {
            // Double width
            poke(SIZEP[index], 3);
        }

        void doCheckCollitions() {
            // Check collisions
            if (this.reactToCollitions) {
                boolean detected = isCollisionDetected();
                if (detected && !hasCollisionRecord()) {
                    setCollisionRecord(true);
                    synchronized (semaphore) {
                        semaphore.notify(); // Notifity my thread to run to do animations
                    }
                } else if (!detected && hasCollisionRecord()) {
                    setCollisionRecord(false);
                }
                ;
            }
        }

        /**
         * Updates the hardware registers based on the object state
         */
        synchronized void updateScreen() {
            poke(HPOSP[index], x);
            doCheckCollitions();
        }

        void doStep() {
            x = x + speed;
            updateScreen();
        }

        boolean isCollisionDetected() {
            return peek(PXPL[index]) > 0 ? true : false;
        }

        public boolean hasCollisionRecord() {
            return collisionRecord;
        }

        public void setCollisionRecord(boolean collisionState) {
            this.collisionRecord = collisionState;
        }

        public void doAnimations() {
            try {
                synchronized (semaphore) {
                    semaphore.wait();
                    semaphore.notifyAll(); // Notify all that is not need to wait for me
                }
                if (hasCollisionRecord()) {
                    int originalColor = peek(COLRP[index]);
                    for (int j = 4; j < 10; j++) {
                        Atari.sound(2, 180 + j, 12, j);
                        poke(COLRP[index], originalColor + j);
                    }
                    Atari.sound(2, 0, 0, 0);
                    drawNoise();
                    drawBitmap();
                    poke(COLRP[index], originalColor);
                }
            } catch (InterruptedException e) {
                ;
            }
        }

        // Animate and make a blink and sound while has collision record
        @Override
        public void run() {
            while (true) {
                doAnimations();
            }
        }
    }

    public static class SoundLoop extends Thread {

        /***
         * In Java numbers in code are 32-bit signed integers literals
         * then, here we are wasting 4*84=336 bytes at least
         */
        static final short song1[] = {
                Notes.G3$, 4, Notes.G3$, 1, Notes.D4$, 2, Notes.D4$, 1, // 2
                Notes.G4$, 1, Notes.G4$, 1, Notes.D4$, 2, Notes.D4$, 1, // 3
                Notes.C4$, 1, Notes.C4$, 1, Notes.G4$, 1, Notes.G4, 1, // 10
                Notes.C5$, 1, Notes.C5$, 1, Notes.G4$, 1, Notes.G4, 1, // 11
                Notes.F3$, 1, Notes.F3$, 1, Notes.C4$, 1, Notes.C4$, 1, // 14
                Notes.F4$, 1, Notes.F4$, 1, Notes.C4$, 1, Notes.C4$, 1, // 15
                Notes.G3$, 1, Notes.G3$, 1, Notes.D4$, 1, Notes.D4$, 1, // 18
                Notes.G4$, 1, Notes.G4$, 1, Notes.D4$, 1, Notes.E4, 2, // 21
                Notes.G4$, 1, Notes.G4$, 1, Notes.D4$, 2, Notes.E4, 2, // 21
                Notes.G4$, 2, Notes.G4, 8, Notes.S, 2, Notes.G3, 12, Notes.G3$, 24, Notes.S, 4
        };
        final short voice;
        final short volume;

        SoundLoop(short voice, short volume) {
            this.voice = voice;
            this.volume = volume;
        }

        @Override
        public void run() {
            try {
                short i = 0, j = 0;
                short elapse1 = 0;

                short pitch1;
                short pitch2;

                while (true) {
                    if (elapse1 == 0) {
                        // Pause at the end of a note and before start th next
                        Atari.sound(0, 0, 0, 0);
                        Atari.sound(1, 0, 0, 0);
                        // Thread.sleep(1);

                        if (i >= song1.length) {
                            i = 0;
                        }

                        pitch1 = song1[i];
                        elapse1 = song1[i + 1];

                        if (pitch1 > 0) {
                            Atari.sound(0, pitch1, 10, volume);
                            Atari.sound(1, pitch1 + 2, 10, volume - 2);
                        }

                        i += 2;

                    }

                    Thread.sleep(200);
                    --elapse1;
                }
            } catch (InterruptedException e) {
                ; // DoNothing
            }
        }

        static final class Notes {
            public final static short C3 = 0xF3;
            public final static short C3$ = 0xE6;
            public final static short D3 = 0xD9;
            public final static short D3$ = 0xCC;
            public final static short E3 = 0xC1;
            public final static short F3 = 0xB6;
            public final static short F3$ = 0xAC;
            public final static short G3 = 0xA2;
            public final static short G3$ = 0x99;
            public final static short A3 = 0x90;
            public final static short A3$ = 0x88;
            public final static short B3 = 0x80;
            public final static short C4 = 0x79;
            public final static short C4$ = 0x72;
            public final static short D4 = 0x6C;
            public final static short D4$ = 0x66;
            public final static short E4 = 0x60;
            public final static short F4 = 0x5B;
            public final static short F4$ = 0x55;
            public final static short G4 = 0x51;
            public final static short G4$ = 0x4C;
            public final static short A4 = 0x48;
            public final static short A4$ = 0x44;
            public final static short B4 = 0x40;
            public final static short C5 = 0x3C;
            public final static short C5$ = 0x39;
            public final static short D5 = 0x35;
            public final static short D5$ = 0x32;
            public final static short E5 = 0x2F;
            public final static short F5 = 0x2D;
            public final static short F5$ = 0x2A;
            public final static short G5 = 0x28;
            public final static short G5$ = 0x25;
            public final static short A5 = 0x23;
            public final static short A5$ = 0x21;
            public final static short B5 = 0x1F;
            public final static short C6 = 0x1E;
            public final static short C6$ = 0x1C;
            public final static short D6 = 0x1A;
            public final static short D6$ = 0x19;
            public final static short E6 = 0x17;
            public final static short F6 = 0x16;
            public final static short F6$ = 0x15;
            public final static short G6 = 0x13;
            public final static short G6$ = 0x12;
            public final static short A6 = 0x11;
            public final static short A6$ = 0x10;
            public final static short B6 = 0x0F;
            public final static short C7 = 0x0E;
            public final static short S = 0x00;
        }
    }
}

