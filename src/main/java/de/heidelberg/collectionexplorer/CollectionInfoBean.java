package de.heidelberg.collectionexplorer;

/**
 * Bean object created to hold the collections info.
 * 
 * @author Diego
 *
 */
public class CollectionInfoBean {
	
	private String typeName;
	private String className;
	private int lineNumber;
	private int columnNumber;
	private String objectHeld;
	
	private final String delimiterString = " ";
	
	// TODO: Constructor chaining
	
	public CollectionInfoBean(String typeName, String className, int lineNumber, int columnNumber, String objectHeld) {
		super();
		this.typeName = typeName;
		this.className = className;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.objectHeld = objectHeld;
	}
	
	public CollectionInfoBean(String typeName, int lineNumber, int columnNumber, String objectHeld) {
		super();
		this.typeName = typeName;
		this.className = null;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.objectHeld = objectHeld;
	}
	
	public CollectionInfoBean(String typeName, int lineNumber, int columnNumber) {
		super();
		this.typeName = typeName;
		this.className = null;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.objectHeld = null;
	}

	public CollectionInfoBean() {
		this.objectHeld = null;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public Boolean hasObjectHeld() {
		if (objectHeld == null)
			return false;
		else return true;
	}
	
	public String getObjectHeld() {
		return objectHeld;
	}

	public void setObjectHeld(String objectHeld) {
		this.objectHeld = objectHeld;
	}
	
	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		returnString.append(getTypeName() + delimiterString);
		returnString.append(getLineNumber() + delimiterString);
		returnString.append(getColumnNumber());
		if (getObjectHeld() != null)
			returnString.append(delimiterString + getObjectHeld());
		returnString.append("\n");
		return returnString.toString();
	}
}
