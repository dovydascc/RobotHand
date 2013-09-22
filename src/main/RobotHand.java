package main;

import jssc.SerialPort;
import jssc.SerialPortException;

public class RobotHand {
	private SerialPort serialPort;
	
	private static final int[][] PULSES_INTERVALS = new int[][]{ // varikliø impulsø min ir maks ribos, individualiai varikliui
		{ 500, 2500 }, { 500, 2500 }, { 500, 2500 }, { 500, 2500 }, { 500, 2500 }, { 1000, 2500 } 
	};
	
	private static final double[] SPEEDS = new double[] {1.0, 1.0, 1.0, 1.0, 0.5, 50.0};

	private int[] pulses = new int[] {
			1500,
			1500,
			1500,
			1500,
			1500,
			1500
	};
	
	public static enum GripAction {PRESS, STOP, RELEASE}; // griebtuvo veiksmas
	private GripAction gripAction = GripAction.STOP;

	Thread gripEngineThread;
	boolean shouldGripEngineThreadRun = true;
	
	
	
	public RobotHand(String commPort) {
		serialPort = new SerialPort(commPort);
        try {
        	/* 
        	 * Nustatymai: 
        	 * Baudrate 115200;
			 * Parity – none;
			 * Databits – 8;
			 * Stopbits - 1
        	 */
        	serialPort.openPort();
        	serialPort.setParams(115200, 8, 1, 0);
        }
        catch (SerialPortException e){
            System.out.println(e);
        }
        readEnginesPositions();
        createGripEngineThread();
        gripEngineThread.start();
	}
	
	public void move(int engineNo, double pulseChange) {
		pulses[engineNo] = fitInInterval(engineNo, (int) (pulses[engineNo] + (pulseChange * SPEEDS[engineNo])));
		sendCommand(engineNo, pulses[engineNo]);
	}

	private void sendCommand(int engineNo, int p) {
		log(engineNo, p);
		String command = "#" + engineNo + "P" + p + "\r";
		try {
			serialPort.writeBytes(command.getBytes());
		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}
	
	public void setGripAction(GripAction gripAction) {
		this.gripAction = gripAction;
	}
	
	public void stopEngine(int i) {
		sendCommand(i, 0);
	}
	
	public void stopEngines() {
		for (int i = 0; i < 6; i++) {
			sendCommand(i, 0);
		}
	}
	
	public void closePort() {
		try {
			serialPort.closePort();
		} catch (SerialPortException e) {
			System.out.println(e);
		}
	}
	
	public void shutdown() {
		shouldGripEngineThreadRun = false;
		try {
			gripEngineThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopEngines();
		closePort();
	}
	
	private void readEnginesPositions() {
//		
//		try {
//			String command = "QP 0 1 2 3 4 5\r";
//			serialPort.writeBytes(command.getBytes());
//			String varikliai = serialPort.readString();
//			System.out.println("Nuskaitytos Roboto pozicijos" + varikliai);
//		} catch (SerialPortException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void createGripEngineThread() {
        gripEngineThread = new Thread() {
            public void run() {
            	while (shouldGripEngineThreadRun) {
            		System.out.println("Veikia");
	            	if (gripAction == GripAction.PRESS) {
	            		move(5, 1);
	            	} else if (gripAction == GripAction.RELEASE) {
	            		move(5, -1);
	            	}
	            	
	            	try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        };
	}
	
	private int fitInInterval(int engineNo, int pulse) {
	    pulse = (pulse >= PULSES_INTERVALS[engineNo][0]) ? pulse : PULSES_INTERVALS[engineNo][0];
	    pulse = (pulse <= PULSES_INTERVALS[engineNo][1]) ? pulse : PULSES_INTERVALS[engineNo][1];
		return pulse;
	}
	
	private void log(int engineNo, int pulse) {
		String[] pulses = new String[6];
		for (int i = 0; i < 6; i++) {
			pulses[i] = "-";
		}
		pulses[engineNo] = Integer.toString(pulse);
		System.out.format("P0= %7s P1= %7s P2= %7s P3= %7s P4= %7s P5= %7s\n", (Object[]) pulses);
	}
}
