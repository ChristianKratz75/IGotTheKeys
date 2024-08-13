//package IGotTheKeys;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyGhostscript {
	private String Programm, Parameter;
	
	public MyGhostscript(MyXML xml) {
		ini_Ghostscript(xml);
	}
	
	private void ini_Ghostscript(MyXML xml) {
		for(int i = 0; i < xml.Programme.size(); i++) {
			if(xml.Programme.get(i).getVereinsName().equals("Ghostscript")) {
				this.Parameter = xml.Programme.get(i).getParameter();
				this.Programm =  xml.Programme.get(i).getPfad();
				break;
			}
		}
	}
	
	public void extractPages(String Quelle, String Ziel, String von, String bis) throws IOException, InterruptedException {
		String cmd = "\"" + this.Programm + "\" " + this.Parameter;
		cmd += "-dFirstPage=";
		cmd += "\"" + von + "\" ";
		cmd += "-dLastPage=";
		cmd += "\"" + bis + "\" ";
		cmd +="-sOutputFile=";
		cmd += "\"" + Ziel + "\" \"" + Quelle + "\"";
		System.out.println(cmd);
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		}catch(IOException e) {
			System.out.println("Exit Value: " + String.valueOf(p.exitValue()));
			System.out.println(e.getMessage());
		}
		// wenn Linux, dann "Runtime.getRuntime().exec(cmd);"
	}
	
	public void combinePDFs(List<File> Quellen, String Ziel) throws IOException, InterruptedException
	{
		String files = "";
	    for(int i = 0; i < Quellen.size(); i++)
	    {
	        files += "\"" + Quellen.get(i).getAbsolutePath() + "\" ";
	    }

	    String s = this.Programm + " " + this.Parameter;
	    s += "-sOutputFile=";
	    s += "\"" + Ziel + "\" " + files;
	    System.out.println("Ghostscript: " + s);
	    Process p = null;
	    try {
			p = Runtime.getRuntime().exec(s);
			p.waitFor();
		}catch(IOException e) {
			System.out.println("Exit Value: " + String.valueOf(p.exitValue()));
			System.out.println(e.getMessage());
		}
	}
}
