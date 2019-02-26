package de.heidelberg.collectionsexplorer.visitors;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.ImportDeclarationInfo;
import de.heidelberg.collectionsexplorer.beans.ImportDeclarationInfo.ImportDeclarationInfoBuilder;
import de.heidelberg.collectionsexplorer.context.Result;

public class ImportDeclarationVisitor extends VoidVisitorAdapter<Result<ImportDeclarationInfo>> {
	
	Filter filter;
	
	public ImportDeclarationVisitor(Filter filter) {
		this.filter = filter;
	}

	@Override
	public void visit(ImportDeclaration imp, Result<ImportDeclarationInfo> ret) {
		
		String type = imp.getNameAsString();;
		if (filter.filter_match(type)) {
			ret.add(parse(imp));
		}
		super.visit(imp, ret);
	}

	private ImportDeclarationInfo parse(ImportDeclaration imp) {
		
		ImportDeclarationInfoBuilder builder = ImportDeclarationInfo.builder();
		
		builder.packageImported(imp.getNameAsString());
		
		return builder.build();
	}
	

}
