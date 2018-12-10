package de.heidelberg.collectionsexplorer;

import lombok.Builder;
import lombok.Getter;

/**
 * Bean object created to hold the collections info.
 * 
 * @author Diego
 *
 */
@Builder
public class CollectionsInfo {

	@Getter
	private String typeName;

	@Getter
	private String className;

	@Getter
	private int lineNumber;

	@Getter
	private int columnNumber;

	@Getter
	private String objectHeld;

	private final String delimiterString = " ";

	public Boolean hasObjectHeld() {
		if (objectHeld == null)
			return false;
		else
			return true;
	}

	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		returnString.append(typeName + delimiterString);
		returnString.append(lineNumber + delimiterString);
		returnString.append(columnNumber);
		if (objectHeld != null)
			returnString.append(delimiterString + objectHeld);
		returnString.append("\n");
		return returnString.toString();
	}
}
