package de.heidelberg.collectionsexplorer.visitors;

import org.pmw.tinylog.Logger;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;

import de.heidelberg.collectionsexplorer.CollectionsExplorer;
import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.MethodCallsInfo;
import de.heidelberg.collectionsexplorer.beans.MethodCallsInfo.MethodCallsInfoBuilder;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;

public class MethodCallsVisitor extends VoidVisitorAdapter<Result<MethodCallsInfo>> {
	
	private static final String UNKNOWN_TYPE = "UNK";
	
	public MethodCallsVisitor(Filter filter) {
		// Ignoring filter for now...
	}
	
	@Override
	public void visit(final MethodCallExpr n, final Result<MethodCallsInfo> result) {
		super.visit(n, result);
		MethodCallsInfo info = extractTypeMethods(n);
		result.add(info);
	}

	private MethodCallsInfo extractTypeMethods(MethodCallExpr n) {
		
		MethodCallsInfoBuilder builder = MethodCallsInfo.builder();
		
		builder.fullMethod(n.toString());
		builder.className(ParserUtil.retrieveClass(n));
		builder.lineNumber(ParserUtil.getLineNumber(n));
		builder.columnNumber(ParserUtil.getColumn(n));
		
		String type = extractType(n);
		builder.type(type);
		
		CollectionsExplorer.totalCounter ++;
		
		return builder.build();
	}

	private String extractType(Expression expression) {
		try {
			ResolvedType resolvedType = expression.calculateResolvedType();
			return resolvedType.describe();
		
		} catch (Exception e) {
			// Trace level as this is expected to happen quite often
			Logger.trace(String.format("Error while identifying the types for the expresion = %s", expression.toString()));
			Logger.trace(e.toString());
		}
		CollectionsExplorer.unknownCounter ++;
		return UNKNOWN_TYPE;
	}
}
