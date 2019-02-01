package de.heidelberg.collectionsexplorer;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple class to provide methods for using a filter.
 * This filter is responsible for adding only certain types into the results.
 * 
 * @author Janos Sebök
 *
 */
public class Filter {
	
	public static Filter NO_FILTER = new Filter();
	
	private List<String> interface_filter = new ArrayList<String>();
	
	
	public void add(String filter) {
		interface_filter.add(filter);
	}
	
	/**
	 * A simple comparator. Given a String, check if it's in our list of filters, which are also Strings.
	 * 	
	 * @param type		a String, typically a type like ",List" or "ArrayList". It can also be a regex.
	 * @return			a boolean, true if the String matches the filter or there are no filters, false otherwise
	 */
	public boolean filter_match(String type) {
		if (interface_filter.isEmpty())
			return true;
		else {
			for (String s: interface_filter) {
				if (type.matches(s)) {
					return true;
				}
			}
			return false;
		}
	}
}
