package de.heidelberg.collectionsexplorer.examples;

import java.util.HashMap;
import java.util.Map;

public class ClassA {
	
	@SuppressWarnings("unused")
	private Integer field1 = new Integer(2);
	
	@SuppressWarnings("unused")
	private Map<String, Integer> map = new HashMap<String, Integer>(195);
	
	@SuppressWarnings("unused")
	private Map<String, Integer> map2 = new HashMap<>(map);

}
