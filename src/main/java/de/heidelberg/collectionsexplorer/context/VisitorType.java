package de.heidelberg.collectionsexplorer.context;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.ImportDeclarationInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;
import de.heidelberg.collectionsexplorer.beans.StreamOperationsInfo;
import de.heidelberg.collectionsexplorer.beans.VariableDeclarationInfo;
import de.heidelberg.collectionsexplorer.visitors.ImportDeclarationVisitor;
import de.heidelberg.collectionsexplorer.visitors.ObjectCreationVisitor;
import de.heidelberg.collectionsexplorer.visitors.StreamAPIUsageVisitor;
import de.heidelberg.collectionsexplorer.visitors.VariableDeclarationVisitor;

@SuppressWarnings("unchecked")
public enum VisitorType {
	
	OBJECT_CREATION("obj-creation.csv")
	{
		@Override
		public VoidVisitorAdapter<Result<ObjectCreationInfo>> getInstance(Filter filter, TypeSolver solver) {
			return new ObjectCreationVisitor(filter);
		}

	},
	
	VARIABLE_DECLARATION("var-declaration.csv") {
		@Override
		public VoidVisitorAdapter<Result<VariableDeclarationInfo>> getInstance(Filter filter, TypeSolver solver) {
			return new VariableDeclarationVisitor(filter);
		}
	},
	
	IMPORT_DECLARATION("import-declaration.csv") {
		@Override
		public VoidVisitorAdapter<Result<ImportDeclarationInfo>> getInstance(Filter filter, TypeSolver solver) {
			return new ImportDeclarationVisitor(filter);
		}
	},
	
	STREAM_API_USAGE("stream-api-usage.csv") {
		@Override
		public VoidVisitorAdapter<Result<StreamOperationsInfo>>  getInstance(Filter filter, TypeSolver solver) {
			return new StreamAPIUsageVisitor(filter, solver);
		}
	};
	
	public abstract <T> VoidVisitorAdapter<T> getInstance(Filter filter, TypeSolver solver);
	public String outputFile;
	
	private VisitorType(String outputFile) {
		this.outputFile = outputFile;
	}

	
	
}
