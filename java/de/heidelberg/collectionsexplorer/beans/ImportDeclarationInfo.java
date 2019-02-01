package de.heidelberg.collectionsexplorer.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Bean that stores any information about imports in Java code
 * 
 * @author diego.costa
 *
 */
@Builder
@ToString
public class ImportDeclarationInfo extends GenericInfo {
	
	@Getter @CsvBindByName String packageImported;

}
