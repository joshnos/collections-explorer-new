package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import de.heidelberg.collectionsexplorer.beans.ImportDeclarationInfo;
import de.heidelberg.collectionsexplorer.visitors.ImportDeclarationVisitor;

@DisplayName("Test for ImportDeclaration visitor")
public class ImportDeclarationVisitorTest {
	
	String classA = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.HashMap;" + 
			"import java.util.Map;" + 
			"public class ClassA {" + 
			"	private Integer field1 = new Integer(2);" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>(195);" + 
			"	private List<String> list = new ArrayList<>();" +
			"	private Map<String, Integer> map3 = new HashMap(map);" +
			"}");
	
	String classB = new String("package de.heidelberg.collectionsexplorer.examples;" + 
			"import java.util.*;" + 
			"import java.util.Map;" + 
			"public class ClassA {" + 
			"	private Integer field1 = new Integer(2);" + 
			"	private Map<String, Integer> map = new HashMap<String, Integer>(195);" + 
			"	private List<String> list = new ArrayList<>();" +
			"	private Map<String, Integer> map3 = new HashMap(map);" +
			"}");
	
	@Test
	public void testSimpleImportExtraction() {
		
		CompilationUnit compilationUnit = JavaParser.parse(classA);
		Result<ImportDeclarationInfo> result = new Result<>("");
		compilationUnit.accept(new ImportDeclarationVisitor(Filter.NO_FILTER), result);
		
		assertEquals(2, result.getEntries().size());
		
		ImportDeclarationInfo import1 = result.getEntries().get(0);
		assertEquals("java.util.HashMap", import1.getPackageImported());
		
		
	}
	
	@Test
	public void testSimpleImportExtractionWithFilter() {
		
		CompilationUnit compilationUnit = JavaParser.parse(classA);
		Result<ImportDeclarationInfo> result = new Result<>("");
		Filter filter = new Filter();
		filter.add(".*Map<.*>");
		filter.add(".*Map");
		compilationUnit.accept(new ImportDeclarationVisitor(filter), result);
		
		assertEquals(2, result.getEntries().size());
		
		ImportDeclarationInfo import1 = result.getEntries().get(0);
		assertEquals("java.util.HashMap", import1.getPackageImported());
		
		
	}
	
	@Test
	public void testJavaUtilImport() {
		
		CompilationUnit compilationUnit = JavaParser.parse(classB);
		Result<ImportDeclarationInfo> result = new Result<>("");
		Filter filter = new Filter();
		filter.add("java.util");
		compilationUnit.accept(new ImportDeclarationVisitor(filter), result);
		
		assertEquals(1, result.getEntries().size());
		
		ImportDeclarationInfo import1 = result.getEntries().get(0);
		assertEquals("java.util", import1.getPackageImported());
		
		
	}


}
