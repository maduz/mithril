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
public class RuleBasedTimeTable extends TimeTable {
	
	public RuleBasedTimeTable(Config runConfig) throws Throwable {
		CourseDetails courseDetails = CourseDetails.getCourseDetails(runConfig.getCourseDetailsFile());
	}

	@Override
	public Iterator<TimeTableDay> getTimeTableIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}