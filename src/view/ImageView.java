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
public class ImageView extends Canvas implements Runnable
{

	private ImageModel model;
	private ArrayList<Rectangle> rectangles;
	private Rectangle selectedRect;
	private boolean selectingRectangles;
	private double resizingFactorX;
	private double resizingFactorY;

	public Rectangle getSelectedRect()
	{
		return selectedRect;
	}

	public void setSelectedRect(Rectangle selectedRect)
	{
		this.selectedRect = selectedRect;
	}

	public boolean isSelectingRectangles()
	{
		return selectingRectangles;
	}

	public void setSelectingRectangles(boolean selectingRectangles)
	{
		this.selectingRectangles = selectingRectangles;
	}

	public ImageView(ImageModel model)
	{
		this.model = model;
		this.rectangles = new ArrayList<Rectangle>();
		this.selectingRectangles = false;
		this.resizingFactorX = 1;
		this.resizingFactorY = 1;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(model.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);

		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();)
		{
			Rectangle next = it.next();
			if (next != null && next != selectedRect)
			{
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(next.x, next.y, next.width, next.height);
			}
		}

		if (selectedRect != null)
		{
			if (selectingRectangles)
				g.setColor(Color.ORANGE);
			else
				g.setColor(Color.LIGHT_GRAY);
			g.fillRect(selectedRect.x, selectedRect.y, selectedRect.width, selectedRect.height);
		}

	}

	public void setModel(ImageModel model)
	{
		this.model = model;
	}

	@Override
	public void run()
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(new Rectangle(0, 0, model.getImage().getWidth(), model.getImage().getHeight()));
		f.setMinimumSize(new Dimension(model.getImage().getWidth(), model.getImage().getHeight()));
		f.add(this);
		f.setAlwaysOnTop(true);
		f.setLocationRelativeTo(this);
		f.setVisible(true);
	}

	/*
	 * Paint existing rectangles and selected rectangle.
	 * 
	 * Boolean parameter determines whether selected rectangle is being created
	 * or has been selected, in which case its color will be Color.ORANGE.
	 */
	public void paintRectangles(Rectangle selectedRect, Boolean selectingRectangles)
	{
		this.selectedRect = selectedRect;
		if (selectingRectangles != this.selectingRectangles)
			this.selectingRectangles = selectingRectangles;
		repaint();
	}

	/*
	 * Paint selected rectangles of array in Color.GRAY, others in
	 * Color.LIGHT_GRAY.
	 */
	public void paintRectangles(ArrayList<Rectangle> rectanglesToSelect)
	{
		this.selectingRectangles = true;
		// TODO
	}

	public void incorporateRectangle()
	{
		this.rectangles.add(this.selectedRect);
		this.selectedRect = null;
	}

	public ArrayList<Rectangle> getRectangles()
	{
		return this.rectangles;
	}

	public void remove(Rectangle selectedRect) throws Exception
	{
		if (this.rectangles.contains(selectedRect))
			this.rectangles.remove(selectedRect);
		else
			throw new Exception("Cannot remove non-existent rectangle!");
	}

	public void setRectangles(ArrayList<Rectangle> rectangles)
	{
		this.rectangles = rectangles;
		if (rectangles == null)
		{
			this.selectedRect = null;
			this.selectingRectangles = false;
		}
	}

	/**
	 * 
	 * @param resizingFactorX
	 */
	public void setResizingFactorX(int resizedWidth)
	{
		int imageWidth = model.getImage().getWidth();

		double resizeFactorX = (double) imageWidth / (double) resizedWidth;

		for (Iterator<Rectangle> it = this.rectangles.iterator(); it.hasNext();)
		{
			Rectangle rectangle = it.next();
			rectangle.x = (int) (rectangle.x * resizeFactorX);
			rectangle.width = (int) (rectangle.width * resizeFactorX);
			System.out.println(rectangle);
		}
	}

	public void setResizingFactorY(int resizedHeight)
	{
		int imageHeight = model.getImage().getHeight();

		double resizeFactorY = (double) imageHeight / (double) resizedHeight;

		for (Iterator<Rectangle> it = this.rectangles.iterator(); it.hasNext();)
		{
			Rectangle rectangle = it.next();
			rectangle.y = (int) (rectangle.y * resizeFactorY);
			rectangle.height = (int) (rectangle.height * resizeFactorY);
			System.out.println(rectangle);
		}
	}

	public double getResizingFactorX()
	{
		return this.resizingFactorX;
	}

	public double getResizingFactorY()
	{
		return this.resizingFactorY;
	}
}
