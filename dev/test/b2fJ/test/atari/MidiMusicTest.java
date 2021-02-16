package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class MidiMusicTest {

    public static void main(String[] args) throws InterruptedException {
        //out.println(i);
        // Start the sound loop
        SoundLoop soundLoop1 = (new SoundLoop((short) 8));
        //soundLoop1.printSong();
        soundLoop1.run();

    }

    private static class Atari {

        static final int[] AUDF = {'\uD200', '\uD202', '\uD204', '\uD206'};
        static final int[] AUDC = {'\uD201', '\uD203', '\uD205', '\uD207'};

        static boolean emulator = false;
        static char dv = 0;
        static char i = 0;

        static {
            // Initialize 8-bit sound
            poke(53768, 0);
        }

        static void sound(int voice, int pitch, int distortion, int volume) {
            poke(AUDF[voice], pitch);
            poke(AUDC[voice], (16 * distortion) + volume);
        }

        static void multiSound(int[] pitch, int distortion, int volume) {
            dv = (char)(16 * distortion + volume);
            for(i=2;i<pitch.length;i++) {
                if(pitch[i]>0) poke(AUDF[i],pitch[i]);
            }
            for(i=2;i<pitch.length;i++) {
                if(pitch[i]>0) poke(AUDC[i],dv);
            }
        }

    }

    public static class SoundLoop {

        // A note is packed in 32 bit integer
        // Note + Time (3 digits) + voice = 1210023
        // means 121 pitch, 002 time, voice 3
        static final int[] segment1 = {
                Notes.C$5+23,
                Notes.F$3+24,
                Notes.A$4+53,
                Notes.F$4+73,
                Notes.F$5+93,
                Notes.D$3+94,
                Notes.F5+123,
                Notes.D$5+143,
                Notes.C$5+163,
                Notes.C$3+164,
                Notes.F$5+203,
                Notes.A$4+213,
                Notes.C$5+233,
                Notes.F3+234,
                Notes.G$4+253,
                Notes.D$3+274,
                Notes.F3+294,
                Notes.F$3+323
        };
        
        // Note + Time + voice
        static final int[] segment2 = {
                Notes.C$5+323,
                Notes.A$3+324,
                Notes.C$5+343,
                Notes.C$3+344,
                Notes.C$5+363,
                Notes.A$3+364,
                Notes.C$5+383,
                Notes.F$3+384,
                Notes.A$4+393,
                Notes.A$3+394,
                Notes.G$4+413,
                Notes.C$3+414,
                Notes.F$4+433,
                Notes.A$3+434,
                Notes.F$3+454,
                Notes.F$5+463,
                Notes.B3+464,
                Notes.F$5+483,
                Notes.D$3+484,
                Notes.F$5+503,
                Notes.B3+504,
                Notes.G$5+523,
                Notes.F$3+524,
                Notes.F$5+543,
                Notes.B3+544,
                Notes.F5+553,
                Notes.D$3+554,
                Notes.D$5+573,
                Notes.B3+574,
                Notes.C$5+593,
                Notes.F$3+594,
                Notes.A$4+613,
                Notes.A$3+614,
                Notes.C$3+634,
                Notes.A$4+643,
                Notes.A$3+644,
                Notes.F$5+663,
                Notes.F$3+664,
                Notes.A$3+684,
                Notes.A$4+703,
                Notes.C$3+704,
                Notes.A$3+724,
                Notes.C$5+733,
                Notes.F3+734,
                Notes.G$4+753,
                Notes.G$3+754,
                Notes.C$3+774,
                Notes.G$3+794,
                Notes.D$3+814,
                Notes.G$3+824,
                Notes.F3+844,
                Notes.G$3+864,
                Notes.F$3+884,
                Notes.A$4+893,
                Notes.A$3+894,
                Notes.A$4+913,
                Notes.C$3+914,
                Notes.B4+933,
                Notes.A$3+934,
                Notes.C$5+953,
                Notes.F$3+954,
                Notes.D$5+973,
                Notes.A$3+974,
                Notes.F5+983,
                Notes.C$3+984,
                Notes.D$5+1003,
                Notes.A$3+1004,
                Notes.F$3+1024,
                Notes.B3+1044,
                Notes.D$3+1064,
                Notes.B3+1074,
                Notes.F$3+1094,
                Notes.B3+1114,
                Notes.D$3+1134,
                Notes.B3+1154,
                Notes.B4+1163,
                Notes.F$3+1164,
                Notes.B4+1183,
                Notes.B3+1184,
                Notes.C$5+1203,
                Notes.D$3+1204,
                Notes.D$5+1223,
                Notes.B3+1224,
                Notes.F$3+1244,
                Notes.F5+1253,
                Notes.B3+1254,
                Notes.F$5+1273,
                Notes.D$3+1274,
                Notes.F5+1293,
                Notes.B3+1294,
                Notes.G$3+1314,
                Notes.C$4+1324,
                Notes.C$3+1344,
                Notes.C$4+1364,
                Notes.G$3+1384,
                Notes.C$4+1404,
                Notes.C$5+1413,
                Notes.C$3+1414,
                Notes.C$4+1434,
                Notes.F$5+1453,
                Notes.F$3+1454,
                Notes.A$3+1474,
                Notes.C$3+1494,
                Notes.A$3+1504,
                Notes.F5+1523,
                Notes.F$3+1524,
                Notes.A$3+1544,
                Notes.D$5+1563,
                Notes.C$3+1564,
                Notes.C$5+1583,
                Notes.A$3+1584,
                Notes.F5+1593,
                Notes.F$3+1594,
                Notes.B3+1614,
                Notes.D$5+1633,
                Notes.D$3+1634,
                Notes.B3+1654,
                Notes.F$3+1674
        };

        static final int[] segment3 = {
                Notes.F$3+8404,
                Notes.F3+8424,
                Notes.G$4+8443,
                Notes.B3+8444,
                Notes.D$4+8463,
                Notes.C$3+8464,
                Notes.F4+8473,
                Notes.B3+8474,
                Notes.F$4+8493,
                Notes.F$3+8494,
                Notes.A$3+8514,
                Notes.C$3+8534,
                Notes.A$3+8554
        };

        static final int[] segment4 = {
                Notes.F$4+8493,
                Notes.F$3+8491,
                Notes.A$3+8512,
                Notes.C$3+8634,
                Notes.A$3+8654
        };



        static final int[] pitch = {0, 0, 0, 0};
        final short volume;

        SoundLoop(short volume) {

            this.volume = volume;

        }
/*
        public void printSong() {
            final Song song=new Song();
            int s=0,i=0;
            for (s = 0; s < song.sections.length; s++) {
                    int[] segment = song.sections[s].segment;
                    out.println("Segment:"+s);
                    for (i = 0; i < segment.length; i++) {
                        out.println(segment[i]);
                    }
            }
        }
*/
        public void run() throws InterruptedException {

            final Song song=new Song();

            out.println("Creating song");

            short i = 0, j = 0, t = 0;

            int[] song1;

            int pitch1 = 0;
            int color;
            int noteOn = 0;
            int note = 0;
            int voice = 0;
            int time = 0;
            int noteTime =0;

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
                        noteTime = (note / 10) % 1000;
                    }

                    while (i < song1.length) {

                        // Pause at the end of a note and before start th next
                        // Atari.chorusSound(pitch1, 10, 1);
                        //Thread.sleep(1);

                        while (time == noteTime) {

                            pitch1 = (int) (note / Notes.FACTOR);
                            voice = note % 10;
                            Atari.sound(voice - 1, 0, 0, 0);
                            if (pitch1 > 0) {
                                if (pitch1 > 0) {
                                    pitch[voice - 1] = pitch1;
                                }
                            } else {
                                pitch[voice - 1] = 0;
                            }

                            i += 1;
                            if (i < song1.length) {
                                note = song1[i];
                                noteTime = (note / 10) % 1000;
                            } else {
                                noteTime = 0; // Force play notes and exit
                            }
                            // If we get all time info, play the sounds for this time
                            if (time != noteTime) {
                                Atari.multiSound(pitch, 10, 6);
                                //if(time%10==0) out.println(time);
                            }
                        }

                        Thread.sleep(1);
                        time++;
                    }
                    if (pitch[0] > 0) Atari.sound(0, 0, 10, 6);
                    if (pitch[1] > 0) Atari.sound(1, 0, 10, 6);
                    if (pitch[2] > 0) Atari.sound(2, 0, 10, 6);
                    if (pitch[3] > 0) Atari.sound(3, 0, 10, 6);
                }

            }

        }

        static final class Notes {

            static final char FACTOR = '\u2710'; // 10000 decimal

            public static int C3 = '\u00F3' * FACTOR;
            public static int C$3 = '\u00E6' * FACTOR;
            public static int D3 = '\u00D9' * FACTOR;
            public static int D$3 = '\u00CC' * FACTOR;
            public static int E3 = '\u00C1' * FACTOR;
            public static int F3 = '\u00B6' * FACTOR;
            public static int F$3 = '\u00AC' * FACTOR;
            public static int G3 = '\u00A2' * FACTOR;
            public static int G$3 = '\u0099' * FACTOR;
            public static int A3 = '\u0090' * FACTOR;
            public static int A$3 = '\u0088' * FACTOR;
            public static int B3 = '\u0080' * FACTOR;
            public static int C4 = '\u0079' * FACTOR;
            public static int C$4 = '\u0072' * FACTOR;
            public static int D4 = '\u006C' * FACTOR;
            public static int D$4 = '\u0066' * FACTOR;
            public static int E4 = '\u0060' * FACTOR;
            public static int F4 = '\u005B' * FACTOR;
            public static int F$4 = '\u0055' * FACTOR;
            public static int G4 = '\u0051' * FACTOR;
            public static int G$4 = '\u004C' * FACTOR;
            public static int A4 = '\u0048' * FACTOR;
            public static int A$4 = '\u0044' * FACTOR;
            public static int B4 = '\u0040' * FACTOR;
            public static int C5 = '\u003C' * FACTOR;
            public static int C$5 = '\u0039' * FACTOR;
            public static int D5 = '\u0035' * FACTOR;
            public static int D$5 = '\u0032' * FACTOR;
            public static int E5 = '\u002F' * FACTOR;
            public static int F5 = '\u002D' * FACTOR;
            public static int F$5 = '\u002A' * FACTOR;
            public static int G5 = '\u0028' * FACTOR;
            public static int G$5 = '\u0025' * FACTOR;
            public static int A5 = '\u0023' * FACTOR;
            public static int A$5 = '\u0021' * FACTOR;
            public static int B5 = '\u001F' * FACTOR;
            public static int C6 = '\u001E' * FACTOR;
            public static int C$6 = '\u001C' * FACTOR;
            public static int D6 = '\u001A' * FACTOR;
            public static int D$6 = '\u0019' * FACTOR;
            public static int E6 = '\u0017' * FACTOR;
            public static int F6 = '\u0016' * FACTOR;
            public static int F$6 = '\u0015' * FACTOR;
            public static int G6 = '\u0013' * FACTOR;
            public static int G$6 = '\u0012' * FACTOR;
            public static int A6 = '\u0011' * FACTOR;
            public static int A$6 = '\u0010' * FACTOR;
            public static int B6 = '\u000F' * FACTOR;
            public static int C7 = '\u000E' * FACTOR;
            public static int S = '\u0000';
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
            Section[] sections = new Section[5];

            Song() {
                sections[0] = new Section(1, segment1);
                sections[1] = new Section(1, segment2);
                sections[2] = new Section(1, segment1);
                sections[3] = new Section(2, segment3);
                sections[4] = new Section(3, segment4);
            }
        }
    }
}

