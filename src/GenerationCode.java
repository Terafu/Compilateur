import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GenerationCode {

	private int label_counter = 0;
	private TableSymboles symboles;
	int niveau_bloc = 0;
	
	public GenerationCode(){
	}
	
	public void genererCode(Arbre a, TableSymboles _symboles){
		
		symboles = _symboles;
		
		try (PrintWriter out = new PrintWriter( "test.txt" )){
			
			out.println(".start");			

			out.print(interpretBlock(a, niveau_bloc));
			
			out.println("halt");
		}
		
		catch (FileNotFoundException e) {
	         e.printStackTrace();

	    }
		
		catch (IOException e) {

	         e.printStackTrace();
		}
	}
	
	public String interpretBlock(Arbre a, int niveau) {		
		
		String code = "";
		
		for(int i = 0; i < symboles.getStack(niveau).size(); i ++) {
			code += "push.i 0\n";
		}
		
		for (int i = 0; i < a.getEnfants().length; i ++) {	
			
			code += interpretToken(a.getEnfants()[i]);
		}
		
		return code;
	}
	
	public String interpretToken(Arbre a) {
		
		String code = "";
		
		switch (a.getToken().getClassname()) {
		
			case ("cst_int"):
				code += "push.i " + a.getToken().getName() + "\n";
				break;
			case ("identifier"):
				code += "get " + a.getToken().getName() + "\n";
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
			case ("=="):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmpeq.i\n";
				break;
			case ("!="):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmpne.i\n";
				break;
			case (">"):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmpgt.i\n";
				break;
			case (">="):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmpge.i\n";
				break;
			case ("<"):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmplt.i\n";
				break;
			case ("<="):
				code += interpretToken(a.getEnfants()[0]);
				code += interpretToken(a.getEnfants()[1]);
				code += "cmple.i\n";
				break;
			case ("if"):
				int if_counter = label_counter++;
				code += interpretToken(a.getEnfants()[0]);
				code += "jumpf else" + if_counter + "\n";
				niveau_bloc++;
				code += interpretBlock(a.getEnfants()[1], niveau_bloc);
				if (a.getEnfants().length > 2) {
					code += "jump fin_else" + if_counter + "\n";
					niveau_bloc++;
					code += ".else" + if_counter + "\n" + interpretBlock(a.getEnfants()[2].getEnfants()[0], niveau_bloc);
					code += ".fin_else" + if_counter + "\n";
				}
				
				else {
					code += ".else" + if_counter + "\n";
				}
				
				break;
			case ("while"):
				int while_counter = label_counter++;
				code += ".while" + while_counter + "\n" + interpretToken(a.getEnfants()[0]);
				code += "jumpf fin_while" + while_counter + "\n";
				niveau_bloc++;
				code += interpretBlock(a.getEnfants()[1], niveau_bloc);
				code += "jump while" + while_counter + "\n";
				code += ".fin_while" + while_counter + " ";
				break;
			case ("for"):
				int for_counter = label_counter++;
				code += interpretToken(a.getEnfants()[0]);
				code += ".for" + for_counter + "\n" + interpretToken(a.getEnfants()[1]);
				code += "jumpf fin_for" + for_counter + "\n";
				code += interpretToken(a.getEnfants()[2]);
				niveau_bloc++;
				code += interpretBlock(a.getEnfants()[3], niveau_bloc);
				code += "jump for" + for_counter + "\n";
				code += ".fin_for" + for_counter + "\n";
				break;
			case ("="):
				code += interpretToken(a.getEnfants()[1]);
				code += "set " + a.getEnfants()[0].getToken().getName() + "\n";				
				break;
		}
		return code;
	}
}
