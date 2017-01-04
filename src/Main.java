import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		// Préparation du fichier
		FileInputStream ips = null;
		
		String file = "";		

		// Ouverture du fichier
		try {
			ips = new FileInputStream(new File("program.txt"));
			
			InputStreamReader ipsr=new InputStreamReader(ips);
			
			BufferedReader br=new BufferedReader(ipsr);
			
			String ligne;
			while ((ligne=br.readLine())!=null){
				System.out.println(ligne);
				file+=ligne;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Analyseur lexical
		AnalyseurLexical lexicalAnalyseur = new AnalyseurLexical();
		
		// Création d'une table des symboles
		TableSymboles table = new TableSymboles ();		
		
		// Création d'une liste servant à stocker les tokens
		ArrayList<Token> tok = new ArrayList<Token>();
		
		// Ajout des tokens à cette liste
		Token a = lexicalAnalyseur.nextToken(file);
		
		while (a != null) {
			tok.add(a);
			// Affichage de test
			System.out.println(tok.get(tok.size() - 1));
			a = lexicalAnalyseur.nextToken(file);
		}
		
		// Création d'un tableau de tokens (plus facile à gérer qu'une arrayList)
		Token[] toks = new Token[tok.size()];
		
		for (int i = 0; i < tok.size(); i++) {
			toks[i] = tok.get(i);
		}		
		
		// Passage de l'analyseur syntaxique
		AnalyseurSyntaxique syntaxicAnalyseur = new AnalyseurSyntaxique (toks);
		Arbre arbre = syntaxicAnalyseur.next_X();
		
		// Affichages de test
		System.out.println("\n------------------------------------\n");
		System.out.println(arbre);		
		System.out.println("\n------------------------------------\n");
	}

}
