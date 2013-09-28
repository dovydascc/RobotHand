package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import configs.EnginesConfigsBundle;
import configs.MainConfigsBundle;
import jssc.SerialPort;
import jssc.SerialPortException;

public class RobotHand {
	private static Logger logger = LogManager.getLogger("RobotHand");
	 
	private static final MainConfigsBundle mainConfigs = new MainConfigsBundle();
	private static final EnginesConfigsBundle enginesConfigs = new EnginesConfigsBundle();
	
	private SerialPort serialPort;
		
	Engine[] engines;
	
	public RobotHand() 
	{
		serialPort = new SerialPort(mainConfigs.getString("port"));
        try {
        	serialPort.openPort();
        	serialPort.setParams(Integer.parseInt(mainConfigs.getString("baudrate")), Integer.parseInt(mainConfigs.getString("databits")), 
        			Integer.parseInt(mainConfigs.getString("stopbits")), Integer.parseInt(mainConfigs.getString("parity"))); 
        }
        catch (SerialPortException e) {
            logger.error(e);
        }
        engines = initEngines();
	}
	
	
	
	public Engine[] initEngines() 
	{
		Engine[] engines = new Engine[enginesConfigs.keySet().size()];

		Object[] pinConfigs = enginesConfigs.keySet().toArray();
		for(int i = 0; i < engines.length; i++) {
			Object[] engineConfigs = (Object[]) enginesConfigs.getObject((String) pinConfigs[i]);
			engines[i] = new Engine(
					this, // Robothand
					Integer.parseInt( (String)pinConfigs[i]), // portNo 
					Boolean.parseBoolean( (String)engineConfigs[0]), // isPositional
					Double.parseDouble( (String) engineConfigs[1]), // speed
					Integer.parseInt( (String)engineConfigs[2]), // idlePulse
					Integer.parseInt( (String)engineConfigs[3]), // minPulse
					Integer.parseInt( (String)engineConfigs[4]), // maxPulse
					Integer.parseInt( (String)engineConfigs[5]) // inactivityTimeMillisec
			);
		}
		for (Engine e: engines) {
			e.start();
		}
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
		for (int i = 0; i < 6; i++) {
			sendCommand(i, 0);
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
		logger.info("Robotas iðjungtas");
	}
	
	
	
	private void log(int engineNo, int pulse) 
	{
		String[] pulses = new String[6];
		for (int i = 0; i < 6; i++) {
			pulses[i] = "-";
		}
		pulses[engineNo] = Integer.toString(pulse);
		logger.info("P0= %7s P1= %7s P2= %7s P3= %7s P4= %7s P5= %7s\n", (Object[]) pulses);
	}
}
