package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Config;
import model.RuleBasedTimeTable;
import model.TimeTable;
import model.TimeTableDay;


import view.TimeTableApp.AppStates;

public class PanelsFactory {
	private static PanelsFactory factory = null;
	
	private AppStates currentState = AppStates.Uninititalized;
	List<TimeTablePanel> panels = new ArrayList<TimeTablePanel>();
	TimeTableApp parent;
	
	public static PanelsFactory getFactory(TimeTableApp parent) {
		if(factory == null)
			factory = new PanelsFactory(parent);
		
		return factory;
	}
	
	private PanelsFactory(TimeTableApp parent) {
		this.parent = parent;
	}
	
	public TimeTablePanel getNextPanel(Object context) {
		TimeTablePanel panel = null;

		try {
			if((panels.size() > currentState.ordinal()) && (!panels.isEmpty())) {
				panel = panels.get(currentState.ordinal());
				currentState = panel.getState();
				
				return panel;
			}
			
			switch(currentState){
			case Uninititalized :
				panel = new IntroPanel(parent);
				break;
			
			case Intro:
				panel = new CourseInfoDialog(parent);
				break;

			case CourseInfo: {
					Config config = (Config) context;
					
					panel = new InstructorAvailabilityPanel(config, 10, parent);
					break;
			}
				
			case InstructorAvailabilityInfo: {
					Config config = (Config) context;
					
					TimeTable timeTable;
					timeTable = new RuleBasedTimeTable(config);
					
					panel = new TimeTableEntryPanel(timeTable, parent);
					break;
			}

			case TimeTableEntry:
				List<TimeTableDay> timeTableEntries = (List<TimeTableDay>) context;
				System.out.println("Getting Finish Panel: " + timeTableEntries.size());
				panel = new FinishPanel(parent, timeTableEntries);
				break;
			}
			
			if(panel != null) {
				currentState = panel.getState();
				panels.add(panel);
			}
		}  catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return panel;
	}
	
	public TimeTablePanel getPreviousPanel() {
		panels.remove(currentState.ordinal()-1);
		TimeTablePanel panel = panels.get(currentState.ordinal() - 2);
		currentState = panel.getState();
		
		return panel;
	}
}
