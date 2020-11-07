public class HelloWorld {
	
	public static void main(String[] args) throws InterruptedException {
		String s = "Hello World. ";
		while (true) {            
			System.out.print(s);
			Thread.sleep(1000);
	   }
	}
}