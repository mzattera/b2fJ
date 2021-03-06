/**
 * Tests some b2fJ internals relative to String class.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class Sting {

	private final static String C0 = "CCC";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a1 = "AAA";

		String a2 = "AAA";
		String b = "BBB";
		String c1 = C0;
		String c2 = C0;
		
//		System.out.println ("a1 offset: " + a1._TVM_constantOffset);
//		System.out.println ("a2 offset: " + a2._TVM_constantOffset);
	
		System.out.println ("a1 == a1 " + (a1==a1));
		System.out.println ("a1 == a2 " + (a1==a2));
		System.out.println ("a1 eq a1 " + a1.equals(a1));
		System.out.println ("a1 eq a2 " + a1.equals(a2));

		System.out.println ("a2 == b " + (a2==b));
		System.out.println ("a2 eq b " + a1.equals(b));
		
		a1 = a2;

		System.out.println ("a1 = a2 ");
		System.out.println ("a1 == a1 " + (a1==a1));
		System.out.println ("a1 == a2 " + (a1==a2));
		System.out.println ("a1 eq a1 " + a1.equals(a1));
		System.out.println ("a1 eq a2 " + a1.equals(a2));		

		System.out.println ("c1 == c1 " + (c1==c1));
		System.out.println ("c1 == c2 " + (c1==c2));
		System.out.println ("c1 eq c1 " + c1.equals(c1));
		System.out.println ("c1 eq c2 " + c1.equals(c2));

		/*
		System.out.println ("a1 next: " + (a1._TVM_next==null?"null":a1._TVM_next));
		System.out.println ("a2 next: " + (a2._TVM_next==null?"null":a2._TVM_next));
		System.out.println ("b next: " + (b._TVM_next==null?"null":b._TVM_next));
		System.out.println ("C0 next: " + (C0._TVM_next==null?"null":C0._TVM_next));
		System.out.println ("c1 next: " + (c1._TVM_next==null?"null":c1._TVM_next));
		System.out.println ("c2 next: " + (c2._TVM_next==null?"null":c2._TVM_next));
		*/
	}
}
