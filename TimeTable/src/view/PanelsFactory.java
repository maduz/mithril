package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Config;
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
		if((panels.size() > currentState.ordinal()) && (!panels.isEmpty())) {
			TimeTablePanel panel = panels.get(currentState.ordinal());
			currentState = panel.getState();
			
			return panel;
		}
		
		TimeTablePanel panel = null;
		switch(currentState){
		case Uninititalized :
			panel = new IntroPanel(parent);
			break;
		case Intro:
			panel = new CourseInfoDialog(parent);
			break;
		case CourseInfo:
			Config config = (Config) context;
			panel = new FileChoicePanel(parent, config);
			break;
		case FileChoice:
//			Iterator<TimeTableDay> timeTableIterator = (Iterator<TimeTableDay>) context;
//			panel = new TimeTableEntryPanel(timeTableIterator, parent);
			TimeTable timeTable = (TimeTable) context;
			panel = new TimeTableEntryPanel(timeTable, parent);
//			panel = new TimeTableScrollPanel(timeTable, parent);
			break;
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
		
		return panel;
	}
	
	public TimeTablePanel getPreviousPanel() {
		panels.remove(currentState.ordinal()-1);
		TimeTablePanel panel = panels.get(currentState.ordinal() - 2);
		currentState = panel.getState();
		
		return panel;
	}
}
