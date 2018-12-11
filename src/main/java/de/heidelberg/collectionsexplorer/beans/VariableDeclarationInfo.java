package de.heidelberg.collectionsexplorer.beans;

import java.util.List;

import com.opencsv.bean.CsvBindByName;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class VariableDeclarationInfo implements Reportable {
	
	@Getter @CsvBindByName String type;
	@Getter @CsvBindByName List<String> typeArguments;
	@Getter	@CsvBindByName private int lineNumber;
	@Getter	@CsvBindByName private int columnNumber;
	
	public String toReport() {
		throw new UnsupportedOperationException("Not yet implemented...");
	}

}
