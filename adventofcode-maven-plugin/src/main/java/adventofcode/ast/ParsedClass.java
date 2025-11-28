package adventofcode.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParsedClass {
    private final ParsedSourceFile sourceFile;
    private final ClassOrInterfaceDeclaration decl;

    public boolean isDayClass() {
        for (ClassOrInterfaceType extendedType : decl.getExtendedTypes())
            if (sourceFile.nameMatches(asName(extendedType), "adventofcode.AbstractDay"))
                return true;
        return false;
    }

    public PuzzleAnnotation getPuzzleAnnotation() {
        for (AnnotationExpr annoExpr : decl.getAnnotations())
            if (sourceFile.nameMatches(annoExpr.getName(), "adventofcode.Puzzle"))
                return new PuzzleAnnotation(annoExpr);
        return null;
    }

    private static Name asName(ClassOrInterfaceType type) {
        Name qualifier = type.getScope().map(scope -> asName(scope)).orElse(null);
        return new Name(qualifier, type.getName().getIdentifier());
    }
}
