import b2fj.util.Debug;

public class Inheritance {
	
	public static class AClass {
		private String name = null;

		public String getName() {
			return name;
		}
		
		protected AClass(String name) {
			this.name = name;
		}
	}
	
	public static class BClass extends AClass {
		private BClass(String name) {
			super(name);
		}
	}
			
	public static void main(String[] args) {
		BClass b = new BClass("A B Class Instance");
        System.out.println(b.getName());    
	}
}