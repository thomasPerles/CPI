package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class NewZoneImageController extends ImageController {

	private Rectangle selectedRect;

	private Point start;
	
	public NewZoneImageController() {
		this.start = new Point();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.start = arg0.getPoint();
		this.selectedRect = new Rectangle(start.x, start.y, 0, 0);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.view.incorporateRectangle();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		Point end = me.getPoint();
		this.selectedRect = new Rectangle(this.start);
		selectedRect.add(end);
		this.view.paintRectangles(this.selectedRect, false);
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
