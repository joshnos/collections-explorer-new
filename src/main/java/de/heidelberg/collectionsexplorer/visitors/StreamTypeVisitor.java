package de.heidelberg.collectionsexplorer.visitors;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import de.heidelberg.collectionsexplorer.Filter;
import de.heidelberg.collectionsexplorer.beans.StreamTypeInfo;
import de.heidelberg.collectionsexplorer.beans.StreamTypeInfo.StreamTypeInfoBuilder;
import de.heidelberg.collectionsexplorer.context.Result;
import de.heidelberg.collectionsexplorer.util.ParserUtil;
import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.BaseStream;

public class StreamTypeVisitor extends VoidVisitorAdapter<Result<StreamTypeInfo>> {

    private static final String UNKNOWN_TYPE = "UNK";
    private static final String STREAM = "stream";
    private static final String PARALLEL_STREAM = "parallelStream";
    private static final String OF = "of";
    private HashMap<Expression,String> cache;

    public StreamTypeVisitor(Filter filter) {
        // Ignoring filter for now...
        cache=new HashMap<>();
    }

    @Override
    public void visit(final MethodCallExpr call, final Result<StreamTypeInfo> result) {
        String returnType = extractType(call);
        String scopeType = extractType(call.getScope());
        boolean streamKeyword = call.getNameAsString().contentEquals(STREAM) ||
                call.getNameAsString().contentEquals(PARALLEL_STREAM) ||
                call.getNameAsString().contentEquals(OF);
        if(streamKeyword || isStream(returnType) || isStream(scopeType)){
            result.add(buildResult(call, returnType, scopeType));
        }
        super.visit(call, result);
    }

    private StreamTypeInfo buildResult(MethodCallExpr call, String methodType, String scopeType) {

        StreamTypeInfoBuilder builder = StreamTypeInfo.builder();

        builder.className(ParserUtil.retrieveClass(call));
        builder.lineNumber(ParserUtil.getLineNumber(call));
        builder.columnNumber(ParserUtil.getColumn(call));
        builder.methodName(call.getNameAsString());
        builder.methodType(methodType);
        builder.scopeType(scopeType);
        builder.build();

        return builder.build();
    }

    private boolean isStream(String type) {
        if(type.contains("java.util.stream")){
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
        return false;
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

            String typeString=resolvedType.describe();
            cache.put(expression,typeString);
            return typeString;
        } catch (Exception e) {
            // Trace level as this is expected to happen quite often
            Logger.trace(String.format("Error while identifying the types for the expresion = %s", expression.toString()));
        }
        cache.put(expression,UNKNOWN_TYPE);
        return UNKNOWN_TYPE;
    }
}
