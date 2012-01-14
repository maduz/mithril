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
public class TimeTableIterator implements Iterator<TimeTableDay>{

	int day, numRooms, numSessions;
	TimeTableGenerator timetableGenerator = new TimeTableGenerator();
	List<TimeTableDay> timeTable = new ArrayList<TimeTableDay>(); 
	
	boolean continueCreation = true;
	/**
	 * 
	 * @param cfName Course details file name
	 * @param saName Student Course Allocation details file name
	 * @throws Throwable 
	 */
	public TimeTableIterator(String cfName, String saName, int rooms, int sessions) throws Throwable {
		numRooms = rooms;
		numSessions = sessions;
		timetableGenerator.createTimeTable(cfName, saName);
		day = 1;
	}
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return continueCreation;
	}

	@Override
	public TimeTableDay next() {
		// TODO Auto-generated method stub
		TimeTableDay nextDay = new TimeTableDay(day++, numSessions);
		try {
			continueCreation = timetableGenerator.scheduleDay(nextDay, numRooms);
		} catch (TimeTableSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		timeTable.add(nextDay);
		return nextDay;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	

}
