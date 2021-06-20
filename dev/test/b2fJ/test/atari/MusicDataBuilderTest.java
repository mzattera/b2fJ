package b2fJ.test.atari;

import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

/**
 * Create a dump of a song from the provided data
 */
public class MusicDataBuilderTest {

    public static void main(String[] args) throws InterruptedException {
        // Start the sound loop
        SoundLoop soundLoop1 = new SoundLoop();
        soundLoop1.printSong();

    }

    public static class SoundLoop {

        // Note/Instruction + Duration/Parameter
        static final int[] song1 = {
                Notes.C$5*10000+23,
                Notes.F$3*10000+24,
                Notes.A$4*10000+53,
                Notes.F$4*10000+73,
                Notes.F$5*10000+93,
                Notes.D$3*10000+94,
                Notes.F5*10000+123,
                Notes.D$5*10000+143,
                Notes.C$5*10000+163,
                Notes.C$3*10000+164,
                Notes.F$5*10000+203,
                Notes.A$4*10000+213,
                Notes.C$5*10000+233,
                Notes.F3*10000+234,
                Notes.G$4*10000+253,
                Notes.D$3*10000+274,
                Notes.F3*10000+294,
                Notes.F$3*10000+304,
                Notes.C$5*10000+323,
                Notes.A$3*10000+324,
                Notes.C$5*10000+343,
                Notes.C$3*10000+344,
                Notes.C$5*10000+363,
                Notes.A$3*10000+364,
                Notes.C$5*10000+383,
                Notes.F$3*10000+384,
                Notes.A$4*10000+393,
                Notes.A$3*10000+394,
                Notes.G$4*10000+413,
                Notes.C$3*10000+414,
                Notes.F$4*10000+433,
                Notes.A$3*10000+434,
                Notes.F$3*10000+454,
                Notes.F$5*10000+463,
                Notes.B3*10000+464,
                Notes.F$5*10000+483,
                Notes.D$3*10000+484,
                Notes.F$5*10000+503,
                Notes.B3*10000+504,
                Notes.G$5*10000+523,
                Notes.F$3*10000+524,
                Notes.F$5*10000+543,
                Notes.B3*10000+544,
                Notes.F5*10000+553,
                Notes.D$3*10000+554,
                Notes.D$5*10000+573,
                Notes.B3*10000+574,
                Notes.C$5*10000+593,
                Notes.F$3*10000+594,
                Notes.A$4*10000+613,
                Notes.A$3*10000+614,
                Notes.C$3*10000+634,
                Notes.A$4*10000+643,
                Notes.A$3*10000+644,
                Notes.F$5*10000+663,
                Notes.F$3*10000+664,
                Notes.A$3*10000+684,
                Notes.A$4*10000+703,
                Notes.C$3*10000+704,
                Notes.A$3*10000+724,
                Notes.C$5*10000+733,
                Notes.F3*10000+734,
                Notes.G$4*10000+753,
                Notes.G$3*10000+754,
                Notes.C$3*10000+774,
                Notes.G$3*10000+794,
                Notes.D$3*10000+814,
                Notes.G$3*10000+824,
                Notes.F3*10000+844,
                Notes.G$3*10000+864,
                Notes.F$3*10000+884,
                Notes.A$4*10000+893,
                Notes.A$3*10000+894,
                Notes.A$4*10000+913,
                Notes.C$3*10000+914,
                Notes.B4*10000+933,
                Notes.A$3*10000+934,
                Notes.C$5*10000+953,
                Notes.F$3*10000+954,
                Notes.D$5*10000+973,
                Notes.A$3*10000+974,
                Notes.F5*10000+983,
                Notes.C$3*10000+984,
                Notes.D$5*10000+1003,
                Notes.A$3*10000+1004,
                Notes.F$3*10000+1024,
                Notes.B3*10000+1044,
                Notes.D$3*10000+1064,
                Notes.B3*10000+1074,
                Notes.F$3*10000+1094,
                Notes.B3*10000+1114,
                Notes.D$3*10000+1134,
                Notes.B3*10000+1154,
                Notes.B4*10000+1163,
                Notes.F$3*10000+1164,
                Notes.B4*10000+1183,
                Notes.B3*10000+1184,
                Notes.C$5*10000+1203,
                Notes.D$3*10000+1204,
                Notes.D$5*10000+1223,
                Notes.B3*10000+1224,
                Notes.F$3*10000+1244,
                Notes.F5*10000+1253,
                Notes.B3*10000+1254,
                Notes.F$5*10000+1273,
                Notes.D$3*10000+1274,
                Notes.F5*10000+1293,
                Notes.B3*10000+1294,
                Notes.G$3*10000+1314,
                Notes.C$4*10000+1324,
                Notes.C$3*10000+1344,
                Notes.C$4*10000+1364,
                Notes.G$3*10000+1384,
                Notes.C$4*10000+1404,
                Notes.C$5*10000+1413,
                Notes.C$3*10000+1414,
                Notes.C$4*10000+1434,
                Notes.F$5*10000+1453,
                Notes.F$3*10000+1454,
                Notes.A$3*10000+1474,
                Notes.C$3*10000+1494,
                Notes.A$3*10000+1504,
                Notes.F5*10000+1523,
                Notes.F$3*10000+1524,
                Notes.A$3*10000+1544,
                Notes.D$5*10000+1563,
                Notes.C$3*10000+1564,
                Notes.C$5*10000+1583,
                Notes.A$3*10000+1584,
                Notes.F5*10000+1593,
                Notes.F$3*10000+1594,
                Notes.B3*10000+1614,
                Notes.D$5*10000+1633,
                Notes.D$3*10000+1634,
                Notes.B3*10000+1654,
                Notes.F$3*10000+1674,
                Notes.B3*10000+1684,
                Notes.D$5*10000+1703,
                Notes.D$3*10000+1704,
                Notes.B3*10000+1724,
                Notes.C$5*10000+1743,
                Notes.F3*10000+1744,
                Notes.B3*10000+1754,
                Notes.G$5*10000+1773,
                Notes.C$3*10000+1774,
                Notes.B3*10000+1794,
                Notes.F$5*10000+1813,
                Notes.F3*10000+1814,
                Notes.B3*10000+1834,
                Notes.F5*10000+1843,
                Notes.C$3*10000+1844,
                Notes.B3*10000+1864,
                Notes.F$5*10000+1883,
                Notes.F$3*10000+1884,
                Notes.A$3*10000+1904,
                Notes.C$3*10000+1924,
                Notes.A$3*10000+1934,
                Notes.F$3*10000+1954,
                Notes.A$3*10000+1974,
                Notes.C$3*10000+1994,
                Notes.A$3*10000+2014,
                Notes.F$5*10000+2023,
                Notes.F$3*10000+2024,
                Notes.F$5*10000+2043,
                Notes.A$3*10000+2044,
                Notes.G$5*10000+2063,
                Notes.C$3*10000+2064,
                Notes.A$5*10000+2083,
                Notes.A$3*10000+2084
        };

        SoundLoop() {
        }

        public void printSong() {
            for (int i = 0; i < song1.length; i++) {
                out.println(song1[i]+",");
            }
        }

        public void run() {
        }

        static final class Notes {
            public static int C3 = '\u00F3';
            public static int C$3 = '\u00E6';
            public static int D3 = '\u00D9';
            public static int D$3 = '\u00CC';
            public static int E3 = '\u00C1';
            public static int F3 = '\u00B6';
            public static int F$3 = '\u00AC';
            public static int G3 = '\u00A2';
            public static int G$3 = '\u0099';
            public static int A3 = '\u0090';
            public static int A$3 = '\u0088';
            public static int B3 = '\u0080';
            public static int C4 = '\u0079';
            public static int C$4 = '\u0072';
            public static int D4 = '\u006C';
            public static int D$4 = '\u0066';
            public static int E4 = '\u0060';
            public static int F4 = '\u005B';
            public static int F$4 = '\u0055';
            public static int G4 = '\u0051';
            public static int G$4 = '\u004C';
            public static int A4 = '\u0048';
            public static int A$4 = '\u0044';
            public static int B4 = '\u0040';
            public static int C5 = '\u003C';
            public static int C$5 = '\u0039';
            public static int D5 = '\u0035';
            public static int D$5 = '\u0032';
            public static int E5 = '\u002F';
            public static int F5 = '\u002D';
            public static int F$5 = '\u002A';
            public static int G5 = '\u0028';
            public static int G$5 = '\u0025';
            public static int A5 = '\u0023';
            public static int A$5 = '\u0021';
            public static int B5 = '\u001F';
            public static int C6 = '\u001E';
            public static int C$6 = '\u001C';
            public static int D6 = '\u001A';
            public static int D$6 = '\u0019';
            public static int E6 = '\u0017';
            public static short F6 = '\u0016';
            public static int F$6 = '\u0015';
            public static int G6 = '\u0013';
            public static int G$6 = '\u0012';
            public static int A6 = '\u0011';
            public static int A$6 = '\u0010';
            public static int B6 = '\u000F';
            public static int C7 = '\u000E';
            public static short S = '\u0000';
        }

    }
}

