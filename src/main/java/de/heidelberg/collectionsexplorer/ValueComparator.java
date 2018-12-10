package de.heidelberg.collectionsexplorer;

import java.util.Comparator;
import java.util.Map;

/**
 * A simple Comparator, required as a helper class to sort a Map by value.
 * 
 * @author Janos Sebök
 *
 */
public class ValueComparator implements Comparator<Object> {
	
	Map<Object, Integer> map;
	
	public ValueComparator(Map<Object, Integer> base) {
		this.map = base;
	}
	
	@Override
	public int compare(Object a, Object b) {
		return map.get(b) - map.get(a);
	}
}
