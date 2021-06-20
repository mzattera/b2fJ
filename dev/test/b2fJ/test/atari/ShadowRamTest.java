package b2fJ.test.atari;

import java.util.Queue;
import java.util.Random;

import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

/**
 * This example test fails without the Garbage Collector enabled on platform_config.h
 */
public class ShadowRamTest {

	private static final int SHADOW1_START = 0xD800;
	private static final int SHADOW1_END = 0xDFFF;
	private static final int SHADOW2_START = 0xE400;
	private static final int SHADOW2_END = 0xFFF9;

	Random random = new Random(3423);

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
		final char[] helloWorld = "Hello".toCharArray();
		final char[] about = "about RAM!".toCharArray();

		int x=0;

		Queue pointQueue = new Queue();

		// Put some points in the queue
		pointQueue.push(new Point(10,20));

		int address = SHADOW1_START;
		int original = 0;

		while (true) {
			int integer =x;
			String output=  new String(helloWorld)+new String(about)+"->"+String.valueOf(x);
			out.println(output);

			// Peek the a point from the queue and print it
			((Point)pointQueue.pop()).print();

			// Push a new point
			pointQueue.push(new Point(x,x));

			original=peek(address);
			int number=random.nextInt(255);
			poke(address,number);

			if (!(original==0 && number==peek(address))) {
				out.println(address + " NOK.");
				System.exit(1);
			} else {
				out.println(address + " OK.");
			}

			address++;

			if (address== SHADOW1_END)
				address= SHADOW2_START;
			else if (address== SHADOW2_END)
				address= SHADOW1_START;

			x++;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ShadowRamTest test = new ShadowRamTest();

		test.run();

	}
}