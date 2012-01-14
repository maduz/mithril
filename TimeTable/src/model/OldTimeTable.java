package model;


import java.io.PrintStream;
import java.util.*;
import java.util.Map.Entry;




/**
 * This class is the starting point of the TimeTable program.
 * @author Akshay
 *
 */

public class OldTimeTable {
	
	/* 
	 * the number of courses
	 */
    static int numCourses = 10;
    
    /*
     * it indicates the conflict matrix for courses 
     */
    static int courseConflictMatrix [] [];

    // number of Class Rooms available
    static int rooms = 8;
    
    //array of number of minimum required sessions for each course
    static int sessionsRequirement [];
    static Integer courseOrder[], inverseCourseOrder[];
    
    //number of sessions in a classroom per day
    static int sessionsPerDay = 7;

	static Map<String, List<String>> courseStudentMap = new HashMap<String, List<String>> ();

	static PrintStream myOut = System.out;
	
	static int maxColor = 0;
	static boolean courseCompleted[];

	private static int[] colors;
	private static MySortedIntegerMap colorSessions = new MySortedIntegerMap();

	private static Map<Integer, List<List<Integer>>> colorToSubsets;

	private static Map<Integer, List<Integer>> colorToSets;
	
	/**
	 * Sets the printStream to which output should be sent. Default is System.Out
	 * @param os
	 */
	public static void setOutputStream(PrintStream os) {
		myOut = os;
	}

	/**
	 * reads the following information about courses from a CSV file only
	 * 1. Course Number
	 * 2. Course Name
	 * 3. Instructor Name
	 * 4. Required Sessions
	 * @param courseFileName a CSV file where course information is stored
	 * @throws Throwable
	 */
	private static void readCourseData(String courseFileName) throws Throwable {
		String[] record;
		CSVFileReader inputFileReader = new CSVFileReader(courseFileName);
		
//		Comparator<Integer> myComparator = new Comparator<Integer>() {
//			
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				if(o1.intValue() < o2.intValue()) return -1;
//				if(o1.equals(o2)) return 0;
//				return 1;
//			}
//		};
		
		Map<Integer, String> courseInstructorMap = new HashMap<Integer, String> ();
		
		Map<Integer, Integer> coursesSessionsMap = new HashMap<Integer, Integer>();

		//The input file is expected to have the following information in the order indicated below
		//int COURSE_NAME_INDEX = 1;
		int COURSE_NUMBER_INDEX = 0;
		int COURSE_INSTRUCTOR_INDEX = 2;
		int COURSE_SESSIONS_INDEX = 3;
		
		numCourses = 0;

		//read data from file and create a map of course-instructor and course-sessions
		while((record = inputFileReader.getNextRecord()) != null) {
			numCourses++;
			courseInstructorMap.put(new Integer(record[COURSE_NUMBER_INDEX]), record[COURSE_INSTRUCTOR_INDEX]);
			coursesSessionsMap.put(new Integer(record[COURSE_NUMBER_INDEX]), new Integer(record[COURSE_SESSIONS_INDEX]));
		}
		
		
		sessionsRequirement = new int[numCourses];
		courseOrder = new Integer[numCourses];
		inverseCourseOrder = new Integer[numCourses];
		courseCompleted = new boolean[numCourses];
		
		for(int i = 0; i < numCourses; i++)
			courseCompleted[i] = false;
		
		sortSessions(coursesSessionsMap);
		
		
		//initialize course conflict matrix
		courseConflictMatrix = new int[numCourses][numCourses];
		for(int i = 0; i < numCourses; i++)
			for(int j = i; j < numCourses; j++)
				if(i==j)
					courseConflictMatrix[i][j] = 1;
				else
					courseConflictMatrix[i][j] = courseConflictMatrix[j][i] = 0;

		
		Integer[] allCourses = (Integer[]) courseInstructorMap.keySet().toArray(new Integer[numCourses]);
		
		//mark course conflict matrix if two courses have the same instructor
		for(int courseIndex = 0; courseIndex < numCourses; courseIndex++) {
			for(int idx = courseIndex+1; idx < numCourses; idx++) {
				if(courseInstructorMap.get(allCourses[courseIndex]).equals(courseInstructorMap.get(allCourses[idx]))) {
					courseConflictMatrix[courseOrder[allCourses[courseIndex]-1]][courseOrder[allCourses[idx]-1]] = courseConflictMatrix[courseOrder[allCourses[idx]-1]][courseOrder[allCourses[courseIndex]-1]] = 1;
				}
			}
		}
		
		inputFileReader.close();
		
		return;
	}
	
	/**
	 * @param coursesSessionsMap 
	 * 
	 */
	private static void sortSessions(Map<Integer, Integer> coursesSessionsMap) {
		class sessionStructure {
			int courseNum, sessions;
			sessionStructure(int c, int s) {
				courseNum = c;
				sessions = s;
			}
			
			public String toString() {
				return "sessionStructure: course: " + courseNum + ", sessions: " + sessions;
			}
		}
		
		Set<sessionStructure> sessionList = new TreeSet<sessionStructure>(new Comparator<sessionStructure>() {
			@Override
			public int compare(sessionStructure o1, sessionStructure o2) {
				if(o1.equals(o2)) return 0;
				if(o1.sessions > o2.sessions) return -1;
				return 1;
			}			
		});
		
		for (Iterator idx = coursesSessionsMap.entrySet().iterator(); idx.hasNext(); ) {
			Entry nextEntry = (Entry) idx.next();
			int key = ((Integer) nextEntry.getKey()).intValue()-1;
			int value = ((Integer) nextEntry.getValue()).intValue();
//			myOut.println("key: " + key + ", value: " + value);
			sessionList.add(new sessionStructure(key, value));
		}
		
		int sessionCounter = 0;
		
		/*
		 * courseOrder stores the order of courses (session-wise) for every course
		 * inverseCourseOrder stores the course number for every position in the order (session-wise)
		 */
		
		for (Iterator iter = sessionList.iterator(); iter.hasNext();) {
			sessionStructure obj = (sessionStructure) iter.next();
//			myOut.println(obj);
			sessionsRequirement[sessionCounter] = obj.sessions;
			courseOrder[obj.courseNum] = sessionCounter;
			inverseCourseOrder[sessionCounter++] = obj.courseNum;
		}
		
		for (int counter = 0; counter < sessionsRequirement.length; counter++) {
			myOut.println("course: " +  inverseCourseOrder[counter] + ", sessions: " + sessionsRequirement[counter]);
		}
	}
	
	/**
	 * Read details of which courses students have selected from a CSV file. Typically,
	 * the information in the file will be in the following format
	 * (student Name or Id)(course number)'
	 * 
	 * @param courseStudentFileName a CSV file in which student-course data is available
	 * @throws Throwable
	 */
    private static void readCourseStudentData(String courseStudentFileName) throws Throwable {

    	String[] record;
    	CSVFileReader inputFileReader = new CSVFileReader(courseStudentFileName, true);

    	//mark the course conflict matrix for a pair of courses if 
    	// the same student has opted for the two courses
    	while((record = inputFileReader.getNextRecord()) != null) {
//    		printCourseConflictMatrix();
    		for(int i = 1; i < record.length; i++) {
    			for(int j = i+1; j < record.length; j++) {
    				int refCourse = Integer.parseInt(record[i])-1;
    				int compareCourse = Integer.parseInt(record[j])-1;
    				courseConflictMatrix[courseOrder[refCourse]][courseOrder[compareCourse]] = courseConflictMatrix[courseOrder[compareCourse]][courseOrder[refCourse]] = 1;
    			}
    		}
    	}
    	
    	//printCourseConflictMatrix();
    	
    	inputFileReader.close();

    	return;
    }
    
    /**
     * Prints the courseConfictMatrix
     */
    private static void printCourseConflictMatrix() {
    	myOut.println("Course Conflict Matrix: ");
    	for (int i = 0; i < numCourses; i++) {
    		for (int j = 0; j < numCourses; j++) {
				myOut.print(courseConflictMatrix[i][j] + " ");
			}
    		myOut.println("");
		}
    }

    /**
     * Assigns colors to courses
     * @return
     */
    private static int[] assignColors() {
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
        
        return colors;
    }
    
    private static void reOrderCourses(int course) {
    	
    	int endButOne = numCourses-1;

    	for(int i = courseOrder[course]; i < endButOne; i++) {
    		for(int j = 0; j < numCourses; j++) {
    			courseConflictMatrix[i][j] = courseConflictMatrix[i+1][j];
    		}
    	}
    	
    	for(int i = courseOrder[course]; i < endButOne; i++) {
    		for(int j = 0; j < numCourses; j++) {
    			courseConflictMatrix[j][i] = courseConflictMatrix[j][i+1];
    		}
    	}
    	
    	for(int i = 0; i < numCourses; i++) {
    		courseConflictMatrix[endButOne][i] = courseConflictMatrix[i][endButOne] = 0;
    	}
    	courseConflictMatrix[endButOne][endButOne] = 1;
    	
    	
    	for(int i = courseOrder[course]; i < endButOne; i++) {
    		sessionsRequirement[i] = sessionsRequirement[i+1];
    		courseOrder[inverseCourseOrder[i+1]]--;
    		inverseCourseOrder[i] = inverseCourseOrder[i+1];
    	}
    	
    	courseOrder[course] = endButOne;
    	sessionsRequirement[endButOne] = 0;
    	inverseCourseOrder[endButOne] = course;

    }
    
    private static Map<Integer, List<List<Integer>>> makeColortoSubsets(int[] colors) {
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
        
        
        ColorToCoursesEnumerator colorsEnum = new ColorToCoursesEnumerator(colorToSets, rooms);
        Map<Integer, List<List<Integer>>> colorToSubsets = new HashMap<Integer, List<List<Integer>>>();
        
        /*
         * 
         */
        int color = -1;
        while(colorsEnum.hasNext()) {
        	Pair<Integer, List<Integer>> pair = colorsEnum.next();
        	if(pair.getLeft() != color) {
        		color = pair.getLeft();
        		
        		List<List<Integer>> parallelCourses = new ArrayList<List<Integer>>();
        		parallelCourses.add(pair.getRight());
        		colorToSubsets.put(color, parallelCourses);
        		continue;
        	}
        	
        	List<List<Integer>> parallelCourses = colorToSubsets.get(color);
        	parallelCourses.add(pair.getRight());
        }
        return colorToSubsets;
    }
    
    /**
     * Create a new Time Table
     * @param cfName file that contains course details
     * @param saName file that contains student course allocation details
     * @throws Throwable
     */
    public static void createOldTimeTable(String cfName, String saName) throws Throwable {
		//read input data and exit if exceptions are raised
		readCourseData(cfName);
		readCourseStudentData(saName);
		
		//printCourseConflictMatrix();
		myOut.println(numCourses);
		
		//PROCEED WITH THE REST OF THE ALGORITHM
		
		int colors[] = assignColors();

		Map<Integer, List<List<Integer>>> colorToSubsets = makeColortoSubsets(colors);
		
        int day = 1;
        int sessionsRemaining = sessionsPerDay;
        Random random = new Random();
        
//        for (int idx = 0; idx < sessionsRequirement.length; idx++) {
//			myOut.println("course: " + idx + "; sessions: " + sessionsRequirement[courseOrder[idx]]);
//		}

        int nextColor = -1;
        boolean sessionUsed = false;
        myOut.println("Day: " + day);
        while (true)
        {

            sessionUsed = false;
            nextColor = (nextColor+1)%(maxColor+1); //random.nextInt(maxColor + 1);
            
            //myOut.println("nextColor: " + nextColor);
            int freeCounter = 0;
            int roomCounter = rooms;
            boolean any = false;
            List<List<Integer>> courses = colorToSubsets.get(nextColor);
            //int coursesSize = courses.size();
            for (int course: courses.get(0))//(int course : courses.get(random.nextInt(courses.size())))
            {
            	//int course = courses.get(crsIdx);
            	roomCounter--;
                if (sessionsRequirement[courseOrder[course]] == 0)
                {
                    //myOut.print("\t| ");
                	if(!courseCompleted[course]) {
                		courseCompleted[course] = true;

                		//this is a potentially dangerous piece of code. remove the next three lines if
                		//there is a disaster
                		reOrderCourses(course);
                		colors = assignColors();
                		colorToSubsets = makeColortoSubsets(colors);
                	}
                	freeCounter++;
                    continue;
                }

                any = true;
                
                while(freeCounter > 0) {
                	myOut.print("[ ]");
                	freeCounter--;
                }
                
                myOut.print("[" + course /*+ ":" + --sessionsRequirement[courseOrder[course]] + */ + "]");
                --sessionsRequirement[courseOrder[course]];
                sessionUsed = true;
                //sessionsRequirement[courseOrder[course]]--;
            }

            if(any) {
                while(roomCounter > 0) {
                	myOut.print("[ ]");
                	roomCounter--;
                }
            	myOut.println();
            }

            int hoursRemaining = 0;
            for (int hour : sessionsRequirement)
            {
                hoursRemaining += hour;
            }

            if (hoursRemaining == 0)
                break;

            if(sessionUsed) sessionsRemaining--;
            if (sessionsRemaining == 0)
            {
                sessionsRemaining = sessionsPerDay;
                day++;
                myOut.println("Day: " + day);
                nextColor = -1;

                int courseId = 0;
                myOut.println();
            }
        }
    }
    
    /**
     * Create a new Time Table
     * @param cfName file that contains course details
     * @param saName file that contains student course allocation details
     * @throws Throwable
     */
    public static void createTimeTable(String cfName, String saName) throws Throwable {
		//read input data and exit if exceptions are raised
		readCourseData(cfName);
		readCourseStudentData(saName);
		
		myOut.println(numCourses);
		
		//PROCEED WITH THE REST OF THE ALGORITHM
		
		colors = assignColors();
		colorToSubsets = makeColortoSubsets(colors);
		
        int day = 1;
        while(scheduleDay(day++));
        
    }

    /**
     * 
     * @param day
     * @return
     */
    public static boolean scheduleDay(int day) {

    	int sessionCounter = 0;
        boolean continueCreating = true;
        
        int nextColor = -1;
        myOut.println("Day: " + day);
        myOut.println("");
        while(sessionCounter < sessionsPerDay) {
            nextColor = (nextColor+1)%(maxColor+1); 
        	if(!scheduleSession(sessionCounter, nextColor)) {
        		continueCreating = false;
                break;
        	}
        	sessionCounter++;
        }
        
        return continueCreating;
    }
    
    /**
     * 
     * @param session
     * @param color
     * @return
     */
    public static boolean scheduleSession(int session, int color) {
    	
    	boolean continueCreating = true;
    	while(!scheduleColor((Integer) ((Entry) colorSessions.entryList().get(color)).getKey())) {
    		color = (color+1)%(maxColor+1);
    	}

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
     * @return
     */
    public static boolean scheduleColor(int color) {
    	
    	boolean sessionUsed = false;
    	//myOut.println("scheduling: " + color);
    	boolean any = false;
    	List<List<Integer>> courses = colorToSubsets.get(color);
    	for(int subsetIndex = 0; subsetIndex < courses.size(); subsetIndex++ ) {
        	int freeCounter = 0;
        	int roomCounter = rooms;
    		for (int course: courses.get(subsetIndex))
    		{
    			if (sessionsRequirement[courseOrder[course]] == 0)
    			{
    				if(!courseCompleted[course]) {
    					courseCompleted[course] = true;

    					//this is a potentially dangerous piece of code. remove the next three lines if
    					//there is a disaster
    					//reOrderCourses(course);
    					colors = assignColors();
    					colorToSubsets = makeColortoSubsets(colors);
    					return sessionUsed;
    				}
    				continue;
    			}
    		}

    		for(int course:courses.get(subsetIndex)) {

    			roomCounter--;
    			if (sessionsRequirement[courseOrder[course]] == 0)
    			{
    				freeCounter++;
    				continue;
    			}

    			//decrement the sessions remaining
    			colorSessions.put(color, colorSessions.get(color)-1);

    			any = true;

    			while(freeCounter > 0) {
    				myOut.print("[ ]");
    				freeCounter--;
    			}

    			myOut.print("[" + course + "]");
    			--sessionsRequirement[courseOrder[course]];
    			sessionUsed = true;
    		}

    		if(any) {
    			while(roomCounter > 0) {
    				myOut.print("[ ]");
    				roomCounter--;
    			}
    			myOut.println("");
    			break;
    		}
    	}
    	return sessionUsed;
    }

    /**
	 * @param args 
	 *  - args[0]: .CSV file which contains course details information
	 *  - args[1]: .CSV file which contains information about courses selected by students 
	 * @throws Throwable 
	 */
	public static void main(String[] args) {
		try {
			createTimeTable(args[0], args[1]);
		} catch(Throwable e) {
			myOut.println(e);
			e.printStackTrace();
			System.exit(0);
		}
    }

}