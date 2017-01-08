package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import model.ImageModel;

@SuppressWarnings("serial")
public class ImageView extends Canvas implements Runnable {

	private ImageModel model;
	private Rectangle selectedRect;
	private boolean selectingRectangles;
	private ArrayList<Rectangle> rectangles;
	private ArrayList<Rectangle> preparedRectangles;

	public Rectangle getSelectedRect() {
		return selectedRect;
	}

	public void setSelectedRect(Rectangle selectedRect) {
		this.selectedRect = selectedRect;
	}

	public boolean isSelectingRectangles() {
		return selectingRectangles;
	}

	public ArrayList<Rectangle> getPreparedRectangles()
	{
		return preparedRectangles;
	}

	public void setSelectingRectangles(boolean selectingRectangles) {
		this.selectingRectangles = selectingRectangles;
	}

	public ImageView(ImageModel model) {
		this.model = model;
		this.selectingRectangles = false;
		this.rectangles = new ArrayList<Rectangle>();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(model.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);

		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();) {
			Rectangle next = it.next();
			if (next != null && next != selectedRect) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(next.x, next.y, next.width, next.height);
			}
		}

		if (selectedRect != null) {
			if (selectingRectangles)
				g.setColor(Color.ORANGE);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.fillRect(selectedRect.x, selectedRect.y, selectedRect.width, selectedRect.height);
		}

	}

	public void setModel(ImageModel model) {
		this.model = model;
	}
	
	@Override
	public void run() {
		/*
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, model.getImage().getWidth(), model.getImage().getHeight()));
		f.setMinimumSize(new Dimension(model.getImage().getWidth(), model.getImage().getHeight()));
		f.add(this);
		f.setAlwaysOnTop(true);
		f.setLocationRelativeTo(this);
		f.setVisible(true);
		*/
	}

	
	/**
	 * Affiche les rectangles selectionnes et existants
	 * @param selectedRect
	 * Rectangle : rectangle selectionne
	 * @param selectingRectangles
	 * Boolean : indique si le rectangle est en train d'etre cree ou s'il est selectionne
	 */
	public void paintRectangles(Rectangle selectedRect, Boolean selectingRectangles) {
		this.selectedRect = selectedRect;
		
		if (selectingRectangles != this.selectingRectangles)
		{
			this.selectingRectangles = selectingRectangles;
		}
		
		if(selectingRectangles)
		{
			ArrayList<Rectangle> temp = new ArrayList<Rectangle>();
			for(Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();)
			{
				Rectangle rectangle = it.next();
				if(rectangle != selectedRect)
					temp.add(rectangle);
			}
			temp.add(selectedRect);
			this.rectangles = temp;
		}
		repaint();
	}

	public void incorporateRectangle() {
		this.rectangles.add(selectedRect);
		this.selectedRect = null;
	}

	public void remove(Rectangle selectedRect) throws Exception {
		if (this.rectangles.contains(selectedRect))
			this.rectangles.remove(selectedRect);
		else
			throw new Exception("Cannot remove non-existent rectangle!");
	}
	/*
	 * public void setResizingFactorX(int resizedWidth) {
	 * 
	 * double resizeFactorX = (double) oldWidth / (double) resizedWidth;
	 * 
	 * for (Iterator<Rectangle> it = this.rectangles.iterator(); it.hasNext();)
	 * { Rectangle rectangle = it.next();
	 * 
	 * rectangle.setBounds((int) (rectangles.get(rectangle).x * resizeFactorX),
	 * rectangle.y, (int) (rectangles.get(rectangle).width * resizeFactorX),
	 * rectangle.height); } System.out.println(rectangles); }
	 * 
	 * public void setResizingFactorY(int resizedHeight) {
	 * 
	 * double resizeFactorY = (double) oldHeight / (double) resizedHeight;
	 * 
	 * for (Iterator<Rectangle> it = this.rectangles.iterator(); it.hasNext();)
	 * { Rectangle rectangle = it.next();
	 * 
	 * rectangle.setBounds(rectangle.x, (int) (rectangles.get(rectangle).y *
	 * resizeFactorY), rectangle.width, (int) (rectangles.get(rectangle).height
	 * * resizeFactorY));
	 * 
	 * } System.out.println(rectangles); }
	 */

	/**
	 * Affiche l'etat des rectangles par rapport a l'ecran
	 */
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();) {
			Rectangle rectangle = it.next();
			res.append("Rectangle : ").append(rectangle).append('\n');
		}
		return res.toString();
	}
	
	/**
	 * Affiche la liste des rectangles adaptes aux dimensions de la fenetre
	 * @return
	 */
	public String preparedRectanglesToString() {
		StringBuilder res = new StringBuilder();
		for(Iterator<Rectangle> it = preparedRectangles.iterator(); it.hasNext();)
		{
			Rectangle rectangle = it.next();
			res.append("Prepared Rectangle : ").append(rectangle).append('\n');
		}
		return res.toString(); 
	}

	public void setRectangles(ArrayList<Rectangle> rectangles) {
		this.rectangles = rectangles;
	}

	public ArrayList<Rectangle> getRectangles() {
		return this.rectangles;
	}

	/**
	 * Adapte les rectangles en fonction de la taille de la fenetre
	 */
	public void prepareRectangles() {
		
		//System.out.println(toString());
		
		preparedRectangles = new ArrayList<Rectangle>();
		Rectangle preparedRectangle = null;

		double resizeFactorX = (double) this.model.getImage().getWidth() / (double) this.getWidth();
		double resizeFactorY = (double) this.model.getImage().getHeight() / (double) this.getHeight();
		
		//System.out.println("Resize factor x : " + resizeFactorX);
		//System.out.println("Resize factor y : " + resizeFactorY);

		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();) {
			Rectangle rectangle = it.next();
			preparedRectangle = new Rectangle();
			preparedRectangle.setBounds((int) (rectangle.x * resizeFactorX), (int) (rectangle.y * resizeFactorY),
					(int) (rectangle.width * resizeFactorX), (int) (rectangle.height * resizeFactorY));
			
			preparedRectangles.add(preparedRectangle);
		}
		
		//System.out.println(preparedRectanglesToString());
	}
}
