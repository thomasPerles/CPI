package gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;


import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;

public class MainWindow
{

	private JFrame frame;
	private JToggleButton btnNewZones;
	private JToggleButton btnSelectZones;
	private JButton btnSelectAll;
	private JButton btnLoadImage;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	//final JFileChooser fc = new JFileChooser();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow(){
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	
	private void allowButton(){
		btnNewZones.setEnabled(true);
		btnSelectZones.setEnabled(true);
		btnSelectAll.setEnabled(true);		
	}
	
	private void disableButton(){
		btnNewZones.setEnabled(false);
		btnSelectZones.setEnabled(false);
		btnSelectAll.setEnabled(false);
	}
	
	private void updateImage(JLabel lblImage, JFrame frame, File fichier){
		ImageIcon imageIcon = new ImageIcon(fichier.toString());
		if (imageIcon.getIconWidth() > 883){
			float height = imageIcon.getIconHeight();
			float width = imageIcon.getIconWidth();
			Image image = imageIcon.getImage(); // transform it  
			Image newimg = image.getScaledInstance(883, (int)(height*(883/width)),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
		}
		if (imageIcon.getIconHeight() > 439){
			float height = imageIcon.getIconHeight();
			float width = imageIcon.getIconWidth();
			Image image = imageIcon.getImage(); // transform it  
			Image newimg = image.getScaledInstance((int)(width*(439/height)), 439,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
		}
		
		if (imageIcon.getIconHeight() > 0){
			allowButton();
		}else{
			imageIcon = new ImageIcon("res/bg.gif");
			disableButton();
		}
		
		lblImage.setIcon(imageIcon);
		frame.repaint();	
	}
	
	private void initialize(){
		frame = new JFrame("PIE - Partial Image Encryption");
		frame.setBounds(100, 100, 987, 742);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


        ImageIcon image = new ImageIcon("res/bg.gif");
        JLabel lblImage = new JLabel(image);
		lblImage.setBounds(41, 35, 883, 439);
		lblImage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		frame.getContentPane().add(lblImage);

		
		
		
		JButton btnLoadImage = new JButton("Load Image");
		this.btnLoadImage = btnLoadImage;
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser dialogue = new JFileChooser(new File("."));
				PrintWriter sortie = null;
				File fichier;
				
				if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
				    fichier = dialogue.getSelectedFile();
				    try{
						sortie = new PrintWriter(new FileWriter(fichier.getPath(), true));
					}
					catch (IOException e){
						e.printStackTrace();
					}
				    sortie.close();
				    updateImage(lblImage, frame, fichier);
				}
			}	
		});
		btnLoadImage.setBounds(421, 596, 115, 25);
		frame.getContentPane().add(btnLoadImage);
		
		
		
		JButton btnEncrypt = new JButton("Encrypt");
		this.btnEncrypt = btnEncrypt;
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncryptionWindow ew = new EncryptionWindow();
				ew.setVisible(true);
			}
		});
		btnEncrypt.setBounds(725, 557, 97, 25);
		frame.getContentPane().add(btnEncrypt);
		
		
		
		JButton btnDecrypt = new JButton("Decrypt");
		this.btnDecrypt = btnDecrypt;
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DecryptionWindow dw = new DecryptionWindow();
				dw.setVisible(true);
			}
		});
		btnDecrypt.setBounds(725, 609, 97, 25);
		frame.getContentPane().add(btnDecrypt);
		
		
		
		//Button to create zones 
		Icon createIcon = new ImageIcon("res/select.png");
		JToggleButton btnNewZones = new JToggleButton(createIcon);
		this.btnNewZones = btnNewZones;
		btnNewZones.setToolTipText("Create new zones");
		btnNewZones.setEnabled(false);
		btnNewZones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewZones.setBounds(117, 512, 50, 38);
		frame.getContentPane().add(btnNewZones);
		
		
		JButton btnSelectAll = new JButton("Select All");
		this.btnSelectAll = btnSelectAll;
		/*
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		*/
		btnSelectAll.setToolTipText("Select all image");
		btnSelectAll.setBounds(93, 621, 97, 25);
		btnSelectAll.setEnabled(false);
		frame.getContentPane().add(btnSelectAll);
		
		
		
		//Button to select zones
		Icon mousePointer = new ImageIcon("res/mouse.png");
		// JButton btnSelectZones = new JButton(mousePointer);
		JToggleButton btnSelectZones = new JToggleButton(mousePointer);
		this.btnSelectZones = btnSelectZones;
		btnSelectZones.setToolTipText("Select zones");
		btnSelectZones.setBounds(117, 570, 50, 38);
		btnSelectZones.setEnabled(false);
		frame.getContentPane().add(btnSelectZones);
	
		
	}
}
