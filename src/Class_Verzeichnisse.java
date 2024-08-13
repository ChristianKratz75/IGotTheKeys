//package IGotTheKeys;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Class_Verzeichnisse {
	private String Pfad, Name, Parameter, Datenbank, Pfad_Backup, PDFQuelle;
	private SystemHandler sysHandler;

	public Class_Verzeichnisse(String Name, String Pfad) {
		// für Verzeichnisse
		this.sysHandler = new SystemHandler();
		this.Name = Name;
		this.Pfad = Pfad;
	}
	public Class_Verzeichnisse(String Name, String Pfad, String dB, String Pfad_Backup){
		// für Vereine
		this.sysHandler = new SystemHandler();
		this.Name = Name;
		this.Pfad = Pfad;
		this.Datenbank = dB;
		this.Pfad_Backup = Pfad_Backup;
		this.PDFQuelle = PDFQuelle;
		this.Parameter = "";
	}
	public Class_Verzeichnisse(String Name, String Command, String Parameter){
		// für Commands
		this.sysHandler = new SystemHandler();
		this.Name = Name;
		this.Pfad = Command;
		this.Parameter = Parameter;
	}

	public void setPath(String pfad) {
		this.Pfad = pfad;
	}
	
	public String getVereinsName() {
		return Name;
	}
	
	public String getParameter() {
		return this.Parameter;
	}
	
	public String getPfad() {
		return this.Pfad;
	}
	public String getDatenBank() {
		System.out.println("Datenbank: " + this.Datenbank);
		return this.Datenbank;
	}
	public String getPfad_Backup() {
		System.out.println("Pfad_Backup: " + this.Pfad_Backup);
		return this.Pfad_Backup;
	}
	public String getPDFQuelle() {
		System.out.println("PDFQuelle: " + this.PDFQuelle);
		return this.PDFQuelle;
	}
}
