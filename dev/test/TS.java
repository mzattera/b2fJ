import b2fj.io.ConsoleOutputStream;

public class TS {

	private void doRun() {
        String s = "Ciao.";
        System.out.print(s);
        
		s = "bbb";
        System.out.println(s);
        
		System.out.print("ccc");
        
		s = "ddd";
        System.out.println(s);

        System.out.println("eee");

        System.out.println("fff\n\nggg");
	}

	public static void main(String[] args) {
		TS c = new TS();
		c.doRun();
	}
}
