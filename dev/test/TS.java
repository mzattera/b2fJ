import b2fj.io.ConsoleOutputStream;

public class TS {

    private native void putStringToStdout0 (String s);

	private ConsoleOutputStream c = new ConsoleOutputStream();

	private void doRun() {
        String s = "Ciao.";
        System.out.print(s);
		putStringToStdout0 (s);
        
		s = "bbb";
        System.out.println(s);
		putStringToStdout0 (s);
        
		System.out.print("ccc");
		putStringToStdout0 ("ccc");
        
		s = "ddd";
        System.out.println(s);
		putStringToStdout0 (s);

        System.out.println("eee");
		putStringToStdout0 ("ccc");

        s = null;
        System.out.println(s);
	}

	
	private void doOut() {
        String s = "Ciao.";
        c.print(s);
        
		s = "bbb";
        c.println(s);
        
		c.print("ccc");
        
		s = "ddd";
        c.println(s);

		c.print("eee");

        s = null;
        System.out.println(s);
	}

	public static void main(String[] args) {
		TS c = new TS();
		c.doOut();
	}

}
