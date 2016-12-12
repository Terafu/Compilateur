import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyseurLexical {

	private String path;
	private FileInputStream file;
	private byte[] buf = new byte [8];
	private int pos;
	private String[] keyWords = {"if", "for", "while", "var", "int", "else"};
	private String[] operators = {"+", "-", "*", "/", "%", "(", ")", "==", "!=", "<", ">", "<=", ">=", "=", ";", "{", "}", "&&", "||"};
	
	public AnalyseurLexical() {
		
	}
	
	public AnalyseurLexical (String _path) {
		
		path = _path;
		
		try {
			file = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pos = 0;
	}
	
	public Token nextToken(String test) {

		if (test.length() > pos) {
			while (test.charAt(pos) == ' ') {
				pos++;
			}
			
			if (isAlphaNumeric(test.charAt(pos))) {
				String tokClass = "";
				
				//pos++;
				while (pos < test.length() && isAlphaNumeric(test.charAt(pos)) && test.charAt(pos) != ' ') {
					tokClass = tokClass.concat("" + test.charAt(pos));
					pos++;
				}
				
				if (!tokClass.equalsIgnoreCase("")) {
					if (isNumeric(tokClass)){
		
						return new Token("cst_int", tokClass);
					}	
					
					for (int i = 0; i < keyWords.length; i++) {
						
						if (tokClass.equalsIgnoreCase(keyWords[i])) {
							return new Token(keyWords[i], "");
						}
					}
					
					return new Token("identifier", tokClass);
				}
			}
			
			else {
				if (pos+1 < test.length()) {				
	
					String tokClass = "" + test.charAt(pos) + test.charAt(pos+1);
					
					for (int i = 0; i < operators.length; i++) {
						
						if (tokClass.equalsIgnoreCase(operators[i])) {
							pos += 2;
							return new Token(operators[i], "");
						}
					}
				}
				
				for (int i = 0; i < operators.length; i++) {
				
					if (("" + test.charAt(pos)).equalsIgnoreCase(operators[i])) {
						pos++;
						return new Token(operators[i], "");
					}
				}
			}	
		}
		
		return null;		
	}
	
	public Token lookNextToken(String test) {
		
		int position = pos;
		
		if (isAlphaNumeric(test.charAt(position))) {
			String tokClass = "" + test.charAt(position);
			
			position++;
			if (position+1 < test.length()) {
				while (isAlphaNumeric(test.charAt(position)) && test.charAt(position) != ' ') {
					tokClass = tokClass.concat("" + test.charAt(position));
					position++;
				}
			}
			
			if (isNumeric(tokClass)){

				return new Token("cst_int", tokClass);
			}	
			
			for (int i = 0; i < keyWords.length; i++) {
				
				if (tokClass.equalsIgnoreCase(keyWords[i])) {
					return new Token(keyWords[i], "");
				}
			}
			
			return new Token("identifier", tokClass);
		}
		
		else {
			if (position+1 < test.length()) {				

				String tokClass = "" + test.charAt(position) + test.charAt(position+1);
				
				for (int i = 0; i < operators.length; i++) {
					
					if (tokClass.equalsIgnoreCase(operators[i])) {
						position += 2;
						return new Token(operators[i], "");
					}
				}
			}
			
			for (int i = 0; i < operators.length; i++) {
			
				if (("" + test.charAt(position)).equalsIgnoreCase(operators[i])) {
					position++;
					return new Token(operators[i], "");
				}
			}
		}	
		
		return null;
	}
	
	private Boolean isAlphaNumeric(char _toTest) {
		
		Pattern p = Pattern.compile("[a-zA-Z_0-9]");
		Matcher m = p.matcher("" + _toTest);
		
		if (m.find()) {
			return true;
		}
		
		return false;
		
	}
	
private Boolean isNumeric(String _toTest) {
		
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(_toTest);
		
		if (m.find()) {
			return true;
		}
		
		return false;
		
	}
}
