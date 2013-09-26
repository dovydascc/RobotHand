package configs;

import java.util.ListResourceBundle;

public class EnginesConfigsBundle extends ListResourceBundle 
{

    @Override
    protected Object[][] getContents() 
    {
        return contents;
    }

    private Object[][] contents = {
    		
    		// varikliø nustatymai
            { 10, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", false}, {"speed", 2.0}, {"idlePulse", 1470}, 
            		{"minPulse", 1440}, {"maxPulse", 1500}, {"inactivityTimeMillisec", 150},
            	} 
            },
            { 11, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", false}, {"speed", 6.0}, {"idlePulse", 1500}, 
            		{"minPulse", 1400}, {"maxPulse", 1540}, {"inactivityTimeMillisec", 50},
            	} 
            },
            { 12, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", false}, {"speed", 500.0}, {"idlePulse", 1500}, 
            		{"minPulse", 1200}, {"maxPulse", 2500}, {"inactivityTimeMillisec", 500},
            	} 
            },
            { 13, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", false}, {"speed", 2.0}, {"idlePulse", 1500}, 
            		{"minPulse", 1450}, {"maxPulse", 1550}, {"inactivityTimeMillisec", 150},
            	} 
            },
            { 14, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", true}, {"speed", 0.05}, {"idlePulse", 1500}, 
            		{"minPulse", 500}, {"maxPulse", 2500}, {"inactivityTimeMillisec", 150},
            	} 
            },            
            { 15, new Object[][] { // pino numeris prie kurio prijungtas variklis
            		{"isPositional", true}, {"speed", 50.0}, {"idlePulse", 1500}, 
            		{"minPulse", 1000}, {"maxPulse", 2500}, {"inactivityTimeMillisec", 20},
            	} 
            },            
    };
}