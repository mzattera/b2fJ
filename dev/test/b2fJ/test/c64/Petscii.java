package b2fJ.test.c64;

/**
 * Prints first 128 ASCII characters to see if they look correct on the C64
 * 
 * @author maxi
 *
 */
public class Petscii {
	
	public static char prtAscii[] = {
			' ',
			'!', '"', '#', '$', '%', '&', '\'', '(',
			')', '*', '+', ',', '-', '.', '/', '0',
			'1', '2', '3', '4', '5', '6', '7', '8',
			'9', ':', ';', '<', '=', '>', '?', '@',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', '[', '\\', ']', '^', '_', '`',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z', '{', '|', '}', '~', 127
		};
	
	public static void main(String[] args) {
//		for (int i=32; i<128; ++i) {
//			System.out.print((char)i);
//			if (((i-32) % 20) == 19)
//				System.out.println();
//		}
		int i=0;
		for (char c : prtAscii) {
			System.out.print(c);
			if ((i% 20) == 19)
				System.out.println();
		}
	}
}
