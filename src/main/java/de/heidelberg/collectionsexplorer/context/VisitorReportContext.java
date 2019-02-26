package de.heidelberg.collectionsexplorer.context;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.GenericInfo;

public class VisitorReportContext<T extends GenericInfo> {
	
	private Report report;
	private VisitorType visitorType;
	private Filter filter;
	
	
	
	public VisitorReportContext(VisitorType visitorType, Filter filter) {
		super();
		this.visitorType = visitorType;
		this.filter = filter;
		this.report = new Report();
	}

	public Report getReport() {
		return this.report;
	}
	
	public void inspect(CompilationUnit cu, String path) {
		
		Result<T> objResult = new Result<>(path);
		
		// We have a state per file 
		VoidVisitorAdapter<Result<T>> instance = visitorType.getInstance(filter);
		
		cu.accept(instance, objResult);
		report.add(objResult);
		
	}

}
