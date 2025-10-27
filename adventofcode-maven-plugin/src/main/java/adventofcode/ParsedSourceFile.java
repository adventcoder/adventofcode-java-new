package adventofcode;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.Name;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParsedSourceFile {
    private static final String PUZZLE_ANNO_NAME = "adventofcode.Puzzle";

    private final CompilationUnit cu;

    public PuzzleInfo getPuzzleInfo() {
        var primaryType = cu.getPrimaryType().orElse(null);
        if (primaryType != null && primaryType.isClassOrInterfaceDeclaration()) {
            for (var anno : primaryType.getAnnotations()) {
                if (isPuzzleAnnoName(anno.getName())) {
                    return new PuzzleInfo(anno);
                }
            }
        }
        return null;
    }

    private boolean isPuzzleAnnoName(Name name) {
        if (name.getQualifier().isPresent()) {
            return name.asString().equals(PUZZLE_ANNO_NAME);
        }
        for (ImportDeclaration imp : cu.getImports()) {
            String qualifiedName = resolve(imp, name.getIdentifier());
            if (qualifiedName != null && qualifiedName.equals(PUZZLE_ANNO_NAME))
                return true;
        }
        return false;
    }

    private String resolve(ImportDeclaration imp, String identifier) {
        if (imp.isAsterisk()) {
            return imp.getNameAsString() + "." + identifier;
        }
        if (imp.getName().getIdentifier().equals(identifier)) {
            return imp.getNameAsString();
        }
        return null;
    }
}
