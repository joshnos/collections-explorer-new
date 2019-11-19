package de.heidelberg.collectionsexplorer.visitors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;

import org.pmw.tinylog.Logger;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import de.heidelberg.collectionsexplorer.CollectionsExplorer;
import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.StreamOperationsInfo;
import de.heidelberg.collectionsexplorer.beans.StreamOperationsInfo.StreamOperationsInfoBuilder;
import de.heidelberg.collectionsexplorer.beans.StringListInfo;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;

// FIXME: This class can be more generic - extract all methodCall until you
// reach a particular methodCall (anchor)
public class StreamProductionSideVisitor extends VoidVisitorAdapter<Result<StreamOperationsInfo>> {

	private static final String UNKNOWN_TYPE = "UNK";
		
	private String lastVisitMethodSignature = "";
	private String lastConstructorSignature = "";
	private String lastClassOrInterfaceSignature = "";
	
	public StreamProductionSideVisitor(Filter filter) {
		// Ignoring filter for now...
	}
	
	ArrayList<MethodCallExpr> visited = new ArrayList<>();
	
	public MethodCallExpr get(MethodCallExpr call) {
		for(MethodCallExpr e: visited) {
			if(e==call) {
				return e;
			}
		}
		return null;
	}
	
	@Override
	public void visit(final MethodCallExpr n, final Result<StreamOperationsInfo> result) {
		Boolean v = get(n)==null;
		if(v) {
			StreamOperationsInfo info = extractStreamOperations(n);
			List<MethodCallExpr> allExpCalls = n.findAll(MethodCallExpr.class);
			countRecognizedMethodTypes(allExpCalls);
			//countRecognizedMethodTypes(n);
			if (info != null) {
				result.add(info);
			}
		}
		super.visit(n, result);
	}
	
	@Override
	public void visit(final MethodDeclaration n, final Result<StreamOperationsInfo>  result) {
		lastVisitMethodSignature = n.getSignature().asString();
		super.visit(n,result);
		lastVisitMethodSignature = "";
	}
	
	@Override
	public void visit(final ConstructorDeclaration n, final Result<StreamOperationsInfo>  result) {
		lastConstructorSignature = n.getSignature().asString();
		super.visit(n,result);
		lastConstructorSignature = "";
	}
	
	@Override
	public void visit(final ClassOrInterfaceDeclaration n, final Result<StreamOperationsInfo>  result) {
		lastClassOrInterfaceSignature = n.getNameAsString();
		super.visit(n,result);
		lastConstructorSignature = "";
	}
	
	private StreamOperationsInfo extractStreamOperations(MethodCallExpr methodCall) {
			StreamOperationsInfoBuilder builder = StreamOperationsInfo.builder();
		LinkedList<String> list = extractChainByType(methodCall, builder);
			
			if(list.size()>0) {
				// FullStreamCall
				builder.fullStreamOperation(methodCall.toString());
				
				// Class Name
				builder.className(ParserUtil.retrieveClass(methodCall));

				// Position (line + col)
				builder.lineNumber(ParserUtil.getLineNumber(methodCall));
				builder.columnNumber(ParserUtil.getColumn(methodCall));

				// Stream chain operations
				StringListInfo chain = new StringListInfo(list);
				builder.streamOperations(chain);
				
				builder.lastVisitMethodSignature(lastVisitMethodSignature);
				builder.lastConstructorSignature(lastConstructorSignature);
				builder.lastClassOrInterfaceSignature(lastClassOrInterfaceSignature);
				
				return builder.build();
			}else {
				return null;
			}
	}
	
	private LinkedList<String> extractChainByType(MethodCallExpr call, StreamOperationsInfoBuilder builder) {
		Optional<Expression> scope = call.getScope();
		String receiverType = extractType(scope);
		LinkedList<String> result = new LinkedList<>();
		if(receiverType.contains("java.util.stream")) {
			if(isStream(receiverType)) {
				visited.add(call);
				if(scope.get() instanceof MethodCallExpr) {
					result.addAll(extractChainByType((MethodCallExpr)scope.get(),builder));
				}else {
					builder.sourceType(receiverType);
				}
				result.add(call.getName().getIdentifier());
			}else {
				String typeCall=extractType(call);
				if(typeCall.contains("java.util.stream")) {
					// Source type
					if(isStream(typeCall)) {
						visited.add(call);
						result.add(call.getName().getIdentifier());
						builder.sourceType(receiverType);
					}
				}else{
					// then is not related to streams
				}
			}
		} else {
			String typeCall=extractType(call);
			if(typeCall.contains("java.util.stream")) {
				// Source type
				if(isStream(typeCall)) {
					visited.add(call);
					result.add(call.getName().getIdentifier());
					builder.sourceType(receiverType);
				}
			}else{
				// then is not related to streams
			}
		}
		return result;
	}
	
	private boolean isStream(String type) {
		String str = getStreamClass(type);
		Class<?> cls = null;
		try {
			cls = Class.forName(str);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean res = BaseStream.class.isAssignableFrom(cls);
		return res;	
	}
	
	private static String getStreamClass(String type) {
		String stream = "java.util.stream"; 
		String streamClass = type.substring(type.indexOf(stream));
		char c = 0;
		int index = 1;
		for (int i = 0; i < streamClass.length(); i++) {
			c = streamClass.charAt(i);
			if ((charIsIn(c, 65, 90)) || (charIsIn(c, 97, 122)) || (isPoint(c))) {
				index = i;
			} else {
				index = i-1;
				break;
			}
		}
		return streamClass.substring(0, index+1);
	}
	
	private static Boolean charIsIn(char c, int bottom, int top) {
		int ascii = (int) c;
		if ((ascii >= bottom) && (ascii <= top))
			return true;
		return false;
	}
	
	private static Boolean isPoint(char c) {
		int ascii = (int) c;
		if (ascii == 46)
			return true;
		return false;
	}
	
	private String extractType(Optional<Expression> scope) {
		try {
			if (scope.isPresent()) {

				Expression expression = scope.get();
				ResolvedType resolvedType = expression.calculateResolvedType();

				return resolvedType.describe();
			}
		} catch (Exception e) {
			// Trace level as this is expected to happen quite often
			Logger.trace(String.format("Error while identifying the types for the scope = %s", scope.get().toString()));
		}
		return UNKNOWN_TYPE;
	}
	
	private String extractType(Expression expression) {
		try {
				ResolvedType resolvedType = expression.calculateResolvedType();
				
				return resolvedType.describe();
			
		} catch (Exception e) {
			// Trace level as this is expected to happen quite often
			Logger.trace(String.format("Error while identifying the types for the expresion = %s", expression.toString()));
		}
		return UNKNOWN_TYPE;
	}
	
	private void countRecognizedMethodTypes(List<MethodCallExpr> listOfCalls) {
		listOfCalls.forEach(call -> {
			if(extractType(call).contains("UNK")) {
				CollectionsExplorer.unknownCounter ++;
				//System.out.println(call.toString());
			}
			CollectionsExplorer.totalCounter ++;
		});
	}
	
	private void countRecognizedMethodTypes(MethodCallExpr call) {
		if(extractType(call).contains("UNK")) 
			CollectionsExplorer.unknownCounter ++;
		CollectionsExplorer.totalCounter ++;
	}
}
