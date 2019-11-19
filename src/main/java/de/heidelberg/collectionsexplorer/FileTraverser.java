package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

/**
 * FileTraverser provides the logic for traversing files in a directory.
 * 
 * @author Janos Sebök
 *
 */
public class FileTraverser {

	/**
	 * A recursive method that visits either a file, or all the files in a given
	 * path. More specifically, it calls the process() method for each file,
	 * which handles visiting.
	 * 
	 * @param file
	 *            a file or folder to be handled
	 * @return a report that contains all the found results
	 */
	public static List<File> visitAllDirsAndFiles(File file, String fileExtension) {

		List<File> files = new ArrayList<>();
		if (file.isDirectory())  {
			for (String child : file.list()) {
				files.addAll(visitAllDirsAndFiles(new File(file, child), fileExtension));
			}
		} else {
			if (file.getName().endsWith(fileExtension)) {
				files.add(file);
			}
		}
		return files;
	}
	
	public static void fillDirectories(File file, CombinedTypeSolver solver) {

		solver.add(new JavaParserTypeSolver(file.getPath()));
		for(File f : file.listFiles()) {
			if(f.isDirectory()) {
				fillDirectories(f, solver);
			}
		}
	}
	
	public static void fillJar(File file, CombinedTypeSolver solver) throws IOException {

		for(File f : file.listFiles()) {
			if(f.getName().endsWith(".jar")) {
				solver.add(new JarTypeSolver(f.getPath()));
			}
		}
	}
}
