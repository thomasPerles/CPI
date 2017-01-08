package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import controller.WindowController;
import main.Main;
import model.ImageModel;
import view.ImageView;

public class MainWindow {

	private JFrame frame;
	private JPanel buttons;
	private JPanel imagePortal;
	private JToggleButton btnNewZones;
	private JToggleButton btnSelectZones;
	private JButton btnSelectAll;
	// private JButton btnLoadImage;
	// private JButton btnEncrypt;
	// private JButton btnDecrypt;

	private ImageModel model;
	private ImageView view;
	private WindowController wcontroller;
	
	private String fileName, path;

	private final int xOffset = 25;
	private final int yOffset = 25;

	/**
	 * Create the application.
	 */
	public MainWindow(ImageModel model, ImageView view, WindowController wcontroller) {
		this.model = model;
		this.view = view;
		this.wcontroller = wcontroller;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void allowButtons() {
		btnNewZones.setEnabled(true);
		btnSelectZones.setEnabled(true);
		btnSelectAll.setEnabled(true);
	}

	private void disableButtons() {
		btnNewZones.setEnabled(false);
		btnSelectZones.setEnabled(false);
		btnSelectAll.setEnabled(false);
	}

	/*
	 * Vérifie les dimensions de l'image chargée dans la vue et adapte
	 * l'affichage
	 */
	private void checkBounds() {
		if(model.getImage() != null)
		{
			int original_width = model.getImage().getWidth();
			int original_height = model.getImage().getHeight();
			int bound_width = this.imagePortal.getWidth() - 2 * this.xOffset;
			int bound_height = this.imagePortal.getHeight() - 2 * this.yOffset;
			int new_width = original_width;
			int new_height = original_height;

			// first check if we need to scale width
			if (original_width > bound_width) {
				// scale width to fit
				new_width = bound_width;
				// scale height to maintain aspect ratio
				new_height = (new_width * original_height) / original_width;
			}

			// then check if we need to scale even with the new height
			if (new_height > bound_height) {
				// scale height to fit instead
				new_height = bound_height;
				// scale width to maintain aspect ratio
				new_width = (new_height * original_width) / original_height;
			}
			
			this.view.setBounds((int) ((double) this.imagePortal.getWidth() / 2 - (double) new_width / 2),
					(int) ((double) this.imagePortal.getHeight() / 2 - (double) new_height / 2), new_width,
					new_height);
		}
	}

	private void updateImageModel(File file) {
		
		this.view.setRectangles(new ArrayList<Rectangle>());
		this.model.loadByPixel(file.getAbsolutePath());
		checkBounds();
		this.view.repaint();

		/*
		 * ImageIcon imageIcon = new ImageIcon(fichier.toString()); if
		 * (imageIcon.getIconWidth() > view.getWidth()) { float height =
		 * imageIcon.getIconHeight(); float width = imageIcon.getIconWidth();
		 * Image image = imageIcon.getImage(); // transform it Image newimg =
		 * image.getScaledInstance(view.getWidth(), (int) (height *
		 * (view.getWidth() / width)), java.awt.Image.SCALE_SMOOTH); // scale //
		 * way imageIcon = new ImageIcon(newimg); // transform it back } if
		 * (imageIcon.getIconHeight() > view.getHeight()) { float height =
		 * imageIcon.getIconHeight(); float width = imageIcon.getIconWidth();
		 * Image image = imageIcon.getImage(); // transform it Image newimg =
		 * image.getScaledInstance((int) (width * (view.getHeight() / height)),
		 * view.getHeight(), java.awt.Image.SCALE_SMOOTH); // scale // way
		 * imageIcon = new ImageIcon(newimg); // transform it back }
		 * 
		 * if (imageIcon.getIconHeight() > 0) { allowButton(); } else {
		 * imageIcon = new ImageIcon("res/bg.gif"); disableButton(); }
		 * 
		 * 
		 * lblImage.setIcon(imageIcon);
		 * 
		 * frame.repaint();
		 */
	}

	public void addContentsToPane() {
		
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
		this.imagePortal = new JPanel();
		this.imagePortal.add(this.view);
		pane.add(imagePortal, BorderLayout.CENTER);
		this.buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 6));
		
		JButton btnLoadImage = new JButton("Load Image");
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser dialogue = new JFileChooser(new File("."));
				PrintWriter pw = null;
				File file;

				if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = dialogue.getSelectedFile();
					try {
						pw = new PrintWriter(new FileWriter(file.getPath(), true));
						allowButtons();
					} catch (IOException e) {
					}
					pw.close();
					
					// getting filePath and fileName and give it to Encription window
					path = file.getAbsolutePath();
					fileName = file.getName();
					updateImageModel(file);
				}
			}
		});
		buttons.add(btnLoadImage);

		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EncryptionWindow ew = new EncryptionWindow(view, fileName, path, model);
				ew.setVisible(true);
			}
		});
		buttons.add(btnEncrypt);

		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//DecryptionWindow dw = new DecryptionWindow();
				DecryptionWindow dw = new DecryptionWindow(view, fileName, path, model);
				dw.setVisible(true);
			}
		});
		buttons.add(btnDecrypt);

		// Button to create zones
		Icon createIcon = new ImageIcon("res/select.png");
		JToggleButton btnNewZone = new JToggleButton(createIcon);
		this.btnNewZones = btnNewZone;
		btnNewZone.setToolTipText("Create new zones");
		btnNewZone.setEnabled(false);
		buttons.add(btnNewZone);
		this.wcontroller.addComponent("newZone", btnNewZone);
		btnNewZone.addItemListener(this.wcontroller);

		JButton btnSelectAll = new JButton("Select All");
		this.btnSelectAll = btnSelectAll;
		btnSelectAll.setToolTipText("Select all image");
		btnSelectAll.setEnabled(false);
		buttons.add(btnSelectAll);

		// Button to select zones
		Icon mousePointer = new ImageIcon("res/mouse.png");
		JToggleButton btnSelectZone = new JToggleButton(mousePointer);
		this.btnSelectZones = btnSelectZone;
		btnSelectZone.setToolTipText("Select zones");
		btnSelectZone.setEnabled(false);
		buttons.add(btnSelectZone);
		this.wcontroller.addComponent("selectZone", btnSelectZone);
		btnSelectZone.addItemListener(this.wcontroller);
		
		pane.add(buttons, BorderLayout.SOUTH);
		
	}

	private void initialize() {

		setFrame(new JFrame("PIE - Partial Image Encryption"));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setMaximumSize(dim);
		frame.setSize(dim);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addContentsToPane();
		frame.setResizable(true);
		frame.addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		        checkBounds();        
		    }

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
