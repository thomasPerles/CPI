package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import view.ImageView;

public class SelectZoneImageController extends ImageController {

	private ArrayList<Rectangle> rectangles;
	private Rectangle selectedRect;
	private Point oldPoint;
	
	
	public SelectZoneImageController() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Boolean rectangleSelected = false;
		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();) {
			Rectangle next = it.next();
			if (next.contains(e.getPoint())) {
				selectedRect = next;
				rectangleSelected = true;
				this.view.paintRectangles(selectedRect, true);
			}
		}
		if(!rectangleSelected)
		{
			this.selectedRect = null;
			this.view.paintRectangles(null, false);
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
		for (Iterator<Rectangle> it = rectangles.iterator(); it.hasNext();) {
			Rectangle next = it.next();
			if (next.contains(e.getPoint())) {
				selectedRect = next;
				oldPoint = new Point(e.getPoint());
				break;
			} else {
				selectedRect = null;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(selectedRect != null) {
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
		if(e.getKeyChar() == KeyEvent.VK_DELETE && this.selectedRect != null) {
			try {
				this.view.remove(selectedRect);
				this.view.paintRectangles(null, false);
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
		if(view != null)
			this.rectangles = view.getRectangles();
		super.setView(view);
	}

}
