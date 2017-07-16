public class TS {

	private void doRun() {
        boolean v = true;
        System.out.println(v);
        
        char v2 = 'M';
        System.out.println(v2);
        
        char[] v3 = {'a','b','c'};
        System.out.println(v3);
        
        int v6 = 666;
        System.out.println(v6);
        
        long v7 = 0x0102030405060708L;
        System.out.println(v7);
        
        System.out.println(this);
        
        System.out.println("Ho finito.");
        System.out.println();
        String s = "puoi chiudere.";
        for (int i=0; i<s.length(); ++i)
        	System.out.write(s.charAt(i));
        
        s = null;
        System.out.println(s);        
        
//        float v5 = 3.333333F;
//        System.out.println(v5);        
//        
//        double v4 = 3.14D;
//        System.out.println(v4);
	}

	public static void main(String[] args) {
		TS c = new TS();
		c.doRun();
	}
}
