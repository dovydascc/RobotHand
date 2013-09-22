package main;

public class Engine extends Thread 
{
	RobotHand rh;
	int engineNo;
	volatile int pulse = 1500; // varikliui paduodamas impulsas
	boolean isPositional; // ar variklis pozicinis ar greièio?
	double speed; // impulso kitimo greièio koeficientas
	int idlePulse; // impulso dydis, prie kurio variklis nejuda
	int minPulse; // impulso apatinë riba. Skirta riboti greièiui
	int maxPulse; // impulso virðutinë riba. Skirta riboti greièiui
	long sleepTime; // gijos miego laikas. Reguliuoja variklio reakijos laikà / daþná
	boolean shouldThreadBeOn = true; // valdo pagrindiná gijos ciklà
	double prevChange; // anksèiau gautas judesio pokytis
	
	
	
	public Engine(RobotHand rh, int engineNo, boolean isPositional, double speed, int idlePulse, int minPulse, int maxPulse, long sleepTime) 
	{
		this.rh = rh;
		this.engineNo = engineNo;
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
		rh.sendCommand(engineNo, 0);
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
				rh.sendCommand(engineNo, pulse);
				if (engineNo == 5) { // griebtuvas
					pulse = fitInInterval( (int) (pulse + speed * prevChange)); // griebtuvui impulsà didinam èia, nes jam nëra pelës koord pokyèiø
				}
			} else { // greièio variklis
				rh.sendCommand(engineNo, pulse);
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
