package de.heidelberg.collectionsexplorer.visitors;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


import de.heidelberg.collectionsexplorer.Result;
import de.heidelberg.collectionsexplorer.beans.MessageInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;

public class ExistMessage extends VoidVisitorAdapter<Result<MessageInfo>>{
	private String methodName;
	private int frequency;
	
	public ExistMessage(String name){
		methodName=name;
		frequency=0;
	}

	@Override
	public void visit(final MethodCallExpr n, final Result<MessageInfo> arg) {
		super.visit(n, arg);
		if (n.getNameAsString().compareTo(methodName) == 0) {
			frequency++;
		}
	}
	
	public int frequency() {
		return frequency;
	}
}
