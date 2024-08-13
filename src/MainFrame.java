//package IGotTheKeys;

import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class MainFrame implements KeyListener, ItemListener, MouseListener{

	private JFrame frame;
	private MySQLite sql;
	private MyXML xml;
	private SystemHandler sysHandler;
	private JScrollPane scrollPane;
	private MyTable table_AlleNoten;
	//private MyComboBox cb_Orchester;
	private JComboBox cb_Orchester;
	private JTextField txt_Nummer;
	private JTextField txt_Titel;
	private JPanel panel_1;
	private JButton btNeuerTitel;
	private JButton btStimmenOrdner;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public MainFrame() throws ClassNotFoundException, SQLException {
		this.sysHandler = new SystemHandler();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	private void initialize() throws ClassNotFoundException, SQLException {
		frame = new JFrame();
		frame.setBounds(100, 100, 802, 567);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		TransferHandler th = new TransferHandler() {
			//@Override
			public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
				return true;
			}
			
			public boolean importData(JComponent comp, Transferable t) {
				try {
					List <File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
					for(File file : files) {
						System.out.print(file.getName());
					}
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		};
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{54, 446, 176, 0};
		gridBagLayout.rowHeights = new int[]{14, 38, 134, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		this.xml = new MyXML("IGotTheKeys.xml");
		
		ini_cb_Orchester();
		ini_SQL();
		{
			txt_Nummer = new JTextField();
			GridBagConstraints gbc_txt_Nummer = new GridBagConstraints();
			gbc_txt_Nummer.insets = new Insets(0, 0, 5, 5);
			gbc_txt_Nummer.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_Nummer.gridx = 0;
			gbc_txt_Nummer.gridy = 1;
			frame.getContentPane().add(txt_Nummer, gbc_txt_Nummer);
			txt_Nummer.setColumns(10);
			txt_Nummer.addKeyListener(this);
		}

		{
			txt_Titel = new JTextField();
			GridBagConstraints gbc_txt_Titel = new GridBagConstraints();
			gbc_txt_Titel.insets = new Insets(0, 0, 5, 5);
			gbc_txt_Titel.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_Titel.gridx = 1;
			gbc_txt_Titel.gridy = 1;
			frame.getContentPane().add(txt_Titel, gbc_txt_Titel);
			txt_Titel.setColumns(10);
			txt_Titel.addKeyListener(this);
		}
		
		{
			
			String[] Columns = {"", "Nummer", "Titel"};
			
			String query = getQuery();
			
			scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridwidth = 3;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 2;
			frame.getContentPane().add(scrollPane, gbc_scrollPane);
			
			table_AlleNoten = new MyTable(this.sql, Columns, query);
			table_AlleNoten.removeColumn(table_AlleNoten.getColumnModel().getColumn(0));
			table_AlleNoten.addMouseListener(this);
			scrollPane.setViewportView(table_AlleNoten);
		}
		{
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 0;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
			{
				btNeuerTitel = new JButton("Neuer Titel");
				GridBagConstraints gbc_btNeuerTitel = new GridBagConstraints();
				gbc_btNeuerTitel.fill = GridBagConstraints.HORIZONTAL;
				gbc_btNeuerTitel.insets = new Insets(0, 0, 5, 0);
				gbc_btNeuerTitel.gridx = 0;
				gbc_btNeuerTitel.gridy = 0;
				panel_1.add(btNeuerTitel, gbc_btNeuerTitel);
				{
					btStimmenOrdner = new JButton("Stimmen Ordner");
					btStimmenOrdner.addMouseListener(this);
					GridBagConstraints gbc_btStimmenOrdner = new GridBagConstraints();
					gbc_btStimmenOrdner.fill = GridBagConstraints.HORIZONTAL;
					gbc_btStimmenOrdner.gridx = 0;
					gbc_btStimmenOrdner.gridy = 1;
					panel_1.add(btStimmenOrdner, gbc_btStimmenOrdner);
				}
				btNeuerTitel.addMouseListener(this);
			}
		}
		
	}
	
	private void ini_cb_Orchester() throws ClassNotFoundException, SQLException {
		cb_Orchester = new JComboBox();
		if(xml.Vereine.isEmpty()) {
			Message x = new Message("Fehler", "Kein Verein in XML angelegt !");
		}
		for(int x = 0; x < xml.Vereine.size(); x++) {
			cb_Orchester.addItem(new MyComboItem(xml.Vereine.get(x).getVereinsName(), Integer.toString(x)));
		}

		GridBagConstraints gbc_cb_Orchester = new GridBagConstraints();
		gbc_cb_Orchester.insets = new Insets(0, 0, 5, 0);
		gbc_cb_Orchester.fill = GridBagConstraints.HORIZONTAL;
		gbc_cb_Orchester.gridx = 2;
		gbc_cb_Orchester.gridy = 1;
		frame.getContentPane().add(cb_Orchester, gbc_cb_Orchester);
		this.cb_Orchester.addItemListener(this);
	}
	
	private void ini_SQL() throws ClassNotFoundException {
		String dBName = this.cb_Orchester.getSelectedItem().toString();
		String pfad = "";
		for(int i = 0; i < xml.Vereine.size(); i++) {
			String test =  xml.Vereine.get(i).getVereinsName();
			if(xml.Vereine.get(i).getVereinsName().toString().equals(dBName)) {
				pfad = xml.Vereine.get(i).getPfad();
				this.sql = new MySQLite(xml.Vereine.get(i).getDatenBank());
				//System.out.println("Datenbank: " + pfad + "/" + dBName +".sqlite");
				break;
			}
		}
		if(pfad == "") {
			Message m = new Message("Fehler", "Pfad in XML nicht angegeben !");
		}
	}
	
	////@Override
	public void itemStateChanged(ItemEvent e) {
		try {
			update_table();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private String getQuery() {
		String FilterAktiv ="";
		
		return "Select tblTitel.ID, Nummer, Titel "
				+ "From tblTitel "
				+ "INNER JOIN tblOrchester ON tblOrchester.ID = OrchesterID "
				+ "WHERE Nummer LIKE '%" + this.txt_Nummer.getText() + "%' "
				+ "AND Titel LIKE '%" + this.txt_Titel.getText() + "%' "
				+ "AND Aktiv LIKE '%" + FilterAktiv + "%' "
				+ "AND Orchester = '" + this.cb_Orchester.getSelectedItem().toString() + "' "
				+ "ORDER BY Nummer";
	}
	
	private void update_table() throws ClassNotFoundException, SQLException {
		this.sql.SQLClose();
		ini_SQL();
		this.table_AlleNoten.setQuery(getQuery());
		this.table_AlleNoten.update();
	}

	//@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void keyReleased(KeyEvent e) {
		this.table_AlleNoten.setQuery(getQuery());
		this.table_AlleNoten.update();
	}

	//@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == table_AlleNoten) {
			on_tblAlleNoten_clicked();
		}
		if(e.getSource() == btNeuerTitel) {
			on_btNeuerTitel_clicked();
		}
		if(e.getSource() == this.btStimmenOrdner) {
			on_btStimmenOrdner_clicked();
		}
	}

	//@Override
	public void mousePressed(MouseEvent e) {}

	//@Override
	public void mouseReleased(MouseEvent e) {}

	//@Override
	public void mouseEntered(MouseEvent e) {}

	//@Override
	public void mouseExited(MouseEvent e) {}

	private void on_btNeuerTitel_clicked() {
		try {
			NeuerTitel dialog = new NeuerTitel(this.sql, this.xml);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception event) {
			event.printStackTrace();
		}
	}
	private void on_btStimmenOrdner_clicked() {
		try {
			Stimme dialog = new Stimme(this.sql, this.xml);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception event) {
			event.printStackTrace();
		}
	}
	
	private void on_tblAlleNoten_clicked() {
		int ID = (int)table_AlleNoten.getModel().getValueAt(table_AlleNoten.getSelectedRow(), 0);
		try {
			Titel dialog = new Titel(this.sql, this.xml, ID);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception event) {
			event.printStackTrace();
		}
	}

}