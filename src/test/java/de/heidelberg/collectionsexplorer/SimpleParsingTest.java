package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

@DisplayName("Simple parsing test")
public class SimpleParsingTest {

	@Test
	public void testSimpleParsing() {

		try {

			CompilationUnit compilationUnit = JavaParser
					.parse("class A { " + "private int field1;" + "private float field2;" + "}");

			Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("A");

			assertEquals(classA.get().getFields().size(), 2);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
