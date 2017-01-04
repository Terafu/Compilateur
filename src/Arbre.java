import java.util.ArrayList;

public class Arbre {

	private Arbre[] enfants;
	private Token tok;
	
	// Constructeurs
	public Arbre(Token _tok, Arbre[] _enfants ) {
		
		tok = _tok;
		enfants = _enfants;
	}
	
	public Arbre() {
		
		tok = null;
		enfants = null;
	}
	
	// Accesseurs
	
	public Arbre[] getEnfants() {
		
		return enfants;
	}
	
	public Token getToken() {
		
		return tok;
	}
	
	public void setToken(Token _tok) {
		
		tok = _tok;
	}
	
	// Ajoute une tableau d'arbres en tant qu'enfants
	public void setEnfants(Arbre[] _enfants) {
		
		enfants = _enfants;
	}
	
	// Ajoute une enfant à l'arbre
	public void addEnfants(Arbre _enfant) {
		
		Arbre[] newEnfants;
		
		if (this.enfants == null) {
			newEnfants = new Arbre[1];
			newEnfants[0] = _enfant;
		}
		
		else {
			newEnfants = new Arbre[this.enfants.length+1];
			
			for (int i = 0; i < this.enfants.length; i ++) {
				newEnfants[i] = this.enfants[i];
			}
			
			newEnfants[this.enfants.length] = _enfant;			
		}
		
		
		
		this.setEnfants(newEnfants);
	}
	
	// Permet un affichage de l'arbre
	public String toString() {
		
		String retour = "";
		
		if(enfants == null){
			if (tok.getName()!= "") {

				return tok.getClassname() + ":" + tok.getName();
			}
			
			else {
				return tok.getClassname();
			}
		}
		
		for (int i = 0; i < enfants.length; i++){
			
			retour += enfants[i].toString();
		}
		
		return tok.getClassname() + retour;
		
	}
}
