//package IGotTheKeys;

public class SystemHandler {
	String Seperator;

	public SystemHandler(){
		if (System.getProperty("os.name").equals("Linux")){
			this.Seperator = "/";
		}
		else //(System.getProperty("os.name").equals("Windows")){
			this.Seperator = "\\";
		//}
	
	}
	public String getFolderSeparator() {
		return this.Seperator;
	}
	public String pruefeAufSonderzeichen(String t) {
		t.replace("", "\"");
		t.replace("\\", "");
		t.replace("/", "");
		t.replace("ü", "ue");
		t.replace("ä", "ae");
		t.replace("ö", "oe");
		t.replace("'", " ");
		return t;
	}
	public String getHomePath() {
		return System.getProperty("user.home") + this.getFolderSeparator() + "Downloads";
	}
	
}
