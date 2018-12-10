package de.heidelberg.collectionsexplorer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	private List<Result> report = new ArrayList<Result>();
	
	public void add(Result r) {
		report.add(r);
	}
	
	public List<Result> getResults() {
		return report;
	}
	
	public void print() {
		System.out.println(this);
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Result r : report)
			s.append(r.toString());
		return s.toString();
	}

	public void saveResults(File outputFile) throws IOException {
		try (BufferedWriter br =
                new BufferedWriter(new FileWriter(outputFile))) {
			
			br.write(this.toString());
		} 
		
	}
}
