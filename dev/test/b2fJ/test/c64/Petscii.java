package b2fJ.test.c64;

/**
 * Prints first 128 ASCII characters to see if they look correct on the C64
 * 
 * @author maxi
 *
 */
public class Petscii {
	//
	// public static char prtAscii[] = {
	// ' ',
	// '!', '"', '#', '$', '%', '&', '\'', '(',
	// ')', '*', '+', ',', '-', '.', '/', '0',
	// '1', '2', '3', '4', '5', '6', '7', '8',
	// '9', ':', ';', '<', '=', '>', '?', '@',
	// 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
	// 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	// 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
	// 'Y', 'Z', '[', '\\', ']', '^', '_', '`',
	// 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
	// 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
	// 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
	// 'y', 'z', '{', '|', '}', '~', 127
	// };

	public static void main(String[] args) {
		System.out.print('A');
		System.out.print('B');
		System.out.print('\n');
		System.out.print('C');
		System.out.println();

		System.out.print('A');
		System.out.println('B');
		System.out.print('C');
		System.out.println();

		System.out.print("AB\n");
		System.out.print('C');
		System.out.println();

		System.out.println("AB");
		System.out.print('C');
		System.out.println();

		String s = null;
		System.out.println(s);
		System.out.println();

		Object o = null;
		System.out.println(o);
		System.out.println();

		for (char c = 32; c < 127; ++c) {
			System.out.print(c);
		}
	}
}
