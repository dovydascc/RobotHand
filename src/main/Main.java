package main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener
{
	static final Dimension frameSize = new Dimension(400, 400); 
	static RobotHand robotHand;
	
	static Main main;
	static MouseInput mouseInput;
	
	static boolean isTurnOnPressed = false;
	
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

        JButton button = new JButton("Ájungti");
        main.add(button);
        button.addActionListener(main);
        button.addKeyListener(new KeyListener());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(frameSize);
        
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
				System.exit(0);
			}
		});
    }   
    
    
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if ( !isTurnOnPressed) {
			robotHand = new RobotHand();
			
	    	mouseInput = new MouseInput(robotHand);
	    	main.addMouseMotionListener(mouseInput);
	    	main.addMouseListener(mouseInput);
	    	main.addMouseWheelListener(mouseInput);
	    	isTurnOnPressed = true;
		} else {
			main.removeMouseMotionListener(mouseInput);
	    	main.removeMouseListener(mouseInput);
	    	main.removeMouseWheelListener(mouseInput);
	    	robotHand.shutdown();
	    	isTurnOnPressed = false;
		}
	}
	
	static class KeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
            	robotHand.record();        	
            }
            
            if (e.getKeyCode() == KeyEvent.VK_P) {
            	robotHand.playback();             	
            }
        }
	}
}