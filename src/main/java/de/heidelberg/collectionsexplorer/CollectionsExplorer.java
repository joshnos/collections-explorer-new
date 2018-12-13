package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.pmw.tinylog.Logger;

import de.heidelberg.collectionsexplorer.beans.GenericInfo;
import de.heidelberg.collectionsexplorer.writer.CsvWriter;
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
	
	
	/**
	 * Input Parameters
	 */
	@Option(names = { "-v", "--verbose" }, description = "Be verbose.")
	private boolean verbose = false;
	
	@Option(arity = "0..*", names= {"-filter"}, description="Use this to filter the specific types to be inspected")
	private String[] filters;
	
	@Parameters(index="0", arity = "1..*", paramLabel= "dir", description="Input directory where the explorer will retrieve collections usage")
	private File[] inputDirectories;
	
	@Option(arity = "1", names= {"-out"}, paramLabel= "out", description="Directory where the report will be saved")
	private File outputDirectory;
	
	public static void main(String[] args) {
		
	    // CheckSum implements Callable, so parsing, error handling and handling user
        // requests for usage help or version help can be done with one line of code.
        CommandLine.call(new CollectionsExplorer(), args);
	}

	@Override
	public Void call() throws Exception {
		
		Logger.info("Starting the Collections-Explorer");
		
		Filter filter = new Filter();
		
		if(filters == null) {
			Logger.info(String.format("No filters configured. Inspecting all types"));
		} else {
			Logger.info(String.format("%d filters configured", filters.length));
			for(int i = 0; i < filters.length; i++) {
				Logger.info(String.format("Filter %d -< %s", i + 1, filters[i]));
				filter.add(filters[i]);
			}
		}
		
		FileProcessor processor = new FileProcessor(filter);
		
		
		try {
			// create a complete report and parse all the files
			Logger.info("Finding the amount of java files in the directory");
			
			List<File> filesList = new ArrayList<>();
			for(File dir : inputDirectories) {
				Logger.info(String.format("Adding directory %s", dir.getPath()));
				filesList.addAll(FileTraverser.visitAllDirsAndFiles(dir, JAVA_EXTENSION));
			}

			Logger.info(String.format("%d files found...", filesList.size()));
			processor.process(filesList);
			
			Logger.info("All files processed, preparing the export");
			
			Report objReport = processor.getObjCreationReport();
			Report varDeclarationReport = processor.getVarDeclarationReport();
			
			File objCreationFile = new File(outputDirectory + "obj-creation.csv");
			CsvWriter.writeInfo(objCreationFile , formatToWrite(objReport));
			
			File varDeclarationFile;
			if(outputDirectory == null) {
				varDeclarationFile = new File("var-declaration.csv");
			} else {
				varDeclarationFile = new File(outputDirectory + "var-declaration.csv");
			}
			
			CsvWriter.writeInfo(varDeclarationFile , formatToWrite(varDeclarationReport));
			
			Logger.info("All files processed and exported successfully");
			
		} catch (IOException e) {
			Logger.error(
					String.format("Error while parsing the input. Message: %s. %s", e.getMessage(), e.getStackTrace()));
		}
		return null;
	}

	private List<GenericInfo> formatToWrite(Report report) {
		
		List<GenericInfo> returnedList = new ArrayList<>();
		
		for(var result: report.getResults()) {
			returnedList.addAll(result.getEntries());
		}
		
		return returnedList;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public File[] getInputDirectory() {
		return inputDirectories;
	}

	public File getOutputFile() {
		return outputDirectory;
	}
	
	
}
