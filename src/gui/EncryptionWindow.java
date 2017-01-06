package gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Main;
import model.ImageModel;
import view.ImageView;

public class EncryptionWindow {

	private JFrame frame;
	private JTextField passwordTextField;
	private ImageView view;
	private ArrayList<Rectangle> rectangles;
	private String  password;

	public void setVisible(boolean state) {
		frame.setVisible(state);
	}

	/**
	 * Create the application.
	 * @param view 
	 */
	public EncryptionWindow(ImageView view) {
		this.view = view;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Encryption");
		frame.setSize(300, 50);
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);

		JLabel passwordLabel = new JLabel("Enter password :");
		passwordLabel.setBounds(31, 83, 138, 36);
		frame.getContentPane().add(passwordLabel);

		passwordTextField = new JTextField();
		passwordTextField.setBounds(193, 83, 208, 36);
		frame.getContentPane().add(passwordTextField);
		passwordTextField.setColumns(10);

		JButton encryptButton = new JButton("Encrypt");
		encryptButton.setBounds(251, 142, 105, 36);
		frame.getContentPane().add(encryptButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(251, 189, 105, 36);
		frame.getContentPane().add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		encryptButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(passwordTextField.getText() != null)
					password = passwordTextField.getText();
				
				ImageModel  imageModel;
				
				// Créer le fichier json
				if(Main.model != null) {
					imageModel = Main.model;
					String filename = imageModel.getImage().toString();
					System.out.println(filename);
				}
				
				// TODO
						 
				// Recuperer les rectangles de la vue
				rectangles = view.getRectangles();
				
				// Recuperer un emplacement commun a plusieurs os et l'utiliser pour le stockage de donnees
				String location = System.getProperty("user.home");
				File file = new File(location, "Test.txt");
				
				try {
					// Ecrire des octets dans un fichier
					// FileOutputStream fout = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(file);
					
					pw.write("test");
					pw.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Gestion du mot de passe
				/*
				if (passwordTextField.getText().equals("password"))
					frame.setContentPane(new ResultPanel(frame, "Encryption successed"));
				else
					frame.setContentPane(new ResultPanel(frame, "Encryption failed"));
				frame.revalidate();
				*/
			}
		});
	}
}
