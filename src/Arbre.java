import java.util.ArrayList;

public class Arbre {

	private Arbre[] enfants;
	private Token tok;
	
	public Arbre[] getEnfants() {
		
		return enfants;
	}
	
	public void setEnfants(Arbre[] _enfants) {
		
		enfants = _enfants;
	}
	
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
	
	public Token getToken() {
		
		return tok;
	}
	
	public void setToken(Token _tok) {
		
		tok = _tok;
	}
	
	public Arbre(Token _tok, Arbre[] _enfants ) {
		
		tok = _tok;
		enfants = _enfants;
	}
	
	public Arbre() {
		
		tok = null;
		enfants = null;
	}
	
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
	
	public ArrayList<Token> orderToken() {
		
		ArrayList<Token> toks = new ArrayList<Token>();
		
		if(enfants == null){
			
			toks.add(tok);
			return toks;
		}
	
		toks.add(tok);
		
		for (int i = 0; i < enfants.length; i++){
			toks.addAll(enfants[i].orderToken());
		}
		
		return toks;
	}
	
//	public Arbre getChild(Token _enfant){
//		
//		if(enfant.isEmpty()){
//			return null;
//		}
//		
//		for(int i = 0; i < enfant.size(); i++){
//			if (enfant.get(i).tok.getClassname().equals(_enfant.getClassname()))
//				return enfant.get(i);
//		}
//		
//		return getChild(_enfant);
//	}
}
