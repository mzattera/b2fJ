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

		public void print() {
			out.print('(');
			out.print(x);
			out.print(',');
			out.print(y);
			out.println(')');
		}
	}

	private void run() {
		final String helloWorld = "Hello";
		final String about = " GC !";

		int x=0;

		Queue pointQueue = new Queue();

		// Put some points in the queue
		pointQueue.push(new Point(10,20));

		while (true) {
			int integer =x;
			String output=helloWorld+about+"-> Iteration: "+String.valueOf(x);
			out.println(output);

			// Peek the a point from the queue and print it
			((Point)pointQueue.pop()).print();

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