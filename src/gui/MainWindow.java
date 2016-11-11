package gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class MainWindow
{

	private JFrame frame;
	//final JFileChooser fc = new JFileChooser();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void updateImage(JLabel lblImage, JFrame frame, File fichier){
		ImageIcon image = new ImageIcon(fichier.toString());
		lblImage.setIcon(image);
		frame.repaint();
	}
	
	private void initialize()
	{
		frame = new JFrame("PIE - Partial Image Encryption");
		frame.setBounds(100, 100, 987, 742);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


        ImageIcon image = new ImageIcon("res/select.png");
        JLabel lblImage = new JLabel(image);
		lblImage.setBounds(41, 35, 883, 439);
		frame.getContentPane().add(lblImage);

		
		JButton btnLoadImage = new JButton("Load Image");
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser dialogue = new JFileChooser(new File("."));
				PrintWriter sortie = null;
				File fichier;
				
				if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
				    fichier = dialogue.getSelectedFile();
				    try
					{
						sortie = new PrintWriter(new FileWriter(fichier.getPath(), true));
					}
					catch (IOException e)
					{
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
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncryptionWindow ew = new EncryptionWindow();
				ew.setVisible(true);
			}
		});
		btnEncrypt.setBounds(725, 557, 97, 25);
		frame.getContentPane().add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Decrypt");
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
		JButton btnNewZones = new JButton(createIcon);
		btnNewZones.setToolTipText("Create new zones");
		btnNewZones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewZones.setBounds(117, 512, 50, 38);
		frame.getContentPane().add(btnNewZones);
		
		JButton btnSelectAll = new JButton("Select All");
		/*
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		*/
		btnSelectAll.setToolTipText("Select all zones");
		btnSelectAll.setBounds(93, 621, 97, 25);
		frame.getContentPane().add(btnSelectAll);
		
		//Button to select zones
		Icon mousePointer = new ImageIcon("res/mouse.png");
		JButton btnSelectZones = new JButton(mousePointer);
		btnSelectZones.setToolTipText("Select zones");
		btnSelectZones.setBounds(117, 570, 50, 38);
		frame.getContentPane().add(btnSelectZones);
		
		
	}
}
