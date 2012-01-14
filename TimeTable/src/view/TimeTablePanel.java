package view;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.TimeTableApp.AppStates;

public class TimeTablePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9121542543966961630L;
	private AppStates state = AppStates.Intro;
	private TimeTableApp parent;
	
	public TimeTablePanel(TimeTableApp parent, AppStates state) {		
		this.state = state;
		this.parent = parent;
	}
	
	
	public AppStates getState() {
		return this.state;
	}
	
	public void handleNext() {
		
	}
	
	public void handleBack() {
		
	}
	
	public boolean canProceed() {
		return true;
	}
	
	public boolean canRevert() {
		return true;
	}
	
	public boolean canFinish() {
		return false;
	}
	
	public boolean canCancel() {
		return true;
	}
	
	public void handleFinish() {
		
	}
	
	public void handleCancel() {
		JOptionPane confirmation = new JOptionPane("Do you want to exit the wizard?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = confirmation.createDialog(getParent(), "Confirm");
		dialog.setVisible(true);
		
		Object selectedValue = confirmation.getValue();
		if(((Integer)selectedValue).intValue() == 0)
			parent.terminate();
	}
	
	public TimeTableApp getParent() {
		return parent;
	}
}
