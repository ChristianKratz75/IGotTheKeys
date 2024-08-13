//package IGotTheKeys;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;




public class MySQLite {
	private Connection conn = null;
	private String url;
	private Path database;

	public MySQLite(String file) throws ClassNotFoundException
	{
		this.database = Paths.get(file);
        try {
            Class.forName("org.sqlite.JDBC");
            // db parameters
            url = "jdbc:sqlite:" + file;
            // create a connection to the database
            //JOptionPane.showMessageDialog(null, url);

            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established :\n" + url);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            	JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

	public ResultSet SQLquery(String query) throws SQLException, ClassNotFoundException {
		System.out.println(query);
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(url);
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(query);
		return rs;
	}
	public void sendData (String send) throws SQLException, ClassNotFoundException {
		System.out.println(send);
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection(url);
		Statement stat = conn.createStatement();
		stat.execute(send);
	}

	public String getString(ResultSet rs, String ColumnName) throws SQLException {
		String r = rs.getString(ColumnName);
		if(r == null) {
			r = "";
		}
		return r;
	}
	public String getSQLFileName() {
		return this.url;
	}
	public void backup_Database(Path backupfolder) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();

		SystemHandler s = new SystemHandler();
		String file = backupfolder.toString();
		file +=  s.getFolderSeparator() + sdf.format(date) + "_backup.sqlite";
		Path out = Paths.get(file);
		Files.copy(this.database, out , REPLACE_EXISTING);
	}
	
	public void SQLClose() throws SQLException {
		this.conn.close();
	}
}


