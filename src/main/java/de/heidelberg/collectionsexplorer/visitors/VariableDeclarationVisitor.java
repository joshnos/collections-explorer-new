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
		
		builder.type(exp.getTypeAsString());
		builder.name(exp.getNameAsString());
		
		builder.typeArguments(new StringListInfo(retrieveTypeArguments(exp)));
		
		Optional<Range> range = exp.getRange();
		if(range.isPresent()) {
			Position pos = range.get().begin;
			builder.lineNumber(pos.line);
			builder.columnNumber(pos.column);
		}
		
		return builder.build();
	}

	private List<String> retrieveTypeArguments(VariableDeclarator exp) {
		
		List<ClassOrInterfaceType> findAll = exp.findAll(ClassOrInterfaceType.class);
		
		List<String> argumentTypes = new ArrayList<>();
		findAll.stream().forEach(f -> argumentTypes.add(f.getNameAsString()));
		
		return argumentTypes;
	}

}
