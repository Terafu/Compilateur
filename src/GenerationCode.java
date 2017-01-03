import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Stack;

public class GenerationCode {

	private int label_counter = 0;
	private Stack<Hashtable<String, String>> symboles;
	int niveau_bloc = 0;
	private String codef = "";
	
	public GenerationCode(){
	}
	
	public void genererCode(Arbre a, Stack<Hashtable<String, String>> _symboles){
		
		symboles = _symboles;
		
		try (PrintWriter out = new PrintWriter( "test.txt" )){
			
			out.println(".start");			

			if (a.getEnfants()[0].getToken().getClassname().equals("{")) {

				out.print(interpretToken(a.getEnfants()[0]));
			}
			
			else {
				
				out.print(interpretBlock(a, niveau_bloc, true));
			}
			
			out.println("halt");
			out.println(codef);
		}
		
		catch (FileNotFoundException e) {
	         e.printStackTrace();

	    }
	}
	
	public String interpretBlock(Arbre a, int niveau, boolean doDrop) {		
		
		String code = "";
		//boolean doDrop = true;
		
		for(int i = 0; i < symboles.get(niveau).size(); i ++) {
			code += "push.i 0\n";
		}
		
		for (int i = 0; i < a.getEnfants().length; i ++) {	
			
			code += interpretToken(a.getEnfants()[i]);
		}
		
		if (doDrop) {
			
			for(int i = 0; i < symboles.get(niveau).size(); i ++) {
				code += "drop\n";
			}
		}
		
		return code;
	}
	
	public String interpretToken(Arbre a) {
		
		String code = "";
		
		switch (a.getToken().getClassname()) {
		
			case ("function"):
				codef += "." + a.getEnfants()[0].getToken().getName() + "\n";
				codef += interpretBlock (a.getEnfants()[a.getEnfants().length - 1], niveau_bloc, false);
				break;
				
			case ("call_function"):
				code += "prep " + a.getToken().getName() + "\n";
				for (int i = 0; i < a.getEnfants().length; i ++) {
					code += interpretToken(a.getEnfants()[i]);
				}
				code += "call " + a.getEnfants().length + "\n";
				break;
				
			case ("return"):
				code += interpretToken(a.getEnfants()[0]);
				code += "ret\n";
				break;
		
			case ("{"):
				code += interpretBlock(a, niveau_bloc, true);
				break;
				
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
				code += interpretBlock(a.getEnfants()[1], niveau_bloc, true);
				if (a.getEnfants().length > 2) {
					code += "jump fin_else" + if_counter + "\n";
					niveau_bloc++;
					code += ".else" + if_counter + "\n" + interpretBlock(a.getEnfants()[2].getEnfants()[0], niveau_bloc, true);
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
				code += interpretBlock(a.getEnfants()[1], niveau_bloc, true);
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
				code += interpretBlock(a.getEnfants()[3], niveau_bloc, true);
				code += "jump for" + for_counter + "\n";
				code += ".fin_for" + for_counter + "\n";
				break;
			case ("="):
				code += interpretToken(a.getEnfants()[1]);
				code += "set " + a.getEnfants()[0].getToken().getName() + "\n";				
				break;
			case ("out"):
				code += interpretToken(a.getEnfants()[0]);
				code += "out.i \n";				
			default:
				break;
		}
		return code;
	}
}
