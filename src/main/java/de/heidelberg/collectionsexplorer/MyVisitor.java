package de.heidelberg.collectionsexplorer;

import java.util.Optional;

import com.github.javaparser.Position;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.CollectionsInfo.CollectionsInfoBuilder;

/**
 * The simplest implementation of a Visitor. It just adds found
 * ObjectCreationExpr(essions) to the tally, which is stored as the Result
 * object res.
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
		if (filter.filter_match(formatted))
			ret.add(parse(n));
		super.visit(n, ret);
	}

	/**
	 * This little function parses the type from a new statement String. It does
	 * that by simply looking at the content between the angled brackets.
	 * 
	 * @param s the String to be parsed
	 * @return the resulting trimmed, cleaned-up type
	 */
	private String formatRawObjectToString(String s) {
		return s.split("\\<")[0].substring(4).split("\\(")[0];
	}

	private CollectionsInfo parse(ObjectCreationExpr n) {

		String type = "";
		String string = n.toString();
		if (string.matches(".*?<.*?>.*?")) {
			type = string.split("<")[1].split(">")[0];
			string = string.split("\\<")[0];
		}
		type = type.replace(",", "");
		string = string.substring(4); // remove "new "
		string = string.split("\\(")[0];

		Position begin = n.getBegin().get();

		// Builds the info
		CollectionsInfoBuilder builder = CollectionsInfo.builder()
				.className(string)
				.lineNumber(begin.line)
				.columnNumber(begin.column);
		if (type.length() > 0) {
			builder = builder.typeName(type);
		}
		return builder.build();
	}
}
