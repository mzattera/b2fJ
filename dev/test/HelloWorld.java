public class HelloWorld {

	// We need a constant or b2fJ will allocate a new string each time and run out of memory.
	private final static String MSG = "Hello World! ";
	
	public static void main(String[] args) throws InterruptedException {
		while (true) {            
            System.out.print(MSG);
            Thread.sleep(500);
        }
	}
}