package main;

import jssc.SerialPort;
import jssc.SerialPortException;

public class RobotHand {
	private SerialPort serialPort;
		
	Engine[] engines;
	
	public RobotHand(String commPort) 
	{
		serialPort = new SerialPort(commPort);
        try {
        	serialPort.openPort();
        	serialPort.setParams(115200, 8, 1, 0); // Nustatymai: Baudrate 115200; Databits – 8; Stopbits - 1; Parity – none; 
        }
        catch (SerialPortException e) {
            System.out.println(e);
        }
        sendCommand(5, 2500);
        //engines = initEngines();
	}
	
	
	
	public Engine[] initEngines() 
	{
		Engine[] engines = new Engine[6];
		//Nustatymai Engine(RobotHand rh, int engineNo, boolean isPositional, double speed, int idlePulse, int minPulse, int maxPulse, long sleepTime)
		engines[0] = new Engine(this, 0, false, 2, 1470, 1440, 1500, 150);
		engines[1] = new Engine(this, 1, false, 6, 1500, 1400, 1540, 50);
		engines[2] = new Engine(this, 2, false, 500, 1500, 1200, 2500, 500);
		engines[3] = new Engine(this, 3, false, 2, 1500, 1450, 1550, 150);
		engines[4] = new Engine(this, 4, true, 0.05, 1500, 500, 2500, 150);
		engines[5] = new Engine(this, 5, true, 50, 1500, 1000, 2500, 20);
		for (Engine e: engines) {
			e.start();
		}
		return engines;
	}
	
	
	
	public synchronized void sendCommand(int engineNo, int p) 
	{
		log(engineNo, p);
		String command = "#" + engineNo + "P" + p + "\r";
		try {
			serialPort.writeBytes(command.getBytes());
		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}
	
	
	
	public void move(int engineNo, double change) 
	{
		engines[engineNo].move(change);
	}
	
	
	
	public void stopEngines() 
	{
		for (int i = 0; i < 6; i++) {
			sendCommand(i, 0);
		}
	}
	
	
	
	public void closePort() 
	{
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}
	
	
	
	public void shutdown() 
	{
		for (Engine e: engines) {
			e.setShouldThreadBeOn(false);
			try {
				e.join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		stopEngines();
		closePort();
	}
	
	
	
	private void log(int engineNo, int pulse) 
	{
		String[] pulses = new String[6];
		for (int i = 0; i < 6; i++) {
			pulses[i] = "-";
		}
		pulses[engineNo] = Integer.toString(pulse);
		System.out.format("P0= %7s P1= %7s P2= %7s P3= %7s P4= %7s P5= %7s\n", (Object[]) pulses);
	}
}
