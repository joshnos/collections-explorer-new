package de.heidelberg.collectionsexplorer;


import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * STUB
 * This class will contain an alternative Visitor that instead of tallying results
 * will modify them, to create a new compilation. Work in progress.
 * 
 * @author Janos Sebök
 *
 */
public class MyEditor extends VoidVisitorAdapter<Result> {
	
	@Override
	public void visit(ObjectCreationExpr n, Result ret) {
		ret.add(parse(n));
		super.visit(n, ret);
	}

	private CollectionsInfo parse(ObjectCreationExpr n) {
		// TODO Auto-generated method stub
		return null;
	}
}
