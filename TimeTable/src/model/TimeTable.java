package model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public abstract class TimeTable {
	
	abstract public Iterator<TimeTableDay> getTimeTableIterator();

}
