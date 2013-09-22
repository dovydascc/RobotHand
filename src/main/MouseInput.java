package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

public class MouseInput extends MouseAdapter {
	
	RobotHand rh;
	
	Point prevPos; 
	Point wheelPressedPos;
	
	boolean isWheelPressed = false;
	
	public MouseInput(RobotHand rh) {
		this.rh = rh;
		prevPos = MouseInfo.getPointerInfo().getLocation();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {	
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (isWheelPressed) {
			Point p = e.getLocationOnScreen();
			if (p.getX() > wheelPressedPos.getX()) { 
				rh.move(4, p.getX() - wheelPressedPos.getX());
			} else if (p.getX() < wheelPressedPos.getX()) {
				rh.move(4, -1 * (wheelPressedPos.getX() - p.getX()));
			}
			
			if (p.getY() > wheelPressedPos.getY()) {
				rh.move(3, p.getY() - wheelPressedPos.getY());
			} else if (p.getY() < wheelPressedPos.getY()) {
				rh.move(3, -1 * (wheelPressedPos.getY() - p.getY()));
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		rh.move(2, e.getWheelRotation());
	}
	
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			System.out.println("Press");
			rh.setGripAction(RobotHand.GripAction.PRESS);
		} else if (SwingUtilities.isRightMouseButton(e)) {
			System.out.println("Release");
			rh.setGripAction(RobotHand.GripAction.RELEASE);
		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			//System.out.println("Ratukas");
			isWheelPressed = true;
			wheelPressedPos = e.getLocationOnScreen();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isMiddleMouseButton(e)) {
			isWheelPressed = false;
		} else {
			rh.setGripAction(RobotHand.GripAction.STOP);
		}
	}
}
