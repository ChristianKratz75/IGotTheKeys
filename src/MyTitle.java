//package IGotTheKeys;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyTitle {

	private MySQLite sql;
	private MyXML xml;
	private SystemHandler sysHandler;

	public int ID;
	public int Nummer;
	public String Titel;
	public String Orchester;
	public String OrchesterPfad;
	public String AktivPassiv;
	
	public MyTitle(MySQLite s, MyXML x, int ID) throws ClassNotFoundException, SQLException {
		this.sql = s;
		this.xml = x;
		ini_Titel(ID);
	}
	public MyTitle(MySQLite s, MyXML x, int Nummer, String Titel, String Orchester, String AktivPassiv) throws ClassNotFoundException, SQLException {
		this.sql = s;
		this.xml = x;
		this.Nummer = Nummer;
		this.sysHandler = new SystemHandler();
		this.Titel = sysHandler.pruefeAufSonderzeichen(Titel);
		this.Orchester = Orchester;
		set_OrchesterPfad();
		insert_NeuerTitel();
	}
	
	public String get_NummerFormatted() {
		return String.format("%04d", this.Nummer); 
	}
	
	public String get_TitelPfad() {
		return get_NummerFormatted() + " " + this.Titel;
	}
	
	private void ini_Titel(int ID) throws ClassNotFoundException, SQLException {
		this.ID = ID;
		String query = "SELECT Nummer, Titel, Orchester "
				+ "FROM tblTitel "
				+ "INNER JOIN tblOrchester ON tblTitel.OrchesterID = tblOrchester.ID "
				+ "Where tblTitel.ID = " + Integer.toString(ID);
		ResultSet rs = this.sql.SQLquery(query);
		while(rs.next()){
			this.Nummer = rs.getInt("Nummer");
			this.Titel = rs.getString("Titel");
			this.Orchester = rs.getString("Orchester");
		}
		set_OrchesterPfad();
	}
	
	public void insert_NeuerTitel() throws ClassNotFoundException, SQLException {
		String query = "SELECT ID "
				+ "FROM tblTitel "
				+ "WHERE Nummer = '" + String.valueOf(this.Nummer) + "' "
				+ "AND Titel = '" + Titel + "' "
				+ "AND OrchesterID  = (SELECT ID FROM tblOrchester WHERE Orchester = '" + Orchester + "')";
		ResultSet rs = this.sql.SQLquery(query);		
		
		if (!rs.next()) {
			query = "INSERT OR IGNORE INTO tblTitel (Nummer, Titel, OrchesterID, Aktiv) "
            		+ "VALUES ("
            		+ "'" + String.valueOf(this.Nummer) + "', "
            		+ "'" + Titel + "', "
            		+ "(SELECT ID FROM tblOrchester WHERE Orchester = '" + Orchester +"'), "
            		+ "'" + this.AktivPassiv + "')";
	
			this.sql.sendData(query);
			
			query = "SELECT ID "
					+ "FROM tblTitel "
					+ "WHERE Nummer = '" + String.valueOf(this.Nummer) + "' "
					+ "AND Titel = '" + Titel + "' "
					+ "AND OrchesterID  = (SELECT ID FROM tblOrchester WHERE Orchester = '" + Orchester + "')";
			ResultSet set = this.sql.SQLquery(query);
			while(set.next()) {
				this.ID = set.getInt("ID");
			}
			pruefeVerzeichnis();
		} else {
		    //TODO Anzeige existiert schon
		}
		

	}
	
	public void insert_Stimme(String Stimme) throws ClassNotFoundException, SQLException {
		String query = "INSERT OR IGNORE INTO tblNoten (TitelID, StimmeID) "
				+ "VALUES ('" + String.valueOf(this.ID) + "', "
				+ "(SELECT ID FROM tblStimme WHERE Stimme = '" + Stimme + "'))";

		this.sql.sendData(query);
	}
	
	
	private void pruefeVerzeichnis() {
		Path p = Paths.get(this.OrchesterPfad + sysHandler.getFolderSeparator() + this.get_TitelPfad());
        if(!Files.exists(p)) {
        	File a = p.toFile();
        	a.mkdir();
        }
	}
	
	private void set_OrchesterPfad() {
		for(int i = 0; i < xml.Vereine.size(); i++) {
			if(xml.Vereine.get(i).getVereinsName().equals(this.Orchester)) {
				this.OrchesterPfad = xml.Vereine.get(i).getPfad();
			}
		}
	}
	
}
