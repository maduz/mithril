package view;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Config;

import view.TimeTableApp.AppStates;

public class CourseInfoDialog extends TimeTablePanel implements DocumentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000652211261177015L;
	ConfigurablePanel roomsInfo = new ConfigEntries ("Number of rooms", 50);
	ConfigurablePanel sessionsInfo = new ConfigEntries ("Sessions per day", 24);
	
	ConfigurablePanel startDateInfo = new DateConfigEntries("Start date for time table", null);
	ConfigurablePanel endDateInfo = new DateConfigEntries("End date for time table", null);
	ConfigurablePanel courseInfo = new FileChoiceTrio("Course details file ", SwingConstants.CENTER, 40, this);
	
	private boolean nextEnabled = false;
	private Config config;

	public CourseInfoDialog(TimeTableApp parent) {
		super(parent, AppStates.CourseInfo);

		GridLayout layout = new GridLayout(0, 1, 10, 2);
		this.setLayout(layout);
		
		{
			ConfigurablePanel temp = new ConfigEntries("", 0);
			this.add(temp);
		}
				
		this.add (roomsInfo);
				
		this.add (sessionsInfo);
		
		this.add(startDateInfo);
		
		this.add(endDateInfo);
		
		this.add(courseInfo);
		
		{
			ConfigurablePanel temp = new ConfigEntries("", 0);
			this.add(temp);
		}
		
		{
			ConfigurablePanel temp = new ConfigEntries("", 0);
			this.add(temp);
		}
	}

	public void handleNext() {
		try {
			Config config = new Config();
			config.setRoomsPerSession(((Integer) roomsInfo.getValue()).intValue());
			config.setSessionsPerDay(((Integer) sessionsInfo.getValue()).intValue());
			
			Date start, end;
			start = (Date) startDateInfo.getValue();
			end = (Date) endDateInfo.getValue();

			if(end.before(start)) {
				//throw an error
				JOptionPane.showMessageDialog(this.getParent(), "End-date cannot exist before Start-date", "Error: Date Choice", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			config.setStartDate(start);
			config.setEndDate(end);
			
			config.setCourseDetailsFile((String)courseInfo.getValue());

			TimeTablePanel nextPanel = PanelsFactory.getFactory(getParent()).getNextPanel(config);
			getParent().setPanel(nextPanel);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleBack() {
		TimeTablePanel intro = PanelsFactory.getFactory(getParent()).getPreviousPanel();
		getParent().setPanel(intro);
	}
	
	public boolean canProceed() {
		if(courseInfo.getValue().toString().isEmpty())
			return false;
		
		return true;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(canProceed() && !nextEnabled) {
			getParent().setPanel(this);
			nextEnabled = true;
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(!canProceed() && nextEnabled) {
			getParent().setPanel(this);
			nextEnabled = false;
		}
		
	}
}
