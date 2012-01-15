package view;

import java.awt.GridLayout;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Config;
import model.GenericTimeTable;
//import model.NewTimeTable;
import model.TimeTable;

import view.TimeTableApp.AppStates;

public class FileChoicePanel extends TimeTablePanel implements DocumentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -795220467586932378L;
	private FileChoiceTrio courseDetailsPanel = new FileChoiceTrio("Course details file ", SwingConstants.CENTER, 40, this);
	//private FileChoiceTrio studentDetailsPanel = new FileChoiceTrio("Student details file ", SwingConstants.CENTER, 40, this);
	private FileChoiceTrio instructorDatePanel = new FileChoiceTrio("Instructor-date availability file", SwingConstants.CENTER, 40, this);
	private FileChoiceTrio courseSessionPanel = new FileChoiceTrio("Course-session preference file", SwingConstants.CENTER, 40, this);
	private FileChoiceTrio courseRoomPanel = new FileChoiceTrio("Course-classroom preference file", SwingConstants.CENTER, 40, this);

	
	private boolean nextEnabled = false;
	private Config config;
	
	public FileChoicePanel(TimeTableApp parent, Config config) {
		super(parent, AppStates.FileChoice);
		this.config = config;
		
		GridLayout layout = new GridLayout(5, 1, 10, 2);
		this.setLayout(layout);
		
		{
			FileChoiceTrio temp = new FileChoiceTrio("", SwingConstants.CENTER, 0, null);
			this.add(temp);
		}
				
		this.add (courseDetailsPanel);
				
//		this.add (studentDetailsPanel);
		
		{
			FileChoiceTrio temp = new FileChoiceTrio("", SwingConstants.CENTER, 0, null);
			this.add(temp);
		}
		
		{
			FileChoiceTrio temp = new FileChoiceTrio("", SwingConstants.CENTER, 0, null);
			this.add(temp);
		}
	}
	
	public void handleNext() {
		try {
			TimeTable timeTable = new GenericTimeTable(config);
//			NewTimeTable timeTable = new NewTimeTable(courseDetailsPanel.getFileName(), studentDetailsPanel.getFileName(), config.getRoomsPerSession(), config.getSessionsPerDay());
			//NewTimeTable timeTable = new NewTimeTable(courseDetailsPanel.getFileName(), null, config.getRoomsPerSession(), config.getSessionsPerDay(), config.getStartDate(), config.getEndDate());
			TimeTablePanel timeTableEntryPanel = PanelsFactory.getFactory(getParent()).getNextPanel(timeTable);
			getParent().setPanel(timeTableEntryPanel);

		
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
		if(courseDetailsPanel.getFileName().isEmpty())// || studentDetailsPanel.getFileName().isEmpty())
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
