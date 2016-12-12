import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GenerationCode {
	
	public GenerationCode(){
	}
	
	public void genererCode(Arbre a){
		
		try (PrintWriter out = new PrintWriter( "test.txt" )){
			
			out.println(".start");
			
			out.println(interpretToken(a));
			
			out.println("halt");
		}
		
		catch (FileNotFoundException e) {
	         e.printStackTrace();

	    }
		
		catch (IOException e) {

	         e.printStackTrace();
		}
	}
	
	public String interpretToken(Arbre a) {
		
		String code = "";
		
		switch (a.getToken().getClassname()) {
		
			case ("cst_int"):
				code += "push.i " + a.getToken().getName() + "\n";
				break;
			case ("+"):				
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "add.i\n";
				break;
			case ("-"):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "sub.i\n";
				break;
			case ("*"):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "mul.i\n";
				break;
		}
		
		return code;
	}
}
