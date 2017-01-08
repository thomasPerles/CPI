package controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
		/*
		Graphics g = this.model.getImage().createGraphics();
		g.setColor(Color.BLUE);
		g.drawRect((int) selectedRect.getX(), (int) selectedRect.getY(), (int) selectedRect.getWidth(),
				(int) selectedRect.getHeight());
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) selectedRect.getX(), (int) selectedRect.getY(), (int) selectedRect.getWidth(),
				(int) selectedRect.getHeight());
		*/
		this.view.incorporateRectangle();
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		// start = me.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		Point end = me.getPoint();
		// selectedRect = new Rectangle(start, new Dimension(end.x - start.x,
		// end.y - start.y));
		this.selectedRect = new Rectangle(this.start);
		selectedRect.add(end);
		this.view.paintRectangles(this.selectedRect, false);
		// repaint(this.model.getImage(), this.model.getImage());
		// System.out.println("Rectangle: " + selectedRect);
	}
	
	/*
	public void repaint(BufferedImage orig, BufferedImage copy) {
		Graphics2D g = copy.createGraphics();
		// g.drawImage(orig, 0, 0, null);
		if (selectedRect != null) {
			// g.setColor(Color.BLUE);
			g.setColor(Color.LIGHT_GRAY);
			// System.out.println("repaint ...");
			g.draw(selectedRect);
			g.setColor(Color.LIGHT_GRAY);
			g.fill(selectedRect);
		}
	}
	*/
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
