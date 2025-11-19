package adventofcode;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.Name;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AstUtils {
    public static AnnotationExpr getPuzzleAnnotation(TypeDeclaration<?> type, CompilationUnit cu) {
        for (AnnotationExpr anno : type.getAnnotations())
            if (AstUtils.nameMatches(anno.getName(), "adventofcode.Puzzle", cu))
                return anno;
        return null;
    }

    public static Integer getPuzzleDay(AnnotationExpr puzzleAnno) {
        if (puzzleAnno.isNormalAnnotationExpr()) {
            for (MemberValuePair pair : puzzleAnno.asNormalAnnotationExpr().getPairs()) {
                if (pair.getNameAsString().equals("day") && pair.getValue().isIntegerLiteralExpr()) {
                    return pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                }
            }
        }
        return null;
    }

    public static boolean nameMatches(Name name, String qualifiedName, CompilationUnit cu) {
        if (name.getQualifier().isPresent()) {
            return name.asString().equals(qualifiedName);
        }
        for (ImportDeclaration imp : cu.getImports()) {
            if (imp.isAsterisk()) {
                if ((imp.getNameAsString() + "." + name.getIdentifier()).equals(qualifiedName))
                    return true;
            } else {
                if (imp.getName().getIdentifier().equals(name.getIdentifier()) && imp.getNameAsString().equals(qualifiedName))
                    return true;
            }
        }
        return false;
    }
}
