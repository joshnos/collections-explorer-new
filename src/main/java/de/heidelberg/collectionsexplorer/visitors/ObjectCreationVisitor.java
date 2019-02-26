package de.heidelberg.collectionsexplorer.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.Position;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo.ObjectCreationInfoBuilder;
import de.heidelberg.collectionsexplorer.beans.StringListInfo;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;

/**
 * Visitor that records object creation instances on code.
 * 
 * @author diego.costa
 *
 */
public class ObjectCreationVisitor extends VoidVisitorAdapter<Result<ObjectCreationInfo>> {

	Filter filter;
	
	Map<String, String> importsDeclared;

	public ObjectCreationVisitor(Filter filter) {
		this.filter = filter;
		importsDeclared = new HashMap<>();
		
		importsDeclared.keySet().stream().filter(x -> x.equals("s")).collect(Collectors.toList());
	}
	
	@Override
	public void visit(ObjectCreationExpr n, Result<ObjectCreationInfo> ret) {

		String type = n.getTypeAsString();
		if (filter.filter_match(type)) {
			ret.add(parse(n));
		}
		super.visit(n, ret);
	}
	
	@Override
	public void visit(ImportDeclaration n, Result<ObjectCreationInfo> arg) {
			
		Name name = n.getName();
		
		String identifier = name.getIdentifier();
		Optional<Name> qualifier = name.getQualifier();
		
		if(qualifier.isPresent()) {
			importsDeclared.put(identifier, qualifier.get().asString());
		}
		
		super.visit(n, arg);
		
	}


	private ObjectCreationInfo parse(ObjectCreationExpr exp) {

		// Builds the info
		ObjectCreationInfoBuilder builder = ObjectCreationInfo.builder();

		// Class
		builder.className(ParserUtil.retrieveClass(exp));
		
		// Type Name
		builder.objectType(exp.getType().getNameAsString());
		
		// Full Name
		builder.fullObjectType(retrieveFullObjectType(exp));
		
		// Argument Types
		builder.argumentTypes(retrieveTypeArguments(exp));

		// Arguments
		builder.arguments(retrieveArguments(exp));

		// Position in the code
		builder.lineNumber(ParserUtil.getLineNumber(exp));
		builder.columnNumber(ParserUtil.getColumn(exp));
		return builder.build();
	}

	private String retrieveFullObjectType(ObjectCreationExpr exp) {
		
		String type = exp.getType().getNameAsString();
		
		if(importsDeclared.containsKey(type)) {
			String qualifiedName = importsDeclared.get(type);
			return qualifiedName + "." + type;
			
		}
		
		return type;
	}

	private StringListInfo retrieveArguments(ObjectCreationExpr exp) {

		NodeList<Expression> arguments = exp.getArguments();

		List<String> argumentsAsString = new ArrayList<>();
		// Populate
		arguments.stream().forEach(f -> argumentsAsString.add(f.toString()));

		return new StringListInfo(argumentsAsString);
	}

	private StringListInfo retrieveTypeArguments(ObjectCreationExpr n) {

		Optional<NodeList<Type>> typeArguments = n.getType().getTypeArguments();
		if (typeArguments.isPresent()) {

			NodeList<Type> nodeList = typeArguments.get();
			List<String> argumentTypes = new ArrayList<>();
			nodeList.stream().forEach(f -> argumentTypes.add(f.asString()));
			return new StringListInfo(argumentTypes);

		}
		// Empty
		return new StringListInfo();
	}

}
