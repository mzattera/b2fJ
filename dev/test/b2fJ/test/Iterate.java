package b2fJ.test;

import b2fj.lang.TinyString;
import java.io.IOException;

import static java.lang.System.out;

public class Iterate {

    static final TinyString message$ = new TinyString((byte)35);;
    static final TinyString value$ = new TinyString((byte)5);
    static final TinyString free$ = new TinyString((byte)5);
    static final TinyString hello$ = new TinyString("Hello World! Times:");
    static final TinyString msgfree$ =new TinyString(" Free:");

    private static int freeMemory() {
        return (int) System.getRuntime().freeMemory();
    }

    /**
     * Iterate using a byte array backed TinyString
     * appending an string and an integer without generate garbage
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        out.println(freeMemory()); // This leaves garbage
        System.gc();

        for (int i = 0; i < 50000; i++) {
            message$.clear();
            free$.setValue(freeMemory());
            message$.append(hello$).append(value$.setValue(i)).append(msgfree$).append(free$);
            out.write(message$.getBytes());
            out.write('\n'); // Not generate garbage
            // out.println(); // Generate garbage
        }
    }

}
