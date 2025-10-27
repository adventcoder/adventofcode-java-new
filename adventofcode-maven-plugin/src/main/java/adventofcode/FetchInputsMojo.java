package adventofcode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

@Mojo(name = "fetch-inputs", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class FetchInputsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources", readonly = true)
    private File resourceDirectory;

    @Parameter(defaultValue = "${aoc.year}", required = true, readonly = true)
    private int year;

    @Parameter(defaultValue = "${aoc.session}", required = true, readonly = true)
    private String sessionToken;

    @Parameter(defaultValue = "adventofcode/**/Day*.java", required = true, readonly = true)
    private String sourceFilePattern;

    @Parameter(defaultValue = "adventofcode/year%d/Day%d.txt", required = true, readonly = true)
    private String inputPathFormat;

    @Override
    public void execute() throws MojoExecutionException {
        SourceFileParser sourceFileParser = new SourceFileParser();
        Session session = new Session(sessionToken);

        try {
            List<File> sourceFiles = FileUtils.getFiles(sourceDirectory, sourceFilePattern, null);
            for (File sourceFile : sourceFiles) {

                getLog().info("Parsing source file " + sourceFile);
                ParsedSourceFile parsedSourceFile = sourceFileParser.parse(sourceFile);

                PuzzleInfo puzzleInfo = parsedSourceFile.getPuzzleInfo();
                if (puzzleInfo != null)
                    fetchInput(session, puzzleInfo.getDay());
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Failed downloading inputs", e);
        }
    }

    private void fetchInput(Session session, int day) throws IOException {
        Path inputPath = resourceDirectory.toPath().resolve(String.format(inputPathFormat, year, day));
        if (Files.exists(inputPath)) {
            getLog().info("Input already exists: " + inputPath);
            return;
        }

        getLog().info("Fetching input for day " + day + " -> " + inputPath);

        Files.createDirectories(inputPath.getParent());
        try (OutputStream out = Files.newOutputStream(inputPath)) {
            session.downloadInput(year, day, out);
        } catch (IOException ioe) {
            Files.deleteIfExists(inputPath);
            throw ioe;
        }
    }
}