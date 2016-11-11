package gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

/** Getting a Rectangle of interest on the screen.
Requires the MotivatedEndUser API - sold separately. */
public class ScreenCaptureRectangle {

    Rectangle captureRect;
    JScrollPane screenScroll;
    ArrayList<Rectangle> listSelection = new ArrayList<>();

    ScreenCaptureRectangle(final BufferedImage screen) {
        final BufferedImage screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
        screenScroll = new JScrollPane(screenLabel);

        screenScroll.setPreferredSize(new Dimension(
                (int)(screen.getWidth()/3),
                (int)(screen.getHeight()/3)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(screenScroll, BorderLayout.CENTER);

        final JLabel selectionLabel = new JLabel(
                "Drag a rectangle in the screen shot!");
        panel.add(selectionLabel, BorderLayout.SOUTH);

        repaint(screen, screenCopy);
        screenLabel.repaint();

        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                //repaint(screen, screenCopy);
                //selectionLabel.setText("Start Point: " + start);
                //screenLabel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                captureRect = new Rectangle(start,
                        new Dimension(end.x-start.x, end.y-start.y));
                repaint(screen, screenCopy);
                screenLabel.repaint();
                selectionLabel.setText("Rectangle: " + captureRect);
            }
        });
        
        screenLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				Graphics g = screen.createGraphics();
				System.out.println("Coler rect...");
		        g.setColor(Color.RED);
		        g.drawRect((int)captureRect.getX(), (int)captureRect.getY(), (int)captureRect.getWidth(), (int)captureRect.getHeight());
		        g.setColor(new Color(255,255,255,150));
		        g.fillRect((int)captureRect.getX(), (int)captureRect.getY(), (int)captureRect.getWidth(), (int)captureRect.getHeight());
		        listSelection.add(captureRect);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

        JOptionPane.showMessageDialog(null, panel);
       
        System.out.println("Rectangle of interest: " + listSelection);
    }

    public void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.RED);
            System.out.println("repaint ...");
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,150));
            g.fill(captureRect);
        }
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        final Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
        final BufferedImage screen = robot.createScreenCapture(
                new Rectangle(screenSize));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ScreenCaptureRectangle(screen);
            }
        });
    }
}