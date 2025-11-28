package adventofcode.ast;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PuzzleAnnotation {
    private final AnnotationExpr expr;

    public Integer getDay() {
        if (expr.isNormalAnnotationExpr())
            for (MemberValuePair pair : expr.asNormalAnnotationExpr().getPairs())
                if (pair.getNameAsString().equals("day") && pair.getValue().isIntegerLiteralExpr())
                    return pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
        return null;
    }
}
