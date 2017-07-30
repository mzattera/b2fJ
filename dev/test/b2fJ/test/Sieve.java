/**
 * Simple Eratostene's Sieve for testing int math speed.
 */
package b2fJ.test;

/**
 * @author maxi
 *
 */
public class Sieve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean[] r = new boolean[100];
		int i, n;

		for (i = 0; i < 100; r[i++] = true)
			;
		for (i = 2; i < 10; i++) {
			n = i * 2;
			do {
				r[n] = false;
				n = n + i;
			} while (n < 100);
		}
		for (i = 0; i < 100; i++) {
			if (r[i])
				System.out.println(i);
		}
	}

}
