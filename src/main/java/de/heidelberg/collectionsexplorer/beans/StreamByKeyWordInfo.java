package de.heidelberg.collectionsexplorer.beans;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class StreamByKeyWordInfo extends GenericInfo {

    @Getter@CsvBindByName private String className;
    @Getter	@CsvBindByName private int lineNumber;
    @Getter	@CsvBindByName private int columnNumber;

    @Getter @CsvBindByName private StringListInfo streamOperations;

    @Getter @CsvBindByName private String fullStreamOperation;
}
