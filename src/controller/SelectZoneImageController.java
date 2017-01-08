package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import view.ImageView;

public class SelectZoneImageController extends ImageController {

	private Rectangle selectedRect;
	private Point oldPoint;
	private ArrayList<Rectangle> rectangles;

	public SelectZoneImageController() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		rectangles = view.getRectangles();
		Boolean rectangleSelected = false;
		for(Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();)
		{
			Rectangle next = it.next();
			if (next.contains(e.getPoint())) {
				selectedRect = next;
				rectangleSelected = true;
				oldPoint = e.getPoint();
			}
		}
		
		if (!rectangleSelected) {
			this.selectedRect = null;
			this.view.paintRectangles(null, false);
		} else {
			this.view.paintRectangles(selectedRect, true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		rectangles = view.getRectangles();
		Boolean rectangleSelected = false;
		for(Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();)
		{
			Rectangle next = it.next();
			if (next.contains(e.getPoint())) {
				selectedRect = next;
				rectangleSelected = true;
				oldPoint = e.getPoint();
			}
		}
		
		if (!rectangleSelected) {
			this.selectedRect = null;
			this.view.paintRectangles(null, false);
		} else {
			this.view.paintRectangles(selectedRect, true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (selectedRect != null) {
			Point point = new Point(arg0.getPoint());
			int xDiff = point.x - oldPoint.x;
			int yDiff = point.y - oldPoint.y;
			selectedRect.setLocation(new Point(selectedRect.x + xDiff, selectedRect.y + yDiff));
			this.view.paintRectangles(selectedRect, true);
			oldPoint.setLocation(point);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_DELETE && this.selectedRect != null) {
			try {
				this.view.remove(selectedRect);
				this.view.paintRectangles(null, false);
				this.selectedRect = null;
				this.view.setSelectingRectangles(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setView(ImageView view) {
		if (view != null)
			this.rectangles = view.getRectangles();
		super.setView(view);
	}

}
