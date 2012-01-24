/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Akshay
 *
 */
public class GenericTimeTable extends TimeTable {
	//TimeTableIterator iterator;// = new TimeTableIterator(cfName, saName, rooms, sessions)
	private int numRooms;
	private int numSessions;
	private TimeTableGenerator timetableGenerator = new TimeTableGenerator();
	private int day;
	private List<TimeTableDay> timeTableDayList = new ArrayList<TimeTableDay>();
	private boolean continueCreating = true;
	private TTIterator iterator;
	
	public GenericTimeTable(Config runConfig) throws Throwable {
		numRooms = runConfig.getRoomsPerSession();
		numSessions = runConfig.getSessionsPerDay();
//		timetableGenerator.createTimeTable(runConfig.getCourseDetailsFile(), runConfig.getStudentDetailsFile());
		timetableGenerator.createTimeTable(runConfig.getCourseFileReader(), runConfig.getStudentDetailsFile());
		
		day = 0;
		while(continueCreating) getTimeTable(day++);
	}
	
	public TimeTableDay getTimeTable(int day) throws TimeTableSessionException {
		TimeTableDay curDay;
		
		if(day < timeTableDayList.size()) {
			curDay = timeTableDayList.get(day); 
			if(curDay != null) {
				reverseTimeTable(curDay);
			}
		}
		curDay = new TimeTableDay(day, numSessions);
		continueCreating  = timetableGenerator.scheduleDay(curDay, numRooms);
		timeTableDayList.add(day, curDay);
		return curDay;
	}
	
	public void swapDays(TimeTableDay day1, TimeTableDay day2) {
		int d1 = day1.getDayNumber();
		day1.setDayNumber(day2.getDayNumber());
		day2.setDayNumber(d1);
		timeTableDayList.remove(day1.getDayNumber());
		timeTableDayList.add(day1.getDayNumber(), day1);
		timeTableDayList.remove(day2.getDayNumber());
		timeTableDayList.add(day2.getDayNumber(), day2);
	}
	
	public void reOrderDay(int fromDay, int toDay) {
		TimeTableDay day = timeTableDayList.remove(fromDay);
		day.setDayNumber(toDay);
		timeTableDayList.add(toDay, day);
		int numElements = timeTableDayList.size();
		for(int i = toDay+1; i < numElements; i++) {
			timeTableDayList.get(i).setDayNumber(i+1);
		}
	}
	
	public void reverseTimeTable(TimeTableDay curDay) {
		timetableGenerator.unscheduleDay(curDay);
	}
	
	public Iterator<TimeTableDay> getTimeTableIterator() {
		iterator = new TTIterator(this);
		return iterator;
	}
	
	protected List<TimeTableDay> getCompleteTimeTable() {
		return timeTableDayList;
	}
	
	/*public static void main(String[] args) {
		try {
			TimeTable tt = new TimeTable(args[0], args[1], 6, 7);
			Iterator<TimeTableDay> myIterator = tt.getTimeTableIterator();
			while(myIterator.hasNext()) {
				System.out.println(myIterator.next());
			}
			//createTimeTable(args[0], args[1]);
		} catch(Throwable e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(0);
		}
	}*/
}

class TTIterator implements Iterator<TimeTableDay>{

	private int counter = 0;
	private GenericTimeTable tt;
	private List<TimeTableDay> myList;
//	private boolean removeToggle = false;
	
	TTIterator(GenericTimeTable myTT) {
		tt = myTT;
		myList = tt.getCompleteTimeTable();
		counter = 0;
	}
	
	@Override
	public boolean hasNext() {
		if(counter < myList.size()) return true;
		return false;
	}

	@Override
	public TimeTableDay next() {
		if(counter < myList.size()) {
//			removeToggle = true;
			return myList.get(counter++);
		}
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
//		if((removeToggle) && (counter > 0)) {
//			removeToggle = false;
//			tt.reverseTimeTable(myList.get(--counter));
//		}
	}
	
}