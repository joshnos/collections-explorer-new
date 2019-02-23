package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.pmw.tinylog.Logger;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

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
@Command(description = "Finds and parses Java code inside a directory and retrieve information about the collections usage.", 
name = "Collections-Explorer", mixinStandardHelpOptions = true, version = "1.0")
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
	
	@Option(arity = "0", names= {"-var"}, paramLabel= "var", description="Analyze every variable declaration that matches the filter.")
	private boolean inspectVarDeclaration;
	
	@Option(arity = "0", names= {"-new"}, paramLabel= "new", description="Analyze every object instantiation that matches the filter.")
	private boolean inspectObjCreation;
	
	@Option(arity = "0", names= {"-import"}, paramLabel= "import", description="Analyze every import declaration that matches the filter.")
	private boolean inspectImportDeclaration;
	
	@Option(arity = "0", names= {"-stream"}, paramLabel= "stream", description="Analyze every stream methods	 declaration using the filter.")
	private boolean inspectStreamMethodDeclaration;
	
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
		
		if(inspectImportDeclaration) {
			Logger.info(String.format("Inspecting IMPORT-DECLARATIONS"));
			processor.addVisitorContext(VisitorType.IMPORT_DECLARATION);
		}
		
		if(inspectVarDeclaration) {
			Logger.info(String.format("Inspecting VARIABLE-DECLARATIONS"));
			processor.addVisitorContext(VisitorType.VARIABLE_DECLARATION);
		}
		
		if(inspectObjCreation) {
			Logger.info(String.format("Inspecting OBJECT-CREATIONS"));
			processor.addVisitorContext(VisitorType.OBJECT_CREATION);
		}
		
		if(inspectStreamMethodDeclaration) {
			Logger.info(String.format("Inspecting STEAM-API-USAGE"));
			processor.addVisitorContext(VisitorType.STREAM_API_USAGE);
		}

		
		try {
			// create a complete report and parse all the files
			Logger.info("Finding the amount of java files in the directory");
			
			List<File> filesList = new ArrayList<>();
			for(File dir : inputDirectories) {
				Logger.info(String.format("Adding directory %s", dir.getPath()));
				filesList.addAll(FileTraverser.visitAllDirsAndFiles(dir, JAVA_EXTENSION));
				
				TypeSolver solver = new CombinedTypeSolver(
						new ReflectionTypeSolver(), new JavaParserTypeSolver(dir));
				Logger.info(String.format("%d files found...", filesList.size()));
				processor.process(filesList, solver);
				
			}
			
			Logger.info("All files processed, preparing the export");
			
			EnumMap<VisitorType,VisitorReportContext<?>> allVisitorContexts = processor.getAllVisitorContexts();
			
			// TODO: Move this to a factory - It is quite bad here...
			File objCreationFile;
			File varDeclarationFile;
			File importDeclarationFile;
			File streamAPIUsageFile;
			
			if(outputDirectory == null) {
				varDeclarationFile = new File("var-declaration.csv");
				objCreationFile = new File("obj-creation.csv");
				importDeclarationFile = new File("import-declaration.csv");
				streamAPIUsageFile = new File("stream-api-usage.csv");
			
			} else {
				objCreationFile = new File(outputDirectory + "obj-creation.csv");
				varDeclarationFile = new File(outputDirectory + "var-declaration.csv");
				importDeclarationFile = new File(outputDirectory + "import-declaration.csv");
				streamAPIUsageFile = new File(outputDirectory + "stream-api-usage.csv");
			}
			
			CsvWriter.writeInfo(objCreationFile , formatToWrite(allVisitorContexts
					.get(VisitorType.OBJECT_CREATION).getReport()));
			
			CsvWriter.writeInfo(varDeclarationFile , formatToWrite(allVisitorContexts
					.get(VisitorType.VARIABLE_DECLARATION).getReport()));
			
			CsvWriter.writeInfo(importDeclarationFile , formatToWrite(allVisitorContexts
					.get(VisitorType.IMPORT_DECLARATION).getReport()));
			
			CsvWriter.writeInfo(streamAPIUsageFile , formatToWrite(allVisitorContexts
					.get(VisitorType.STREAM_API_USAGE).getReport()));
			
			
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
