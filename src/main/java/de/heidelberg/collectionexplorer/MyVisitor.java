package de.heidelberg.collectionexplorer;

import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * The simplest implementation of a Visitor. It just adds found ObjectCreationExpr(essions) to
 * the tally, which is stored as the Result object res.
 * 
 * @author Janos Sebök
 *
 */
public class MyVisitor extends VoidVisitorAdapter<Result> {
	
	Filter filter;
	
	public MyVisitor(Filter filter) {
		super();
		this.filter = filter;
	}

	@Override
	public void visit(ObjectCreationExpr n, Result ret) {
	
		String formatted = formatRawObjectToString(n.toString());
		if(filter.filter_match(formatted))
			ret.add(parse(n));
		super.visit(n, ret);
	}
	
	/**
	 * This little function parses the type from a new statement String.
	 * It does that by simply looking at the content between the angled brackets.
	 * 
	 * @param s		the String to be parsed
	 * @return		the resulting trimmed, cleaned-up type
	 */
	private String formatRawObjectToString(String s) {
		return s.split("\\<")[0].substring(4).split("\\(")[0];
	}
	
	private CollectionInfoBean parse(ObjectCreationExpr n) {
		
		String type = "";
		String string = n.toString();
		if (string.matches(".*?<.*?>.*?")) {
			type = string.split("<")[1].split(">")[0];
			string = string.split("\\<")[0];
		}
		type = type.replace(",", "");
		string = string.substring(4); // remove "new "
		string = string.split("\\(")[0];
		if (type.length() > 0) {
			CollectionInfoBean bean = new CollectionInfoBean(string, n.getBeginLine(), n.getBeginColumn(), type);
			return bean;
		} else {
			CollectionInfoBean bean = new CollectionInfoBean(string, n.getBeginLine(), n.getBeginColumn());
			return bean;
		}
	}
}
