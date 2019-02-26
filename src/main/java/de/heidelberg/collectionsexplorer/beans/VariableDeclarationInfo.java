package de.heidelberg.collectionsexplorer.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class VariableDeclarationInfo extends GenericInfo {
	
	@Getter @CsvBindByName String type;
	@Getter @CsvBindByName String name;
	@Getter @CsvBindByName String className;
	@Getter @CsvBindByName StringListInfo typeArguments;
	@Getter	@CsvBindByName private int lineNumber;
	@Getter	@CsvBindByName private int columnNumber;
	
}
