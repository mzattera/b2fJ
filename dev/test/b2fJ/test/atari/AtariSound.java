package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;

class AtariSound {

    static final int[] AUDF = {0xD200, 0xD202, 0xD204, 0xD206};
    static final int[] AUDC = {0xD201, 0xD203, 0xD205, 0xD207};

    private static boolean emulator = false;
    private static int dv = 0;
    private static int i = 0;

    static {
        // Initialize 8-bit sound
        poke(53768, 0);
    }

    /**
     *
     * https://www.atarimagazines.com/v1n4/somesoundadvice.html
     *
     * @param voice
     * @param pitch
     * @param distortion
     * @param volume
     */
    static void sound(int voice, int pitch, int distortion, int volume) {
        poke(AUDF[voice], pitch);
        poke(AUDC[voice], (16 * distortion) + volume);
    }

    static void doubleSound(int pitch, int pitch2, int distortion, int volume) {
        dv = (16 * distortion) + volume;
        poke(AUDF[0], pitch);
        poke(AUDF[1], pitch2);
        poke(AUDC[0], dv);
        poke(AUDC[1], dv);
    }

    static void multiSound(short[] pitch, int distortion, int volume) {
        dv = (char)(16 * distortion + volume);
        int length=pitch.length;
        for(i=0;i<length;i++) {
            poke(AUDF[i],pitch[i]);
        }
        for(i=0;i<length;i++) {
            poke(AUDC[i],dv);
        }
    }
}
