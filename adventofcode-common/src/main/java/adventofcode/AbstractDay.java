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

import adventofcode.decorators.Decorators;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public abstract class AbstractDay implements Callable<Integer>, Logger {
    private static final Cache cache = new Cache();

    protected final int year;
    protected final int day;
    protected final String name;

    @Option(names = "--debug", description = "Enable debug output")
    public boolean debug;

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
        System.out.println(title());

        String input = getInput();
        Stopwatch stopwatch = new Stopwatch();

        System.out.println();
        System.out.println("- Parsing input");
        long parseTime = stopwatch.resumeFor(this::parse, input);
        System.out.println("  Took " + formatTime(parseTime));

        PartFunction[] partFunctions = { this::part1, this::part2 };
        for (int part = 1; part <= 2; part++) {
            if (partSupported(part)) {
                System.out.println();
                System.out.println("- Part " + part);
                PartResult partResult = stopwatch.resumeFor(partFunctions[part - 1]);
                System.out.println(formatAnswer("  Answer: ", partResult.answer));
                System.out.println("  Took " + formatTime(partResult.time));
            }
        }

        System.out.println();
        System.out.println("Took in total " + formatTime(stopwatch.time()));

        return 0;
    }

    private String formatAnswer(String prefix, Object answer) {
        String answerString = answerToString(answer, "<No answer>");
        return prefix + String.join("\n" + " ".repeat(prefix.length()), answerString.split("\n"));
    }

    private String answerToString(Object answer, String defaultString) {
        if (answer instanceof Optional<?> opt)
            return opt.map(Object::toString).orElse(defaultString);
        if (answer instanceof OptionalInt optInt)
            return optInt.isPresent() ? Integer.toString(optInt.getAsInt()) : defaultString;
        if (answer instanceof OptionalLong optLong)
            return optLong.isPresent() ? Long.toString(optLong.getAsLong()) : defaultString;
        return answer != null ? answer.toString() : defaultString;
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

    public String getInput() throws IOException {
        if (inputFile != null)
            return TextIO.read(inputFile, inputEncoding);
        URL inputURL = getClass().getResource(getInputName());
        if (inputURL != null)
            return TextIO.read(inputURL, StandardCharsets.UTF_8);
        return cache.getInput(year, day, session);
    }

    public String getInputName() {
        return Decorators.undecorate(getClass()).getSimpleName() + ".txt";
    }

    public void parse(String input) {
    }

    public Object part1() {
        throw new UnsupportedOperationException();
    }

    public Object part2() {
        throw new UnsupportedOperationException();
    }

    public boolean partSupported(int part) throws NoSuchMethodException {
        Method method = getClass().getMethod("part" + part);
        return !method.getDeclaringClass().equals(AbstractDay.class);
    }

    public void debug(Object... args) {
        //TODO: should only nest when running inside parser/part1/part2. also the stopwatch should be paused
        if (debug)
            System.out.println(Logger.format("  [DEBUG]", args));
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
