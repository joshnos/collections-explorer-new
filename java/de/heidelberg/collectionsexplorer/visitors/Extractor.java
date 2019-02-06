package de.heidelberg.collectionsexplorer.visitors;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


import de.heidelberg.collectionsexplorer.Result;
import de.heidelberg.collectionsexplorer.beans.MessageInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;

public class Extractor extends VoidVisitorAdapter<Result<MessageInfo>>{
	private String extracted;
	private boolean found;
	public Extractor(){
		extracted="";
		found=false;
	}

	@Override
	public void visit(final MethodCallExpr n, final Result<MessageInfo> arg) {
		if(!found){
			n.getScope().ifPresent(l -> l.accept(this, arg));
			extracted = extracted +n.getNameAsString()+",";
		}
		if(n.getNameAsString().equals("stream")) {
			found=true;
		}
	}
	public String getExtracted() {
		return extracted;
	}
}
