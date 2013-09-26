package config;

import java.util.ListResourceBundle;

public class MainConfigsBundle extends ListResourceBundle 
{

    @Override
    protected Object[][] getContents() 
    {
        return contents;
    }

    private Object[][] contents = {
    		
    		// pagrindiniai nustatymai
            { "port"   , "COM14" },
            { "baudrate", 115200 },
            { "databits", 8},
            { "stopbits", 1},
            { "parity", 0},
    };
}
