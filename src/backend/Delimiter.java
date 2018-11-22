package backend;

public enum Delimiter {

	NEW_LINE("\n"), PERIOD("\\."), SEMI_COLON(";"), COLON(":"), SPACE(" "),
	QUESTION_MARK("?"), EXCLAMATION_POINT("!"), COMMA(",");
	
	public String delim;
	
	Delimiter(String delim) {
		this.delim = delim;
	}
	
	
}
