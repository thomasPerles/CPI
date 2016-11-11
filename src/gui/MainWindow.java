package gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JFrame;
import java.awt.Component;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import javax.swing.JButton;

public class MainWindow
{

	private JFrame frame;

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
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 987, 742);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setBounds(0, 436, 969, 160);
		frame.getContentPane().add(horizontalStrut);
		
		JButton btnLoadImage = new JButton("Load Image");
		btnLoadImage.setBounds(421, 596, 115, 25);
		frame.getContentPane().add(btnLoadImage);
		
		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.setBounds(725, 557, 97, 25);
		frame.getContentPane().add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.setBounds(725, 609, 97, 25);
		frame.getContentPane().add(btnDecrypt);
		
		JButton btnNewButton = new JButton();
		try {
		    Image img = ImageIO.read(getClass().getResource("res/select.bmp"));
		    btnNewButton.setIcon(new ImageIcon(img));
		  } catch (IOException ex) {
		  }
		btnNewButton.setBounds(83, 540, 97, 25);
		frame.getContentPane().add(btnNewButton);
	}
}
