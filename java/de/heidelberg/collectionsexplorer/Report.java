package de.heidelberg.collectionsexplorer;

import java.util.ArrayList;
import java.util.List;

/**
 * Report compiles a report from a list of results, mainly
 * through the use of a toString() implementation.
 * 
 * @author Janos Sebök
 *
 */
public class Report {
	
	private List<Result<?>> report = new ArrayList<Result<?>>();
	
	public void add(Result<?> r) {
		report.add(r);
	}
	
	public List<Result<?>> getResults() {
		return report;
	}
	
}
