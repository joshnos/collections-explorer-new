package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import de.heidelberg.collectionsexplorer.beans.VariableDeclarationInfo;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.visitors.VariableDeclarationVisitor;

@DisplayName("Test suite for VariableDeclarator visitor")
public class VariableDeclarationVisitorTest {
	
	String classA = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.HashMap;" + 
			"import java.util.Map;" + 
			"public class ClassA {" + 
			"	private Integer field1 = new Integer(2);" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>(195);" + 
			"	private List<String> list = new ArrayList<>();" +
			"	private Map<String, Integer> map3 = new HashMap(map);" +
			"}");
	
	@Test
	public void testSimpleParsing() {
		
		CompilationUnit compilationUnit = StaticJavaParser.parse(classA);
		Result<VariableDeclarationInfo> result = new Result<>("");
		compilationUnit.accept(new VariableDeclarationVisitor(Filter.NO_FILTER), result);
		
		assertEquals(4, result.getEntries().size());
		
		VariableDeclarationInfo var1 = result.getEntries().get(0);
		assertEquals("Integer", var1.getType());
		assertEquals("field1", var1.getName());

		VariableDeclarationInfo var2 = result.getEntries().get(1);
		assertEquals("Map<String,Integer>", var2.getType());
		assertEquals("Map", var2.getTypeArguments().getListedInfo().get(0));
		assertEquals("String", var2.getTypeArguments().getListedInfo().get(1));
		assertEquals("Integer", var2.getTypeArguments().getListedInfo().get(2));
		
		VariableDeclarationInfo var3 = result.getEntries().get(2);
		assertEquals("List<String>", var3.getType());
		assertEquals("list", var3.getName());
		assertEquals("List", var3.getTypeArguments().getListedInfo().get(0));
		assertEquals("String", var3.getTypeArguments().getListedInfo().get(1));
		
	}

}
