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
public class CourseFileReader {
	
	private List<TimeTableCourse> courseList = null;
	private List<String> instructorList = null;
	
	private int numCourses;
	
	public CourseFileReader(String courseFileName) throws Exception {
		courseList = new ArrayList<TimeTableCourse>();
		instructorList = new ArrayList<String>();
		
		readFile(courseFileName);
	}
	
	private void readFile(String courseFileName) throws Exception {
		if(courseFileName == null || courseFileName.isEmpty()) 
			throw new Exception("Invalid File Name");
		
		String[] record;
		CSVFileReader inputFileReader = new CSVFileReader(courseFileName);
		
		//The input file is expected to have the following information in the order indicated below
		int COURSE_NAME_INDEX = 1;
		int COURSE_NUMBER_INDEX = 0;
		int COURSE_INSTRUCTOR_INDEX = 2;
		int COURSE_SESSIONS_INDEX = 3;
		
		numCourses = 0;

		//read data from file and create a map of course-instructor and course-sessions
		while((record = inputFileReader.getNextRecord()) != null) {
			TimeTableCourse course = new TimeTableCourse(numCourses, Integer.parseInt(record[COURSE_NUMBER_INDEX]), record[COURSE_NAME_INDEX], record[COURSE_INSTRUCTOR_INDEX], Integer.parseInt(record[COURSE_SESSIONS_INDEX]));
			courseList.add(course);
			instructorList.add(record[COURSE_INSTRUCTOR_INDEX]);
			numCourses++;
		}

		inputFileReader.close();
		
		return;
	}
	
	public List<TimeTableCourse> getCourseList() {
		return courseList;
	}
	
	public List<String> getInstructorList() {
		return instructorList;
	}
	
	public int getNumberOfCourses() {
		return numCourses;
	}
}
