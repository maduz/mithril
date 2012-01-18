package model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class CourseDetails {
	private int numCourses;
	
	private Map<Integer, TimeTableCourse> courseMap = new HashMap<Integer, TimeTableCourse>();
	private Map<Integer, Integer> courseIndexNumberMap = new HashMap<Integer, Integer>();
	
	private boolean courseCompleted[];
	
    private int sessionsRequirement [];
    private Integer courseOrder[], inverseCourseOrder[];
    
    private static CourseDetails _details = null;
	
	public static CourseDetails getCourseDetails(String courseFileName) throws Throwable {
		if(_details != null) return _details;
		
		if(courseFileName == null || courseFileName.isEmpty()) return null;
		_details = new CourseDetails();
		
		String[] record;
		CSVFileReader inputFileReader = new CSVFileReader(courseFileName);
		
		//The input file is expected to have the following information in the order indicated below
		int COURSE_NAME_INDEX = 1;
		int COURSE_NUMBER_INDEX = 0;
		int COURSE_INSTRUCTOR_INDEX = 2;
		int COURSE_SESSIONS_INDEX = 3;
		
		_details.numCourses = 0;

		//read data from file and create a map of course-instructor and course-sessions
		while((record = inputFileReader.getNextRecord()) != null) {
			TimeTableCourse course = new TimeTableCourse(_details.numCourses, Integer.parseInt(record[COURSE_NUMBER_INDEX]), record[COURSE_NAME_INDEX], record[COURSE_INSTRUCTOR_INDEX], Integer.parseInt(record[COURSE_SESSIONS_INDEX]));
			_details.courseMap.put(course.getCourseNumber(), course);
			_details.courseIndexNumberMap.put(course.getCourseIndex(), course.getCourseNumber());
			_details.numCourses++;
		}
		
		
		_details.sessionsRequirement = new int[_details.numCourses];
		_details.courseOrder = new Integer[_details.numCourses];
		_details.inverseCourseOrder = new Integer[_details.numCourses];
		_details.courseCompleted = new boolean[_details.numCourses];
		
		for(int i = 0; i < _details.numCourses; i++)
			_details.courseCompleted[i] = false;
		
		_details.sortSessions();
		inputFileReader.close();
		
		return _details;
	}
	
	private void sortSessions() {
			Set<TimeTableCourse> sessionList = new TreeSet<TimeTableCourse>(new Comparator<TimeTableCourse>() {
			@Override
			public int compare(TimeTableCourse o1, TimeTableCourse o2) {
				if(o1 == null) {
					if(o2 == null) return 0;
					return 1;
				}
				if(o1.equals(o2)) return 0;
				if(o1.getSessionsRequired() >= o2.getSessionsRequired()) return -1;
				return 1;
			}			
		});
		
		for (Entry<Integer, TimeTableCourse> nextEntry: courseMap.entrySet()) {
			sessionList.add((TimeTableCourse) nextEntry.getValue());
		}
		
		int sessionCounter = 0;

		for (TimeTableCourse course: sessionList) {
			sessionsRequirement[sessionCounter] = course.getSessionsRequired();
			courseOrder[course.getCourseIndex()] = sessionCounter;
			inverseCourseOrder[sessionCounter++] = course.getCourseIndex();
		}
	}
	
	public Iterator<TimeTableCourse> getCourses() {
		return courseMap.values().iterator();
	}
}
