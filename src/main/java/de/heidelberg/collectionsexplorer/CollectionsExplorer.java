package de.heidelberg.collectionsexplorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;
import org.pmw.tinylog.writers.Writer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import de.heidelberg.collectionsexplorer.beans.GenericInfo;
import de.heidelberg.collectionsexplorer.context.Report;
import de.heidelberg.collectionsexplorer.context.VisitorReportContext;
import de.heidelberg.collectionsexplorer.context.VisitorType;
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
public class CollectionsExplorer implements Callable<Void> {

	public static int unknownCounter = 0;
	public static int totalCounter = 0;
	private static final String JAVA_EXTENSION = ".java";

	/**
	 * INPUT PARAMETERS
	 */

	@Parameters(index = "0", arity = "1..*", paramLabel = "dir",
			description = "Input directory where the explorer will retrieve collections usage")
	private File[] inputDirectories;

	/**
	 * OPTIONAL PARAMETERS
	 */
	@Option(names = { "-v", "--verbose" }, description = "Be verbose.")
	private boolean verbose = false;

	@Option(arity = "0..*", names = {"-filter" }, 
			description = "Use this to filter the specific types to be inspected")
	private String[] filters;

	@Option(arity = "1", names = {"-out" }, paramLabel = "out", 
			description = "Directory where the report will be saved")
	private File outputDirectory;

	/**
	 *  VISITORS PARAMETERS
	 */
	@Option(arity = "0", names = {"-var" }, paramLabel = "var", 
			description = "Analyze every variable declaration that matches the filter.")
	private boolean inspectVarDeclaration;

	@Option(arity = "0", names = {"-new" }, paramLabel = "new", 
			description = "Analyze every object instantiation that matches the filter.")
	private boolean inspectObjCreation;

	@Option(arity = "0", names = {"-import" }, paramLabel = "import", 
			description = "Analyze every import declaration that matches the filter.")
	private boolean inspectImportDeclaration;

	@Option(arity = "0", names = {"-streamByKeyWord" }, paramLabel = "streamByKeyWord", 
			description = "Analyze every stream methods	 declaration using the filter.")
	private boolean inspectStreamByKeyWordMethodDeclaration;
	
	@Option(arity = "0", names = {"-streamByType" }, paramLabel = "streamByType", 
			description = "Analyze every stream methods	 declaration using the filter.")
	private boolean inspectStreamByTypeMethodDeclaration;
	
	@Option(arity = "0", names = {"-type" }, paramLabel = "type", 
			description = "Extract type from all methods")
	private boolean inspectMethodsType;
	
	/**
	 * TYPE SOLVER PARAMETERS
	 */
	@Option(arity = "0", names = {"-jar" }, paramLabel = "jar", 
			description = "Jar file to help resolve symbol types (stream usage).")
	private File jarFile;
	
	@Option(arity = "0", names = {"-projectName" }, paramLabel = "projectName", 
			description = "name of the project analyzed.")
	public static String projectName;
	
	@Option(arity = "0", names = {"-jarsFile" }, paramLabel = "jarsFile", 
			description = "file where are jars for the project")
	public static String jarsFile;
	
	public static void main(String[] args) throws IOException {

		// CheckSum implements Callable, so parsing, error handling and handling user
		// requests for usage help or version help can be done with one line of code.
		CommandLine.call(new CollectionsExplorer(), args);
	}

	@Override
	public Void call() throws Exception {
		Configurator.defaultConfig()
		   .writer(new ConsoleWriter(), Level.INFO)
		   .addWriter(new FileWriter(projectName + ".txt"), Level.TRACE)
		   .activate();
		
		Logger.info("Starting the Collections-Explorer");

		Filter filter = new Filter();

		if (filters == null) {
			Logger.info(String.format("No filters configured. Inspecting all types"));
		} else {
			Logger.info(String.format("%d filters configured", filters.length));
			for (int i = 0; i < filters.length; i++) {
				Logger.info(String.format("Filter %d -< %s", i + 1, filters[i]));
				filter.add(filters[i]);
			}
		}

		FileProcessor processor = createAndConfigureProcessor(filter);

		try {
			// create a complete report and parse all the files
			Logger.info("Finding the amount of java files in the directory");

			List<File> filesList = new ArrayList<>();
			for (File dir : inputDirectories) {
				Logger.info(String.format("Adding directory %s", dir.getPath()));
				filesList.addAll(FileTraverser.visitAllDirsAndFiles(dir, JAVA_EXTENSION));
				
				
				CombinedTypeSolver solver = new CombinedTypeSolver(
						//new JavaParserTypeSolver(dir), // Needs an accurate root directory THIS IS VERY SLOW
						new ReflectionTypeSolver());   // Works for types we also use here (java.util, java.lang...)
				FileTraverser.fillDirectories(dir, solver);
				if(jarsFile != null) {
					Logger.info(String.format("Jar file %s specified for the type solver.", jarsFile));
					FileTraverser.fillJar(new File(jarsFile), solver);
				}
				
				if(jarFile != null) {
					solver.add(new JarTypeSolver(jarFile));
					Logger.info(String.format("Jar file %s specified for the type solver.", jarFile));
				}
				
				// Configure JavaParser to use type resolution
				JavaSymbolSolver symbolSolver = new JavaSymbolSolver(solver);
				StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

				Logger.info(String.format("%d files found...", filesList.size()));
				processor.process(filesList);
				//System.out.println(dir.toString());
			}

			Logger.trace("Total types: " + totalCounter);
			Logger.trace("Undefined types: " + unknownCounter);
			Logger.trace("Percentage Of Undefined Types: " + getPercentageOfUndefinedTypes(totalCounter, unknownCounter) + "%");
			Logger.info("All files processed, preparing the export");

			EnumMap<VisitorType, VisitorReportContext<?>> allVisitorContexts = processor.getAllVisitorContexts();

			// Export the context of each visitor into CSV
			for (Entry<VisitorType, VisitorReportContext<?>> entry : allVisitorContexts.entrySet()) {

				VisitorType visitorType = entry.getKey();
				VisitorReportContext<?> context = entry.getValue();

				Logger.info(String.format("Writing the context found with %s analysis", visitorType));

				// Handle output dir option
				File outputFile;
				if (outputDirectory == null) {
					outputFile = new File(visitorType.outputFile);
				} else {
					outputFile = new File(outputDirectory + visitorType.outputFile);
				}

				// Write in a CSV file
				CsvWriter.writeInfo(outputFile, formatToWrite(context.getReport()));

			}

			Logger.info("All files processed and exported successfully");

		} catch (IOException e) {
			Logger.error(
					String.format("Error while parsing the input. Message: %s. %s", e.getMessage(), e.getStackTrace()));
		}
		return null;
	}

	private FileProcessor createAndConfigureProcessor(Filter filter) throws IOException {
		FileProcessor processor = new FileProcessor(filter);

		if (inspectImportDeclaration) {
			Logger.info(String.format("Inspecting IMPORT-DECLARATIONS"));
			processor.addVisitorContext(VisitorType.IMPORT_DECLARATION);
		}

		if (inspectVarDeclaration) {
			Logger.info(String.format("Inspecting VARIABLE-DECLARATIONS"));
			processor.addVisitorContext(VisitorType.VARIABLE_DECLARATION);
		}

		if (inspectObjCreation) {
			Logger.info(String.format("Inspecting OBJECT-CREATIONS"));
			processor.addVisitorContext(VisitorType.OBJECT_CREATION);
		}

		if (inspectStreamByKeyWordMethodDeclaration) {
			Logger.info(String.format("Inspecting STREAM-API-USAGE-KEY-WORD"));
			processor.addVisitorContext(VisitorType.STREAM_API_USAGE_KEY_WORD);
		}
		
		if (inspectStreamByTypeMethodDeclaration) {
			Logger.info(String.format("Inspecting STREAM-API-USAGE-TYPE"));
			processor.addVisitorContext(VisitorType.STREAM_API_USAGE_TYPE);
		}
		
		if (inspectMethodsType) {
			Logger.info(String.format("Inspecting METHOD-TYPE"));
			processor.addVisitorContext(VisitorType.METHOD_TYPE);
		}

		return processor;
	}

	private List<GenericInfo> formatToWrite(Report report) {

		List<GenericInfo> returnedList = new ArrayList<>();

		for (var result : report.getResults()) {
			returnedList.addAll(result.getEntries());
		}

		return returnedList;
	}
	
	private String getPercentageOfUndefinedTypes(int total, int undefined) {
		DecimalFormat formato = new DecimalFormat("#.##");
		float result = undefined * 100.00f / total;
		return formato.format(result);
	}
}
