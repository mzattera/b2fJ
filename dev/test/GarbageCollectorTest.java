import java.util.List;
import java.util.Queue;

import static java.lang.System.out;

/**
 * This example test fails without the Garbage Collector enabled on platform_config.h
 */
public class GarbageCollectorTest {

	class Point {
		private final int x,y;
		Point(int x,int y) {
			this.x=x;
			this.y=y;
		}

		@Override
		public String toString() {
			return "Point[" +
					"x=" + x +
					", y=" + y +
					']';
		}
	}

	private void run() {
		final String helloWorld = new String("Hello don't worry " );
		final String about = new String("about RAM! " );

		int x=0;

		Queue pointQueue = new Queue();

		// Put some points in the queue
		pointQueue.push(new Point(10,20));

		while (true) {
			Integer integer = new Integer(x);
			String output=helloWorld+about+"->"+String.valueOf(x);
			out.println(output);

			// Peek the a point from the queue and print it
			out.println(pointQueue.pop());

			// Push a new point
			pointQueue.push(new Point(x,x));

			x++;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		GarbageCollectorTest test = new GarbageCollectorTest();

		test.run();

	}
}