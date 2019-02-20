package de.heidelberg.collectionsexplorer.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

public class GenericInfo {
	
	@Getter @Setter @CsvBindByName
	private String path; // Added later

}
