package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pmw.tinylog.Logger;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class MainClass {
	private static final String JAVA_EXTENSION = ".java";

	public static void main(String[] args) throws Exception {

		File inputDirectory = new File("C:\\Users\\internet\\eclipse-workspace\\Demo\\src");
		Filter filter = new Filter();
		TypeSolver solver = new CombinedTypeSolver(
	    		new ReflectionTypeSolver(), new JavaParserTypeSolver(inputDirectory));

		FileProcessor processor = new FileProcessor(filter);

		// create a complete report and parse all the files
		Logger.info("Finding the amount of java files in the directory");

		List<File> filesList = new ArrayList<>();

		filesList.addAll(FileTraverser.visitAllDirsAndFiles(inputDirectory, JAVA_EXTENSION));

		Logger.info(String.format("%d files found...", filesList.size()));
		processor.process(filesList, solver);

		Logger.info("All files processed and exported successfully");
		processor.close();

	}
}
