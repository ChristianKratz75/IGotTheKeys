//package IGotTheKeys;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class Message extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();

	private JButton
		okButton,
		cancelButton;
	private final JLabel lbMessageTyp = new JLabel("Error:");
	private final JTextArea txtErrorField = new JTextArea();
	/**
	 * Create the dialog.
	 */
	public Message(String Typ, String Msg) {
		ini_Dialog();
		this.lbMessageTyp.setText(Typ);
		{
			contentPanel.add(txtErrorField, BorderLayout.CENTER);
		}
	}
	private void ini_Dialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			contentPanel.add(lbMessageTyp, BorderLayout.NORTH);
		}
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
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "OK") {
			this.dispose();
		}
	}

}
