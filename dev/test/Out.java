import b2fj.io.*;
import b2fj.util.*;

public class Out {
	public static class AClass {
		protected String name;
		private AClass(String name) {
			this.name=name;
		}
	}
	public static class BClass extends AClass {
		private BClass(String name, String lastname, double d) {
			super(name);
		}
		void print() {
			System.out.println(name);
		}
	}
	public static void main(String[] args) {
		// ConsoleOutputStream.getInstance().write(65);
		// ConsolePrintStream.getInstance().println("Ciao.");
		// AClass a = new AClass("AAA", 3.33d);
		// a.print();
		BClass b = new BClass("BBB", "SSS", 6.66d);
		b.print();		
	}
}
