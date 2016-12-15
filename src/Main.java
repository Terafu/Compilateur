import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream ips = null;
		
//		String test = "for(int i=1; i < 5; i = i + 1){i = i + 1}";
		
		String test = "";
		

		try {
			ips = new FileInputStream(new File("program.txt"));
			
			InputStreamReader ipsr=new InputStreamReader(ips);
			
			BufferedReader br=new BufferedReader(ipsr);
			
			String ligne;
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				test+=ligne+"\n";
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = test.replace("\n", "");
		
	
		AnalyseurLexical anal = new AnalyseurLexical();
		TableSymboles table = new TableSymboles ();
		
		
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
		Arbre arbrisseau = analProfond.next_X();
		System.out.println(arbrisseau);		
		System.out.println("\n------------------------------------\n");
		
		
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
