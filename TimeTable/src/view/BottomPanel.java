package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Constants;

import control.WizardController;

public class BottomPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TimeTableApp parent;
	private JButton next = new JButton(Constants.Next);
	private JButton back = new JButton(Constants.Back);
	private JButton finish = new JButton(Constants.Finish);
	private JButton cancel = new JButton(Constants.Cancel);

	public BottomPanel(TimeTableApp parentApp) {
		this.parent = parentApp;
		FlowLayout layout = new FlowLayout();
		
		WizardController controller = new WizardController(parent);
		next.addActionListener(controller);
		back.addActionListener(controller);
		finish.addActionListener(controller);
		cancel.addActionListener(controller);
		
		this.setLayout(layout);
		this.add(back);
		this.add(next);
		this.add(finish);
		this.add(cancel);
		
		reset();
	}
	
	public void reset() {
		back.setEnabled(true);
		next.setEnabled(true);
		finish.setEnabled(true);
		cancel.setEnabled(true);
		
		if(this.parent.getPanel().canRevert() == false)
			back.setEnabled(false);
		
		if(this.parent.getPanel().canProceed() == false)
				next.setEnabled(false);
		
		if(this.parent.getPanel().canFinish() == false)
			finish.setEnabled(false);
		
		if(this.parent.getPanel().canCancel() == false)
			cancel.setEnabled(false);
	}
}