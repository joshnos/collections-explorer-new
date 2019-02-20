package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
}
