/**
 * 
 */
package model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;


/**
 * @author Akshay
 *
 */
public class TimeTableGenerator {
	
	/* 
	 * the number of courses
	 */
    private int numCourses = 10;
    
    /*
     * it indicates the conflict matrix for courses 
     */
    private int courseConflictMatrix [] [];

    // number of Class Rooms available
    private int rooms = 8;
    
    //array of number of minimum required sessions for each course
    private int sessionsRequirement [];
    private Integer courseOrder[], inverseCourseOrder[];
    
    private Map<Integer, TimeTableCourse> courseMap = new HashMap<Integer, TimeTableCourse>();
    private Map<Integer, Integer> courseIndexNumberMap = new HashMap<Integer, Integer>();
    
    //number of sessions in a classroom per day
    private int sessionsPerDay = 7;

	private PrintStream myOut = System.out;
	
	private int maxColor = 0;
	private boolean courseCompleted[];

	private int[] colors;
	private MySortedIntegerMap colorSessions = new MySortedIntegerMap();

	private Map<Integer, List<Integer>> colorToSets;
	
	/**
	 * Sets the printStream to which output should be sent. Default is System.Out
	 * @param os
	 */
	public void setOutputStream(PrintStream os) {
		myOut = os;
	}

	/**
	 * reads the following information about courses from a CSV file only
	 * 1. Course Number
	 * 2. Course Name
	 * 3. Instructor Name
	 * 4. Required Sessions
	 * @param reader a CSV file where course information is stored
	 * @throws Throwable
	 */
	private void readCourseData(CourseFileReader reader) throws Throwable {
		
		for (Iterator<TimeTableCourse> iterator = reader.getCourseList().iterator(); iterator.hasNext();) {
			TimeTableCourse course = (TimeTableCourse) iterator.next();
			courseMap.put(course.getCourseNumber(), course);
			courseIndexNumberMap.put(course.getCourseIndex(), course.getCourseNumber());
		}
		
		numCourses = reader.getNumberOfCourses();
		sessionsRequirement = new int[numCourses];
		courseOrder = new Integer[numCourses];
		inverseCourseOrder = new Integer[numCourses];
		courseCompleted = new boolean[numCourses];
		
		for(int i = 0; i < numCourses; i++)
			courseCompleted[i] = false;
		
		//sortSessions(coursesSessionsMap);
		sortSessions();
		
		
		//initialize course conflict matrix
		courseConflictMatrix = new int[numCourses][numCourses];
		for(int i = 0; i < numCourses; i++)
			for(int j = i; j < numCourses; j++)
				if(i==j) {
					courseConflictMatrix[i][j] = 1;
					courseConflictMatrix[i][j] = courseConflictMatrix[j][i] = 0;
				}
		
//		Integer[] allCourses = (Integer[]) courseInstructorMap.keySet().toArray(new Integer[numCourses]);
		
		//mark course conflict matrix if two courses have the same instructor
		Integer[] keys = new Integer[numCourses]; 
		keys = (Integer[]) courseMap.keySet().toArray(keys);
		
		for(int courseIndex = 0; courseIndex < numCourses; courseIndex++) {
			for(int idx = courseIndex+1; idx < numCourses; idx++) {
				TimeTableCourse course1 = courseMap.get(keys[courseIndex]);
				TimeTableCourse course2 = courseMap.get(keys[idx]);
				if(course1.getInstructorName().equals(course2.getInstructorName())) {
					courseConflictMatrix[courseOrder[course1.getCourseIndex()]][courseOrder[course2.getCourseIndex()]] = courseConflictMatrix[courseOrder[course2.getCourseIndex()]][courseOrder[course1.getCourseIndex()]] = 1;
				}
			}
		}
		
		return;
	}
	
	private void sortSessions() {
		
		Set<TimeTableCourse> sessionList = new TreeSet<TimeTableCourse>(new Comparator<TimeTableCourse>() {
			@Override
			public int compare(TimeTableCourse o1, TimeTableCourse o2) {
				// TODO Auto-generated method stub
				if(o1 == null) {
					if(o2 == null) return 0;
					return 1;
				}
				if(o1.equals(o2)) return 0;
//				if(o1.getSessionsRequired() == o2.getSessionsRequired()) return 0;
				if(o1.getSessionsRequired() >= o2.getSessionsRequired()) return -1;
				return 1;
			}			
		});
		
		for (Entry<Integer, TimeTableCourse> nextEntry: courseMap.entrySet()) {
			//myOut.println("Key: " + nextEntry.getKey());
			sessionList.add((TimeTableCourse) nextEntry.getValue());
		}
		
		int sessionCounter = 0;

		for (TimeTableCourse course: sessionList) {
//			sessionStructure obj = (sessionStructure) iter.next();
//			myOut.println(course);
			sessionsRequirement[sessionCounter] = course.getSessionsRequired();
			courseOrder[course.getCourseIndex()] = sessionCounter;
			inverseCourseOrder[sessionCounter++] = course.getCourseIndex();
		}

//		for (int counter = 0; counter < sessionsRequirement.length; counter++) {
//			myOut.println("course: " +  inverseCourseOrder[counter] + ", sessions: " + sessionsRequirement[counter]);
//		}
		
	}
	
	/**
	 * Read details of which courses students have selected from a CSV file. Typically,
	 * the information in the file will be in the following format
	 * (student Name or Id)(course number)'
	 * 
	 * @param courseStudentFileName a CSV file in which student-course data is available
	 * @throws Throwable
	 */
    private void readCourseStudentData(String courseStudentFileName) throws Throwable {
    	
    	if(courseStudentFileName == null || courseStudentFileName.isEmpty()) return;

    	String[] record;
    	CSVFileReader inputFileReader = new CSVFileReader(courseStudentFileName, true);

    	//mark the course conflict matrix for a pair of courses if 
    	// the same student has opted for the two courses
    	while((record = inputFileReader.getNextRecord()) != null) {
//    		printCourseConflictMatrix();
    		for(int i = 1; i < record.length; i++) {
    			for(int j = i+1; j < record.length; j++) {
    				TimeTableCourse course1 = courseMap.get(Integer.parseInt(record[i]));
    				TimeTableCourse course2 = courseMap.get(Integer.parseInt(record[j]));
    				courseConflictMatrix[courseOrder[course1.getCourseIndex()]][courseOrder[course2.getCourseIndex()]] = courseConflictMatrix[courseOrder[course2.getCourseIndex()]][courseOrder[course1.getCourseIndex()]] = 1;
    			}
    		}
    	}
    	
    	//printCourseConflictMatrix();
    	
    	inputFileReader.close();

    	return;
    }
    
    /**
     * Assigns colors to courses
     * @return
     */
    private int[] assignColors() {
		// All colours are set to zero
		int colors[] = new int[numCourses];
        for (int i = 0; i < numCourses; i++) 
                colors[i] = 0;

        /*
         * I think this piece of code assigns a common colour to all courses that are not conflicting with a course
         * Going in order, this will ensure that the most conflicting courses are given the highest color code while
         * the least conflicting course will be given the lowest color code
         */
        maxColor = 0;
        for (int i = 0; i < numCourses; i++)
            for (int j = i+1; j < numCourses; j++)
            {
                if ((courseConflictMatrix[i] [j] == 1) && (colors[inverseCourseOrder[j]] == colors[inverseCourseOrder[i]]))
                {
                    colors[inverseCourseOrder[j]] = colors[inverseCourseOrder[i]] + 1;
                    if (colors[inverseCourseOrder[j]] > maxColor) maxColor = colors[inverseCourseOrder[j]];
                }
            }
        
        colorSessions.clear();
        
        for(int i = 0; i < numCourses; i++) {
        	Integer curSum = colorSessions.get(colors[i]);
        	colorSessions.put(colors[i], (curSum == null? 0 : curSum) + sessionsRequirement[courseOrder[i]]);
        }
        
//		int sum = 0;
//
//        for(Integer color: colorSessions.keyList()) {
//        	myOut.println("Color: " + color + ", Sessions: " +  colorSessions.get(color));
//        	sum += colorSessions.get(color);
//        }
//        
//        myOut.println("maxColor: " + maxColor + "; Sum: " + sum);

        return colors;
    }

    /**
     * Returns first N courses from the list who still have outstanding session requirements
     * @param list list of course index numbers
     * @param N number of courses
     * @return list with index numbers of N courses
     */
    private List<Integer> getNElementsFromList(List<Integer> list, int N) {
    	if(list == null) return null;
    	List<Integer> retList = new ArrayList<Integer>();
    	int idx = 0, counter = 0;
    	int listSize = list.size();
    	while((counter < N)&&(idx<listSize)) {
    		if(sessionsRequirement[courseOrder[list.get(idx)]]>0) {
    			retList.add(list.get(idx));
    			counter++;
    		}
    		idx++;
    	}
    	return retList;
    }
    
    private void makeColortoSubsets(int[] colors) {
        /*
         * This piece of code creates a map for each colour and the corresponding courses
         */
        colorToSets = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < numCourses; i++)
        {
            if (false == colorToSets.containsKey(colors[i]))
            {
                colorToSets.put(colors[i], new ArrayList<Integer>());
            }

            colorToSets.get(colors[i]).add(i);
        }
        
        for(Integer color: colorToSets.keySet()) {
    		Collections.sort(colorToSets.get(color), new Comparator<Integer>(){

    			@Override
    			public int compare(Integer o1, Integer o2) {
    				// TODO Auto-generated method stub
    				if(o1 == null) {
    					if(o2 == null) return 0;
    					else return 1;
    				}
    				int course1 = o1.intValue();;
    				int course2 = o2.intValue();;
    				if(o1.equals(o2)) return 0;
    				if(course1 == course2) return 0;
    				if(sessionsRequirement[courseOrder[course1]] == sessionsRequirement[courseOrder[course2]]) {
    					if(course1<course2) return 1;
    					else return -1;
    				}
    				if(sessionsRequirement[courseOrder[course1]] > sessionsRequirement[courseOrder[course2]]) return -1;
    				return 1;
    			}

    		});

        }
        
//        for(Entry<Integer, Integer> entry: colorSessions.entryList()) {
//        	List<Integer> courses = colorToSets.get(entry.getKey());
//        	System.out.print("color: " + entry.getKey() + ": ");
//        	for(Integer course: courses)
//        		System.out.print(course + " ");
//        	System.out.println();
//        }
    }
    
     /**
     * Create a new Time Table
     * @param cfName file that contains course details
     * @param saName file that contains student course allocation details
     * @throws Throwable
     */
    public void createTimeTable(CourseFileReader reader, String saName) throws Throwable {
		//read input data and exit if exceptions are raised
		readCourseData(reader);
		readCourseStudentData(saName);

		//PROCEED WITH THE REST OF THE ALGORITHM
		colors = assignColors();
		makeColortoSubsets(colors);
    }
   
    /**
     * 
     * @param color
     * @return
     */
    public List<TimeTableCourse> getCoursesForColor(int color) {
    	List<Integer> courses = colorToSets.get(color);
    	List<TimeTableCourse> retVal = new ArrayList<TimeTableCourse>();
    	for(Integer courseIdx: courses) {
    		retVal.add(courseMap.get(courseIndexNumberMap.get(courseIdx)));
    	}
    	return retVal;
    }

    /**
     * 
     * @param day
     * @param currentSession 
     * @return
     * @throws TimeTableSessionException 
     */
    public boolean scheduleDay(TimeTableDay curDay, int numRooms) throws TimeTableSessionException {

    	rooms = numRooms;
		//colorToSubsets = makeColortoSubsets(colors);
		
		int sessionCounter = 0;
        boolean continueCreating = true;

        sessionsPerDay = curDay.getSessionCapacity();
        int nextColor = -1;
//        myOut.println("Day: " + curDay.getDayNumber());
//        myOut.println("");
        List<Integer> colorList = colorSessions.keyList();
        while(curDay.getSessions().size() < sessionsPerDay) {
        	TimeTableSession curSession = new TimeTableSession(rooms);
            nextColor = (nextColor+1)%(maxColor+1); 
        	if(!scheduleSession(sessionCounter, ((Integer) colorList.get(nextColor)).intValue(), curSession)) {
        		continueCreating = false;
                break;
        	}
        	if(curSession.getNumberOfCourses() > 0)
        		curDay.addSession(curSession);
//        	sessionCounter++;
        }
        
        return continueCreating;
    }
    
    /**
     * 
     * @param curDay
     */
    public void unscheduleDay(TimeTableDay curDay) {
    	List<TimeTableSession> sessions = curDay.getSessions();
    	for(TimeTableSession session: sessions) {
    		List<TimeTableCourse> courses = session.getCourses();
    		for(TimeTableCourse course: courses) {
    			sessionsRequirement[courseOrder[course.getCourseIndex()]]++;
    			courseCompleted[course.getCourseIndex()] = false;
    			int color = colors[course.getCourseIndex()];
    			colorSessions.put(color, colorSessions.get(color)+1);
    		}
    	}
    }
    
    /**
     * 
     * @param session
     * @param color
     * @param currentSession 
     * @return
     * @throws TimeTableSessionException 
     */
    protected boolean scheduleSession(int session, int color, TimeTableSession currentSession) throws TimeTableSessionException {
    	
    	boolean continueCreating = true;
    	currentSession.setColor(color);
    	//while(!scheduleColor((Integer) ((Entry) colorSessions.entryList().get(color)).getKey(), currentSession)) {
//    	while(!scheduleColor(color, currentSession)) {
//    		color = (color+1)%(maxColor+1);
//    		currentSession.setColor(color);
//    	}

    	scheduleColor(color, currentSession);
    	int hoursRemaining = 0;
        for (int hour : sessionsRequirement)
        {
            hoursRemaining += hour;
        }

        if (hoursRemaining == 0) {
        	continueCreating = false;
        }
        return continueCreating;
    }
    
    /**
     * 
     * @param color
     * @param currentSession 
     * @return
     * @throws TimeTableSessionException 
     */
    protected boolean scheduleColor(int color, TimeTableSession currentSession) throws TimeTableSessionException {

    	boolean sessionUsed = false;
    	//    	myOut.println("scheduling: " + color);
    	int roomCounter = rooms;
    	for(int course: getNElementsFromList(colorToSets.get(color), rooms))
    	{
    		if (sessionsRequirement[courseOrder[course]] == 0)
    		{
    			if(!courseCompleted[course]) {
    				courseCompleted[course] = true;
    				return sessionUsed;
    			}
    			continue;
    		}
    	}

    	for(int course:getNElementsFromList(colorToSets.get(color), rooms)) {
    		roomCounter--;
    		if (sessionsRequirement[courseOrder[course]] == 0)
    		{
    			continue;
    		}

    		currentSession.addCourse(courseMap.get(courseIndexNumberMap.get(course)), (rooms-roomCounter-1));
    		--sessionsRequirement[courseOrder[course]];
    		sessionUsed = true;
    	}

    	return sessionUsed;
    }

}
