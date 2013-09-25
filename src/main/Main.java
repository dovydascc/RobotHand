package main;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener
{

	static RobotHand rh;
	static final String PORT = "COM5"; // nurodyti port�
	
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
        JFrame frame = new JFrame("Robotin� Ranka");
    	
        main.setOpaque(true);
        frame.setContentPane(main);
        main.setLayout(new GridBagLayout());

        JButton button = new JButton("�jungti");
        main.add(button);
        button.addActionListener(main); 
        button.addKeyListener(new KeyListener());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    
    /*
     * Metodas i�jungia variklius, u�daro portus, kai u�daroma programa
     */
    public void attachShutDownHook() 
    {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				rh.shutdown();
				System.out.println("Robotas i�jungtas");
				System.exit(0);
			}
		});
    }

    
    
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if ( !isButtonPressed) {
			rh = new RobotHand(PORT); 
	    	mouseInput = new MouseInput(rh);
	    	main.addMouseMotionListener(mouseInput);
	    	main.addMouseListener(mouseInput);
	    	main.addMouseWheelListener(mouseInput);
	    	isButtonPressed = true;
		} else {
			main.removeMouseMotionListener(mouseInput);
	    	main.removeMouseListener(mouseInput);
	    	main.removeMouseWheelListener(mouseInput);
	    	rh.shutdown();
	    	System.out.println("Robotas i�jungtas");
	    	isButtonPressed = false;
		}
	}
	
	static class KeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                rh.record();
            }
            
            if (e.getKeyCode() == KeyEvent.VK_P) {
                rh.playback();
            }
        }
    }
}