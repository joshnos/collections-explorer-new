package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

@DisplayName("Test suite for a very simple streaming parsing")
public class SimpleStreamParsingTest {
	
	String classA = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.HashMap;" + 
			"import java.util.Map;" + 
			"public class ClassA {" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>();" + 
			"   private void method() {" +
			"   	map.stream().filter(x).collect();" +
			"	}" +
			"}");
	
			
	@Test
	public void testSimpleObjectCreation() {

		try {
			// Set up a minimal type solver that only looks at the classes used to run this sample.
			CombinedTypeSolver solver = new CombinedTypeSolver();
			solver.add(new ReflectionTypeSolver());
			
	        // Configure JavaParser to use type resolution
	        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(solver);
	        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
			
		} catch (Exception e) {
			System.out.println(e);
			fail(e.getMessage());
		}

	}
	

}
