package main;

import java.awt.Button;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {

	static RobotHand rh;
	
    public static void main(String[] args) {
    	Main m = new Main();
    	m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	m.pack();
    	m.setVisible(true);
    	m.attachShutDownHook();
    	m.setSize(600, 600);
    	
    	rh = new RobotHand("COM14"); // Nurodyk savo port�.
    	MouseInput mi = new MouseInput(rh);
 
    	m.addMouseMotionListener(mi);
    	m.addMouseListener(mi);
    	m.addMouseWheelListener(mi);
    }   
    
    
    /*
     * Metodas i�jungia variklius, u�daro portus, kai u�daroma programa
     */
    public void attachShutDownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				rh.shutdown();
				System.out.println("Robotas i�jungtas");
				System.exit(0);
			}
		});
    }
}