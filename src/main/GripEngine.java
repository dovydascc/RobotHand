//package main;
//
//public class GripEngine extends Engine implements Runnable {
//	RobotHand rh;
//	volatile double change;
//	
//	public GripEngine(RobotHand rh, int portNo, double speed, int idlePulse, int minPulse, int maxPulse) {
//		super(portNo, speed, idlePulse, minPulse, maxPulse);
//		this.rh = rh;
//	}
//	
//	@Override
//	public int move(double change) {
//		this.change = change;
//		return super.move(change);
//	}
//	
//	
//}
