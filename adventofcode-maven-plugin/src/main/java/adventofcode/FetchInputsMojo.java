package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

@Mojo(name = "fetch-inputs", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class FetchInputsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources", readonly = true)
    private File resourceDirectory;

    @Parameter(defaultValue = "${aoc.year}", required = true, readonly = true)
    private int year;

    @Parameter(defaultValue = "${aoc.session}", required = true, readonly = true)
    private String session;

    @Parameter(defaultValue = "adventofcode/**/*.java", required = true, readonly = true)
    private String sourceFilePattern;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            List<File> sourceFiles = FileUtils.getFiles(sourceDirectory, sourceFilePattern, null);
            for (File sourceFile : sourceFiles) {
                var day = getPuzzleDay(sourceFile);
                if (day != null) {
                    fetchInput(day);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed downloading inputs", e);
        }
    }

    public Integer getPuzzleDay(File sourceFile) throws IOException {
        getLog().info("Parsing source file " + sourceFile);

        CompilationUnit cu = new JavaParser()
                .parse(sourceFile)
                .getResult()
                .orElseThrow(() -> new IOException("Failed to parse " + sourceFile));

        for (ClassOrInterfaceDeclaration decl : cu.findAll(ClassOrInterfaceDeclaration.class)) {
            for (AnnotationExpr anno : decl.getAnnotations()) {
                String annoIdentifier = anno.getName().getIdentifier();
                if (annoIdentifier.equals("Puzzle")) {
                    if (anno.isNormalAnnotationExpr()) {
                        NormalAnnotationExpr normal = anno.asNormalAnnotationExpr();
                        for (MemberValuePair pair : normal.getPairs()) {
                            if (pair.getNameAsString().equals("day") && pair.getValue().isIntegerLiteralExpr()) {
                                return pair.getValue().asIntegerLiteralExpr().asNumber().intValue();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private void fetchInput(int day) throws IOException {
        Path outputPath = resourceDirectory.toPath()
            .resolve("adventofcode/year" + year + "/Day" + day + ".txt");
        if (Files.exists(outputPath)) {
            getLog().info("Input already exists: " + outputPath);
            return;
        }

        getLog().info("Fetching input for day " + day + " -> " + outputPath);

        Files.createDirectories(outputPath.getParent());
        try (OutputStream out = Files.newOutputStream(outputPath)) {
            downloadInput(day, out);
        } catch (IOException ioe) {
            Files.deleteIfExists(outputPath);
            throw ioe;
        }
    }

    private void downloadInput(int day, OutputStream out) throws IOException {
        URL url = new URL("https://adventofcode.com/" + year + "/day/" + day + "/input");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Cookie", "session=" + session);
        conn.setInstanceFollowRedirects(false);

        if (conn.getResponseCode() != 200) {
            throw new IOException(appendErrorResponse("Failed to download input for day " + day, conn));
        }

        try (InputStream in = conn.getInputStream()) {
            in.transferTo(out);
        }
    }

    private String appendErrorResponse(String message, HttpURLConnection conn) throws IOException {
        StringBuilder builder = new StringBuilder(message);
        builder.append(": HTTP ").append(conn.getResponseCode());
        try (InputStream err = conn.getErrorStream()) {
            if (err != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));
                for (String line; (line = reader.readLine()) != null; )
                    builder.append("\n").append(line);
            }
        }
        return builder.toString();
    }
}