import java.util.Hashtable;
import java.util.Stack;

import javax.swing.JTree;

public class AnalyseurSyntaxique {

	private String[] primaire = {"identifier", "cst_int", "(expression)"};
	private TableSymboles symboles;
	private GenerationCode generation;
	private Stack<Hashtable<String, String>> symboleStack = new Stack<Hashtable<String, String>>();
	
	private Token[] tokens;
	
	private int pos = 0; //current pos in the tokens table
	
	public AnalyseurSyntaxique (Token[] _tokens) {
		
		tokens = _tokens;
		symboles = new TableSymboles();
		generation = new GenerationCode();
	}
	
	public Arbre next_X () {
		
		Arbre retour = new Arbre();
		retour.setToken(new Token("Main"));
		
		Arbre test = next_instruction();
		while (test != null) {
			
			retour.addEnfants(test);
			test = next_instruction();
		}
		
		generation.genererCode(retour, symboleStack);
		return retour;
		
	}
	
	public Arbre next_instruction () {
		Arbre retour1 = new Arbre();
		
		if (look() == null) {
			return null;
		}		

		int position_var1;
		int position_var2;
		
		switch (look().getClassname()) { //look peut être null 
		
			case ("if"):
				retour1.setToken(next());
				if (look().getClassname().equals("(")) {
					next();
					retour1.addEnfants(next_expression());
					if(look().getClassname().equals(")")) {
						next();
						retour1.addEnfants(next_instruction());
						
						if(look() != null && look().getClassname().equals("else")) {
							Token op = next();
							Arbre retour2 = next_instruction();
							if (retour2 == null ) {
								System.err.println("retour2 null pour l'instruction (else)");
								return null;
							}
							
							else {
								Arbre [] at = {retour2};
								retour2 = new Arbre(op, at);
							}
							
							retour1.addEnfants(retour2);
						}
						
						else
							return retour1;
					}
					
					else
						System.err.println("Error : syntax problem, ')' missing (if)");
				}
				
				else
					System.err.println("Error : syntax problem, '(' missing (if)");
				return retour1;
				
			case ("for"):
				retour1.setToken(next());
				if (look().getClassname().equals("(")) {
					next();
					retour1.addEnfants(next_instruction());
					if(look().getClassname().equals(";")) {
						next();
						retour1.addEnfants(next_expression());
						if(look().getClassname().equals(";")) {
							next();
							retour1.addEnfants(next_affectation());
							
							if(look().getClassname().equals(")")) {
								next();
								retour1.addEnfants(next_instruction());
							}
							
							else
								System.err.println("Error : syntax problem, ')' missing (for)");
						}
						
						else
							System.err.println("Error : syntax problem, ';' missing (for 1)");
					}
					
					else
						System.err.println("Error : syntax problem, ';' missing (for 2)");
				}
				
				else
					System.err.println("Error : syntax problem, '(' missing (for)");
				return retour1;
			
			case ("while"):
				retour1.setToken(next());
				if (look().getClassname().equals("(")) {
					next();
					retour1.addEnfants(next_expression());
					if(look().getClassname().equals(")")) {
						next();
						retour1.addEnfants(next_instruction());
					}
					
					else
						System.err.println("Error : syntax problem, ')' missing (while)");
				}
				
				else
					System.err.println("Error : syntax problem, '(' missing (while)");
				return retour1;
				
			case ("{"):
				symboles.push();
				retour1.setToken(next());

				Arbre nextInstruct = next_instruction();
				while (nextInstruct != null) {

					retour1.addEnfants(nextInstruct);
					nextInstruct = next_instruction();
				}
				
				return retour1;
				
			case ("}"):
				next();
				symboleStack.push(symboles.pop());
				symboles.setNbVar(symboles.getNbVar() - (symboleStack.lastElement().size())); 
				return null;
				
			case ("out"):
				Arbre a = new Arbre (new Token("out"), null);
				next(); 	
				int position_var = symboles.searchSymbole(look());
			
				a.addEnfants(new Arbre(new Token("identifier", "" + position_var), null));
				next();
				return a;
				
			case "int": 
				next();
				if(look().getClassname().equals("identifier")){
					position_var1 = symboles.defineNewSymbole(look());
					if (position_var1 != -1) {					
						
						Arbre a2 = new Arbre(new Token("identifier", "" + position_var1), null);
						a2.addEnfants(new Arbre(new Token("int"), null));
						a2.addEnfants(new Arbre(new Token(look().getName()), null));
						
						// =
						next();
						if (look().getClassname().equals("=")) {

							retour1 = new Arbre(look(), null);
						}
						
						else {
							
							System.err.println("Declaration without affectation");
						}
						
						retour1.addEnfants(a2);
						
						// Valeur
						next();
						if (look().getClassname().equals("identifier")) {
							
							position_var2 = symboles.searchSymbole(look());
							Arbre a3 = new Arbre(new Token("identifier", "" + position_var2), null);
							a3.addEnfants(new Arbre(new Token("int"), null));
							a3.addEnfants(new Arbre(new Token(look().getName()), null));
							
							retour1.addEnfants(a3);
						}
						
						else {
							
							retour1.addEnfants(new Arbre (look(), null));							
						}
						next();
					}
				}
				return retour1;
				
			case "var":
				next();
				if(look().getClassname().equals("identifier")){
					position_var1 = symboles.defineNewSymbole(look());
					if (position_var1 != -1) {					
						
						Arbre a2 = new Arbre(new Token("identifier", "" + position_var1), null);
						a2.addEnfants(new Arbre(new Token("var"), null));
						a2.addEnfants(new Arbre(new Token(look().getName()), null));
						
						// = 
						if (next().getClassname().equals("=")) {

							retour1 = new Arbre(next(), null);
						}
						
						else {
							
							System.err.println("Declaration without affectation");
						}
						
						retour1.addEnfants(a2);
						
						// Valeur
						if (next().getClassname().equals("identifier")) {
							
							position_var2 = symboles.searchSymbole(look());
							Arbre a3 = new Arbre(new Token("identifier", "" + position_var2), null);
							a3.addEnfants(new Arbre(new Token("int"), null));
							a3.addEnfants(new Arbre(new Token(look().getName()), null));
							
							retour1.addEnfants(a3);
						}
						
						else {
							retour1.addEnfants(new Arbre (next(), null));							
						}
						next();
					}
				}
				return retour1;
				
			case "identifier":
				retour1 = next_affectation();
				return retour1;
			
			case ";":
				next();
				return next_instruction();
				
			default:
				System.err.println("Error : syntax problem");
				return null;
		}
	}
	
	public Arbre next_affectation(){
		
		Arbre a1 = null;
		int position_var1;
		int position_var2;
		
		if(look().getClassname().equals("identifier")){
			position_var1 = symboles.searchSymbole(look());
			if (position_var1 != -1) {					
				
				Arbre a2 = new Arbre(new Token("identifier", "" + position_var1), null);
				a2.addEnfants(new Arbre(new Token("int"), null));
				a2.addEnfants(new Arbre(new Token(look().getName()), null));
				
				// = 
				next();
				if (look().getClassname().equals("=")) {

					a1 = new Arbre(look(), null);
				}
				
				else {
					
					System.err.println("Declaration without affectation");
				}
				
				a1.addEnfants(a2);
				next();
				a1.addEnfants(next_expression());
			}
		}
		return a1;
	}
	
	//region grammaire des expressions
	public Arbre next_expression () {
		
		Arbre retour = null;
		
		retour = next_logique();
		if (retour == null) {
			System.out.println("Error : syntax problem3");
			pos++;
			return null;
		}
		
		return retour;		
	}
	
	public Arbre next_logique(){
		Arbre a1 = next_compare();
		
		if (a1 == null) {
			return null;
		}
		
		else if (pos >= tokens.length) {
			return a1;
		}
		
		else if(look().getClassname().equals("&&") || look().getClassname().equals("||") ){
			Token op = next();
			Arbre a2 = next_addition();
			if (a2 == null ) {
				System.err.println("A2 null pour la logique");
				return null;
			}
			
			else {
				Arbre [] at = {a1, a2};
				
				return new Arbre(op, at);
			}
		}
		
		return a1;
	}
	
	public Arbre next_compare(){
		Arbre a1 = next_addition();
		
		if (a1 == null) {
			return null;
		}
		
		else if (pos >= tokens.length) {
			return a1;
		}
		
		else if(look().getClassname().equals("==") || look().getClassname().equals("!=") || look().getClassname().equals("<")
				|| look().getClassname().equals(">") || look().getClassname().equals("<=") || look().getClassname().equals(">=")){
			Token op = next();
			Arbre a2 = next_addition();
			if (a2 == null ) {
				System.err.println("A2 null pour les comparaisons");
				return null;
			}
			
			else {
				Arbre [] at = {a1, a2};
				
				return new Arbre(op, at);
			}
		}
		
		return a1;
	}
	
	public Arbre next_addition() {
		
		Arbre a1 = next_multiplication();
		
		if (a1 == null) {
			return null;
		}
		
		else if (pos >= tokens.length) {
			return a1;
		}
		
		else if (look().getClassname().equals("+") || look().getClassname().equals("-")) {
			Token op = next();
			Arbre a2 = next_addition();
			if (a2 == null ) {
				System.err.println("A2 null pour l'addition");
				return null;
			}
			
			else {
				Arbre [] at = {a1, a2};
				
				return new Arbre(op, at);
			}
		}
		
		return a1;
	}
	
	public Arbre next_multiplication() {
		
		Arbre a1 = next_primaire();
		
		if (a1 == null) {
			return null;
		}
		
		else if (pos >= tokens.length) {
			return a1;
		}
		
		else if (look().getClassname().equals("*") || look().getClassname().equals("/") || look().getClassname().equals("%")) {
			Token op = next();
			Arbre a2 = next_multiplication();
			if (a2 == null ) {
				System.err.println("A2 null pour la multiplication");
				return null;
			}
			
			else {
				Arbre [] at = {a1, a2};
				
				return new Arbre(op, at);
			}
		}
		
		return a1;
	}

	public Arbre next_primaire() {
		
		if (look() == null) {
			return null;
		}
		
		// identifier
		if (look().getClassname().equals("identifier")) {
			
			int position_var = symboles.searchSymbole(look());
			Arbre arbre = new Arbre(new Token("identifier", "" + position_var), null);
			arbre.addEnfants(new Arbre(new Token("int"), null));
			arbre.addEnfants(new Arbre(new Token(look().getName()), null));
			next();
			return arbre;
		}
		
		// Moins unaire
		else if (look().getClassname().equals("-")) {
			
			Token op = next();
			Arbre res = next_primaire();
			if (res == null ) {
				System.err.println("Erreur de négation");
				return null;
			}
			
			else {
				Arbre [] at = {res};
				
				return new Arbre(op, at);
			}
		}
		
		else if (look().getClassname().equals("cst_int")) {
			return new Arbre(next(), null);
		}
		
		else if (look().getClassname().equals("(")) {
			next();
			Arbre res = next_expression();
			if (res == null || !next().getClassname().equals(")")) {
				System.err.println("A2 null pour les primaires");
			}
			
			else {
				return res;
			}
		}
		
		return null;
	}
	//endregion
	
	//Region grammaire des structures
	
	//endregion

	public boolean accept (String type) {
		if (tokens[pos].getClassname().equals(type)) {
			pos++;
			return true;
		}
		
		return false;
	}
	
	public Token look() {
		
		if (pos < tokens.length) {

			return tokens[pos];
		}
		
		else {
			return null;
		}
	}
	
	public Token next() {
		
		return tokens[pos++];
	}
}


//
//public boolean next_addition () {
//	
//	int save = pos;
//	
//	if (next_multiplication() && accept("+") && next_addition()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	if (next_multiplication() && accept("-") && next_addition()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	if (next_multiplication()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	return false;
//}
//
//public boolean next_multiplication () {
//	
//	int save = pos;
//	
//	if (next_primaire() && accept("*") && next_multiplication()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	if (next_primaire() && accept("/") && next_multiplication()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	if (next_primaire()) {
//		return true;
//	}
//	
//	else {
//		pos = save;
//	}
//	
//	return false;
//}
//
//public boolean next_primaire() {
//	
//	for (int i = 0; i < primaire.length; i++) {
//		if (accept(primaire[i])) {
//			return true;
//		}
//	}
//	
//	return false;		
//}
