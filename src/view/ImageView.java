package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

import controller.ImageController;
import model.ImageModel;

@SuppressWarnings("serial")
public class ImageView extends Canvas implements Runnable {

	private ImageModel model;
	private ArrayList<Rectangle> rectangles;
	private Rectangle selectedRect;

	public ImageView(ImageModel model) {
		this.model = model;
		this.rectangles = new ArrayList<Rectangle>();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(model.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
		
		if(selectedRect != null)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(selectedRect.x, selectedRect.y, selectedRect.width, selectedRect.height);
		}
		
		for(Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();)
		{
			Rectangle next = it.next();
			if(next != null)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(next.x, next.y, next.width, next.height);
			}
		}
			
	}

	public void setModel(ImageModel model) {
		this.model = model;
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

	public void paintRectangle(Rectangle selectedRect) {
		this.selectedRect = selectedRect;
		repaint();
	}
	
	public void incorporateRectangle() {
		this.rectangles.add(this.selectedRect);
	}

}
