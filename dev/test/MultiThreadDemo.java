/**
 * 
 */
import java.util.Random;

/**
 * Shows an example of multi-threading.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class MultiThreadDemo extends Thread {
	
	private final static Random rnd = new Random(666);

	private final String name;
	
	// must do this as strings are not garbage collected and wil cause out of memory
	private final String sleepMsg;
	private final String wakeUpMsg;

	private MultiThreadDemo(String name) {
		this.name = name;
		sleepMsg = name + " is going to sleep...";
		wakeUpMsg = name + " wakes up and says hello! :-)";
		System.out.println(name + " created.");
	}

	@Override
	public void run() {
		while (true) {
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
		MultiThreadDemo[] t = new MultiThreadDemo[names.length];
		
		for (int i = 0; i < names.length; ++i) {
			t[i] = new MultiThreadDemo(names[i]);
		}
		for (int i = 0; i < t.length; ++i) {
			t[i].start();
		}
		while (true) {}
	}
}
