package adventofcode;

import java.io.File;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class SourceFileParser {
    private final JavaParser parser;
    
    public SourceFileParser() {
        parser = new JavaParser();
    }

    public ParsedSourceFile parse(File sourceFile) throws IOException {
        CompilationUnit cu = parser.parse(sourceFile).getResult()
            .orElseThrow(() -> new IOException("Failed to parse " + sourceFile));
        return new ParsedSourceFile(cu);
    }
}
