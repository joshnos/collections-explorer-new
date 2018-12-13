package de.heidelberg.collectionsexplorer.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Bean object created to hold the info on object creation.
 * 
 * @author diego.costa
 *
 */
@Builder
@ToString
public class ObjectCreationInfo extends GenericInfo{

	@Getter	@CsvBindByName private String objectType;
	@Getter	@CsvBindByName private String className;
	@Getter	@CsvBindByName private int lineNumber;
	@Getter	@CsvBindByName private int columnNumber;
	// This is not the type of the arguments but rather the object held
	// In the new HashMap<Integer, String> -> Integer and String are the 
	// argument types
	@Getter	@CsvBindByName private StringListInfo argumentTypes; 
	@Getter @CsvBindByName private StringListInfo arguments;

	public Boolean hasObjectHeld() {
		if (argumentTypes == null)
			return false;
		else
			return true;
	}
}
