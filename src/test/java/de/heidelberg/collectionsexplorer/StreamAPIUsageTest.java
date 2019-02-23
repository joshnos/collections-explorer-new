package de.heidelberg.collectionsexplorer;

import org.junit.jupiter.api.Test;

public class StreamAPIUsageTest {
	
	
	String classA = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.HashMap;" + 
			"import java.util.Map;" + 
			"import java.util.Collectors;" +
			"public class ClassA {" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>();" + 
			"   private void method() {" +
			"   	map.stream().filter(x -> x == 0).collect(Collectors::tolist);" +
			"	}" +
			"}");

	@Test
	public void parsingClassAStreamAPIUsage() {
		
	}
	
	
}
