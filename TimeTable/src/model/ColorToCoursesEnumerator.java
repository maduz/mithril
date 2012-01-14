package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class ColorToCoursesEnumerator implements Iterator<Pair<Integer, List<Integer>>> {
	
	Map<Integer, List<Integer>> _colorsToSets;
	Iterator<Map.Entry<Integer, List<Integer>>> itColorsToSets;
	Integer _numberOfClassrooms;
	Map.Entry<Integer, List<Integer>> currentMapEntry = null;
	Boolean enumerateFurther = false;
	int color = -1;
	List<Integer> coursesWithSameColor = null;
	Stack<Pair<Integer, Integer>> returnedCourses = new Stack<Pair<Integer, Integer>>();
	int startIdx = 0;
	int endIdx = 0;
	Boolean updateStart = true;
	
	public ColorToCoursesEnumerator (Map<Integer, List<Integer>> colorsToSets, Integer numberOfClassrooms) {
		_colorsToSets = colorsToSets;
		_numberOfClassrooms = numberOfClassrooms;
		
		itColorsToSets = _colorsToSets.entrySet().iterator();
	}

	@Override
	public boolean hasNext() {
		return enumerateFurther || itColorsToSets.hasNext();
	}

	@Override
	public Pair<Integer, List<Integer>> next() {
		/*
		 * Basically, we check whether a particular colour had more courses than rooms available.
		 * The following condition will be true only in that case.
		 * If the number of courses of a colour are less than the rooms available, then
		 * the following code just returns a pair <color, List of courses>
		 */
		if (enumerateFurther == false) {
			currentMapEntry = itColorsToSets.next();
		
			color = currentMapEntry.getKey();
			coursesWithSameColor = currentMapEntry.getValue();
		
			if(coursesWithSameColor.size() <= _numberOfClassrooms) {
				Pair<Integer, List<Integer>> pair =  new Pair<Integer, List<Integer>> (color, coursesWithSameColor);
				return pair;
			}
		}
		
		/*
		 * If there are more courses of a colour than total rooms available then the condition
		 * 'enumerateFurther' is set to TRUE
		 */
		enumerateFurther = true;
		
		/*
		 * If there was already partial set returned earlier, then the following conditions is TRUE
		 * We extract the index of the last course inserted and calculate the number of courses remaining
		 * in the colour set. If there are remaining courses, then pop one course out of the stack and add
		 * a new one to create a new set of courses to return 
		 * 
		 */
		if(returnedCourses.isEmpty() == false) {
				//Pair<Integer, Integer> top = returnedCourses.pop();
				int remaining = coursesWithSameColor.size() - endIdx; //- top.getRight() - 1;
				
				if(remaining > _numberOfClassrooms) {
					remaining = _numberOfClassrooms;
				} else {
					enumerateFurther = false;
				}

				//endIdx = top.getRight() + 1;
				while(remaining > 0) {
					returnedCourses.pop();
					remaining--;
				}

				/*
				if((returnedCourses.size() + remaining) >= _numberOfClassrooms) {
					endIdx = top.getRight() + 1;
					break;
				}
				*/
		}
		
		
		int idx = endIdx;
		/*if(idx == coursesWithSameColor.size()) {
			returnedCourses.pop();
			startIdx++;
			idx = startIdx;
		}*/
		

		/*
		 * While the stack has less number of courses than the number of classrooms, then add another course
		 * of the same colour into the stack. In the second and subsequent enumerations, only a new course is added
		 * to the set by removing an existing one
		 */
		while(returnedCourses.size() < _numberOfClassrooms) {
			returnedCourses.push (new Pair<Integer, Integer>(coursesWithSameColor.get(idx), idx));
			idx++;
		}
		
		/*
		 * Pop the courses off the stack and add them into a List.
		 */
		List<Integer> courses = new ArrayList<Integer>();
		for(Pair<Integer, Integer> course : returnedCourses) {
			courses.add(course.getLeft());
		}
		
		/*
		 * Set the marker for the next 'read()' at the index of the course that was last returned.
		 * If the number of courses remaining of a colour are equal to the number of classrooms, then
		 * no need to enumerate further. The remaining courses can be returned as a new set. Therefore,
		 * the stack too is cleared 
		 */
		if(idx >= coursesWithSameColor.size()) {
			//enumerateFurther = false;
			returnedCourses.clear();
			endIdx = 0;
		}		
		else
			endIdx = idx;
		/*
		if(updateStart == true) {
			startIdx = idx - 2;
			updateStart = false;
		}*/
		
		Pair<Integer, List<Integer>> pair =  new Pair<Integer, List<Integer>> (color, courses);
		return pair;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
