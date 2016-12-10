package view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.ImageLoaderTL;

@SuppressWarnings("serial")
public class ImageDisplay extends Canvas implements Runnable {

	private ImageLoaderTL iTL;

	public ImageDisplay(ImageLoaderTL iTL) {
		this.iTL = iTL;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(iTL.getImage(), 0, 0, this);
	}

	public ImageLoaderTL getModel() {
		return iTL;
	}

	public void setModel(ImageLoaderTL iTL) {
		this.iTL = iTL;
	}
	
	@Override
	public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, iTL.getImage().getWidth(), iTL.getImage().getHeight()));
		f.setMinimumSize(new Dimension(iTL.getImage().getWidth(), iTL.getImage().getHeight()));
		f.add(this);
		f.setAlwaysOnTop(true);
		f.setLocationRelativeTo(this);
		f.setVisible(true);
	}

}
