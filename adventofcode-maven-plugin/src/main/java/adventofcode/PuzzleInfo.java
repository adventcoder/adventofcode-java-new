package adventofcode;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PuzzleInfo {
    private final AnnotationExpr puzzleAnno;

    public int getDay() {
        if (puzzleAnno.isNormalAnnotationExpr()) {
            for (MemberValuePair pair : puzzleAnno.asNormalAnnotationExpr().getPairs()) {
                if (pair.getNameAsString().equals("day") && pair.getValue().isIntegerLiteralExpr()) {
                    return pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                }
            }
        }
        throw new IllegalStateException("Could not extract day from annotation " + puzzleAnno);
    }
}
