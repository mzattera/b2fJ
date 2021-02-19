import static b2fj.memory.Memory.peek;
import static b2fj.memory.Memory.poke;
import static java.lang.System.out;

public class StringGCTest {

	static String helloWorld = "Hello no worries";
	static String about = "about garbage!";

	public static void main(String[] args) throws InterruptedException {
		int i=0;
		try {
			while (true) {
				String output=helloWorld+" "+about+" "+String.valueOf(i);
				out.println(output);
				i++;
			}
		} catch(Throwable e) {
			out.println('*');
			out.println(i);
			throw e;
		}


	}
}