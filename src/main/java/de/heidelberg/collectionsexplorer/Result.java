package de.heidelberg.collectionsexplorer;

import java.util.ArrayList;
import java.util.List;

import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;

/**
 * Result provides the logic for storing all the "new" statements found in a .java file.
 * It can easily be viewed as a String thank to the appropriate override.
 * 
 * @author Janos Sebök
 *
 */
public class Result {
	
	private String fileAbsolutePath;
	private List<ObjectCreationInfo> objCreationInfoList = new ArrayList<ObjectCreationInfo>();
	
	/**
	 * Create a new Result. Each result has an associated .java source file
	 * and a list of "new" expressions found in the code by the Visitor.
	 * 
	 * @param f		the Java source file
	 */
	Result(String fileName) {
		this.fileAbsolutePath = fileName;
	}
	
	public void add(ObjectCreationInfo bean) {
		objCreationInfoList.add(bean);
	}
	
	public Boolean hasEntries() {
		if (objCreationInfoList == null)
			return false;
		else return true;
	}
	
	public List<ObjectCreationInfo> getEntries() {
		return objCreationInfoList;
	}
	
	public String getFilePath() {
		return fileAbsolutePath;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.fileAbsolutePath + "\n");
		for(ObjectCreationInfo bean : objCreationInfoList)
			s.append(bean.toString());
		s.append("\n");
		return s.toString();
	}
}
