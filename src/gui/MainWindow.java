package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToggleButton;

import controller.ImageController;
import controller.WindowController;
import model.ImageModel;
import view.ImageView;

public class MainWindow {

    private JFrame frame;
    private JToggleButton btnNewZones;
    private JToggleButton btnSelectZones;
    private JButton btnSelectAll;
    // private JButton btnLoadImage;
    // private JButton btnEncrypt;
    // private JButton btnDecrypt;

    private ImageModel model;
    private ImageView view;
    private WindowController wcontroller;

    private final int xOffset = 25;
    private final int yOffset = 25;

    private final int buttonWidth = 4 * this.xOffset;
    private final int buttonHeight = 2 * this.yOffset;

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
	final int buttonsY = frame.getHeight() - 5 * this.yOffset;
	int width = this.model.getImage().getWidth();
	int height = this.model.getImage().getHeight();
	if (this.model.getImage().getWidth() > frame.getWidth() - 2 * this.xOffset)
	    width = frame.getWidth() - 2 * this.xOffset;
	if (this.model.getImage().getHeight() > buttonsY)
	    height = buttonsY;
	this.view.setBounds((int) ((frame.getWidth() / 2.0) - (width / 2.0)),
		(int) ((frame.getHeight() / 2.0) - (height / 2.0)), width, height);
    }

    private void updateImageModel(File file) {

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

    private void initialize() {

	setFrame(new JFrame("PIE - Partial Image Encryption"));
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setMaximumSize(dim);
	frame.setSize(dim);
	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);

	final int buttonY = frame.getHeight() - 5 * this.yOffset;

	frame.getContentPane().add(view);

	JButton btnLoadImage = new JButton("Load Image");
	// this.btnLoadImage = btnLoadImage;
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
		    updateImageModel(file);
		}
	    }
	});
	btnLoadImage.setBounds(10 * this.xOffset, buttonY, buttonWidth, buttonHeight);
	frame.getContentPane().add(btnLoadImage);

	JButton btnEncrypt = new JButton("Encrypt");
	// this.btnEncrypt = btnEncrypt;
	btnEncrypt.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		EncryptionWindow ew = new EncryptionWindow();
		ew.setVisible(true);
	    }
	});
	btnEncrypt.setBounds((int) (10 * this.xOffset + (4.0 / 10.0) * frame.getWidth()), buttonY, buttonWidth,
		buttonHeight);
	frame.getContentPane().add(btnEncrypt);

	JButton btnDecrypt = new JButton("Decrypt");
	// this.btnDecrypt = btnDecrypt;
	btnDecrypt.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		DecryptionWindow dw = new DecryptionWindow();
		dw.setVisible(true);
	    }
	});
	btnDecrypt.setBounds((int) (10 * this.xOffset + (5.0 / 10.0) * frame.getWidth()), buttonY, buttonWidth,
		buttonHeight);
	frame.getContentPane().add(btnDecrypt);

	// Button to create zones
	Icon createIcon = new ImageIcon("res/select.png");
	JToggleButton btnNewZone = new JToggleButton(createIcon);
	this.btnNewZones = btnNewZone;
	btnNewZone.setToolTipText("Create new zones");
	btnNewZone.setEnabled(false);
	btnNewZone.setBounds((int) (10 * this.xOffset + (1.0 / 10.0) * frame.getWidth()), buttonY, 2 * this.xOffset,
		buttonHeight);
	frame.getContentPane().add(btnNewZone);
	this.wcontroller.addComponent("newZone", btnNewZone);
	btnNewZone.addItemListener(this.wcontroller);

	JButton btnSelectAll = new JButton("Select All");
	this.btnSelectAll = btnSelectAll;
	/*
	 * btnSelectAll.addActionListener(new ActionListener() { public void
	 * actionPerformed(ActionEvent arg0) { } });
	 */
	btnSelectAll.setToolTipText("Select all image");
	btnSelectAll.setBounds((int) (10 * this.xOffset + (3.0 / 10.0) * frame.getWidth()), buttonY, buttonWidth,
		buttonHeight);
	btnSelectAll.setEnabled(false);
	frame.getContentPane().add(btnSelectAll);

	// Button to select zones
	Icon mousePointer = new ImageIcon("res/mouse.png");
	JToggleButton btnSelectZone = new JToggleButton(mousePointer);
	this.btnSelectZones = btnSelectZone;
	btnSelectZone.setToolTipText("Select zones");
	btnSelectZone.setBounds((int) (10 * this.xOffset + (2.0 / 10.0) * frame.getWidth()), buttonY, 2 * this.xOffset,
		buttonHeight);
	btnSelectZone.setEnabled(false);
	frame.getContentPane().add(btnSelectZone);
	this.wcontroller.addComponent("selectZone", btnSelectZone);
	btnSelectZone.addItemListener(this.wcontroller);

	frame.setResizable(false);
    }

    public JFrame getFrame() {
	return frame;
    }

    public void setFrame(JFrame frame) {
	this.frame = frame;
    }
}
