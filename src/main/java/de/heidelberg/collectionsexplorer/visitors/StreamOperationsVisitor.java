package de.heidelberg.collectionsexplorer.visitors;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.Result;
import de.heidelberg.collectionsexplorer.beans.StreamOperationsInfo;

public class StreamOperationsVisitor extends VoidVisitorAdapter<Result<StreamOperationsInfo>>{
	
	
	private static List<String> streamOperations = List.of("stream");
	
	@Override
	public void visit(MethodCallExpr n, Result<StreamOperationsInfo> arg) {

		if(n.getName().toString() == "stream") {
			System.out.println(n);
		}
		
		String typeName = n.getMetaModel().getTypeName();
		
	
		
		super.visit(n, arg);
	}

}
