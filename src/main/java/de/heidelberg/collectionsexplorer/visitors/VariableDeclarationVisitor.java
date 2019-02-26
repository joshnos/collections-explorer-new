package de.heidelberg.collectionsexplorer.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.StringListInfo;
import de.heidelberg.collectionsexplorer.beans.VariableDeclarationInfo;
import de.heidelberg.collectionsexplorer.beans.VariableDeclarationInfo.VariableDeclarationInfoBuilder;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;

public class VariableDeclarationVisitor extends VoidVisitorAdapter<Result<VariableDeclarationInfo>> {
	
	Filter filter;
	
	public VariableDeclarationVisitor(Filter filter) {
		super();
		this.filter = filter;
	}

	@Override
	public void visit(VariableDeclarator var, Result<VariableDeclarationInfo> ret) {
		
		String type = var.getTypeAsString();
		if (filter.filter_match(type)) {
			ret.add(parse(var));
		}
		super.visit(var, ret);
	}

	private VariableDeclarationInfo parse(VariableDeclarator exp) {
		
		VariableDeclarationInfoBuilder builder = VariableDeclarationInfo.builder();
		
		// Type
		builder.type(exp.getTypeAsString());
		
		// Name
		builder.name(exp.getNameAsString());
		
		// Class Name
		builder.className(ParserUtil.retrieveClass(exp));
		
		// Type Arguments
		builder.typeArguments(new StringListInfo(retrieveTypeArguments(exp)));
		
		// Position (line + col)
		builder.lineNumber(ParserUtil.getLineNumber(exp));
		builder.columnNumber(ParserUtil.getColumn(exp));

		return builder.build();
	}

	private List<String> retrieveTypeArguments(VariableDeclarator exp) {
		
		List<ClassOrInterfaceType> findAll = exp.findAll(ClassOrInterfaceType.class);
		
		List<String> argumentTypes = new ArrayList<>();
		findAll.stream().forEach(f -> argumentTypes.add(f.getNameAsString()));
		
		return argumentTypes;
	}

}
