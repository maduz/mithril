/**
 * 
 */
package model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Akshay
 *
 */
public class TimeTableSession {

	int rooms, courseCounter = 0;
	List<TimeTableCourse> courses;
	Integer color;
	private static final TimeTableCourse EMPTY = new TimeTableCourse(-9999, -9999, null, null, 0);
	
	/**
	 * 
	 * @param numRooms number of rooms to be scheduled in this session
	 */
	public TimeTableSession(int numRooms) {
		rooms = numRooms;
		courses = new ArrayList<TimeTableCourse>();
		
		for(int i = 0; i < rooms; i++) courses.add(i, EMPTY);
	}
	
	/**
	 * 
	 * @param c
	 */
	public void setColor(Integer c) {
		color = c;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getColor() {
		return color;
	}
	
	/**
	 * 
	 * @return the number of rooms
	 */
	public int getRooms() {
		return rooms;
	}
	
	/**
	 * 
	 * @return all the courses
	 */
	public List<TimeTableCourse> getCourses() {
		return courses;
	}
	
	/**
	 * 
	 * @param room room number (index begins with 0)
	 * @return
	 */
	public TimeTableCourse getCourse(int room) {
		if(room < 0) throw new InvalidParameterException("room number should be greater than or equal to zero");
		TimeTableCourse course = courses.get(room);
		if(course.equals(EMPTY)) return null;
		return course;
	}
	
	public void reOrderCourse(int fromRoom, int toRoom) throws TimeTableSessionException {
		if((fromRoom > courses.size())||(toRoom > courses.size())) throw new TimeTableSessionException("Invalid room number specified");
		TimeTableCourse course = courses.remove(fromRoom);
		courses.add(toRoom, course);
	}
	
	/**
	 * 
	 * @param course
	 * @throws TimeTableSessionException 
	 * @throws Exception 
	 */
//	public void addCourse(Integer course) throws TimeTableSessionException {
//		if(courseCounter==rooms) throw new TimeTableSessionException("No more courses can be added");
//		if(course >= 0) {
//			courses.add(course);
//		} else throw new TimeTableSessionException("Course number should be greater than or equal to zero");
//	}
	
	/**
	 * 
	 * @param timeTableCourse
	 * @param room
	 * @throws TimeTableSessionException 
	 */
	public void addCourse(TimeTableCourse timeTableCourse, int room) throws TimeTableSessionException {
		//if(timeTableCourse < 0) throw new TimeTableSessionException("Course number should be greater than or equal to zero");
		if((room < 0) || (room >= rooms)) throw new TimeTableSessionException("Invalid room number specified");
		if(courses.get(room).equals(EMPTY)) courseCounter++;
		else throw new TimeTableSessionException("session-room is already allotted. Try \"TimeTableSession.removeCourse()\" " +
				"and then add using TimeTableSession.addCourse()");
		courses.add(room,timeTableCourse);
	}

	/**
	 * 
	 * @return
	 */
	public int getNumberOfCourses() {
		return courseCounter;
	}
	
	/**
	 * 
	 * @param room
	 * @return
	 * @throws TimeTableSessionException 
	 */
	public TimeTableCourse removeCourse(int room) throws TimeTableSessionException {
		if((room < 0) || (room >= rooms)) throw new TimeTableSessionException("Invalid room number specified");
		TimeTableCourse retVal = courses.get(room);
		if(!retVal.equals(EMPTY)) {
			courses.add(room, EMPTY);
			courseCounter--;
			return retVal;
		} else throw new TimeTableSessionException("There is no course to remove");
	}
	
	@Override
	public String toString() {
		String retVal="";
		for(int i = 0; i < rooms; i++) {
			TimeTableCourse course = courses.get(i);
			if(EMPTY.equals(course)) {
				retVal += "[    ]";
			} else {
				retVal += String.format("[%4d]", course.getCourseNumber());
			}
		}
		return retVal;
	}
}

final class TimeTableSessionException extends Exception {

	public TimeTableSessionException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}