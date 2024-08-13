//package IGotTheKeys;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;

public class NeuerTitel extends JDialog implements ActionListener, ItemListener, MouseListener, KeyListener{

	private final JPanel contentPanel = new JPanel();

	private JTextField txtNummer,txtTitel;
	
	private JButton btNeuerTitel;
	
	private JRadioButton rdbtnAktiv, rdbtnPassive;
	
	private MyComboBox cb_Orchester;
	
	private String ActivePassive;
	
	boolean TitelOK, NummerOK, OrchesterOK;
		
	private MySQLite sql;
	private MyXML xml;
	private MyTitle Titel;
	private SystemHandler sysHandler;


	public NeuerTitel(MySQLite s, MyXML x) throws ClassNotFoundException, SQLException {
		this.sql = s;
		this.xml = x;
		ini_Dialog();
		this.sysHandler = new SystemHandler();
		set_TitelOK(false);
		set_NummerOK(false);
		set_OrchesterOK(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "ActivePassive") {
			if(rdbtnAktiv.isSelected()) {
				this.ActivePassive = "Aktiv";
			}
			else {
				this.ActivePassive = "Passiv";
			}
		}
		if(e.getActionCommand() == "OK") {
			this.dispose();
		}
		if(e.getActionCommand() == "Cancel") {
			this.dispose();
		}
		
	}
	
	public boolean check_Nummerzeichen(String t) {
		if(!t.matches("[0-9]*")) {
		return false;
		}
		else return true;
	}

	public boolean check_Orchester() {
		if(!this.cb_Orchester.getSelectedItem().toString().isEmpty()) {
			return true;
		}
		else
			return false;
	}
	
	private void ini_Dialog() throws ClassNotFoundException, SQLException {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Nummer");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			txtNummer = new JTextField();
			GridBagConstraints gbc_txtNummer = new GridBagConstraints();
			gbc_txtNummer.insets = new Insets(0, 0, 5, 5);
			gbc_txtNummer.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtNummer.gridx = 1;
			gbc_txtNummer.gridy = 0;
			contentPanel.add(txtNummer, gbc_txtNummer);
			txtNummer.setColumns(10);
			txtNummer.addKeyListener(this);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Titel");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 1;
			contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			txtTitel = new JTextField();
			GridBagConstraints gbc_txtTitel = new GridBagConstraints();
			gbc_txtTitel.insets = new Insets(0, 0, 5, 5);
			gbc_txtTitel.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTitel.gridx = 1;
			gbc_txtTitel.gridy = 1;
			contentPanel.add(txtTitel, gbc_txtTitel);
			txtTitel.setColumns(10);
			txtTitel.addKeyListener(this);
		}
		{
			JButton btUpdate = new JButton("Daten Aktualisieren");
			GridBagConstraints gbc_btUpdate = new GridBagConstraints();
			gbc_btUpdate.insets = new Insets(0, 0, 5, 0);
			gbc_btUpdate.gridx = 2;
			gbc_btUpdate.gridy = 1;
			contentPanel.add(btUpdate, gbc_btUpdate);
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
			cb_Orchester = new MyComboBox(this.sql, "SELECT Orchester FROM tblOrchester ORDER BY Orchester");
			GridBagConstraints gbc_cb_Orchester = new GridBagConstraints();
			gbc_cb_Orchester.insets = new Insets(0, 0, 5, 5);
			gbc_cb_Orchester.fill = GridBagConstraints.HORIZONTAL;
			gbc_cb_Orchester.gridx = 1;
			gbc_cb_Orchester.gridy = 2;
			contentPanel.add(cb_Orchester, gbc_cb_Orchester);
			cb_Orchester.addItemListener(this);
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			ButtonGroup grpButtons = new ButtonGroup();
			{
				rdbtnAktiv = new JRadioButton("aktiv");
				rdbtnAktiv.setSelected(true);
				GridBagConstraints gbc_rdbtnAktiv = new GridBagConstraints();
				gbc_rdbtnAktiv.insets = new Insets(0, 0, 0, 5);
				gbc_rdbtnAktiv.gridx = 0;
				gbc_rdbtnAktiv.gridy = 0;
				panel.add(rdbtnAktiv, gbc_rdbtnAktiv);
				rdbtnAktiv.setActionCommand("ActivePassive");
				rdbtnAktiv.addActionListener(this);
			}
			{
				rdbtnPassive = new JRadioButton("passiv");
				GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
				gbc_rdbtnNewRadioButton_1.gridx = 1;
				gbc_rdbtnNewRadioButton_1.gridy = 0;
				panel.add(rdbtnPassive, gbc_rdbtnNewRadioButton_1);
				rdbtnPassive.setActionCommand("ActivePassive");
				rdbtnPassive.addActionListener(this);
			}
			grpButtons.add(rdbtnAktiv);
			grpButtons.add(rdbtnPassive);
		}
		{
			btNeuerTitel = new JButton("Titel anlegen");
			btNeuerTitel.addMouseListener(this);
			GridBagConstraints gbc_btNeuerTitel = new GridBagConstraints();
			gbc_btNeuerTitel.fill = GridBagConstraints.HORIZONTAL;
			gbc_btNeuerTitel.insets = new Insets(0, 0, 5, 0);
			gbc_btNeuerTitel.gridx = 2;
			gbc_btNeuerTitel.gridy = 3;
			contentPanel.add(btNeuerTitel, gbc_btNeuerTitel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == 2) {
			if(check_Orchester()) {
				set_OrchesterOK(true);
			}
			else {
				set_OrchesterOK(false);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == this.btNeuerTitel) {
			try {
				TitelAnlegen();
			} catch (NumberFormatException | ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == this.txtTitel) {
			this.sysHandler.pruefeAufSonderzeichen(txtTitel.getText());
			set_TitelOK(true);
		}
		if(e.getSource() == txtNummer) {
			if(check_Nummerzeichen(txtNummer.getText())) {
				set_NummerOK(true);
			}
			else {
				set_NummerOK(false);
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	private void set_TitelOK(Boolean b) {
		if(b) {
			txtTitel.setBackground(Color.white);
			this.TitelOK = true;
		}
		else {
			txtTitel.setBackground(Color.red);
			this.TitelOK = false;
		}
	}
	private void set_NummerOK(Boolean b) {
		if(b) {
			txtNummer.setBackground(Color.white);
			this.NummerOK = true;
		}
		else {
			txtNummer.setBackground(Color.red);
			this.NummerOK = false;
		}
	}
	private void set_OrchesterOK(Boolean b) {
		if(b) {
			this.cb_Orchester.setBackground(Color.white);
			this.OrchesterOK = true;
		}
		else {
			this.cb_Orchester.setBackground(Color.RED);
			this.OrchesterOK = false;
		}
	}
	
	private void TitelAnlegen() throws NumberFormatException, ClassNotFoundException, SQLException {
		if(this.NummerOK &&
				this.TitelOK &&
				this.OrchesterOK) {
			
			this.Titel = new MyTitle(this.sql, this.xml, Integer.parseInt(this.txtNummer.getText()), this.txtTitel.getText(), this.cb_Orchester.getSelectedItem().toString(), this.ActivePassive);
		}
	}
}
