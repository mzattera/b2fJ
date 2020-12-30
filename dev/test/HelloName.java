import java.io.*;

public class HelloName {
	
	public static void main(String[] args) throws IOException {
		System.out.print("1> ");  
		System.out.println("read: " + System.in.read());  
		
        InputStreamReader isr = new InputStreamReader(System.in); 
		System.out.print("2> ");  
		System.out.println("read: " + isr.read());  
		
        BufferedReader reader =  new BufferedReader(isr); 
		System.out.print("3> ");  
        String name = reader.readLine(); 
        System.out.println(name);    
	}
}