package de.heidelberg.collectionsexplorer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class TypeSolverTest {

	List<Integer> shadowList = new ArrayList<>();
	
	@Test
	public void testTypeSolverInPackageDir() {

		try {
			TypeSolver typeSolver = new JavaParserTypeSolver(
					new File("src/test/java/de/heidelberg/collectionsexplorer"));

			SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType = typeSolver
					.tryToSolveType("TypeSolverTest");
			assertTrue(tryToSolveType.isSolved());
			
		} catch (Exception e) {
			System.out.println(e);
			assertTrue(false);
		}

	}
	

}
