import java.util.Hashtable;
import java.util.Stack;

public class TableSymboles {
	
	private Stack<Hashtable<String, String>> symboleStack = new Stack<Hashtable<String, String>>();
	private int nb_var = 0;
	
	public TableSymboles () {
		push();
	}
	
	public void push(){
		
		symboleStack.push(new Hashtable<String, String>());
	}
	
	public Hashtable<String, String> pop(){
		return symboleStack.pop();
	}
	
	public int getNbVar() {
		
		return nb_var;
	}

	public void setNbVar(int i) {
		
		nb_var = i;
	}
	
	public int defineNewSymbole (Token _identifier) {
		
		if (_identifier.getClassname().equals("identifier")) {
			
			if (symboleStack.peek().containsKey(_identifier.getName())) {
				
				System.err.println("Error : the variable " + _identifier.getName() + " is already declared...");
			}
			
			else {
				
				System.out.println("Ajout d'un nouvel identifiant : " + _identifier.getName() + " a la position : " + nb_var);
				symboleStack.peek().put(_identifier.getName(), "" + nb_var);
				nb_var++;				
				return nb_var-1;
			}
		}
		
		else {
			System.err.println("Error : try to define a symbole who is not an identifier...");
		}
		
		return -1;
	}

	public int searchSymbole (Token _identifier) {
		
		int i = symboleStack.size()-1;
		
		while (i >= 0) {
			
			String x = symboleStack.get(i).get(_identifier.getName());
			
			if (x != null) {
				return Integer.parseInt(x);
			}
			
			i--;
		}
		
		System.err.println("Error : the variable " + _identifier.getName() + " is not declared...");
		return -1;
	}
	
	public Hashtable<String, String> getHastable (int level) {
		
		return symboleStack.get(level);
	}
	
	public Stack<Hashtable<String, String>> getStack () {
		
		return symboleStack;
	}
	
	public String toString () {
		
		String retour = "";
		
		for (int i = 0; i < symboleStack.size(); i++) {
			retour = retour + "Pile n°" + i + "\n";
			
			for (int j = 0; j < symboleStack.get(i).size(); j++) {
				retour = retour + "     identifier name : " +  symboleStack.get(i);
			}
		}
		
		return retour;
	}
}
