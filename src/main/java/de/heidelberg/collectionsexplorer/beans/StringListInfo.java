package de.heidelberg.collectionsexplorer.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * This wrapper just makes it easier to export a List of string to csv using OpenCV
 * 
 * @author diego.costa
 *
 */
public class StringListInfo {
	
	@Getter final List<String> listedInfo;
	
	public StringListInfo() {
		listedInfo = new ArrayList<>();
	}
	
	public StringListInfo(List<String> argumentTypes) {
		super();
		this.listedInfo = argumentTypes;
	}
	
	public void add(String str) {
		this.listedInfo.add(str);
	}
	
	

	@Override
	public String toString() {
		// Specific to string to use when reporting to CSV
		StringBuilder str = new StringBuilder("[");
		str.append(String.join(", ", listedInfo));
		str.append("]");
		return str.toString();
	}

}
