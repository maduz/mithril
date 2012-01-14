package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.TimeTable;
import model.TimeTableCourse;
import model.TimeTableDay;
import model.TimeTableSession;

import view.TimeTableApp.AppStates;

public class TimeTableEntryPanel extends TimeTablePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7639310798729631919L;
	Iterator<TimeTableDay> timeTableIterator;
	List<List<TimeTableDay>> schedule = new ArrayList<List<TimeTableDay>>();
	int index = 0;
	
	JLabel dayLabel;
	public TimeTableEntryPanel(TimeTable timeTable, TimeTableApp parent) {
		super(parent, AppStates.TimeTableEntry);
		
		timeTableIterator = timeTable.getTimeTableIterator();
		
		List<TimeTableDay> daysToDisplay = new ArrayList<TimeTableDay>();
		
		int numSessions = getNextSetOfDays(7, daysToDisplay);
		
//		timeTableIterator.hasNext();
//		TimeTableDay timeTable = timeTableIterator.next();
		
		schedule.add(daysToDisplay);
		index++;
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		GridLayout gridLayout = new GridLayout(numSessions+(daysToDisplay.size()+1), 0, 5, 5);
		JPanel basePane = new JPanel(gridLayout);
//		basePane.setLayout(gridLayout);
		
		setControls(daysToDisplay, basePane);
	}
	
	private int getNextSetOfDays(int numDays, List<TimeTableDay> daysToDisplay) {
		int weekdayCounter = numDays;
		int numSessions = 0;
		
		while(weekdayCounter > 0) {
			if(timeTableIterator.hasNext()) {
				TimeTableDay nextDay = timeTableIterator.next();
				daysToDisplay.add(nextDay);
				numSessions += nextDay.getSessionCapacity();
			} else break;
			weekdayCounter--;
		}
		return numSessions;
	}
	
	public void handleNext() {
		List<TimeTableDay> daysToDisplay = new ArrayList<TimeTableDay>();
		int numSessions = 0;
		
		if(index < schedule.size()) {
			index++;
			daysToDisplay = schedule.get(index - 1);
			
			for(TimeTableDay day: daysToDisplay) {
				numSessions += day.getSessionCapacity();
			}

			BorderLayout layout = new BorderLayout();
			setLayout(layout);

			GridLayout gridLayout = new GridLayout(numSessions+(daysToDisplay.size()+1), 0, 5, 5);
			JPanel basePane = new JPanel(gridLayout);

			setControls(daysToDisplay, basePane);
			
			getParent().setPanel(this);
			return;
		}

		daysToDisplay = new ArrayList<TimeTableDay>();
		
		numSessions = getNextSetOfDays(7, daysToDisplay);
		
		//we have reached the end of the iteration
		if(numSessions == 0) {
			List<TimeTableDay> timeTable = new ArrayList<TimeTableDay>();
			for(List<TimeTableDay> days: schedule) {
				timeTable.addAll(days);
			}
			TimeTablePanel nextPanel = PanelsFactory.getFactory(getParent()).getNextPanel(timeTable);
			getParent().setPanel(nextPanel);
			return;
		}
		schedule.add(daysToDisplay);
		index++;

		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		GridLayout gridLayout = new GridLayout(numSessions+(daysToDisplay.size()+1), 0, 5, 5);
		JPanel basePane = new JPanel(gridLayout);
		setControls(daysToDisplay, basePane);
		getParent().setPanel(this);
	}
	
	public void handleBack() {
		List<TimeTableDay> daysToDisplay = new ArrayList<TimeTableDay>();
		int numSessions = 0;

		if(index > 1) {
			index--;
			daysToDisplay = schedule.get(index - 1);
			
			for(TimeTableDay day: daysToDisplay) {
				numSessions += day.getSessionCapacity();
			}

			BorderLayout layout = new BorderLayout();
			setLayout(layout);

			GridLayout gridLayout = new GridLayout(numSessions+(daysToDisplay.size()+1), 0, 5, 5);
			JPanel basePane = new JPanel(gridLayout);
			setControls(daysToDisplay, basePane);
			
			getParent().setPanel(this);
			return;
		}
		
		TimeTablePanel panel = PanelsFactory.getFactory(getParent()).getPreviousPanel();
		getParent().setPanel(panel);
	}
	
	private void setControls(List<TimeTableDay> daysToDisplay, JPanel basePane) {
		this.removeAll();
		//getParent().getPanel();
		//this.add(getParent().getPanel());
		getParent().setPanel(this);

		JLabel headerLabel = new JLabel("TimeTable");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
		basePane.add(headerLabel);
		for(TimeTableDay day: daysToDisplay) {
			JLabel dayNumberLabel = new JLabel("Day: " + (day.getDayNumber()+1));
			dayNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
			basePane.add(dayNumberLabel);
			int sessionColor = 0;
			for(TimeTableSession session: day.getSessions()) {
				sessionColor = 1-sessionColor;
				JPanel sessionPanel = new JPanel(new GridLayout(0, session.getRooms(), 5, 5));
				List<TimeTableCourse> courses = session.getCourses();
				for(int i = 0; i < session.getRooms(); i++) {
					String courseName = ((TimeTableCourse) courses.get(i)).getCourseName();
					//JLabel courseLabel = new JLabel((courseName == null)?String.format("%20s", "-"):String.format("%20s", courseName));
//					JLabel courseLabel = new JLabel((courseName == null)?padString("-", 20):padString(courseName, 20));
					JButton courseLabel = new JButton((courseName == null)?"-":courseName);
					courseLabel.setSize(72, 20); //to be used only with JButton
//					courseLabel.setEnabled(true);
					courseLabel.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							if(e.getSource()==this) {
								JOptionPane.showMessageDialog(getParent(), "Other course options need to be displayed!");
							}
						}
						
					});
					courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
					courseLabel.setBackground(getColor(sessionColor));
					courseLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
					//sessionPanel.add(courseLabel, BorderLayout.CENTER);
					sessionPanel.add(courseLabel);
				}
				basePane.add(sessionPanel);
			}
		}
		
		this.add(new JScrollPane(basePane));
//		dayLabel = new JLabel(daysToDisplay.toString());
//		this.add(dayLabel);
	}
	
	private Color getColor(int sessionColor) {
		// TODO Auto-generated method stub
		switch(sessionColor) {
		case 0:
			return Color.MAGENTA;
		case 1:
			return Color.CYAN;
		}
		return null;
	}

	private String padString(String str, int width) {
		String retVal = "";
		if(str.length() >= width) {
			retVal = str.substring(0, width);
		} else {
			int diff = width - str.length();
			int leftDiff = diff/2;
			diff -= leftDiff;
			while(leftDiff > 0) {
				retVal += " ";
				leftDiff--;
			}
			retVal += str;
			while(diff > 0) {
				retVal += " ";
				diff--;
			}
		}
		return retVal;
	}
	
}
