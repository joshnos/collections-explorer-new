package de.heidelberg.collectionexplorer;

import java.io.File;
import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * File processor provides the logic for parsing either a file or a path using
 * JavaParser. When it encounters a file, it will call the MyVisitor function,
 * which in turn creates a result, which can then be added to a final report.
 * 
 * A result contains all the found "new" statements in a given file. A report is
 * a list of results, by file.
 * 
 * @author Diego
 *
 */
public class FileProcessor {
	
	private static FileProcessor instance = null;
	private static final String UTF_8 = "utf-8";
	/**
	 * file_ID is currently unused and will be relevant later, when the 'edit
	 * compilation unit' logic will be added.
	 */
	private static int file_ID = 0;
	private Filter filter;
	
	protected FileProcessor(Filter filter) {
		super();
		this.filter = filter;
	}
	
	  public static FileProcessor getInstance(Filter filter) {
	      if(instance == null)
	         instance = new FileProcessor(filter);
	      return instance;
	   }

	/**
	 * This method handles processing a giving File. Processing in this context
	 * means that it will parse the file with JavaParser.parse(), then have it
	 * be handled by MyVisitor, which in turn compiles results. Those results
	 * are appended to a Report object.
	 * 
	 * @param f
	 *            the file to be parsed
	 * @return
	 * 		The {@link Result} object containing the info of the file or <code>null</code> 
	 * in case of any {@link Exception} in the parse 
	 */
	public Result process(File f) { 

		Result result = null;
		FileInputStream in;
		try {
			in = new FileInputStream(f.getAbsolutePath());
			CompilationUnit cu;
			try {
				cu = JavaParser.parse(in, UTF_8);
			} catch (Error e) {
				System.out.println(String.format("Critical Javaparser error while processing the file %s.", f.getName()));
				return null;
			}
			result = new Result(f.getAbsolutePath(), file_ID);
			cu.accept(new MyVisitor(filter), result);
			in.close();
		} catch (Exception e) {
			// We can ignore small errors here
			System.out.println(String.format("Error while processing the file %s.", f.getName()));
		}
		file_ID++;
		return result;
	}
}
