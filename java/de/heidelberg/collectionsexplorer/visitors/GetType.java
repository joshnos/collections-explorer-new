package de.heidelberg.collectionsexplorer.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import de.heidelberg.collectionsexplorer.Result;
import de.heidelberg.collectionsexplorer.beans.MessageInfo;

public class GetType extends VoidVisitorAdapter<Result<MessageInfo>>{
	private String type;
	private boolean found;
	private Node receiver;
	public GetType(){
		type="Undefined";
		found=false;
	}

	@Override
	public void visit(final MethodCallExpr n, final Result<MessageInfo> arg) {
		if(!found){
			n.getScope().ifPresent(l -> l.accept(this, arg));
		}
		if(n.getNameAsString().equals("stream")) {
			found=true;
			if(n.getScope().isPresent()) {
				receiver=n.getScope().get();
			}
		}
	}
	public Node receiver() {
		return receiver;
	}
}
