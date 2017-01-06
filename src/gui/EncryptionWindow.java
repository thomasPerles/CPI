package gui;

import java.awt.Rectangle;
import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Main;
import model.ImageModel;
import model.ImageModelJSON;
import view.ImageView;

public class EncryptionWindow {

	private JFrame frame;
	private JTextField passwordTextField;
	private ImageView view;
	private ArrayList<Rectangle> rectangles;
	private String  password;
	private ImageModelJSON imageModelJSON;;

	public void setVisible(boolean state) {
		frame.setVisible(state);
	}

	/**
	 * Create the application.
	 * @param view 
	 */
	public EncryptionWindow(ImageView view) {
		this.view = view;
		this.imageModelJSON = new ImageModelJSON();
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
					
					//String filename = imageModel.getImage().;
					BufferedImage image = imageModel.getImage();
					
					// pxels en RGB originaux
					int [][] rgbs = getRGB(image);
					//System.out.println(rgbs);
					
					encription(rgbs, image);
					
					// recrée l'image avec les RGB originaux
					File outputfile = new File("saved.jpg");
				    try {
						ImageIO.write(createBufferedImage(rgbs, image.getWidth(), image.getHeight()), "jpg", outputfile);
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					try {
						imageModelJSON.writeImageModelJSONFile(Main.filePath, Main.fileName, password);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
	
	protected void encription(int[][] rgbs, BufferedImage image) {
		
		
	}

	public static int[][] getRGB(BufferedImage image) {
		System.out.println(image.getHeight() + " " + image.getWidth());
		//System.out.println("rgb 1, 1 = " + image.getRGB(1, 1));
		int[][] rgbs = new int[image.getWidth()][image.getHeight()];
		for(int i = 0; i < image.getWidth(); i++){
		    for(int j = 0; j < image.getHeight(); j++){
		    	System.out.println("i = " + i + " j = " + j + " RGB = " + image.getRGB(i, j));
		        rgbs[i][j] = image.getRGB(i, j);
		    }
		}
		return rgbs;
	}
	
	public static BufferedImage createBufferedImage(int[][] rgbs, int width, int height) {
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < width; i++){
		    for(int j = 0; j < height; j++){
		    	//System.out.println("i = " + i + " j = " + j + " RGB = " + image.getRGB(i, j));
		        buff.setRGB(i, j, rgbs[i][j]);
		    }
		}
		return buff;
		
		/*
		 File outputfile = new File("saved.png");
    ImageIO.write(bi, "png", outputfile);
		 */
	}
}
