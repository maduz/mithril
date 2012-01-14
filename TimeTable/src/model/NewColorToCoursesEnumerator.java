/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Akshay
 *
 */
public class NewColorToCoursesEnumerator implements Iterator<Pair<Integer, List<Integer>>>{
	
	//internal map and iterator objects
	Map<Integer, List<Integer>> _colorsToSets;
	Iterator<Map.Entry<Integer, List<Integer>>> iteratorColorsToSets;
	
	int numClassRooms;
	
	//Stores the number of courses
	int numElements;
	
	//two index used to make pairs
	int counters[], combinationCounter = 0;
	
	boolean moreCombinations = false;
	
	Map.Entry<Integer, List<Integer>> currEntry;
	
	public NewColorToCoursesEnumerator(Map<Integer, List<Integer>> colorsToSets, Integer numberOfClassrooms) {
		_colorsToSets = colorsToSets;
		iteratorColorsToSets = _colorsToSets.entrySet().iterator();
		
		//numElements = _colorsToSets.size();
		
		numClassRooms = numberOfClassrooms;
		
		//initialize counters
		initializeCounters();
		
	}
	
	private void initializeCounters() {
		//initialize counters
		counters = new int[numClassRooms];
		for (int idx = 0; idx < counters.length; idx++) {
			counters[idx] = idx;
		}
	}

	@Override
	public boolean hasNext() {
		boolean returnVal = (moreCombinations) || (iteratorColorsToSets.hasNext()); 
		return returnVal;
	}

	@Override
	public Pair<Integer, List<Integer>> next() {
		// TODO Auto-generated method stub
		
		//new entry needs to be taken from the Iterator
		if(!moreCombinations) {
			currEntry = iteratorColorsToSets.next();
			
			numElements = currEntry.getValue().size();
			/* if there are less courses in the set than the number of classrooms then
			*  return the set as it is
			*/  
			if(numElements < numClassRooms) {
				return new Pair<Integer, List<Integer>>(currEntry.getKey(), currEntry.getValue());
			}
			initializeCounters();
		}

		Integer color = currEntry.getKey();
		
		List<Integer> returnSet = new ArrayList<Integer>();
		for (int counterIdx = 0; counterIdx < counters.length; counterIdx++) {
			returnSet.add(currEntry.getValue().get(counters[counterIdx]));
		}

		moreCombinations = true;

		int counterIdx = counters.length-1;
		while(counterIdx >= 0) {
			counters[counterIdx]++;
			if(counters[counterIdx] == (numElements-(numClassRooms-counterIdx-1))) {
				counterIdx--;
			} else {
				break;
			}
		};
		
		//all combinations have been exhausted
		if(counters[0] == (numElements-numClassRooms+1)) {
			combinationCounter = 0;
			moreCombinations = false;
		} else {

			for(int i = counterIdx+1; i < counters.length; i++) {
				counters[i] = counters[i-1]+1;
			}
		}
		
		return new Pair<Integer, List<Integer>>(color, returnSet);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
