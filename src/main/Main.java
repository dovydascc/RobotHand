package main;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import config.MainConfigsBundle;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener
{
	static RobotHand robotHand;
	
	static Main main;
	static MouseInput mouseInput;
	
	static boolean isButtonPressed = false;
	
    public static void main(String[] args) 
    {
    	main = new Main();
 
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
        main.attachShutDownHook();
    }   
    
    
    
    private static void createAndShowGUI() 
    {
        JFrame frame = new JFrame("Robotinë Ranka");
    	
        main.setOpaque(true);
        frame.setContentPane(main);
        main.setLayout(new GridBagLayout());

        JButton button = new JButton("Ájungti");
        main.add(button);
        button.addActionListener(main);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    
    /*
     * Metodas iðjungia variklius, uþdaro portus, kai uþdaroma programa
     */
    public void attachShutDownHook() 
    {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				robotHand.shutdown();
				System.out.println("Robotas iðjungtas");
				System.exit(0);
			}
		});
    }

    
    
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if ( !isButtonPressed) {
			robotHand = new RobotHand();
			
	    	mouseInput = new MouseInput(robotHand);
	    	main.addMouseMotionListener(mouseInput);
	    	main.addMouseListener(mouseInput);
	    	main.addMouseWheelListener(mouseInput);
	    	isButtonPressed = true;
		} else {
			main.removeMouseMotionListener(mouseInput);
	    	main.removeMouseListener(mouseInput);
	    	main.removeMouseWheelListener(mouseInput);
	    	robotHand.shutdown();
	    	System.out.println("Robotas iðjungtas");
	    	isButtonPressed = false;
		}
	}
}