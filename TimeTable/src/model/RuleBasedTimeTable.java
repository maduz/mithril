/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Akshay
 *
 */
public class RuleBasedTimeTable extends TimeTable implements Iterator<TimeTableDay> {
	
	private Date startDate, endDate;
	boolean[][] dayPreferences;
	TimeTableCourse[] courses;
	int count = 0;
	int numSessions = 0, numRooms = 0;
	
	public RuleBasedTimeTable(Config runConfig) throws Throwable {
		CourseDetails courseDetails = CourseDetails.getCourseDetails(runConfig.getCourseDetailsFile());
		if(courseDetails == null) throw new Exception("Invalid course details file");
		
		courses = courseDetails.getCoursesAsArray();
		numSessions = runConfig.getSessionsPerDay();
		numRooms = runConfig.getRoomsPerSession();
		
		dayPreferences = runConfig.getDayPreferences();
		startDate = runConfig.getStartDate();
		endDate = runConfig.getEndDate();
		
		/*
		int count = 0;
		int coursesCount = dayPreferences.length;
		while(startDate.before(runConfig.getEndDate())) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE, 1);
			
			startDate = calendar.getTime();
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) continue;
			
			List<TimeTableCourse> candidateCourses = new ArrayList<TimeTableCourse>();
			for(int courseIndex = 0; courseIndex < coursesCount; courseIndex++) {
				if(dayPreferences[courseIndex][count])
					candidateCourses.add(courses[courseIndex]);
			}
			
			count++;
		}
		*/
	}

	@Override
	public Iterator<TimeTableDay> getTimeTableIterator() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean hasNext() {
		return startDate.before(endDate);
	}

	@Override
	public TimeTableDay next(){
		while(true) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE, 1);
			
			startDate = calendar.getTime();
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) continue;
			
			List<TimeTableCourse> candidateCourses = new ArrayList<TimeTableCourse>();
			for(int courseIndex = 0; courseIndex < courses.length; courseIndex++) {
				if(dayPreferences[courseIndex][count] && (calendar.get(Calendar.DAY_OF_WEEK)/5 == courses[courseIndex].getCourseIndex()%2) )
					candidateCourses.add(courses[courseIndex]);
			}
			
			count++;
			
			TimeTableDay day = new TimeTableDay(count, numSessions);
			
			Random random = new Random();
			while(day.getSessions().size() < numSessions) {
				TimeTableSession session = new TimeTableSession(numRooms);
				
				int room = 0;
				while(room < numRooms) {
					try {
						session.addCourse(candidateCourses.get(random.nextInt(candidateCourses.size())), room);
					} catch (TimeTableSessionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					room++;
				}
				
				day.addSession(session);
			}
			
			return day;
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	

	
}