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
	
	public void pop(){
		symboleStack.pop();
	}
	
	public void defineNewSymbole (Token _identifier, Token _value) {
		
		if (_identifier.getClassname().equals("identifier")) {
			
			if (symboleStack.peek().containsKey(_identifier.getName())) {
				
				System.err.println("Error : the variable " + _identifier.getName() + " is already declared...");
			}
			
			else {
				
				System.out.println("Ajout d'un nouvel identifiant : " + _identifier.getName() + " ayant pour valeur : " + _value.getName());
				symboleStack.peek().put(_identifier.getName(), _value.getName());
			}
		}
		
		else {
			System.err.println("Error : try to define a symbole who is not an identifier...");
		}		
	}

	public void searchSymbole (Token _identifier, Token _value) {
		
		int i = symboleStack.size()-1;
		
		while (i >= 0) {
			
			if (symboleStack.get(i).containsKey(_identifier.getName())) {
				
				System.out.println("Remplacement de la valeur de l'identifiant : " + _identifier.getName() + " par : " + _value.getName() + " (ancienne valeur : " + symboleStack.get(i).get(_identifier.getName()));
				
				symboleStack.get(i).replace(_identifier.getName(), _value.getName());
				return;
			}
			
			else {
				i--;
			}
		}
		
		System.err.println("Error : the variable " + _identifier.getName() + " is not declared...");
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
