package adventofcode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;
import java.util.concurrent.Callable;

import adventofcode.decorators.Decorators;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public abstract class AbstractDay implements Callable<Integer>, Logger {
    protected final int year;
    protected final int day;
    protected final String name;

    private final DayRunner runner = new DayRunner(this);
    private final Cache cache = new Cache();

    @Option(names = "--verbose", description = "Enable verbose output")
    public boolean verbose;

    @Option(names = "--input", description = "Path to input file")
    private File inputFile;

    @Option(names = "--input-encoding", description = "Encoding of the input file")
    private Charset inputEncoding = StandardCharsets.UTF_8;

    @Option(names = "--session", defaultValue = "${env:AOC_SESSION}")
    private String session;

    public AbstractDay() {
        Puzzle puzzle = getPuzzle();
        if (puzzle != null) {
            year = puzzle.year() >= 0 ? puzzle.year() : detectYear().orElseThrow();
            day = puzzle.day() >= 0 ? puzzle.day() : detectDay().orElseThrow();
            name = puzzle.name().isEmpty() ? null : puzzle.name();
        } else {
            year = detectYear().orElseThrow();
            day = detectDay().orElseThrow();
            name = null;
        }
    }

    private Puzzle getPuzzle() {
        return Decorators.undecorate(getClass()).getAnnotation(Puzzle.class);
    }

    private OptionalInt detectYear() {
        String[] parts = Decorators.undecorate(getClass()).getPackageName().split("\\.");
        String lastPart = parts[parts.length - 1];
        if (lastPart.matches("year(\\d+)"))
            return OptionalInt.of(Integer.parseInt(lastPart.substring(4)));
        return OptionalInt.empty();
    }

    private OptionalInt detectDay() {
        String name = Decorators.undecorate(getClass()).getSimpleName();
        if (name.matches("Day(\\d+)"))
            return OptionalInt.of(Integer.parseInt(name.substring(3)));
        return OptionalInt.empty();
    }

    public String title() {
        if (name == null)
            return String.format("--- Day %d ---", day);
        else
            return String.format("--- Day %d: %s ---", day, name);
    }

    public Integer call() throws Exception {
        runner.intro();
        runner.parse(getInput());
        if (hasCommon())
            runner.common();
        if (partSupported(1))
            runner.part(1, this::part1);
        if (partSupported(2))
            runner.part(2, this::part2);
        runner.outro();
        return 0;
    }

    protected String getInput() throws IOException {
        if (inputFile != null)
            return TextIO.read(inputFile, inputEncoding);
        URL inputURL = getClass().getResource(getInputName());
        if (inputURL != null)
            return TextIO.read(inputURL, StandardCharsets.UTF_8);
        return cache.getInput(year, day, session);
    }

    protected String getInputName() {
        return Decorators.undecorate(getClass()).getSimpleName() + ".txt";
    }

    public void parse(String input) {
    }

    // this is done inconsistently since I added it later but in theory this should do any
    // preprocessing that's required for either part 1 or 2, that way part 2 could run standalone
    // without assuming part 1 ran first.
    //TODO: enforce this and add a --part parameter to just run a specific part
    public void common() {
    }

    public Object part1() {
        throw new UnsupportedOperationException();
    }

    public Object part2() {
        throw new UnsupportedOperationException();
    }

    protected boolean hasCommon() throws NoSuchMethodException {
        return methodOverriden("common");
    }

    protected boolean partSupported(int part) throws NoSuchMethodException {
        return methodOverriden("part" + part);
    }

    private boolean methodOverriden(String name) throws NoSuchMethodException {
        Method method = getClass().getMethod(name);
        return method.getDeclaringClass() != AbstractDay.class;
    }

    public void log(Logger.Level level, String message) {
        if (verbose)
            runner.log(level, message);
    }

    public static void main(String[] args) throws Exception {
        Class<?> mainClass = Class.forName(System.getProperty("sun.java.command"));
        main(mainClass.asSubclass(AbstractDay.class), args);
    }

    public static void main(Class<? extends AbstractDay> dayClass, String[] args) throws Exception {
        AbstractDay day = Decorators.decorate(dayClass).getConstructor().newInstance();
        System.exit(new CommandLine(day).execute(args));
    }
}
