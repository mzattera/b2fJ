import static java.lang.System.out;

public class Iterate {

	static String message="Welcome To Lejos";
	static String msg_iteration="Iteration: ";
	static String msg_total=" Total: ";

	public static void main(String[] args) throws InterruptedException {
		out.println(message);
		out.println("**************************");
		int total=0;
		for (byte i=0;i<127;i++) {
			total+=i;
			out.print(msg_iteration);
			out.print(i);
			out.print(msg_total);
			out.println(total);
			Thread.sleep(100);
		}
                out.println("**************************");
                out.println("THE END.");
                out.print("\n");
	}
}
