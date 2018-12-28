package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;
import de.heidelberg.collectionsexplorer.visitors.ObjectCreationVisitor;

@DisplayName("Test suite for Object Creation Visitor")
public class ObjectCreationVisitorTest {

	String classA = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.HashMap;" + 
			"import java.util.Map;" + 
			"public class ClassA {" + 
			"	private Integer field1 = new Integer(2);" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>(195);" + 
			"	private Map<String, Integer> map2 = new HashMap<>(map);" +
			"	private Map<String, Integer> map3 = new HashMap(map);" +
			"}");
	
			
	@Test
	public void testSimpleObjectCreation() {

		try {

			CompilationUnit compilationUnit = JavaParser.parse(classA);
			Result<ObjectCreationInfo> result = new Result<>("");
			compilationUnit.accept(new ObjectCreationVisitor(Filter.NO_FILTER), result);

			assertEquals(4, result.getEntries().size());

			ObjectCreationInfo integer = result.getEntries().get(0);
			assertEquals("ClassA", integer.getClassName());
			assertEquals("Integer", integer.getObjectType());
			assertEquals("2", integer.getArguments().getListedInfo().get(0));
			assertTrue(integer.getArgumentTypes().getListedInfo().isEmpty());

			ObjectCreationInfo map = result.getEntries().get(1);
			assertEquals("ClassA", map.getClassName());
			assertEquals("195", map.getArguments().getListedInfo().get(0));
			assertFalse(map.getArgumentTypes().getListedInfo().isEmpty());
			assertEquals("String", map.getArgumentTypes().getListedInfo().get(0));
			assertEquals("Integer", map.getArgumentTypes().getListedInfo().get(1));
			assertEquals("HashMap", map.getObjectType());
			
			ObjectCreationInfo map2 = result.getEntries().get(2);
			assertEquals("map", map2.getArguments().getListedInfo().get(0));
			assertTrue(map2.getArgumentTypes().getListedInfo().isEmpty());
			assertEquals("HashMap", map2.getObjectType());

		} catch (Exception e) {
			System.out.println(e);
			fail(e.getMessage());
		}

	}
	
	@Test
	public void testFilter() {
		
		try {
			
			CompilationUnit compilationUnit = JavaParser.parse(classA);
			Result<ObjectCreationInfo> result = new Result<>("");
			
			Filter filter = new Filter();
			filter.add("(.*Map<.*>)|(.*Map)");
			
			compilationUnit.accept(new ObjectCreationVisitor(filter), result);
			assertEquals(3, result.getEntries().size());
			
			
		} catch(Exception e) {
			System.out.println(e);
			fail(e.getMessage());
		}
		
	}

}
