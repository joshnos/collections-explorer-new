package de.heidelberg.collectionsexplorer.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.MethodContext;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import de.heidelberg.collectionsexplorer.Result;
import de.heidelberg.collectionsexplorer.beans.MessageInfo;
import de.heidelberg.collectionsexplorer.beans.ObjectCreationInfo;

public class SomethingVisitor extends VoidVisitorAdapter<Result<MessageInfo>>{
	
	TypeSolver solver;
	MethodDeclaration lastMethod;
	public SomethingVisitor(TypeSolver s){
		solver=s;
	}

	 @Override
    public void visit(final MethodDeclaration n, final Result<MessageInfo> arg) {
	     super.visit(n, arg);
	     lastMethod=n;
    }
	@Override
	public void visit(final MethodCallExpr n, final Result<MessageInfo> arg) {
		//super.visit(n,arg);
		if(n.getScope().isPresent()) {
			ExistMessage existVisitor=new ExistMessage("stream");
			n.getScope().get().accept(existVisitor,null);
			
			if(existVisitor.frequency()>0) {
				Extractor extractor=new Extractor();	
				n.getScope().get().accept(extractor,null);
				GetType typeExtractor=new GetType();
				n.getScope().get().accept(typeExtractor,null);
				Node node=typeExtractor.receiver();
				String type="Undefined";
				try {
					if(node!=null) {
						ResolvedType rt=JavaParserFacade.get(solver).getType(node);
						type=rt.describe();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
	    		
				n.getScope().get().accept(typeExtractor,null);
				System.out.println(type+":\n"+extractor.getExtracted()+n.getNameAsString());
			}
		}
	}
	
}
