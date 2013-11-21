package main;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import configs.Configs;

public class RobotHand {
	private static Logger logger = LogManager.getLogger();
	
	private SerialPort serialPort;
		
	Engine[] engines;
	Thread gripEngineThread;
	
	public RobotHand() 
	{
		serialPort = new SerialPort( (String) Configs.robotConfigs[0]); // port
        try {
        	serialPort.openPort();
        	serialPort.setParams( 
        			(int) Configs.robotConfigs[1], // baudrate
        			(int) Configs.robotConfigs[2], // databits
        			(int) Configs.robotConfigs[3], // stopbits
        			(int) Configs.robotConfigs[4]); // parityCheck
        }
        catch (SerialPortException e) {
            logger.error(e);
        }
        engines = initEngines();
	}
	
	
	
	public Engine[] initEngines() 
	{
		Engine[] engines = new Engine[Configs.enginesConfigs.length];
		for(int i = 0; i < engines.length; i++) {
			engines[i] = new Engine(
					this,
					(int) Configs.enginesConfigs[i][0], // portNo
					(double) Configs.enginesConfigs[i][1], // speed
					(int) Configs.enginesConfigs[i][2], // idlePulse
					(int) Configs.enginesConfigs[i][3], // minPulse
					(int) Configs.enginesConfigs[i][4] ); // maxPulse
		}
		for (Engine e : engines) {
			e.move(0);
		}
		gripEngineThread = new Thread(engines[Configs.enginesConfigs.length - 1]);
		gripEngineThread.start();
		return engines;
	}
	
	
	
	public synchronized void sendCommand(int portNo, int p) 
	{
		log(portNo, p);
		String command = "#" + portNo + "P" + p + "\r";
		try {
			serialPort.writeBytes(command.getBytes());
		} catch (SerialPortException e) {
			logger.error(e);
		}
	}
	
	
	
	public void move(int engineNo, double change) 
	{
		engines[engineNo].move(change);
	}
	
	public void stopEngines() 
	{
		engines[Configs.enginesConfigs.length - 1].shouldThreadBeOn = false;
		for (int i = 0; i < Configs.enginesConfigs.length; i++) {
			sendCommand(i, 0);
		}
		try {
			gripEngineThread.join();
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
	
	
	
	public void closePort() 
	{
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			logger.error(e);
		}
	}
	
	
	
	public void shutdown() 
	{
		stopEngines();
		closePort();
		logger.info("Robotas iðjungtas");
	}
	
	
	
	private void log(int engineNo, int pulse) 
	{
		String[] pulses = new String[Configs.enginesConfigs.length];
		for (int i = 0; i < Configs.enginesConfigs.length; i++) {
			pulses[i] = "-";
		}
		pulses[engineNo] = Integer.toString(pulse);
		String msg = String.format("P0= %7s P1= %7s P2= %7s P3= %7s P4= %7s P5= %7s", (Object[]) pulses);
		logger.info(msg);
	}
}
