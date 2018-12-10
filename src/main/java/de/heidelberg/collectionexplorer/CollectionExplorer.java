package de.heidelberg.collectionexplorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * CollectionExplorer is the main class of CollectionExplorer4.
 * It is responsible for the main program logic.
 * 
 * USAGE:
 * Use Run Configurations in Java to provide a path and some filters, if required. An example might look like this:
 * "'C:\Users\James Stuart\Downloads\20 top_projects'"
 * Note that the single and double quotes aren't optional; they're required for handling files with spaces in their names.
 * 
 * @author Janos Sebök
 *
 */
public class CollectionExplorer {
	
	private static final int END_INDEX = 100000;
	private static final int BEGIN_INDEX = 0;
	private static final String JAVA_EXTENSION = ".java";
	private static File target = new File("");
	private static Filter filter = new Filter();
	
	/**
	 * A simple constructor to permit the class to be used by a different piece of code, instead of having to run on its own.
	 * 
	 * @param f					the file or path that should be explored
	 * @throws IOException		
	 */
	public CollectionExplorer(File f) throws IOException {
		if (f.exists())
			target = f;
		else
			throw new IOException();
	}
	
	public static void main(String[] args)  {
		// parse the command line arguments
		// args[0]  is the file or path
		// args[1+] are the filters that should delimit the parsing
		
		try {
			// format the path, if needed, and record it; else default to this project's path
			parseInput(args);
			
			// create a complete report and parse all the files
			List<File> filesList = FileTraverser.visitAllDirsAndFiles(target, JAVA_EXTENSION);
			
			Report report = new Report();
			FileProcessor processor = FileProcessor.getInstance(filter);
			for(int i = BEGIN_INDEX; i < END_INDEX; i++) {
				try {
					File file = filesList.get(i);
					report.add(processor.process(file));
				} catch (IndexOutOfBoundsException e) {
					break;
				}
			}
			
			// at this point, we can print the report to the screen by using
			// report.print();
			
			// use the generated report to count the number of occurrences of the types containers contain
			// TODO: separate crunch and print, so that counting the occurrences isn't
			//		 automatically writing a lot to STDOUT
			ReportCruncher.crunchAndPrint(report);
			
		} catch (IOException e) {
			System.out.println(String.format("Error while parsing the input. Message: %s. %s", e.getMessage(), e.getStackTrace()));
		}
	}

	private static void parseInput(String[] args) throws IOException {
		
		if (target.toString() == "")
			if (args.length == 0)
				target = new File(Paths.get("").toAbsolutePath().toString());
			else {
				target = new File(formatInputPath(args[0]));
				if (!(target.exists()))
					throw new IOException("Input file not found.");
			}
		
		// parse and add the filters from the command line
		if (args.length > 1) {
			int i = 0;
			while (++i < args.length)
				filter.add(args[i]);
		}
	}

	/**
	 * A helper function that helps parse file paths contained spaces.
	 * 
	 * @param args		the string to treat
	 * @return			the cleaned up string, ready for use
	 */
	private static String formatInputPath(String args) {
		return args.replace("'", "");
	}
}
