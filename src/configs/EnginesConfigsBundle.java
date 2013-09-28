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
    		
    		// pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 10, new Object[] { false, 2.0, 1470, 1440, 1500, 150} },
            
            // pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 11, new Object[] { false, 6.0, 1500, 1400, 1540, 50} },
            
            // pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 12, new Object[] {false, 500.0, 1500, 1200, 2500, 500} },
            
            // pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 13, new Object[] {false, 2.0, 1500, 1450, 1550, 150} },
            
            // pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 14, new Object[] { true, 0.05, 1500, 500, 2500, 150} },
            
            // pin number, {isPositional, speed, idlePulse, minPulse, maxPulse, inactivityTimeMillisec}
            { 15, new Object[] { true, 50.0, 1500, 1000, 2500, 20} },            
    };
}