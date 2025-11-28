package adventofcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.build.BuildContext;
import org.codehaus.plexus.util.Scanner;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;

import adventofcode.ast.ParsedClass;
import adventofcode.ast.ParsedSourceFile;
import adventofcode.ast.PuzzleAnnotation;

@Mojo(name = "fetch-inputs", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class FetchInputsMojo extends AbstractMojo {

    @Component
    private BuildContext buildContext;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", readonly = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.basedir}/src/main/resources", readonly = true)
    private File resourceDirectory;

    @Parameter(defaultValue = "${maven.compiler.release}", readonly = true)
    private String compilerRelease;

    @Parameter(defaultValue = "${maven.compiler.source}", readonly = true)
    private String compilerSource;

    @Parameter(defaultValue = "${aoc.year}", required = true)
    private int year;

    @Parameter(defaultValue = "${aoc.session}", required = true)
    private String session;

    @Parameter(defaultValue = "adventofcode/**/Day*.java", required = true)
    private String sourceFilePattern;

    @Parameter(defaultValue = "adventofcode/year%d/Day%d.txt", required = true)
    private String inputPathFormat;

    private File getInputFile(int day) {
        return new File(resourceDirectory, String.format(inputPathFormat, year, day));
    }

    private Runtime.Version getSourceVersion() {
        if (compilerRelease != null && !compilerRelease.isBlank())
            return Runtime.Version.parse(compilerRelease.trim());
        if (compilerSource != null && !compilerSource.isBlank())
            return Runtime.Version.parse(compilerSource.trim());
        return null;
    }

    private LanguageLevel getLanguageLevel() {
        Runtime.Version sourceVersion = getSourceVersion();
        if (sourceVersion != null)
            return LanguageLevel.valueOf("JAVA_" + sourceVersion.version().stream()
                .map(Object::toString)
                .collect(Collectors.joining("_")));
        return null;
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
                File inputFile = getInputFile(day);
                getLog().debug("Fetching input for day " + day + " -> " + inputFile);

                inputFile.getParentFile().mkdirs();
                try (OutputStream out = new FileOutputStream(inputFile)) {
                    client.downloadInput(year, day, out);
                } catch (IOException ioe) {
                    inputFile.delete();
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
        List<File> sourceFiles = scanSourceFiles();
        getLog().info("Parsing " + sourceFiles.size() + " source files");

        JavaParser parser = new JavaParser();
        configureParser(parser.getParserConfiguration());

        Set<Integer> days = new HashSet<>();
        for (File sourceFile : sourceFiles) {
            getLog().debug("Parsing source file: " + sourceFile);
            var parseResult = parser.parse(sourceFile);
            if (!parseResult.isSuccessful()) {
                getLog().warn(sourceFile + ": " + parseResult);
                continue;
            }
            ParsedSourceFile parsedSourceFile = new ParsedSourceFile(parseResult.getResult().orElseThrow());
            for (ParsedClass parsedClass : parsedSourceFile.getClassses()) {
                if (parsedClass.isDayClass()) {
                    PuzzleAnnotation anno = parsedClass.getPuzzleAnnotation();
                    if (anno != null && anno.getDay() != null)
                        days.add(anno.getDay());
                }
            }
        }
        return days;
    }

    private void configureParser(ParserConfiguration config) {
        LanguageLevel languageLevel = getLanguageLevel();
        if (languageLevel == null) {
            getLog().warn("Using default language level of " + config.getLanguageLevel());
        } else {
            config.setLanguageLevel(languageLevel);
        }
    }

    private List<File> scanSourceFiles() {
        Scanner scanner = buildContext.newScanner(sourceDirectory);
        scanner.setIncludes(new String[] { sourceFilePattern });
        scanner.scan();
        return Stream.of(scanner.getIncludedFiles())
            .map(path -> new File(sourceDirectory, path))
            .toList();
    }
}
