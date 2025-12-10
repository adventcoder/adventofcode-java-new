package adventofcode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Callable;

import adventofcode.utils.decorators.Decorators;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public abstract class AbstractDay implements Callable<Integer>, Logger {
    protected final int year;
    protected final int day;
    protected final String name;

    private final Cache cache = new Cache();
    private final Stopwatch stopwatch = new Stopwatch();

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

    public Integer call() throws Exception {
        System.out.println(formatTitle());
        System.out.println();

        String input = getInput();
        stopwatch.reset();

        System.out.println("- Parsing input");
        long parseTime = stopwatch.resumeFor(() -> parse(input));
        System.out.println("  Took " + formatTime(parseTime));

        if (includeCommon()) {
            System.out.println();
            System.out.println("- Common");
            long commonTime = stopwatch.resumeFor(this::common);
            System.out.println("  Took " + formatTime(commonTime));
        }

        if (includePart(1)) {
            System.out.println();
            System.out.println("- Part 1");
            var pair = stopwatch.resumeFor(this::part1);
            System.out.println(formatAnswer("  Answer: ", pair.left()));
            System.out.println("  Took " + formatTime(pair.rightLong()));
        }

        if (includePart(2)) {
            System.out.println();
            System.out.println("- Part 2");
            var pair = stopwatch.resumeFor(this::part2);
            System.out.println(formatAnswer("  Answer: ", pair.left()));
            System.out.println("  Took " + formatTime(pair.rightLong()));
        }

        System.out.println();
        System.out.println("Took in total " + formatTime(stopwatch.time()));
        return 0;
    }

    private String formatTitle() {
        String title = "Day " + day;
        if (name != null)
            title += ": " + name;
        return "--- " + title + " ---";
    }

    private String formatAnswer(String prefix, Object answer) {
        String answerString = answerToString(answer, "<No answer>");
        return prefix + String.join("\n" + " ".repeat(prefix.length()), answerString.split("\n"));
    }

    private String formatTime(long nanos) {
        long seconds = nanos / 1000_000_000;
        nanos %= 1000_000_000;
        if (seconds != 0) {
            return String.format("%d s %d ms", seconds, nanos / 1000_000);
        } else {
            return String.format("%.3f ms", (double) nanos / 1000_000);
        }
    }

    protected String answerToString(Object answer, String defaultString) {
        if (answer instanceof Optional<?> opt)
            return opt.map(Object::toString).orElse(defaultString);
        if (answer instanceof OptionalInt optInt)
            return optInt.isPresent() ? Integer.toString(optInt.getAsInt()) : defaultString;
        if (answer instanceof OptionalLong optLong)
            return optLong.isPresent() ? Long.toString(optLong.getAsLong()) : defaultString;
        return answer != null ? answer.toString() : defaultString;
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

    // this is done inconsistently since I added common later but in theory this should do any
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

    protected boolean includeCommon() throws Exception {
        return methodOverriden("common");
    }

    protected boolean includePart(int part) throws Exception {
        return methodOverriden("part" + part);
    }

    private boolean methodOverriden(String name) throws NoSuchMethodException {
        Method method = getClass().getMethod(name);
        return method.getDeclaringClass() != AbstractDay.class;
    }

    public void log(Logger.Level level, String message) {
        if (verbose)
            stopwatch.pauseFor(() -> System.out.println("  [" + level + "]" + message));
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
