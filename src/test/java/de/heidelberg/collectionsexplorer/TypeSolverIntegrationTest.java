package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pmw.tinylog.Logger;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

@DisplayName("Test suite created from old MainClass")
public class TypeSolverIntegrationTest {

	
	@Test
	public void runIntegrationtest() {

		File inputDirectory = new File("C:\\Users\\diego\\git\\collections-explorer");
		Filter filter = new Filter();
		TypeSolver solver = new CombinedTypeSolver(new ReflectionTypeSolver(),
				new JavaParserTypeSolver(inputDirectory));

		FileProcessor processor;
		try {
			processor = new FileProcessor(filter);

			// create a complete report and parse all the files
			Logger.info("Finding the amount of java files in the directory");
	
			List<File> filesList = new ArrayList<>();
	
			filesList.addAll(FileTraverser.visitAllDirsAndFiles(inputDirectory, ".java"));
	
			Logger.info(String.format("%d files found...", filesList.size()));
			processor.process(filesList);
	
			Logger.info("All files processed and exported successfully");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
