package de.heidelberg.collectionsexplorer.visitors;

import java.util.*;
import java.util.List;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.StreamByKeyWordInfo;
import de.heidelberg.collectionsexplorer.beans.StringListInfo;
import de.heidelberg.collectionsexplorer.beans.StreamByKeyWordInfo.StreamByKeyWordInfoBuilder;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;

public class StreamKeyWordVisitor  extends VoidVisitorAdapter<Result<StreamByKeyWordInfo>> {

    private static final String STREAM = "stream";
    private static final String PARALLEL_STREAM = "parallelStream";
    private static final String OF = "of";

    public StreamKeyWordVisitor(Filter filter) {
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
    public void visit(final MethodCallExpr call, final Result<StreamByKeyWordInfo> result) {
        Boolean v = get(call)==null;
        if(v) {
            List<MethodCallExpr> allExpCalls = call.findAll(MethodCallExpr.class);
            Optional<MethodCallExpr> streamMethodCall = allExpCalls.stream()
                    .filter(x -> x.getNameAsString().contentEquals(STREAM) ||
                            x.getNameAsString().contentEquals(PARALLEL_STREAM) ||
                            x.getNameAsString().contentEquals(OF))
                    .findAny();
            if(streamMethodCall.isPresent()) {
                StreamByKeyWordInfo info = extractStreamOperations(call, streamMethodCall.get().getNameAsString());
                if (info != null) {
                    result.add(info);
                }
            }
        }

        super.visit(call, result);
    }

    private StreamByKeyWordInfo extractStreamOperations(MethodCallExpr methodCall, String name) {
        StreamByKeyWordInfoBuilder builder = StreamByKeyWordInfo.builder();
        LinkedList<String> list = extractChain(methodCall, name);
        Optional<String> streamMethodCall = list.stream()
                .filter(x -> x.contentEquals(STREAM) ||
                        x.contentEquals(PARALLEL_STREAM) ||
                        x.contentEquals(OF))
                .findAny();

        if(streamMethodCall.isPresent() && list.size()>0) {
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

            return builder.build();

        }else {
            return null;
        }
    }

    private LinkedList<String> extractChain(MethodCallExpr methodCall, String name) {
        LinkedList<String> result = new LinkedList<>();
        Optional<Expression> scope = methodCall.getScope();
        visited.add(methodCall);
        if(!methodCall.getNameAsString().contentEquals(name)) {
            if(scope.isPresent() && scope.get() instanceof MethodCallExpr)
                result.addAll(extractChain((MethodCallExpr)scope.get(), name));
            result.add(methodCall.getNameAsString());
        } else {
            result.add(methodCall.getNameAsString());
        }


        return result;
    }

}

