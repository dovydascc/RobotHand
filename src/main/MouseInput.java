package main;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MouseInput extends MouseAdapter 
{
	private static final Logger logger = LogManager.getLogger();
	private static Point center;
	
	RobotHand rh;
	
	boolean isWheelPressed = false;
	Robot mouseController; // pelës auto valdymas
	
	
	public MouseInput(RobotHand rh) 
	{
		try {
			mouseController = new Robot(); // pelës auto valdymas
		} catch (AWTException e) {
			logger.error(e);
		}
		center = new Point(Main.frameSize.width / 2, Main.frameSize.height / 2);
		mouseController.mouseMove(center.x, center.y);
		this.rh = rh;
	}
	
	
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		mouseController.mouseMove(center.x, center.y);
		rh.move(0, center.x - p.getX());
		rh.move(1, center.y - p.getY());
	}
	
	
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		mouseController.mouseMove(center.x, center.y);
		if (isWheelPressed) {
			rh.move(4, center.x - p.getX());
			rh.move(3, center.y - p.getY());
		}
	}
	
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		rh.move(2, -1 * e.getWheelRotation());
	}
	
	
	
	public void mousePressed(MouseEvent e) 
	{
		if (SwingUtilities.isLeftMouseButton(e)) {
			rh.move(5, 1);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			rh.move(5, -1);
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			isWheelPressed = true;
		}
	}

	
	
	public void mouseReleased(MouseEvent e) 
	{
		if (SwingUtilities.isMiddleMouseButton(e)) {
			isWheelPressed = false;
		} else {
			rh.move(5, 0);
		}
	}
}
