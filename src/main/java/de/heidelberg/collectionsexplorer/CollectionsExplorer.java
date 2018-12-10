package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Main entry of CollectionsExplorer program. It is responsible for the main
 * program logic.
 * 
 * @author diego
 *
 */
@Command(description = "Finds and parses Java code inside a directory and retrieve information about the collections usage.", name = "Collections-Explorer", mixinStandardHelpOptions = true, version = "1.0")
public class CollectionsExplorer implements Callable<Void>{

	private static final String JAVA_EXTENSION = ".java";
	
	private static Filter filter = new Filter();
	
	/**
	 * Input Parameters
	 */
	@Option(names = { "-v", "--verbose" }, description = "Be verbose.")
	private boolean verbose = false;
	
	@Option(arity = "0..*", names= {"-filter"}, description="Use this to filter the specific types to be inspected")
	private String[] filters;
	
	@Parameters(index="0", arity = "1", paramLabel= "dir", description="Input directory where the explorer will retrieve collections usage")
	private File inputDirectory;
	
	@Parameters(index="1", arity = "1", paramLabel= "out", description="File where the report will be saved")
	private File outputFile;
	
	public static void main(String[] args) {
		
	    // CheckSum implements Callable, so parsing, error handling and handling user
        // requests for usage help or version help can be done with one line of code.
        CommandLine.call(new CollectionsExplorer(), args);
	}

	@Override
	public Void call() throws Exception {
		
		try {

			// create a complete report and parse all the files
			List<File> filesList = FileTraverser.visitAllDirsAndFiles(inputDirectory, JAVA_EXTENSION);

			Report report = new Report();
			FileProcessor processor = FileProcessor.getInstance(filter);
			for (File file: filesList) {
				report.add(processor.process(file));
			}

			report.saveResults(outputFile);

		} catch (IOException e) {
			System.out.println(
					String.format("Error while parsing the input. Message: %s. %s", e.getMessage(), e.getStackTrace()));
		}
		return null;
	}


	public boolean isVerbose() {
		return verbose;
	}

	public File getInputDirectory() {
		return inputDirectory;
	}

	public File getOutputFile() {
		return outputFile;
	}
	
	
}
