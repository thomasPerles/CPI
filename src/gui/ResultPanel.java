package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ResultPanel extends JPanel {

	private JLabel resutlMessage;
	private JFrame frame;
	
	public ResultPanel(JFrame frame, String msg) {
		super();
		this.frame = frame;
		setLayout(null);
		resutlMessage = new JLabel(msg, SwingConstants.CENTER);
		resutlMessage.setBounds(10, 93, 430, 40);
		this.add(resutlMessage);
		JButton closeButton = new JButton("Close");
		closeButton.setBounds(172, 173, 101, 40);
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});
		this.add(closeButton);
	}
}
