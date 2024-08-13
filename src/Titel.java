//package IGotTheKeys;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JRadioButton;

public class Titel extends JDialog implements ActionListener, MouseListener, KeyListener {

	private final JPanel contentPanel = new JPanel();
	private MySQLite sql;
	private MyXML xml;
	private MyTitle Titel;
	private SystemHandler sysHandler;
	private List<File> selectedFiles;
	private String selectedStimme;
	private String PathSeparator;
	
	boolean FileSelected;
	
	private JTextField txt_Nummer;
	private JTextField txt_Orchester;
	private JTextField txt_Pfad;
	private JTextField txt_titel;
	
	private JButton okButton, cancelButton;
	private JPanel panel;
	private JScrollPane scrollPane, scrollPane_1;

	private MyTable tblOffen, tblVorhanden;
	private JButton btChangeNummer;
	private JButton btChangeTitel;
	private JButton btChangeOrchester;
	private JButton btChangePfad;
	private JTextField txtVonSeite;
	private JPanel panel_1;
	private JButton btnNextPageSet;
	private JButton btnLastPageSet;
	private JButton btnExtractPages;
	private JPanel panel_2;
	private JButton btnCombinePages;
	private JTable tblFileListe;
	private DefaultTableModel tblModel;
	private JScrollPane scrollPane_2;
	private JButton btnSelectFolder;
	private JTextField txt_SourceFolder;
	private JPanel panel_3;
	private JPanel panel_4;
	private JRadioButton rbOneToOne;
	private JRadioButton rbPageTools;
	private JTextField txtStimmeFilter;
	private JButton btStimmenOrdner;
	private JTextField txtBisSeite;

	/**
	 * Create the dialog.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	
	
	
	public Titel(MySQLite s, MyXML x, int ID) throws ClassNotFoundException, SQLException {
		this.sql = s;
		this.xml = x;
		this.sysHandler = new SystemHandler();

		this.Titel = new MyTitle(this.sql, this.xml, ID);
		
		this.selectedFiles = new ArrayList<File>();
		
		reset_selectedStimme();
		FileSelected = false;
		
		ini_Dialog();
		ini_DialogInhalte();
		AblaufHandler();
	}

	private void AblaufHandler() throws ClassNotFoundException, SQLException {
		if(this.rbPageTools.isSelected()) {
			if(selectedFiles.size() < 1) {
				CombineFiles_deactivate();
				ExtractPages_deactivate();
				FileSelected = false;
			}
			else if(selectedFiles.size() == 1) {
				CombineFiles_deactivate();
				ExtractPages_activate();
				FileSelected = true;
			}
			else if(selectedFiles.size() > 1) {
				CombineFiles_activate();
				ExtractPages_deactivate();
				FileSelected = true;
			}
		}
		else if(this.rbOneToOne.isSelected()) {
			CombineFiles_deactivate();
			ExtractPages_deactivate();
			if(!selectedStimme.isEmpty() && FileSelected) {
				
				Filehandler f = new Filehandler();
				System.out.println("Quelle: " +selectedFiles.get(0).toString());
				System.out.println("Ziel: " + Titel.OrchesterPfad +  sysHandler.getFolderSeparator() + Titel.get_TitelPfad() +  sysHandler.getFolderSeparator() + get_TitelName(selectedStimme));
				
				f.moveFile(selectedFiles.get(0).toPath(), Titel.OrchesterPfad +  "/" + Titel.get_TitelPfad() + "/", get_TitelName(selectedStimme));
				Titel.insert_Stimme(this.selectedStimme);
				update_tblOffen();
				update_tblVorhanden();
				openDocument(get_TitelPfad(get_TitelName(selectedStimme)));
				
				ini_table_FileList(new File(this.txt_SourceFolder.getText()));
				
				FileSelected = false;
				reset_selectedStimme();
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "OK") {
			dispose();
		}
		if(e.getActionCommand() == "Cancel") {
			dispose();
		}
		if(e.getActionCommand() == "chNummer") {
			System.out.println("Not implemented yet: Change Nummer");
		}
		if(e.getActionCommand() == "chTitel") {
			System.out.println("Not implemented yet: Change Titel");		
		}
		if(e.getActionCommand() == "chOrchester") {
			System.out.println("Not implemented yet: Change Orchester");
		}
		if(e.getActionCommand() == "chPfad") {
			System.out.println("Not implemented yet: Change Pfad");
		}
		if(e.getActionCommand() == "combineFiles") {
			try {
				combineFiles();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand() == "PagesUp") {
			incrementPages("+");
		}
		if(e.getActionCommand() == "PagesDown") {
			incrementPages("-");
		}
		if(e.getActionCommand() == "SelectFolder") {
			selectFolder();
		}
		if(e.getActionCommand().equals("extractPages")) {
			try {
				extractPages();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Tools")) {
			try {
				AblaufHandler();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("StimmenOrdner")) {
			try {
				TitelAnStimmenOrdner(this.Titel.ID);
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void combineFiles() throws IOException, InterruptedException {
		MyGhostscript gs = new MyGhostscript(this.xml);
		String Ziel = Titel.OrchesterPfad +  sysHandler.getFolderSeparator() + Titel.get_TitelPfad() +  sysHandler.getFolderSeparator() + get_TitelName(this.selectedStimme);
		gs.combinePDFs(selectedFiles, Ziel);
	}

	private void CombineFiles_activate() {
		this.btnCombinePages.setEnabled(true);
	}
	
	private void CombineFiles_deactivate() {
		this.btnCombinePages.setEnabled(false);
	}
	
	private void ExtractPages_activate() {
		this.btnLastPageSet.setEnabled(true);
		this.btnNextPageSet.setEnabled(true);
		this.btnExtractPages.setEnabled(true);
		this.txtVonSeite.setEnabled(true);
	}
	
	private void ExtractPages_deactivate() {
		this.btnLastPageSet.setEnabled(false);
		this.btnNextPageSet.setEnabled(false);
		this.btnExtractPages.setEnabled(false);
		this.txtVonSeite.setEnabled(false);
	}

	private void extractPages() throws IOException, ClassNotFoundException, SQLException, InterruptedException {
		if(!this.selectedStimme.isEmpty()
				&& !this.txtVonSeite.getText().isEmpty()
				&& selectedFiles.size() == 1) {
			String seiteVon = this.txtVonSeite.getText();
			String seiteBis = this.txtBisSeite.getText();
			String Ziel = Titel.OrchesterPfad +  sysHandler.getFolderSeparator() + Titel.get_TitelPfad() +  sysHandler.getFolderSeparator() + get_TitelName(this.selectedStimme);
			
			if(selectedFiles.size() == 1) {
				MyGhostscript gs = new MyGhostscript(this.xml);
				gs.extractPages(selectedFiles.get(0).getAbsolutePath(), Ziel, seiteVon, seiteBis);
				File f = new File(Ziel);
				if(f.exists()) {
					Titel.insert_Stimme(this.selectedStimme);
					update_tblOffen();
					update_tblVorhanden();
					incrementPages("+");
					openDocument(Ziel);
				}
			}
		}
	}

	private String get_TitelPfad(String filename) {
		return Titel.OrchesterPfad +  sysHandler.getFolderSeparator() + Titel.get_TitelPfad() +  sysHandler.getFolderSeparator() + filename;
	}

	private String get_TitelName(String Stimme) {
		return Titel.get_NummerFormatted() + "_" + Titel.Titel + "_" + Stimme + ".pdf";
	}

	private String get_tblOffen_SQL()
	{
		String query = 	"SELECT Stimme "
						+ "FROM tblStimme "
						+ "WHERE NOT EXISTS (SELECT Stimme "
						+ "FROM tblNoten "
						+ "WHERE tblNoten.StimmeID = tblStimme.ID "
						+ "AND TitelID = " + String.valueOf(Titel.ID) +")"
						+ "AND Stimme LIKE '%" + this.txtStimmeFilter.getText() + "%' "
						+ "ORDER By Stimme";
		return query;
	}

	private String get_tblVorhanden_SQL()
	{
		String query = "SELECT Stimme "
				+ "FROM tblNoten "
				+ "INNER JOIN tblStimme ON tblStimme.ID = tblNoten.StimmeID "
				+ "INNER JOIN tblTitel ON tblTitel.ID = tblNoten.TitelID "
				+ "INNER JOIN tblOrchester ON tblOrchester.ID = tblTitel.OrchesterID "
				+ "WHERE tblTitel.Nummer = '" + Integer.toString(Titel.Nummer) + "' "
				+ "AND Orchester = '"+ Titel.Orchester + "' "				
				+ "ORDER BY Stimme";
		return query;
	}

	private void incrementPages(String plusminus) {
		
		String vonNeu = "";
		String bisNeu = "";
		//String[] arrOfString = this.txtVonSeite.getText().split("-");
		
		if(this.txtBisSeite.getText() == "") {
			if(plusminus.equals("+")) {
				vonNeu = Integer.toString(Integer.valueOf(this.txtVonSeite.getText()) + 1);
			}
			
			if(plusminus.equals("-")) {
				if(Integer.valueOf(this.txtVonSeite.getText()) > 1) {
					vonNeu = Integer.toString(Integer.valueOf(this.txtVonSeite.getText()) - 1);	
				}
				else {
					vonNeu = this.txtVonSeite.getText();
				}
			}
		}
		if(this.txtBisSeite.getText() != "") {
			int x = Integer.valueOf(this.txtVonSeite.getText());
			int y = Integer.valueOf(this.txtBisSeite.getText());
			
			if(plusminus.equals("+")) {
				vonNeu = Integer.toString(y + 1);
				bisNeu = Integer.toString(y + 1 + (y-x));
			}
			
			if(plusminus.equals("-")) {
				if(x-1-(y-x) > 0) {
					vonNeu =  Integer.toString(x-1-(y-x));
					bisNeu =  Integer.toString(y-(y-x)-1);
	            }
				else {
					vonNeu = this.txtVonSeite.getText();
					bisNeu = this.txtBisSeite.getText();
				}
			}
		}
		this.txtVonSeite.setText(vonNeu);
		this.txtBisSeite.setText(bisNeu);
	}
	
	private void ini_tbl_vorhanden() {
		
		String query = "SELECT Stimme "
				+ "FROM tblNoten "
				+ "INNER JOIN tblStimme ON tblStimme.ID = tblNoten.StimmeID "
				+ "INNER JOIN tblTitel ON tblTitel.ID = tblNoten.TitelID "
				+ "INNER JOIN tblOrchester ON tblOrchester.ID = tblTitel.OrchesterID "
				+ "WHERE tblTitel.Nummer = '" + Integer.toString(Titel.Nummer) + "' "
				+ "AND Orchester = '"+ Titel.Orchester + "' "				
				+ "ORDER BY Stimme";
		String[] Columns = {"Stimme"};
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);
		{
			this.tblVorhanden = new MyTable(this.sql, Columns, query);
			tblVorhanden.addMouseListener(this);
			scrollPane.setViewportView(tblVorhanden);
		}
	}
	
	private void ini_tbl_offen() {
		
		String[] Columns = {"Stimme"};
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 1;
		panel.add(scrollPane_1, gbc_scrollPane_1);
		{
			tblOffen = new MyTable(this.sql, Columns, get_tblOffen_SQL());
			tblOffen.addMouseListener(this);
			scrollPane_1.setViewportView(tblOffen);
		}
	}
	
	private void ini_Dialog() {
		setBounds(100, 100, 874, 820);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{104, 268, 0, 63, 35, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Nummer");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHEAST;
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			txt_Nummer = new JTextField();
			GridBagConstraints gbc_txt_Nummer = new GridBagConstraints();
			gbc_txt_Nummer.anchor = GridBagConstraints.SOUTH;
			gbc_txt_Nummer.insets = new Insets(0, 0, 5, 5);
			gbc_txt_Nummer.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_Nummer.gridx = 1;
			gbc_txt_Nummer.gridy = 0;
			contentPanel.add(txt_Nummer, gbc_txt_Nummer);
			txt_Nummer.setColumns(10);
		}
		{
			btChangeNummer = new JButton("\u00C4ndern");
			btChangeNummer.setEnabled(false);
			btChangeNummer.setActionCommand("chNummer");
			btChangeNummer.addActionListener(this);
			GridBagConstraints gbc_btChangeNummer = new GridBagConstraints();
			gbc_btChangeNummer.anchor = GridBagConstraints.SOUTH;
			gbc_btChangeNummer.insets = new Insets(0, 0, 5, 5);
			gbc_btChangeNummer.gridx = 2;
			gbc_btChangeNummer.gridy = 0;
			contentPanel.add(btChangeNummer, gbc_btChangeNummer);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Titel");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 1;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			txt_titel = new JTextField();
			GridBagConstraints gbc_txt_titel = new GridBagConstraints();
			gbc_txt_titel.insets = new Insets(0, 0, 5, 5);
			gbc_txt_titel.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_titel.gridx = 1;
			gbc_txt_titel.gridy = 1;
			contentPanel.add(txt_titel, gbc_txt_titel);
			txt_titel.setColumns(10);
		}
		{
			btChangeTitel = new JButton("\u00C4ndern");
			btChangeTitel.setEnabled(false);
			btChangeTitel.setActionCommand("chTitel");
			btChangeTitel.addActionListener(this);
			
			GridBagConstraints gbc_btChangeTitel = new GridBagConstraints();
			gbc_btChangeTitel.insets = new Insets(0, 0, 5, 5);
			gbc_btChangeTitel.gridx = 2;
			gbc_btChangeTitel.gridy = 1;
			contentPanel.add(btChangeTitel, gbc_btChangeTitel);
		}
		{
			panel_2 = new JPanel();
			panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.gridheight = 4;
			gbc_panel_2.gridwidth = 3;
			gbc_panel_2.insets = new Insets(0, 0, 5, 0);
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridx = 3;
			gbc_panel_2.gridy = 0;
			contentPanel.add(panel_2, gbc_panel_2);
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			gbl_panel_2.columnWidths = new int[]{166, 0, 98, 0};
			gbl_panel_2.rowHeights = new int[]{45, 0, 0, 0};
			gbl_panel_2.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0};
			gbl_panel_2.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			panel_2.setLayout(gbl_panel_2);
			{
				panel_3 = new JPanel();
				panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_3 = new GridBagConstraints();
				gbc_panel_3.gridheight = 2;
				gbc_panel_3.gridwidth = 3;
				gbc_panel_3.insets = new Insets(0, 0, 5, 5);
				gbc_panel_3.fill = GridBagConstraints.BOTH;
				gbc_panel_3.gridx = 0;
				gbc_panel_3.gridy = 0;
				panel_2.add(panel_3, gbc_panel_3);
				GridBagLayout gbl_panel_3 = new GridBagLayout();
				gbl_panel_3.columnWidths = new int[]{61, 61, 0, 134, 0};
				gbl_panel_3.rowHeights = new int[]{0, 0, 0};
				gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panel_3.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				panel_3.setLayout(gbl_panel_3);
				{
					txtVonSeite = new JTextField();
					GridBagConstraints gbc_txtVonSeite = new GridBagConstraints();
					gbc_txtVonSeite.ipadx = 5;
					gbc_txtVonSeite.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtVonSeite.insets = new Insets(0, 5, 5, 5);
					gbc_txtVonSeite.gridx = 0;
					gbc_txtVonSeite.gridy = 0;
					panel_3.add(txtVonSeite, gbc_txtVonSeite);
					txtVonSeite.setColumns(10);
				}
				{
					txtBisSeite = new JTextField();
					txtBisSeite.setColumns(10);
					GridBagConstraints gbc_txtBisSeite = new GridBagConstraints();
					gbc_txtBisSeite.insets = new Insets(0, 0, 5, 5);
					gbc_txtBisSeite.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtBisSeite.gridx = 1;
					gbc_txtBisSeite.gridy = 0;
					panel_3.add(txtBisSeite, gbc_txtBisSeite);
				}
				{
					panel_1 = new JPanel();
					GridBagConstraints gbc_panel_1 = new GridBagConstraints();
					gbc_panel_1.insets = new Insets(0, 0, 5, 5);
					gbc_panel_1.gridx = 2;
					gbc_panel_1.gridy = 0;
					panel_3.add(panel_1, gbc_panel_1);
					GridBagLayout gbl_panel_1 = new GridBagLayout();
					gbl_panel_1.columnWidths = new int[]{0, 0};
					gbl_panel_1.rowHeights = new int[]{0, 0, 0};
					gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					panel_1.setLayout(gbl_panel_1);
					{
						btnNextPageSet = new JButton("+");
						btnNextPageSet.setActionCommand("PagesUp");
						btnNextPageSet.addActionListener(this);
						GridBagConstraints gbc_btnNextPageSet = new GridBagConstraints();
						gbc_btnNextPageSet.fill = GridBagConstraints.HORIZONTAL;
						gbc_btnNextPageSet.insets = new Insets(0, 0, 5, 0);
						gbc_btnNextPageSet.gridx = 0;
						gbc_btnNextPageSet.gridy = 0;
						panel_1.add(btnNextPageSet, gbc_btnNextPageSet);
					}
					{
						btnLastPageSet = new JButton("-");
						btnLastPageSet.setActionCommand("PagesDown");
						btnLastPageSet.addActionListener(this);
						GridBagConstraints gbc_btnLastPageSet = new GridBagConstraints();
						gbc_btnLastPageSet.fill = GridBagConstraints.HORIZONTAL;
						gbc_btnLastPageSet.gridx = 0;
						gbc_btnLastPageSet.gridy = 1;
						panel_1.add(btnLastPageSet, gbc_btnLastPageSet);
					}
				}
				{
					btnExtractPages = new JButton("extract pdf pages");
					btnExtractPages.setEnabled(false);
					btnExtractPages.setActionCommand("extractPages");
					btnExtractPages.addActionListener(this);
					GridBagConstraints gbc_btnExtractPages = new GridBagConstraints();
					gbc_btnExtractPages.insets = new Insets(0, 0, 5, 0);
					gbc_btnExtractPages.anchor = GridBagConstraints.EAST;
					gbc_btnExtractPages.gridx = 3;
					gbc_btnExtractPages.gridy = 0;
					panel_3.add(btnExtractPages, gbc_btnExtractPages);
					{
						btnCombinePages = new JButton("combine files");
						btnCombinePages.setActionCommand("combineFiles");
						btnCombinePages.addActionListener(this);
						GridBagConstraints gbc_btnCombinePages = new GridBagConstraints();
						gbc_btnCombinePages.fill = GridBagConstraints.HORIZONTAL;
						gbc_btnCombinePages.gridx = 3;
						gbc_btnCombinePages.gridy = 1;
						panel_3.add(btnCombinePages, gbc_btnCombinePages);
					}
				}
			}
			{
				{
					panel_4 = new JPanel();
					panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
					GridBagConstraints gbc_panel_4 = new GridBagConstraints();
					gbc_panel_4.anchor = GridBagConstraints.NORTH;
					gbc_panel_4.insets = new Insets(0, 0, 5, 0);
					gbc_panel_4.gridx = 3;
					gbc_panel_4.gridy = 0;
					panel_2.add(panel_4, gbc_panel_4);
					GridBagLayout gbl_panel_4 = new GridBagLayout();
					gbl_panel_4.columnWidths = new int[]{0, 0};
					gbl_panel_4.rowHeights = new int[]{0, 0, 0};
					gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_panel_4.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					panel_4.setLayout(gbl_panel_4);
					ButtonGroup grpTools = new ButtonGroup();
					{
						rbOneToOne = new JRadioButton("1-1");
						rbOneToOne.setSelected(true);
						rbOneToOne.setActionCommand("Tools");
						rbOneToOne.addActionListener(this);
						GridBagConstraints gbc_rbOneToOne = new GridBagConstraints();
						gbc_rbOneToOne.anchor = GridBagConstraints.WEST;
						gbc_rbOneToOne.insets = new Insets(0, 0, 5, 0);
						gbc_rbOneToOne.gridx = 0;
						gbc_rbOneToOne.gridy = 0;
						panel_4.add(rbOneToOne, gbc_rbOneToOne);
					}
					{
						rbPageTools = new JRadioButton("Page Tools");
						rbPageTools.setActionCommand("Tools");
						rbPageTools.addActionListener(this);
						GridBagConstraints gbc_rbPageTools = new GridBagConstraints();
						gbc_rbPageTools.gridx = 0;
						gbc_rbPageTools.gridy = 1;
						panel_4.add(rbPageTools, gbc_rbPageTools);
					}
					grpTools.add(rbOneToOne);
					grpTools.add(rbPageTools);
				}
			}
			btnSelectFolder = new JButton("Select Folder");
			btnSelectFolder.setActionCommand("SelectFolder");
			btnSelectFolder.addActionListener(this);
			{
				btStimmenOrdner = new JButton("Stimmen-Ordner");
				btStimmenOrdner.setActionCommand("StimmenOrdner");
				btStimmenOrdner.addActionListener(this);
				GridBagConstraints gbc_btStimmenOrdner = new GridBagConstraints();
				gbc_btStimmenOrdner.insets = new Insets(0, 0, 5, 0);
				gbc_btStimmenOrdner.gridx = 3;
				gbc_btStimmenOrdner.gridy = 1;
				panel_2.add(btStimmenOrdner, gbc_btStimmenOrdner);
			}
			GridBagConstraints gbc_btnSelectFolder = new GridBagConstraints();
			gbc_btnSelectFolder.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnSelectFolder.insets = new Insets(0, 0, 0, 5);
			gbc_btnSelectFolder.gridx = 0;
			gbc_btnSelectFolder.gridy = 2;
			panel_2.add(btnSelectFolder, gbc_btnSelectFolder);
			{
				txt_SourceFolder = new JTextField();
				for(int i = 0; i < xml.Verzeichnisse.size(); i++) 
				{
					if(xml.Verzeichnisse.get(i).getVereinsName().equals("PDFQuelle")) {
						txt_SourceFolder.setText(xml.Verzeichnisse.get(i).getPfad());
						break;
					}
				}
				//txt_SourceFolder.setText("");
				GridBagConstraints gbc_txt_SourceFolder = new GridBagConstraints();
				gbc_txt_SourceFolder.gridwidth = 3;
				gbc_txt_SourceFolder.fill = GridBagConstraints.HORIZONTAL;
				gbc_txt_SourceFolder.gridx = 1;
				gbc_txt_SourceFolder.gridy = 2;
				panel_2.add(txt_SourceFolder, gbc_txt_SourceFolder);
				txt_SourceFolder.setColumns(10);
			}
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Orchester");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 0;
			gbc_lblNewLabel_2.gridy = 2;
			contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		}
		{
			txt_Orchester = new JTextField();
			GridBagConstraints gbc_txt_Orchester = new GridBagConstraints();
			gbc_txt_Orchester.insets = new Insets(0, 0, 5, 5);
			gbc_txt_Orchester.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_Orchester.gridx = 1;
			gbc_txt_Orchester.gridy = 2; 
			contentPanel.add(txt_Orchester, gbc_txt_Orchester);
			txt_Orchester.setColumns(10);
		}
		{
			btChangeOrchester = new JButton("\u00C4ndern");
			btChangeOrchester.setEnabled(false);
			btChangeOrchester.setActionCommand("chOrchester");
			btChangeOrchester.addActionListener(this);
			GridBagConstraints gbc_btChangeOrchester = new GridBagConstraints();
			gbc_btChangeOrchester.insets = new Insets(0, 0, 5, 5);
			gbc_btChangeOrchester.gridx = 2;
			gbc_btChangeOrchester.gridy = 2;
			contentPanel.add(btChangeOrchester, gbc_btChangeOrchester);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Pfad");
			GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
			gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_3.gridx = 0;
			gbc_lblNewLabel_3.gridy = 3;
			contentPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		}
		{
			txt_Pfad = new JTextField();
			GridBagConstraints gbc_txt_Pfad = new GridBagConstraints();
			gbc_txt_Pfad.insets = new Insets(0, 0, 5, 5);
			gbc_txt_Pfad.anchor = GridBagConstraints.NORTH;
			gbc_txt_Pfad.fill = GridBagConstraints.HORIZONTAL;
			gbc_txt_Pfad.gridx = 1;
			gbc_txt_Pfad.gridy = 3;
			contentPanel.add(txt_Pfad, gbc_txt_Pfad);
			txt_Pfad.setColumns(10);
			}				
		{
			btChangePfad = new JButton("\u00C4ndern");
			btChangePfad.setEnabled(false);
			btChangePfad.setActionCommand("chPfad");
			btChangePfad.addActionListener(this);
			GridBagConstraints gbc_btChangePfad = new GridBagConstraints();
			gbc_btChangePfad.insets = new Insets(0, 0, 5, 5);
			gbc_btChangePfad.gridx = 2;
			gbc_btChangePfad.gridy = 3;
			contentPanel.add(btChangePfad, gbc_btChangePfad);
		}
		{
			panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 2;
			gbc_panel.insets = new Insets(0, 0, 0, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{173, 163, 0};
			gbl_panel.rowHeights = new int[]{0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				tblModel = new DefaultTableModel();
				tblModel.addColumn("File");
			}
			{
				scrollPane_2 = new JScrollPane();
				GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
				gbc_scrollPane_2.gridwidth = 3;
				gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
				gbc_scrollPane_2.gridx = 3;
				gbc_scrollPane_2.gridy = 4;
				contentPanel.add(scrollPane_2, gbc_scrollPane_2);
				tblFileListe = new JTable(tblModel);
				tblFileListe.addMouseListener(this);
				scrollPane_2.setViewportView(tblFileListe);
			}
			{
				txtStimmeFilter = new JTextField();
				txtStimmeFilter.addKeyListener(this);
				GridBagConstraints gbc_txtStimmeFilter = new GridBagConstraints();
				gbc_txtStimmeFilter.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtStimmeFilter.insets = new Insets(0, 0, 5, 0);
				gbc_txtStimmeFilter.gridx = 1;
				gbc_txtStimmeFilter.gridy = 0;
				panel.add(txtStimmeFilter, gbc_txtStimmeFilter);
				txtStimmeFilter.setColumns(10);
			}
			{
				ini_tbl_vorhanden();
			}
			{
				ini_tbl_offen();
			}
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				this.okButton = new JButton("OK");
				okButton.addActionListener(this);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				this.cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void ini_table_FileList(File dir) {
		tblModel.setRowCount(0); // clear table
		String[] names = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				boolean value;
				// return files only that end with ".pdf"
				if(name.endsWith(".pdf")){
					value=true;
				}
				else{
					value=false;
				}
				return value;
			}
		});
		
		
		for(int i = 0; i < names.length; i++){
			System.out.println(names[i]);
			tblModel.insertRow(0, new Object[] { names[i] });
		}
	}
	
	private void ini_DialogInhalte() {
		this.txt_Nummer.setText(Integer.toString(this.Titel.Nummer));
		this.txt_titel.setText(this.Titel.Titel);
		this.txt_Orchester.setText(this.Titel.Orchester);
		this.txt_Pfad.setText(this.Titel.OrchesterPfad);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		update_tblOffen();		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

		@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource().equals(this.tblVorhanden)) {
			on_tblVorhanden_clicked();
		}
		if(e.getSource().equals(this.tblOffen)) {
			try {
				on_tblOffen_clicked();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource().equals(this.tblFileListe)) {
			try {
				on_tblFileListe_clicked();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	private void on_tblFileListe_clicked() throws ClassNotFoundException, SQLException {
		this.selectedFiles.clear();
		
		for(int row : tblFileListe.getSelectedRows()) {
			SelectFile((String)tblFileListe.getModel().getValueAt(row,0));
		}
		if(tblFileListe.getSelectedRowCount() == 1) {
			this.FileSelected = true;
		}
		
		AblaufHandler();
	}
	
	private void on_tblOffen_clicked() throws ClassNotFoundException, SQLException {
		this.selectedStimme = (String)tblOffen.getModel().getValueAt(tblOffen.getSelectedRow(), 0);
		AblaufHandler();
	}
	
	private void on_tblVorhanden_clicked() {
		String Titel = get_TitelName((String)tblVorhanden.getModel().getValueAt(tblVorhanden.getSelectedRow(),0));
		openDocument(get_TitelPfad(Titel));
	}

	private void openDocument(String filename) {
		if (Desktop.isDesktopSupported()) {
		    try {
		    	File myFile = new File(filename);
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
	
	private void reset_selectedStimme() {
		this.selectedStimme = "";
	}

	private void SelectFile(String filename) {
		String f = this.txt_SourceFolder.getText() +  sysHandler.getFolderSeparator() + filename;
		File file = new File(f); 
		selectedFiles.add(file);
	}
	
	private void selectFolder() {
		String path;
		if(this.txt_SourceFolder.getText().isEmpty()) {
			path = sysHandler.getHomePath();
		}
		else {
			path = this.txt_SourceFolder.getText();
		}
		final JFileChooser fileChooser = new JFileChooser(path);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		fileChooser.setVisible(true);
        final int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
        	File inputVerzStr = fileChooser.getSelectedFile();
        	this.txt_SourceFolder.setText(inputVerzStr.getPath().toString());
        	ini_table_FileList(inputVerzStr);
        }
            
        fileChooser.setVisible(false);
	}

	private void update_tblOffen()
	{
		tblOffen.setQuery(get_tblOffen_SQL());
		tblOffen.update();
	}
	private void update_tblVorhanden()
	{
		tblVorhanden.setQuery(get_tblVorhanden_SQL());
		tblVorhanden.update();
	}
	
	private void TitelAnStimmenOrdner(int TitelID) throws ClassNotFoundException, SQLException, IOException {
	
		ResultSet rs = this.sql.SQLquery(get_tblVorhanden_SQL());
		while(rs.next()) {
			String Stimme = rs.getString("Stimme");

			String ZielPfad = Titel.OrchesterPfad + sysHandler.getFolderSeparator() + "0000 Stimmen-Ordner" + sysHandler.getFolderSeparator() + Stimme;
			
			File ZielFile = new File(ZielPfad + sysHandler.getFolderSeparator() + get_TitelName(Stimme));
			if(!ZielFile.exists()) {
				File OriginalFile = new File(get_TitelPfad(get_TitelName(Stimme)));
				try {
					Files.copy(OriginalFile.toPath(), ZielFile.toPath());
					System.out.println(OriginalFile + " kopiert nach " + ZielFile);
					
				} catch(IOException e) {
					Message dialog = new Message("Error ", e.getMessage());
					dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					dialog.setModal(true);
					dialog.setVisible(true);
				}
			}
		}
		Message dialog = new Message("Meldung", "Der Stimmenordner wurde aktualisiert");
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setVisible(true);
	}
}
