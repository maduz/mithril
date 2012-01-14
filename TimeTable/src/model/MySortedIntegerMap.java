/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Akshay
 *
 */
public class MySortedIntegerMap {

	private int numElements = 0;
	
	Entry root = null;
	
	public int size() {
		return numElements;
	}
	
	public boolean isEmpty() {
		return (numElements == 0);
	}

	public boolean containsKey(Integer key) {
		// TODO Auto-generated method stub
		if(root == null) {
			return false;
		}
		Entry p = root;
		while(p!=null) {
			if(p.entryKey.equals(key)) return true;
			p = p.next;
		}
		return false;
	}

	public boolean containsValue(Integer value) {
		// TODO Auto-generated method stub
		if(root == null) {
			return false;
		}
		Entry p = root;
		while(p!=null) {
			if(p.entryValue.equals(value)) return true;
			p = p.next;
		}
		return false;
	}

	public Integer get(Integer key) {
		// TODO Auto-generated method stub
		if(root == null) {
			return null;
		}
		Entry p = root;
		while(p!=null) {
			if(p.entryKey.equals(key)) return p.entryValue;
			p = p.next;
		}
		return null;
	}

	/**
	 * Adds an entry into the MAP. If the map already contains the key then the entry is updated.
	 * Otherwise, a new entry is created and inserted into the map.
	 * @param key
	 * @param value
	 * @return
	 */
	public Integer put(Integer key, Integer value) {
		
		//if this is the first node. create a new one
		if(root == null) {
			root = new Entry(key, value, null);
			return null;
		}
		
		Entry p = root;
		Entry c = p.next;
		
		Integer retVal = null;
		
		//if only one node, then compare the root node with the value and insert at the right place
		if(c == null) {
			if(p.entryKey.equals(key)) {
				retVal = p.entryValue;
				p.entryValue = value;
			} else if((p.entryValue > value)||((p.entryValue == value)&&(p.entryKey < key))){ //only if the value is less than the root's value
				Entry newEntry = new Entry(key, value, p);
				p.next = newEntry;
				newEntry.previous = p;
			} else { //if the value is greater than or EQUAL TO the root's value
				Entry newEntry = new Entry(key, value, null);
				p.previous = newEntry;
				newEntry.next = p;
				root = newEntry;
			}
		} else {
			
			//if there are at least two nodes in the map
			Entry newEntry = new Entry(key, value, null);
			
			/*
			 * loop through each node
			 * 1. if a matching key node is found, replace the contents and then go back
			 * in the loop to find the right place for the node
			 * 
			 * 2. else, find the node with the lowest higher number than 'value' and
			 * insert a node right after it
			 */
			p = root;
			c=null;
			while(p != null) {
				//if a matching key is found
				if(p.entryKey.equals(key)) {
					retVal = p.entryValue; //get the previous value for the key
					//if the new value is greater than the previous value, the the node becomes eligible to move
					if(value >= retVal) {
						p.entryValue = value;
						c = p.previous;
						while(c!=null) {
							//if p has a higher value or has a lower key than c for equal values
							if((p.entryValue > c.entryValue)||((p.entryValue == c.entryValue)&&(p.entryKey < c.entryKey))) {
								c.next = p.next;
								if(p.next!=null) p.next.previous = c;
								p.previous = c.previous;
								if(c.previous!=null) c.previous.next = p;
								c.previous = p;
								p.next = c;
							} else {
								break;
							}
							c = p.previous;
						}
						//if root node
						if(c==null) {
							root = p;
						}
					} else { //if the new value is lower than the previous value
						p.entryValue = value;
						c = p.next;
						if(root == p) {
							if((p.entryValue < c.entryValue)||((p.entryValue == c.entryValue)&&(p.entryKey > c.entryKey))) 
								root = c;
						}
						while(c!=null) {
							if((p.entryValue < c.entryValue)||((p.entryValue == c.entryValue)&&(p.entryKey > c.entryKey))) {
								p.next = c.next;
								if(c.next!=null) c.next.previous = p;
								c.previous = p.previous;
								if(p.previous!=null) p.previous.next = c;
								p.previous = c;
								c.next = p;
							} else {
								break;
							}
							c = p.next;
						}
					}
					return retVal;
				}
				//if the key is not matched, then if the current node has a higher value than
				//the new value then that node is noted.
				if((p.entryValue > value)||((p.entryValue == value)&&(p.entryKey < key))) {
					c = p;
				}
				p = p.next;
			}
			
			//when value is the highest
			if(c == null) {
				newEntry.next = root;
				root.previous = newEntry;
				root = newEntry;
			} else {
				//c is now pointing to the node with the lowest higher number than 'value'
				newEntry.next = c.next;
				if(c.next != null) c.next.previous = newEntry;
				newEntry.previous = c;
				c.next = newEntry;
			}
		}
			
		return retVal;
	}
	

	public Integer remove(Integer key) {
		// TODO Auto-generated method stub
		Integer retVal = null;
		
		if(root == null) {
			return retVal;
		}
		
		Entry p = root;
		while(p!=null) {
			if(p.entryKey.equals(key)) {
				numElements--;
				retVal = p.entryValue;
				if(p==root) {
					root = p.next;
				}
				if(p.previous != null) p.previous.next = p.next;
				if(p.next != null) p.next.previous = p.previous; 
				break;
			}
			p = p.next;
		}

		return retVal;
	}

	public void clear() {
		// TODO Auto-generated method stub
		root = null;
	}

	public List<Integer> keyList() {
		// TODO Auto-generated method stub
		List<Integer> keyList = new ArrayList<Integer>();
		Entry p = root;
		while(p!=null) {
			keyList.add(p.entryKey);
			p = p.next;
		}
		return keyList;
	}

	public Collection<Integer> values() {
		// TODO Auto-generated method stub
		List<Integer> retList = new ArrayList<Integer>();
		Entry p = root;
		while(p!=null) {
			retList.add(p.entryKey);
			p = p.next;
		}
		return retList;
	}

	public void printMap() {
		Entry p = root;
		System.out.print("Entry List: ");
		while(p!=null) {
			System.out.print("<" + p.entryKey+","+p.entryValue+">");
			p = p.next;
		}
		System.out.println("");
	}
	
	public List<Entry> entryList() {
		// TODO Auto-generated method stub
		List<Entry> entrySet = new ArrayList<Entry>();
		Entry p = root;
		while(p!=null) {
			entrySet.add(p);
			p = p.next;
		}
		return entrySet;
	}
	
	static final class Entry implements Map.Entry<Integer, Integer> {

		Integer entryKey, entryValue;
		Entry previous, next;
	
		Entry(Integer key, Integer value, Entry parent) {
			entryKey = key;
			entryValue = value;
			previous = parent;
			if(parent!=null)parent.next = this;
			next = null;
		}
		
		@Override
		public Integer getKey() {
			// TODO Auto-generated method stub
			return entryKey;
		}

		@Override
		public Integer getValue() {
			// TODO Auto-generated method stub
			return entryValue;
		}

		@Override
		public Integer setValue(Integer value) {
			// TODO Auto-generated method stub
			Integer oldValue = entryValue;
			entryValue = value;
			return oldValue;
		}
		
		public String toString() {
			String retVal = "<" + entryKey + "," + entryValue + ">";
			return retVal;
		}
		
	}
	
}
