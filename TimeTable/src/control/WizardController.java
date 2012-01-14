package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Constants;

import view.TimeTableApp;
import view.TimeTablePanel;

public class WizardController implements ActionListener {
	private TimeTableApp parent;
	
	public WizardController(TimeTableApp parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		TimeTablePanel panel = parent.getPanel();
		
		if(command == Constants.Next)
			panel.handleNext();
		else if(command == Constants.Back)
			panel.handleBack();
		else if(command == Constants.Finish)
			panel.handleFinish();
		else if(command == Constants.Cancel)
			panel.handleCancel();
	}

}
