//package IGotTheKeys;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

public class MyComboBox extends JComboBox {
	String query;
	MySQLite sql;
	public MyComboBox(MySQLite SQL, String Query) throws ClassNotFoundException, SQLException {
		//super(SQL.query(Query));
		//super();
		sql = SQL;
		query = Query;
		populate();
	}
	public void populate() throws ClassNotFoundException, SQLException {
		ResultSet rs = sql.SQLquery(query);
		this.addItem(new MyComboItem("", Integer.toString(1)));
		int i = 2;
		while(rs.next()) {
			this.addItem(new MyComboItem(rs.getString(1).toString(), Integer.toString(i)));
			i++;
		}

	}
}


