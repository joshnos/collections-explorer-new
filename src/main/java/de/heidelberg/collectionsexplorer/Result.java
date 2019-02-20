package de.heidelberg.collectionsexplorer;

import java.util.ArrayList;
import java.util.List;

import de.heidelberg.collectionsexplorer.beans.GenericInfo;

/**
 * Result provides the logic for storing all the info found in a .java file.
 * 
 * @author diego.costa
 *
 */
public class Result<T extends GenericInfo> {
	
	private String fileAbsolutePath;
	private List<T> infoList = new ArrayList<T>();
	
	public Result(String fileName) {
		this.fileAbsolutePath = fileName;
	}
	
	public void add(T bean) {
		// First add the path
		bean.setPath(fileAbsolutePath);
		infoList.add(bean);
	}
	
	public Boolean hasEntries() {
		if (infoList == null)
			return false;
		else return true;
	}
	
	public List<T> getEntries() {
		return infoList;
	}
	
	public String getFilePath() {
		return fileAbsolutePath;
	}
}
