
public class Token {

	private String className;
	private String name;
	
	public String getClassname () {
		
		return className;
	}
	
	public String getName() {
		
		return name;
	}
	
	public Token (String _className) {
		
		className = _className;
	}

	public Token (String _className, String _name) {
		
		className = _className;
		name = _name;
	}
	
	public String toString () {
		
		return className + " : " + name;
	}
}
