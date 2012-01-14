package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;

import model.Constants;

import view.TimeTableApp.AppStates;

public class IntroPanel extends TimeTablePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4082128155986736374L;
	private JLabel introLabel;
	
	public IntroPanel(TimeTableApp parent)  { 
		super(parent, AppStates.Intro);
		
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		
		introLabel = new JLabel(Constants.Intro);
		introLabel.setFont(new Font("Arial", Font.BOLD, 20));
		
		this.add(introLabel, BorderLayout.EAST);
//		this.add(introLabel);
	}
	
	public void handleNext() {
		TimeTablePanel panel = PanelsFactory.getFactory(getParent()).getNextPanel(null);
		getParent().setPanel(panel);
	}
	
	public boolean canRevert() {
		return false;
	}
}
