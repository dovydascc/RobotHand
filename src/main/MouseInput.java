package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

public class MouseInput extends MouseAdapter 
{
	RobotHand rh;
	
	Point prevPos;
	Point wheelPressedPos;
	
	boolean isWheelPressed = false;
	
	
	
	public MouseInput(RobotHand rh) 
	{
		this.rh = rh;
		prevPos = MouseInfo.getPointerInfo().getLocation();
	}
	
	
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		rh.move(0, prevPos.getX() - p.getX());
		rh.move(1, prevPos.getY() - p.getY());
		prevPos = p;
	}
	
	
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		if (isWheelPressed) {
			rh.move(4, wheelPressedPos.getX() - p.getX());
			rh.move(3, prevPos.getY() - p.getY()); // greièio varikliui pagal prevPos
		}
		prevPos = p;
	}
	
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		rh.move(2, e.getWheelRotation());
	}
	
	
	
	public void mousePressed(MouseEvent e) 
	{
		if (SwingUtilities.isLeftMouseButton(e)) {
			rh.move(5, 1);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			rh.move(5, -1);
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			isWheelPressed = true;
			wheelPressedPos = MouseInfo.getPointerInfo().getLocation();
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
