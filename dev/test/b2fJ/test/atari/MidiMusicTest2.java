package b2fJ.test.atari;

import static java.lang.System.out;

public class MidiMusicTest2 {

    public static void main(String[] args) throws InterruptedException {
        //out.println(i);
        // Start the sound loop
        SoundLoop soundLoop1 = (new SoundLoop((short) 8));
        //soundLoop1.printSong();
        soundLoop1.run();

    }

    public static class SoundLoop {

        static final int[] pitch = {0, 0};
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
                68021_3,
                57023_3,
                182023_4,
                76025_3,
                204027_4,
                182029_4,
                172030_4,
                57032_3,
                136032_4,
                57034_3,
                2300344,
                570363,
                1360364,
                570383,
                1720384,
                680393,
                1360394,
                760413,
                2300414,
                850433,
                1360434,
                1720454,
                420463,
                1280464,
                420483,
                2040484,
                420503,
                1280504,
                370523,
                1720524,
                420543,
                1280544,
                450553,
                2040554,
                500573,
                1280574,
                570593,
                1720594,
                680613,
                1360614,
                2300634,
                680643,
                1360644,
                420663,
                1720664,
                1360684,
                680703,
                2300704,
                1360724,
                570733,
                1820734,
                760753,
                1530754,
                2300774,
                1530794,
                2040814,
                1530824,
                1820844,
                1530864,
                1720884,
                680893,
                1360894,
                680913,
                2300914,
                640933,
                1360934,
                570953,
                1720954,
                500973,
                1360974,
                450983,
                2300984,
                501003,
                1361004,
                1721024,
                1281044,
                2041064,
                1281074,
                1721094,
                1281114,
                2041134,
                1281154,
                641163,
                1721164,
                641183,
                1281184,
                571203,
                2041204,
                501223,
                1281224,
                1721244,
                451253,
                1281254,
                421273,
                2041274,
                451293,
                1281294,
                1531314,
                1141324,
                2301344,
                1141364,
                1531384,
                1141404,
                571413,
                2301414,
                1141434,
                421453,
                1721454,
                1361474,
                2301494,
                1361504,
                451523,
                1721524,
                1361544,
                501563,
                2301564,
                571583,
                1361584,
                451593,
                1721594,
                1281614,
                501633,
                2041634,
                1281654,
                1721674,
                1281684,
                501703,
                2041704,
                1281724,
                571743,
                1821744,
                1281754,
                371773,
                2301774,
                1281794,
                421813,
                1821814,
                1281834,
                451843,
                2301844,
                1281864,
                421883,
                1721884,
                1361904,
                2301924,
                1361934,
                1721954,
                1361974,
                2301994,
                1362014
                //422023,
                //1722024,
                //422043,
                //1362044
        };
        final short volume;

        SoundLoop(short volume) {

            this.volume = volume;

        }

        public void run() {

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
                //out.println();
                //out.print(t);
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
                            voice = (note % 10) - 3;
                            Atari.sound(voice, 0, 0, 0);
                            if (pitch1 > 0) {
                                pitch[voice] = pitch1;
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
                                Atari.multiSound(pitch, 10, 8);
                                //if(time%10==0) out.println(time);
                            }
                        }

                        ///Thread.sleep(1);
                        time++;
                    }
                    if (pitch[0] > 0) Atari.sound(0, 0, 10, 6);
                    if (pitch[1] > 0) Atari.sound(1, 0, 10, 6);
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

