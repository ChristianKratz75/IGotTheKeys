//package IGotTheKeys;
import java.awt.Color;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MyTable extends JTable{
	//private JTable table;
	private String[] columns;
	//private TableModelListener tableModeListener;
	private MySQLite sql;
	DefaultTableModel model;
	private String Tablequery;
	private int numberOfResults;

	public MyTable(){
		super();
	}
	public MyTable(MySQLite s, String[] Columns, String query) {
		super();
		sql = s;
		columns = Columns;
		setQuery(query);

		//table = Table;
		model = new DefaultTableModel(columns, 0);
		numberOfResults = 0;

		this.setModel(model);
		update();
	}

	public void initialise_MyTable(MySQLite s, String[] Columns, String query) {
		sql = s;
		columns = Columns;
		setQuery(query);

		//table = Table;
		model = new DefaultTableModel(columns, 0);
		numberOfResults = 0;

		this.setModel(model);
		update();
	}

	public void setQuery(String str) {
		this.Tablequery = str;
	}
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		  Component returnComp = super.prepareRenderer(renderer, row, column);
		  Color alternateColor = new Color(200,226,255);
		  Color whiteColor = Color.WHITE;
		  if (!returnComp.getBackground().equals(getSelectionBackground())){
		  Color bg = (row % 2 == 0 ?  whiteColor : alternateColor);
		  returnComp .setBackground(bg);
		  bg = null;
		}
		  return returnComp;
	}

	public void populateSQLTable() throws ClassNotFoundException, SQLException {
		if(model.getRowCount() > 0) {
			model.getDataVector().removeAllElements();
		model.fireTableDataChanged();

		revalidate();
		}
		
		ResultSet rs = sql.SQLquery(Tablequery);

		Object[] row = new Object[columns.length];
		while(rs.next()) {
			for (int i = 1; i <= columns.length; i++) {
				//System.out.print(rs.getObject(i)); DEBUG
				row[i-1] = rs.getObject(i);
			}
			model.insertRow(rs.getRow()-1, row);
		}

		numberOfResults = model.getRowCount();
//		for(int i = 0; i < model.getRowCount(); i=i+2) {
//		}
	}
	public void update() {
		try {populateSQLTable();} catch (Exception e) {e.printStackTrace();}
	}
	public int getNumberOfResults() {
		return numberOfResults;
	}
}



