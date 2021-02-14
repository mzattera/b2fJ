package b2fJ.test.atari;

import java.util.Random;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class PlayerMissileTest {

	static final Random random = new Random(2304);

    private static final short bitmap1[] = {
            0x42, 0x24, 0x7E, 0x5A,
            0xFF, 0xFF, 0xBD, 0x99,
            0x5A, 0x18, 0x3C, 0x66,
            0x42, 0x24, 0x24};

    private static final int PMPAGE = 228;

    private final PlayerMissile[] player = new PlayerMissile[4];

    PlayerMissileTest() {

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

        PlayerMissileTest playerMissile = new PlayerMissileTest();

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
			for (int l = 0; l < bitmap1.length; l+=2) {
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

}
