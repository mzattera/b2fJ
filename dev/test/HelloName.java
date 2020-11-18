import java.io.*;

public class HelloName {
	
	public static void main(String[] args) throws IOException {
		
		Reader isr = new InputStreamReader(System.in);
		while (true){
			System.out.print(">: ");
			System.out.println((char)isr.read());
		}
		
		/*
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in)); 
        String name = reader.readLine(); 
        System.out.println(name);    
		*/
	}
}