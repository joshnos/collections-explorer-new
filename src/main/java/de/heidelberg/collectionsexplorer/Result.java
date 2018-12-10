package de.heidelberg.collectionsexplorer;

import java.util.ArrayList;
import java.util.List;

/**
 * Result provides the logic for storing all the "new" statements found in a .java file.
 * It can easily be viewed as a String thank to the appropriate override.
 * 
 * @author Janos Sebök
 *
 */
public class Result {
	
	private String fileAbsolutePath;
	private List<CollectionsInfo> new_statements = null;
	
	/**
	 * Create a new Result. Each result has an associated .java source file
	 * and a list of "new" expressions found in the code by the Visitor.
	 * 
	 * @param f		the Java source file
	 * @param index		STUB - this is meant to store the index of this result,
	 * 				associating each file with a number - probably needs to
	 * 				be removed, but that depends on how editing
	 * 				functionality is going to be implemented
	 */
	Result(String fileName, int index) {
		this.fileAbsolutePath = fileName;
		 new_statements = new ArrayList<CollectionsInfo>();
	}
	
	public void add(CollectionsInfo bean) {
		new_statements.add(bean);
	}
	
	public Boolean hasEntries() {
		if (new_statements == null)
			return false;
		else return true;
	}
	
	public List<CollectionsInfo> getEntries() {
		return new_statements;
	}
	
	public String getFilePath() {
		return fileAbsolutePath;
	}
	

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.fileAbsolutePath + "\n");
		for(CollectionsInfo bean : new_statements)
			s.append(bean.toString());
		s.append("\n");
		return s.toString();
	}
}
