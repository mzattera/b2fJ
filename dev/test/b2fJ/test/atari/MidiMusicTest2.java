package b2fJ.test.atari;

import static java.lang.System.out;

public class MidiMusicTest2 {

    public static void main(String[] args) throws InterruptedException {

        SoundLoop soundLoop1 = (new SoundLoop((short) 8));

        soundLoop1.run();

    }

    public static class SoundLoop {

        static final short[] pitch = {0, 0};
        // A note is packed in 32 bit integer
        // Note + Time (3 digits) + voice = 1210023
        //  1210023 means 121 pitch, 002 time, voice 3
        final int[] segment1 = {
                57_002_3,
               172_002_4,
                68_005_3,
                85_007_3,
                42_009_3,
               204_009_4,
                45_012_3,
                50_014_3,
                57_016_3,
               230_016_4,
                42_020_3,
                68_021_3,
                57_023_3,
                182_023_4,
                76_025_3,
                204_027_4,
                182_029_4,
                172_030_4,
                57_032_3,
                136_032_4,
                57_034_3,
                23_0034_4,
                57036_3,
                136036_4,
                57038_3,
                172038_4,
                68039_3,
                136039_4,
                76041_3,
                230041_4,
                85043_3,
                136043_4,
                172045_4,
                42046_3,
                128046_4,
                42048_3,
                204048_4,
                42050_3,
                128050_4,
                37052_3,
                172052_4,
                42054_3,
                128054_4,
                45055_3,
                204055_4,
                50057_3,
                128057_4,
                57059_3,
                172059_4,
                68061_3,
                136061_4,
                230063_4,
                68064_3,
                136064_4,
                42066_3,
                172066_4,
                136068_4,
                68070_3,
                230070_4,
                136072_4,
                57073_3,
                182073_4,
                76075_3,
                153075_4,
                230077_4,
                153079_4,
                204081_4,
                153082_4,
                182084_4,
                153086_4,
                172088_4,
                68089_3,
                136089_4,
                68091_3,
                230091_4,
                64093_3,
                136093_4,
                57095_3,
                172095_4,
                50097_3,
                136097_4,
                45098_3,
                230098_4,
                50100_3,
                136100_4,
                172102_4,
                128104_4,
                204106_4,
                128107_4,
                172109_4,
                128111_4,
                204113_4,
                128115_4,
                64116_3,
                172116_4,
                64118_3,
                128118_4,
                57120_3,
                204120_4,
                50122_3,
                128122_4,
                172124_4,
                45125_3,
                128125_4,
                42127_3,
                204127_4,
                45129_3,
                128129_4,
                153131_4,
                114132_4,
                230134_4,
                114136_4,
                153138_4,
                114140_4,
                57141_3,
                230141_4,
                114143_4,
                42145_3,
                172145_4,
                136147_4,
                230149_4,
                136150_4,
                45152_3,
                172152_4,
                136154_4,
                50156_3,
                230156_4,
                57158_3,
                136158_4,
                45159_3,
                172159_4,
                128161_4,
                50163_3,
                204163_4,
                128165_4,
                172167_4,
                128168_4,
                50170_3,
                204170_4,
                128172_4,
                57174_3,
                182174_4,
                128175_4,
                37177_3,
                230177_4,
                128179_4,
                42181_3,
                182181_4,
                128183_4,
                45184_3,
                230184_4,
                128186_4,
                42188_3,
                172188_4,
                136190_4,
                230192_4,
                136193_4,
                172195_4,
                136197_4,
                230199_4,
                136201_4,
                202_3,
                202_4,
                210_3,
                210_4
                //422023,
                //1722024,
                //422043,
                //1362044
        };
        final short volume;

        SoundLoop(short volume) {

            this.volume = volume;

        }

        public void run() throws InterruptedException {

            final Song song = new Song();

            out.println("Creating song");

            short i = 0, j = 0, t = 0;

            int[] song1;

            int pitch1 = 0;
            int color;
            int noteOn = 0;
            int note = 0;
            int voice = 0;
            int time = 0;
            int noteTime = 0;

            for (t = 0; t < song.sections.length; t++) {
                for (j = 0; j < song.sections[t].times; j++) {
                    song1 = song.sections[t].segment;
                    time = song.sections[t].startTime;
                    noteTime = time;
                    pitch1 = 0;
                    color = 0;
                    noteOn = 0;
                    note = 0;
                    voice = 0;

                    i = 0;

                    if (noteTime == time) {
                        note = song1[i];
                        noteTime = ((note / 10) % 1000);
                    }

                    while (i < song1.length) {

                        // Pause at the end of a note and before start th next
                        //Thread.sleep(1);

                        while (time == noteTime) {
                            pitch1 = (note / 10000);
                            voice = (note % 10)-3;
                            AtariSound.sound(voice, 0, 0, 0);
                            if (pitch1 > 0) {
                                pitch[voice] = (short)pitch1;
                            } else {
                                pitch[voice] = 0;
                            }

                            i += 1;
                            if (i < song1.length) {
                                note = song1[i];
                                noteTime = ((note / 10) % 1000);
                            } else {
                                noteTime = 0; // Force play notes and exit
                            }
                            // If we get all time info, play the sounds for this time
                            if (time != noteTime) {
                                AtariSound.multiSound(pitch, 10, 6);
                            }
                        }
                        Thread.sleep(1);
                        time++;
                    }
                    AtariSound.sound(0, 0, 0, 0);
                    AtariSound.sound(1, 0, 0, 0);
                    //if (pitch[2] > 0) Atari.sound(2, 0, 10, 6);
                    //if (pitch[3] > 0) Atari.sound(3, 0, 10, 6);
                }

            }

        }

        class Section {
            final int[] segment;
            int times;
            int startTime;

            public Section(int times, int[] segment) {
                this.times = times;
                this.segment = segment;
                int note = segment[0];
                int noteTime = (note / 10) % 1000;
                this.startTime = noteTime;
            }
        }

        class Song {
            Section[] sections = new Section[2];

            Song() {
                sections[0] = new Section(1, segment1);
                sections[1] = new Section(1, segment1);
            }
        }
    }
}

