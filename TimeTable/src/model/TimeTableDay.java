/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akshay
 *
 */
public class TimeTableDay {

	private int day, numSessions;
	private List<TimeTableSession> sessionList = new ArrayList<TimeTableSession>();
	
	TimeTableDay(int dayNumber, int sessions) {
		day = dayNumber;
		numSessions = sessions;
	}
	
	public void setDayNumber(int dayNum) {
		day = dayNum;
	}
	
	public void setSessionCapacity(int sessions) {
		numSessions = sessions;
	}
	
	public int getSessionCapacity() {
		return numSessions;
	}
	
	public int getDayNumber() {
		return day;
	}
	
	public void addSession(TimeTableSession session) {
		if(sessionList.size() < numSessions) {
			sessionList.add(session);
		}
	}
	
	public List<TimeTableSession> getSessions() {
		return sessionList;
	}
	
	public void reOrderSessions(int fromPos, int toPos) {
		if((toPos >= 0) && (toPos < numSessions) && (fromPos >= 0) && (fromPos < numSessions)) {
			TimeTableSession session = sessionList.remove(fromPos);
			sessionList.add(toPos, session);
		}
	}
	
	public TimeTableSession removeSession(int pos) {
		return sessionList.remove(pos);
	}
	
	@Override
	public String toString() {
		String retVal;
		retVal = "Day " + day+":\n";
		for(TimeTableSession session: sessionList) {
			retVal+=(session+"\n");
		}
		return retVal;
	}
}
