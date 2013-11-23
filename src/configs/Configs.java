package configs;

public class Configs {
	// Roboto nustatymai
	public static final Object[] robotConfigs = {"COM5", 115200, 8, 1, 0}; // port, baudrate, databits, stopbits, parityCheck
	
	// varikliø nustatymai
	public static final Object[][] enginesConfigs = {
			// pin number, speed, idlePulse, minPulse, maxPulse
			{0, 0.1, 1500, 500, 2500 },
			{1, 0.3, 1975, 500, 2500 },
			{2, 20.0, 1012, 500, 2500 },
			{3, 0.2, 934, 500, 2500 },
			{4, 0.2, 1500, 500, 2500 },
			{5, 80.0, 1500, 1000, 2500 }, };
}