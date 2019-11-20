package de.heidelberg.collectionsexplorer.context;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import de.heidelberg.collectionsexplorer.CollectionsExplorer;
import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.*;
import de.heidelberg.collectionsexplorer.visitors.*;

@SuppressWarnings("unchecked")
public enum VisitorType {
	
	OBJECT_CREATION("obj-creation.csv")
	{
		@Override
		public VoidVisitorAdapter<Result<ObjectCreationInfo>> getInstance(Filter filter) {
			return new ObjectCreationVisitor(filter);
		}

	},
	
	VARIABLE_DECLARATION("var-declaration.csv") {
		@Override
		public VoidVisitorAdapter<Result<VariableDeclarationInfo>> getInstance(Filter filter) {
			return new VariableDeclarationVisitor(filter);
		}
	},
	
	IMPORT_DECLARATION("import-declaration.csv") {
		@Override
		public VoidVisitorAdapter<Result<ImportDeclarationInfo>> getInstance(Filter filter) {
			return new ImportDeclarationVisitor(filter);
		}
	},
	
	STREAM_API_USAGE_KEY_WORD("stream-api-usage-key-word-" + CollectionsExplorer.projectName + ".csv") {
		@Override
		public VoidVisitorAdapter<Result<StreamByKeyWordInfo>>  getInstance(Filter filter) {
			return new StreamKeyWordVisitor(filter);
		}
	},
	
	STREAM_API_USAGE_TYPE("stream-api-usage-type-" + CollectionsExplorer.projectName + ".csv") {
		@Override
		public VoidVisitorAdapter<Result<StreamOperationsInfo>>  getInstance(Filter filter) {
			return new StreamProductionSideVisitor(filter);
		}
	},

	STREAM_API_METRICS_TYPE("stream-api-metrics-type-" + CollectionsExplorer.projectName + ".csv") {
		@Override
		public VoidVisitorAdapter<Result<StreamTypeInfo>>  getInstance(Filter filter) {
			return new StreamTypeVisitor(filter);
		}
	},
	
	METHOD_TYPE("methods-types-" + CollectionsExplorer.projectName + ".csv") {
		@Override
		public VoidVisitorAdapter<Result<MethodCallsInfo>>  getInstance(Filter filter) {
			return new MethodCallsVisitor(filter);
		}
	};

    public abstract <T> VoidVisitorAdapter<T> getInstance(Filter filter);
	public String outputFile;
	
	private VisitorType(String outputFile) {
		this.outputFile = outputFile;
	}	
}
