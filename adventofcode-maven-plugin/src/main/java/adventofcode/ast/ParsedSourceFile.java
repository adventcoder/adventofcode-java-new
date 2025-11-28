package adventofcode.ast;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ParsedSourceFile {
    private final CompilationUnit compilationUnit;

    public List<ParsedClass> getClassses() {
        List<ParsedClass> classes = new ArrayList<>();
        for (TypeDeclaration<?> typeDecl : compilationUnit.getTypes()) {
            if (typeDecl.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration decl = typeDecl.asClassOrInterfaceDeclaration();
                if (!decl.isInterface())
                    classes.add(new ParsedClass(this, decl));
            }
        }
        return classes;
    }

    public boolean nameMatches(Name name, String qualifiedName) {
        if (name.getQualifier().isPresent()) {
            return name.toString().equals(qualifiedName);
        }
        for (ImportDeclaration importDecl : compilationUnit.getImports()) {
            if (importDecl.isAsterisk()) {
                if ((importDecl.getNameAsString() + "." + name.getIdentifier()).equals(qualifiedName))
                    return true;
            } else {
                if (importDecl.getName().getIdentifier().equals(name.getIdentifier()) && importDecl.getNameAsString().equals(qualifiedName))
                    return true;
            }
        }
        return false;
    }
}
