package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TimeTableApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private JFrame frame;
	
	private TimeTablePanel panel = PanelsFactory.getFactory(this).getNextPanel(null);
	private BottomPanel bottom = new BottomPanel(this);
	
	
	private JPanel leftBandMajor = new JPanel();
	private JPanel leftBandMinor = new JPanel();
	
	public enum AppStates {
		Uninititalized,
		Intro,
		CourseInfo,
		InstructorAvailabilityInfo,
		TimeTableEntry,
		Finish,
		FileChoice
	}


	/**
	 * Create the application.
	 */
	public TimeTableApp() {
		initialize();
	}

	public TimeTablePanel getPanel() {
		return panel;
	}
		
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frame = new JFrame();
		setBounds(100, 100, 700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setResizable(true);
				
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		getContentPane().setLayout(layout);
		
		leftBandMajor.setBackground(Color.RED);
		leftBandMajor.setPreferredSize(new Dimension(150, 400));
		leftBandMinor.setBackground(Color.RED);
		leftBandMinor.setPreferredSize(new Dimension(150, 100));
		
		panel.setPreferredSize(new Dimension(450, 400));
		bottom.setPreferredSize(new Dimension(450, 100));
		
		getContentPane().add(leftBandMajor);
		getContentPane().add(panel);
		getContentPane().add(leftBandMinor);
		getContentPane().add(bottom);
	}
	
	public void setPanel(TimeTablePanel newPanel) {
		getContentPane().remove(panel);
		getContentPane().remove(leftBandMinor);
		getContentPane().remove(bottom);
		
		this.panel = newPanel;
		this.panel.setPreferredSize(new Dimension(450, 400));
		this.panel.setVisible(true);
		
		getContentPane().add(this.panel);
		getContentPane().add(leftBandMinor);
		getContentPane().add(bottom);
		
		bottom.reset();
		
		this.validate();
		this.repaint();
	}

	public void terminate() {
		System.exit(0);
	}
}
