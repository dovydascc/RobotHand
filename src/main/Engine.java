package main;

public class Engine extends Thread 
{
	RobotHand rh;
	int portNo;
	boolean isPositional; // ar variklis pozicinis ar grei�io?
	double speed; // impulso kitimo grei�io koeficientas
	int idlePulse; // impulso dydis, prie kurio variklis nejuda
	int minPulse; // impulso apatin� riba. Skirta riboti grei�iui
	int maxPulse; // impulso vir�utin� riba. Skirta riboti grei�iui
	long sleepTime; // gijos miego laikas. Reguliuoja variklio reakijos laik� / da�n�
	
	
	private boolean shouldThreadBeOn = true; // valdo pagrindin� gijos cikl�
	private double prevChange; // anks�iau gautas judesio pokytis
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
					pulse = fitInInterval( (int) (pulse + speed * prevChange)); // griebtuvui impuls� didinam �ia, nes jam n�ra pel�s koord poky�i�
				}
			} else { // grei�io variklis
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
