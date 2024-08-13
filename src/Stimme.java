//package IGotTheKeys;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;


public class Stimme extends JDialog implements ActionListener, MouseListener, ItemListener{
	private JPanel contentPanel, panel, panel_1, panel_2, panel_3, panel_4;
	private JLabel lblStimme, lblNewLabel_2;
	private JRadioButton rbOneToOne, rbPageTools,  rbAktiv, rbPassiv, rbAlle;
	private JScrollPane scrollPane, scrollPane_1, scrollPane_2;
	private JButton okButton, cancelButton, btChangeNummer, btnNextPageSet, btnLastPageSet, btnExtractPages, btnCombinePages, btStimmenOrdner, btnSelectFolder, btChangeTitel;
	private JTextField txtSeiten, txt_SourceFolder, textField_6;
	private JTable tblFileListe;
	private MyComboBox cbStimme, cbOrchester;
	
	private MyTable tblVorhandeneTitel, tblOffeneTitel;
	private DefaultTableModel tblModel;
	
	private MySQLite sql;
	private MyXML xml;
	private SystemHandler sysHandler;
	
	private String ActivePassive = "", selectedTitle;
	private List<File> selectedFiles;
	private boolean FileSelected;
	//private String PathSeparator;
	

	/**
	 * Create the dialog.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
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
		/*else if(this.rbOneToOne.isSelected()) {
			CombineFiles_deactivate();
			ExtractPages_deactivate();
			if(!selectedTitle.isEmpty() && FileSelected) {
				
				Filehandler f = new Filehandler();
				System.out.println("Quelle: " +selectedFiles.get(0).toString());
				System.out.println("Ziel: " + Titel.OrchesterPfad +  this.PathSeparator + Titel.get_TitelPfad() +  this.PathSeparator + get_TitelName(selectedStimme));
				
				f.moveFile(selectedFiles.get(0).toPath(), Titel.OrchesterPfad +  "\\" + Titel.get_TitelPfad() + "\\", get_TitelName(selectedStimme));
				Titel.insert_Stimme(this.selectedStimme);
				update_tblOffen();
				update_tblVorhanden();
				openDocument(get_TitelPfad(get_TitelName(selectedStimme)));
				
				ini_table_FileList(new File(this.txt_SourceFolder.getText()));
				
				FileSelected = false;
				reset_selectedStimme();
			}*/
		}
	
	public Stimme(MySQLite sql, MyXML xml) throws ClassNotFoundException, SQLException {
		setTitle("Stimme");
		
		this.sql = sql;
		this.xml = xml;
		
		ini_Dialog();
		ini_rbGroupActive();
		ini_rbGroupTools();
		on_ActivePassiveClicked();
		ini_tblOffeneTitel();
		ini_tblVorhandeneTitel();
		
		AblaufHandler();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "OK") {
			on_OKButtonClicked();
		}
		if(e.getActionCommand() == "Cancel") {
			on_CancelButtonClicked();
		}
		if(e.getActionCommand() == "ActivePassive") {
			on_ActivePassiveClicked();
		}
		if(e.getActionCommand() == "SelectFolder") {
			selectFolder();
		}
	}
	
	
	private String getSQL_VorhandeneTitel(){
		String query = 	"SELECT Nummer, Titel "
						+ "FROM tblTitel "
						+ "INNER JOIN tblNoten ON TitelID = tblTitel.ID "
						+ "INNER JOIN tblOrchester ON OrchesterID = tblOrchester.ID "
						+ "WHERE tblNoten.StimmeID = (SELECT ID FROM tblStimme WHERE Stimme = '" + this.cbStimme.getSelectedItem().toString() + "') "
						+ "AND Orchester LIKE '%" + this.cbOrchester.getSelectedItem().toString() + "%' "
						+ "AND Aktiv LIKE '%" + this.ActivePassive + "%' "
						+ "ORDER BY Nummer";
		return query;
	}
	
	private String getSQL_OffeneTitel() {
		String query ="SELECT Nummer, Titel, tblTitel.ID "
				+ "FROM tblTitel "
				+ "INNER Join tblOrchester ON OrchesterID = tblOrchester.ID "
				+ "WHERE NOT EXISTS (SELECT StimmeID "
				+ "		FROM tblNoten "
				+ "		INNER JOIN tblStimme ON StimmeID = tblStimme.ID "
				+ "		WHERE tblNoten.TitelID = tblTitel.ID "
				+ "		AND Stimme = '" + this.cbStimme.getSelectedItem().toString() + "') "
				+ "AND Orchester LIKE '%" + this.cbOrchester.getSelectedItem().toString() + "%' "
				+ "AND Aktiv LIKE '" + this.ActivePassive + "' "
				+ "ORDER BY Nummer "; 
		return query;
	}

 	private void ini_Dialog() throws ClassNotFoundException, SQLException{
		setBounds(100, 100, 1175, 711);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		{
			contentPanel = new JPanel();
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.NORTH);
			GridBagLayout gbl_contentPanel = new GridBagLayout();
			gbl_contentPanel.columnWidths = new int[]{104, 268, 0, 63, 35, 0, 0};
			gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 344, 0};
			gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			contentPanel.setLayout(gbl_contentPanel);
			{
				lblStimme = new JLabel("Stimme");
				GridBagConstraints gbc_lblStimme = new GridBagConstraints();
				gbc_lblStimme.anchor = GridBagConstraints.SOUTHEAST;
				gbc_lblStimme.insets = new Insets(0, 0, 5, 5);
				gbc_lblStimme.gridx = 0;
				gbc_lblStimme.gridy = 0;
				contentPanel.add(lblStimme, gbc_lblStimme);
			}
			{
				cbStimme = new MyComboBox(this.sql, "SELECT Stimme FROM tblStimme ORDER BY Stimme");
				cbStimme.addItemListener(this);
				GridBagConstraints gbc_cbStimme = new GridBagConstraints();
				gbc_cbStimme.insets = new Insets(0, 0, 5, 5);
				gbc_cbStimme.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbStimme.gridx = 1;
				gbc_cbStimme.gridy = 0;
				contentPanel.add(cbStimme, gbc_cbStimme);
			}
			{
				btChangeNummer = new JButton("\u00C4ndern");
				btChangeNummer.setEnabled(false);
				btChangeNummer.setActionCommand("chNummer");
				GridBagConstraints gbc_btChangeNummer = new GridBagConstraints();
				gbc_btChangeNummer.anchor = GridBagConstraints.SOUTH;
				gbc_btChangeNummer.insets = new Insets(0, 0, 5, 5);
				gbc_btChangeNummer.gridx = 2;
				gbc_btChangeNummer.gridy = 0;
				contentPanel.add(btChangeNummer, gbc_btChangeNummer);
			}
			{
				panel = new JPanel();
				panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.fill = GridBagConstraints.BOTH;
				gbc_panel.gridheight = 4;
				gbc_panel.gridwidth = 3;
				gbc_panel.insets = new Insets(0, 0, 5, 0);
				gbc_panel.gridx = 3;
				gbc_panel.gridy = 0;
				contentPanel.add(panel, gbc_panel);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{49, 0, 98, 0, 0};
				gbl_panel.rowHeights = new int[]{45, 0, 0, 0};
				gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				{
					panel_1 = new JPanel();
					panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
					GridBagConstraints gbc_panel_1 = new GridBagConstraints();
					gbc_panel_1.fill = GridBagConstraints.BOTH;
					gbc_panel_1.gridheight = 2;
					gbc_panel_1.gridwidth = 3;
					gbc_panel_1.insets = new Insets(0, 0, 5, 5);
					gbc_panel_1.gridx = 0;
					gbc_panel_1.gridy = 0;
					panel.add(panel_1, gbc_panel_1);
					GridBagLayout gbl_panel_1 = new GridBagLayout();
					gbl_panel_1.columnWidths = new int[]{61, 0, 134, 0};
					gbl_panel_1.rowHeights = new int[]{0, 0, 0};
					gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
					gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					panel_1.setLayout(gbl_panel_1);
					{
						txtSeiten = new JTextField();
						txtSeiten.setColumns(10);
						GridBagConstraints gbc_txtSeiten = new GridBagConstraints();
						gbc_txtSeiten.ipadx = 5;
						gbc_txtSeiten.fill = GridBagConstraints.HORIZONTAL;
						gbc_txtSeiten.insets = new Insets(0, 5, 5, 5);
						gbc_txtSeiten.gridx = 0;
						gbc_txtSeiten.gridy = 0;
						panel_1.add(txtSeiten, gbc_txtSeiten);
					}
					{
						panel_2 = new JPanel();
						GridBagConstraints gbc_panel_2 = new GridBagConstraints();
						gbc_panel_2.insets = new Insets(0, 0, 5, 5);
						gbc_panel_2.gridx = 1;
						gbc_panel_2.gridy = 0;
						panel_1.add(panel_2, gbc_panel_2);
						GridBagLayout gbl_panel_2 = new GridBagLayout();
						gbl_panel_2.columnWidths = new int[]{0, 0};
						gbl_panel_2.rowHeights = new int[]{0, 0, 0};
						gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
						gbl_panel_2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
						panel_2.setLayout(gbl_panel_2);
						{
							btnNextPageSet = new JButton("+");
							btnNextPageSet.setActionCommand("PagesUp");
							GridBagConstraints gbc_btnNextPageSet = new GridBagConstraints();
							gbc_btnNextPageSet.fill = GridBagConstraints.HORIZONTAL;
							gbc_btnNextPageSet.insets = new Insets(0, 0, 5, 0);
							gbc_btnNextPageSet.gridx = 0;
							gbc_btnNextPageSet.gridy = 0;
							panel_2.add(btnNextPageSet, gbc_btnNextPageSet);
						}
						{
							btnLastPageSet = new JButton("-");
							btnLastPageSet.setActionCommand("PagesDown");
							GridBagConstraints gbc_btnLastPageSet = new GridBagConstraints();
							gbc_btnLastPageSet.fill = GridBagConstraints.HORIZONTAL;
							gbc_btnLastPageSet.gridx = 0;
							gbc_btnLastPageSet.gridy = 1;
							panel_2.add(btnLastPageSet, gbc_btnLastPageSet);
						}
					}
					{
						btnExtractPages = new JButton("extract pdf pages");
						btnExtractPages.setEnabled(false);
						btnExtractPages.setActionCommand("extractPages");
						GridBagConstraints gbc_btnExtractPages = new GridBagConstraints();
						gbc_btnExtractPages.anchor = GridBagConstraints.EAST;
						gbc_btnExtractPages.insets = new Insets(0, 0, 5, 0);
						gbc_btnExtractPages.gridx = 2;
						gbc_btnExtractPages.gridy = 0;
						panel_1.add(btnExtractPages, gbc_btnExtractPages);
					}
					{
						btnCombinePages = new JButton("combine files");
						btnCombinePages.setActionCommand("combineFiles");
						GridBagConstraints gbc_btnCombinePages = new GridBagConstraints();
						gbc_btnCombinePages.fill = GridBagConstraints.HORIZONTAL;
						gbc_btnCombinePages.gridx = 2;
						gbc_btnCombinePages.gridy = 1;
						panel_1.add(btnCombinePages, gbc_btnCombinePages);
					}
				}
				{
					panel_3 = new JPanel();
					panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
					GridBagConstraints gbc_panel_3 = new GridBagConstraints();
					gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
					gbc_panel_3.anchor = GridBagConstraints.NORTH;
					gbc_panel_3.insets = new Insets(0, 0, 5, 0);
					gbc_panel_3.gridx = 3;
					gbc_panel_3.gridy = 0;
					panel.add(panel_3, gbc_panel_3);
					GridBagLayout gbl_panel_3 = new GridBagLayout();
					gbl_panel_3.columnWidths = new int[]{0, 0};
					gbl_panel_3.rowHeights = new int[]{0, 0, 0};
					gbl_panel_3.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_panel_3.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
					panel_3.setLayout(gbl_panel_3);
					{
						rbOneToOne = new JRadioButton("1-1");
						GridBagConstraints gbc_rbOneToOne = new GridBagConstraints();
						gbc_rbOneToOne.anchor = GridBagConstraints.WEST;
						gbc_rbOneToOne.insets = new Insets(0, 0, 5, 0);
						gbc_rbOneToOne.gridx = 0;
						gbc_rbOneToOne.gridy = 0;
						panel_3.add(rbOneToOne, gbc_rbOneToOne);
					}
					{
						rbPageTools = new JRadioButton("Page Tools");

						GridBagConstraints gbc_rbPageTools = new GridBagConstraints();
						gbc_rbPageTools.gridx = 0;
						gbc_rbPageTools.gridy = 1;
						panel_3.add(rbPageTools, gbc_rbPageTools);
					}
				}
				{
					btStimmenOrdner = new JButton("Stimmen-Ordner");
					btStimmenOrdner.setActionCommand("StimmenOrdner");
					btStimmenOrdner.addActionListener(this);
					GridBagConstraints gbc_btStimmenOrdner = new GridBagConstraints();
					gbc_btStimmenOrdner.insets = new Insets(0, 0, 5, 0);
					gbc_btStimmenOrdner.gridx = 3;
					gbc_btStimmenOrdner.gridy = 1;
					panel.add(btStimmenOrdner, gbc_btStimmenOrdner);
				}
				{
					btnSelectFolder = new JButton("Select Folder");
					btnSelectFolder.setActionCommand("SelectFolder");
					btnSelectFolder.addActionListener(this);
					GridBagConstraints gbc_btnSelectFolder = new GridBagConstraints();
					gbc_btnSelectFolder.fill = GridBagConstraints.HORIZONTAL;
					gbc_btnSelectFolder.insets = new Insets(0, 0, 0, 5);
					gbc_btnSelectFolder.gridx = 0;
					gbc_btnSelectFolder.gridy = 2;
					panel.add(btnSelectFolder, gbc_btnSelectFolder);
				}
				{
					txt_SourceFolder = new JTextField();
					txt_SourceFolder.setText("");
					txt_SourceFolder.setColumns(10);
					GridBagConstraints gbc_txt_SourceFolder = new GridBagConstraints();
					gbc_txt_SourceFolder.fill = GridBagConstraints.HORIZONTAL;
					gbc_txt_SourceFolder.gridwidth = 3;
					gbc_txt_SourceFolder.gridx = 1;
					gbc_txt_SourceFolder.gridy = 2;
					panel.add(txt_SourceFolder, gbc_txt_SourceFolder);
				}
			}
			{
				lblNewLabel_2 = new JLabel("Orchester");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 1;
				contentPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			}
			{
				cbOrchester = new MyComboBox(this.sql, "SELECT Orchester FROM tblOrchester ORDER BY Orchester");
				cbOrchester.addItemListener(this);
				GridBagConstraints gbc_cbOrchester = new GridBagConstraints();
				gbc_cbOrchester.insets = new Insets(0, 0, 5, 5);
				gbc_cbOrchester.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbOrchester.gridx = 1;
				gbc_cbOrchester.gridy = 1;
				contentPanel.add(cbOrchester, gbc_cbOrchester);
			}
			{
				btChangeTitel = new JButton("\u00C4ndern");
				btChangeTitel.setEnabled(false);
				btChangeTitel.setActionCommand("chTitel");
				GridBagConstraints gbc_btChangeTitel = new GridBagConstraints();
				gbc_btChangeTitel.insets = new Insets(0, 0, 5, 5);
				gbc_btChangeTitel.gridx = 2;
				gbc_btChangeTitel.gridy = 1;
				contentPanel.add(btChangeTitel, gbc_btChangeTitel);
			}
			{
				rbAktiv = new JRadioButton("Aktive");
				GridBagConstraints gbc_rbAktiv = new GridBagConstraints();
				gbc_rbAktiv.anchor = GridBagConstraints.WEST;
				gbc_rbAktiv.insets = new Insets(0, 0, 5, 5);
				gbc_rbAktiv.gridx = 1;
				gbc_rbAktiv.gridy = 2;
				contentPanel.add(rbAktiv, gbc_rbAktiv);
			}
			{
				rbPassiv = new JRadioButton("Passiv");
				GridBagConstraints gbc_rbPassiv = new GridBagConstraints();
				gbc_rbPassiv.anchor = GridBagConstraints.WEST;
				gbc_rbPassiv.insets = new Insets(0, 0, 5, 5);
				gbc_rbPassiv.gridx = 1;
				gbc_rbPassiv.gridy = 3;
				contentPanel.add(rbPassiv, gbc_rbPassiv);
			}
			{
				rbAlle = new JRadioButton("Alle");
				GridBagConstraints gbc_rbAlle = new GridBagConstraints();
				gbc_rbAlle.anchor = GridBagConstraints.WEST;
				gbc_rbAlle.insets = new Insets(0, 0, 5, 5);
				gbc_rbAlle.gridx = 1;
				gbc_rbAlle.gridy = 4;
				contentPanel.add(rbAlle, gbc_rbAlle);
			}
			{
				panel_4 = new JPanel();
				GridBagConstraints gbc_panel_4 = new GridBagConstraints();
				gbc_panel_4.fill = GridBagConstraints.BOTH;
				gbc_panel_4.gridwidth = 2;
				gbc_panel_4.insets = new Insets(0, 0, 0, 5);
				gbc_panel_4.gridx = 0;
				gbc_panel_4.gridy = 5;
				contentPanel.add(panel_4, gbc_panel_4);
				GridBagLayout gbl_panel_4 = new GridBagLayout();
				gbl_panel_4.columnWidths = new int[]{306, 304, 0};
				gbl_panel_4.rowHeights = new int[]{0, 0, 0};
				gbl_panel_4.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gbl_panel_4.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel_4.setLayout(gbl_panel_4);
				{
					textField_6 = new JTextField();
					textField_6.setColumns(10);
					GridBagConstraints gbc_textField_6 = new GridBagConstraints();
					gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
					gbc_textField_6.insets = new Insets(0, 0, 5, 0);
					gbc_textField_6.gridx = 1;
					gbc_textField_6.gridy = 0;
					panel_4.add(textField_6, gbc_textField_6);
				}
				{
					scrollPane = new JScrollPane();
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 1;
					panel_4.add(scrollPane, gbc_scrollPane);
					{
						tblVorhandeneTitel = new MyTable();
						scrollPane.setViewportView(tblVorhandeneTitel);
					}
				}
				{
					scrollPane_1 = new JScrollPane();
					GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
					gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
					gbc_scrollPane_1.gridx = 1;
					gbc_scrollPane_1.gridy = 1;
					panel_4.add(scrollPane_1, gbc_scrollPane_1);
					{
						tblOffeneTitel = new MyTable();
						scrollPane_1.setViewportView(tblOffeneTitel);
					}
				}
			}
			tblModel = new DefaultTableModel();
			tblModel.addColumn("File");
			{
				scrollPane_2 = new JScrollPane();
				GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
				gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
				gbc_scrollPane_2.gridwidth = 3;
				gbc_scrollPane_2.gridx = 3;
				gbc_scrollPane_2.gridy = 5;
				contentPanel.add(scrollPane_2, gbc_scrollPane_2);
				tblFileListe = new JTable(tblModel);
				tblFileListe.addMouseListener(this);
				scrollPane_2.setViewportView(tblFileListe);
			}
		}
	}
 	
	private void ini_rbGroupActive() {
		ButtonGroup rbGroupAktiv = new ButtonGroup();
		rbAktiv.setSelected(true);
		rbAktiv.setActionCommand("AktivPassiv");
		rbAktiv.addActionListener(this);
		
		rbPassiv.setSelected(false);
		rbPassiv.setActionCommand("AktivPassiv");
		rbPassiv.addActionListener(this);
		
		rbAlle.setSelected(false);
		rbAlle.setActionCommand("AktivPassiv");
		rbAlle.addActionListener(this);
		
		rbGroupAktiv.add(rbAktiv);
		rbGroupAktiv.add(rbPassiv);
		rbGroupAktiv.add(rbAlle);
	}
	private void ini_rbGroupTools() {
		ButtonGroup rbGroupTools = new ButtonGroup();
		rbOneToOne.setSelected(true);
		rbOneToOne.setActionCommand("Tools");
		rbOneToOne.addActionListener(this);
		
		rbPageTools.setActionCommand("Tools");
		rbPageTools.addActionListener(this);
		
		rbGroupTools.add(rbOneToOne);
		rbGroupTools.add(rbPageTools);
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
		this.txtSeiten.setEnabled(true);
	}
	private void ExtractPages_deactivate() {
		this.btnLastPageSet.setEnabled(false);
		this.btnNextPageSet.setEnabled(false);
		this.btnExtractPages.setEnabled(false);
		this.txtSeiten.setEnabled(false);
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
	private void ini_tblVorhandeneTitel() {
		String[] Columns = {"Nummer", "Vorhandene Titel"};
		this.tblVorhandeneTitel.initialise_MyTable(sql, Columns, getSQL_VorhandeneTitel());
		this.tblVorhandeneTitel.addMouseListener(this);
		update_tblVorhandeneTitel();
	}
	
	private void ini_tblOffeneTitel() {
		String[] Columns = {"Nummer", "Offene Titel"};
		this.tblOffeneTitel.initialise_MyTable(sql, Columns, getSQL_OffeneTitel());
		this.tblOffeneTitel.addMouseListener(this);
		update_tblOffeneTitel();
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		 update_tblVorhandeneTitel();
		 update_tblOffeneTitel();
	}
	
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == this.tblOffeneTitel) {
			try {
				on_tblOffeneTitelClicked();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == this.tblVorhandeneTitel) {
			on_tblVorhandeneTitelClicked();
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	private void on_ActivePassiveClicked() {
		if(this.rbAktiv.isSelected()) {
			this.ActivePassive = "Aktiv";
		}
		else if(this.rbPassiv.isSelected()) {
			this.ActivePassive = "Passiv";
		}
		else if(this.rbAlle.isSelected()) {
			this.ActivePassive = "";
		}
	}
	private void on_CancelButtonClicked() {
		this.dispose();
	}
	private void on_OKButtonClicked() {
		this.dispose();
	}
	private void on_tblOffeneTitelClicked() throws ClassNotFoundException, SQLException {
		this.selectedTitle = (String)tblOffeneTitel.getModel().getValueAt(tblOffeneTitel.getSelectedRow(), 1);
		AblaufHandler();
	}
	private void on_tblVorhandeneTitelClicked(){
		this.selectedFiles.clear();
		
		for(int row : tblVorhandeneTitel.getSelectedRows()) {
			SelectFile((String)tblVorhandeneTitel.getModel().getValueAt(row,0));
		}
		if(tblVorhandeneTitel.getSelectedRowCount() == 1) {
			this.FileSelected = true;
		}
		
		//AblaufHandler();
	}
	
	private void SelectFile(String filename) {
		String f = this.txt_SourceFolder.getText() +  this.sysHandler.getFolderSeparator() + filename;
		File file = new File(f); 
		selectedFiles.add(file);
		FileSelected = true;
	}
	
	
	private void update_tblOffeneTitel() {
		this.tblOffeneTitel.setQuery(getSQL_OffeneTitel());
		this.tblOffeneTitel.update();
	}
	private void update_tblVorhandeneTitel() {
		this.tblVorhandeneTitel.setQuery(getSQL_VorhandeneTitel());
		this.tblVorhandeneTitel.update();
	}

	private void selectFolder() {
		String path;
		if(this.txt_SourceFolder.getText().isEmpty()) {
			path = this.sysHandler.getHomePath();
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
	
	
}
