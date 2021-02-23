/**
 * 
 */
import java.util.Random;

/**
 * Shows an example of multi-threading.
 *
 * This test requires GC active.
 *
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class MultiThreadGCDemo extends Thread {

	private final static Random rnd = new Random(666);

	private final String name;

	// must do this as strings are not garbage collected and wil cause out of memory
	// private final String sleepMsg;
	// private final String wakeUpMsg;

	private MultiThreadGCDemo(String name) {
		this.name = name;
		System.out.println(name + " created.");
	}

	@Override
	public void run() {
		while (true) {
			String sleepMsg = name + " is going to sleep...";
			String wakeUpMsg = name + " wakes up and says hello! :-)";

			int sleepTime = rnd.nextInt(3) + 1;
			System.out.println(sleepMsg);
			try {
				Thread.sleep(sleepTime*1000);
			} catch (InterruptedException e) {
				break;
			}
			System.out.println(wakeUpMsg);
		}
	}

	public static void main(String[] args) throws Exception {
		String[] names = {"Inky", "Blinky", "Pinky", "Clyde"};
		MultiThreadGCDemo[] t = new MultiThreadGCDemo[names.length];
		
		for (int i = 0; i < names.length; ++i) {
			t[i] = new MultiThreadGCDemo(names[i]);
		}
		for (int i = 0; i < t.length; ++i) {
			t[i].start();
		}
		while (true) {}
	}
}
