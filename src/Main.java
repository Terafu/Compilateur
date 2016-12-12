import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AnalyseurLexical anal = new AnalyseurLexical();
		TableSymboles table = new TableSymboles ();
		
		String test = "5 == 8";
		ArrayList<Token> tok = new ArrayList<Token>();
		
		Token a = anal.nextToken(test);
		
		while (a != null) {
			tok.add(a);
			System.out.println(tok.get(tok.size() - 1));
			a = anal.nextToken(test);
		}
		
		Token[] toks = new Token[tok.size()];
		
		for (int i = 0; i < tok.size(); i++) {
			toks[i] = tok.get(i);
		}
		
		AnalyseurSyntaxique analProfond = new AnalyseurSyntaxique (toks);
		System.out.println("\n------------------------------------\n");
		Arbre arbrisseau = analProfond.next_expression();
		System.out.println(arbrisseau);
		
		GenerationCode generation95 = new GenerationCode();
		System.out.println("\n------------------------------------\n");
		generation95.genererCode(arbrisseau);
		
		
		/*Token var1 = new Token("identifier", "a");
		Token var2 = new Token("int", "5");
		Token var3 = new Token("int", "9");
		Token var4 = new Token("identifier", "b");
		
		table.defineNewSymbole(var1, var2);
		table.searchSymbole(var1, var3);
		table.searchSymbole(var4, var3);
		table.defineNewSymbole(var1, var2);*/
	}

}
