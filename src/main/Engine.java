package main;

public class Engine extends Thread 
{
	RobotHand rh;
	int portNo;
	boolean isPositional; // ar variklis pozicinis ar greièio?
	double speed; // impulso kitimo greièio koeficientas
	int idlePulse; // impulso dydis, prie kurio variklis nejuda
	int minPulse; // impulso apatinë riba. Skirta riboti greièiui
	int maxPulse; // impulso virðutinë riba. Skirta riboti greièiui
	long sleepTime; // gijos miego laikas. Reguliuoja variklio reakijos laikà / daþná
	
	
	private boolean shouldThreadBeOn = true; // valdo pagrindiná gijos ciklà
	private double prevChange; // anksèiau gautas judesio pokytis
	private volatile int pulse; // varikliui paduodamas impulsas
	
	
	
	public Engine(RobotHand rh, int portNo, boolean isPositional, double speed, int idlePulse, int minPulse, int maxPulse, long sleepTime) 
	{
		this.rh = rh;
		this.portNo = portNo;
		this.pulse = idlePulse;
		this.isPositional = isPositional;
		this.speed = speed;
		this.idlePulse = idlePulse;
		this.minPulse = minPulse;
		this.maxPulse = maxPulse;
		this.sleepTime = sleepTime;
	}
	
	
	
	public void move(double change) 
	{ 
		if ( isPositional) {
			pulse = fitInInterval((int) (pulse + (speed * change)));
		} else {
			pulse = fitInInterval((int) (pulse + (speed * Math.signum(change))));
		}
		prevChange = change;
	}
	
	
	
	public void stopEngine()
	{
		rh.sendCommand(portNo, 0);
	}
	
	
	
	private int fitInInterval(int pulse) 
	{
	    pulse = (pulse >= minPulse) ? pulse : minPulse;
	    pulse = (pulse <= maxPulse) ? pulse : maxPulse;
		return pulse;
	}
	
	
	
	@Override
	public void run() 
	{	
		while (shouldThreadBeOn) {
			if (isPositional) {
				rh.sendCommand(portNo, pulse);
				if (portNo == 5) { // griebtuvas
					pulse = fitInInterval( (int) (pulse + speed * prevChange)); // griebtuvui impulsà didinam èia, nes jam nëra pelës koord pokyèiø
				}
			} else { // greièio variklis
				rh.sendCommand(portNo, pulse);
				pulse = idlePulse;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setShouldThreadBeOn(boolean shouldThreadBeOn) {
		this.shouldThreadBeOn = shouldThreadBeOn;
	}
}
