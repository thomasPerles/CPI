package view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;

import controller.ImageController;
import model.ImageModel;

@SuppressWarnings("serial")
public class ImageView extends Canvas implements Runnable {

    private ImageModel model;
    private ImageController imageController;

    public ImageView(ImageModel model) {
	this.model = model;
    }

    @Override
    public void paint(Graphics g) {
	super.paint(g);
	g.drawImage(model.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setModel(ImageModel model) {
	this.model = model;
    }

    public void setController(ImageController imageController) {
	this.imageController = imageController;
    }

    @Override
    public void run() {
	JFrame f = new JFrame();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setBounds(new Rectangle(0, 0, model.getImage().getWidth(), model.getImage().getHeight()));
	f.setMinimumSize(new Dimension(model.getImage().getWidth(), model.getImage().getHeight()));
	f.add(this);
	f.setAlwaysOnTop(true);
	f.setLocationRelativeTo(this);
	f.setVisible(true);
    }

}
