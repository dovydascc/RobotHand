package main;

public class Engine implements Runnable
{
	RobotHand rh;
	int portNo;
	double speed; // impulso kitimo greièio koeficientas
	int idlePulse; // impulso dydis, prie kurio variklis nejuda
	int minPulse; // impulso apatinë riba. Skirta riboti greièiui
	int maxPulse; // impulso virðutinë riba. Skirta riboti greièiui
	
	private int pulse; // varikliui paduodamas impulsas
	volatile double change; // impulso pokytis
	boolean shouldThreadBeOn = true;
	
	
	public Engine(RobotHand rh, int portNo, double speed, int idlePulse, int minPulse, int maxPulse) 
	{
		this.rh = rh;
		this.portNo = portNo;
		this.pulse = idlePulse;
		this.speed = speed;
		this.idlePulse = idlePulse;
		this.minPulse = minPulse;
		this.maxPulse = maxPulse;
	}
	
	
	
	public void move(double change) 
	{ 
		this.change = change;
		pulse = fitInInterval((int) (pulse + (speed * change)));
		rh.sendCommand(portNo, pulse);
	}
	
	
	
	private int fitInInterval(int pulse) 
	{
	    pulse = (pulse >= minPulse) ? pulse : minPulse;
	    pulse = (pulse <= maxPulse) ? pulse : maxPulse;
		return pulse;
	}
	
	public int getPulse() {
		return pulse;
	}
	
	@Override
	public void run() 
	{	
		while (shouldThreadBeOn) {
			if (change != 0) {
				move(change);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setShouldThreadBeOn(boolean shouldThreadBeOn) {
		this.shouldThreadBeOn = shouldThreadBeOn;
	}
}
