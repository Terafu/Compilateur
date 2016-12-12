import java.util.Hashtable;
import java.util.Stack;

import javax.swing.JTree;

public class AnalyseurSyntaxique {

	String[] primaire = {"identifier", "cst_int", "(expression)"};
	
	Token[] tokens;
	
	int pos = 0; //current pos in the tokens table
	
	public AnalyseurSyntaxique (Token[] _tokens) {
		
		tokens = _tokens;
	}
	
	public Arbre next_X () {
		
		return next_instruction();
	}
	
	public Arbre next_instruction () {
		Arbre retour1 = new Arbre();
		
		switch (look().getClassname()) {
		
			case ("if"):
				retour1.setToken(next());
				if (look().getClassname().equals("(")) {
					next();
					retour1.addEnfants(next_expression());
					if(look().getClassname().equals(")")) {
						next();
						if(look().getClassname().equals("{")) {
							next();
							retour1.addEnfants(next_instruction());
							
							if(look().getClassname().equals("}")) {
								next();
								if(look().getClassname().equals("else")) {
									next();
									if(look().getClassname().equals("{")) {
										next();
										retour1.addEnfants(next_instruction());
										
										if(look().getClassname().equals("}")) {
											next();
										}
										
										else
											System.err.println("Error : syntax problem, '}' missing (else)");
									}
									
									else
										System.err.println("Error : syntax problem, '{' missing (else)");
								}
							}
							
							else
								System.err.println("Error : syntax problem, '}' missing (if)");
						}
						
						else
							System.err.println("Error : syntax problem, '{' missing (if)");
					}
					
					else
						System.err.println("Error : syntax problem, ')' missing (if)");
				}
				
				else
					System.err.println("Error : syntax problem, '(' missing (if)");
				return retour1;
			
			default:
				retour1 = next_expression();
				if (retour1 == null) {
					System.err.println("Error : syntax problem");
					pos++;
					return null;
				}
				
				else {
					// Affectation
					if (pos < tokens.length && look().getClassname().equals("=")) {
						Token op = next();
						Arbre retour2 = next_expression();
						Arbre [] at = {retour1, retour2};
						return new Arbre(op, at);
					}
				}
				
				return retour1;
		}
	}
	
	public Arbre next_affectation(){
		
		Arbre a1 = null;
		
		if(look().getClassname().equals("identifier")){
			
			Arbre id = new Arbre(next(),null);
			
			if(look().getClassname().equals("=")){
				
				Token op = next();
				Arbre a2 = next_addition();
				if (a2 == null ) {
					System.err.println("Expression missing for affectation");
					return null;
				}
				else {
					Arbre [] at = {id, a2};
					
					return new Arbre(op, at);
				}
			}
			else
				System.err.println("Problem '=' missing for affectation");
		}
		
		return a1;
	}
	
	//region grammaire des expressions
	public Arbre next_expression () {
		
		Arbre retour = null;
		
		retour = next_logique();
		if (retour == null) {
			System.out.println("Error : syntax problem");
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
		
		if (look().getClassname().equals("identifier")) {
			return new Arbre(next(), null);
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
