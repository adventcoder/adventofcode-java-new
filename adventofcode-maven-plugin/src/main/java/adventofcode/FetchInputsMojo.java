package adventofcode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;

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

    @Parameter(defaultValue = "adventofcode/**/Day*.java", required = true, readonly = true)
    private String sourceFilePattern;

    @Parameter(defaultValue = "adventofcode/year%d/Day%d.txt", required = true, readonly = true)
    private String inputPathFormat;

    public File getInputFile(int day) {
        return new File(resourceDirectory, String.format(inputPathFormat, year, day));
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            Set<Integer> days = getInputsToDownload();
            if (days.isEmpty()) {
                getLog().info("No inputs to download");
                return;
            }

            getLog().info("Fetching " + days.size() + " inputs");
            Client client = new Client(session);
            for (int day : days) {
                Path inputPath = getInputFile(day).toPath();
                getLog().debug("Fetching input for day " + day + " -> " + inputPath);

                Files.createDirectories(inputPath.getParent());
                try (OutputStream out = Files.newOutputStream(inputPath)) {
                    client.downloadInput(year, day, out);
                } catch (IOException ioe) {
                    Files.deleteIfExists(inputPath);
                    throw ioe;
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed fetching inputs", e);
        }
    }

    private Set<Integer> getInputsToDownload() throws Exception {
        Set<Integer> days = getRequiredInputs();
        days.removeIf(day -> getInputFile(day).exists());
        return days;
    }

    private Set<Integer> getRequiredInputs() throws IOException {
        JavaParser parser = new JavaParser();
        parser.getParserConfiguration().setLanguageLevel(LanguageLevel.JAVA_17); //TODO: get this from standard maven property?

        List<File> sourceFiles = FileUtils.getFiles(sourceDirectory, sourceFilePattern, null);
        getLog().info("Scanning " + sourceFiles.size() + " source files");

        Set<Integer> days = new HashSet<>();
        for (File sourceFile : sourceFiles) {
            getLog().debug("Parsing source file: " + sourceFile);
            ParseResult<CompilationUnit> result = parser.parse(sourceFile);
            if (!result.isSuccessful()) {
                getLog().warn(sourceFile + ": " + result);
                continue;
            }

            CompilationUnit cu = result.getResult().orElseThrow();
            for (var type : cu.getTypes()) {
                AnnotationExpr puzzleAnno = AstUtils.getPuzzleAnnotation(type, cu);
                if (puzzleAnno != null)
                    days.add(AstUtils.getPuzzleDay(puzzleAnno));
            }
        }
        return days;
    }
}