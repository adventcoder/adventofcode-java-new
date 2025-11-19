package adventofcode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public abstract class AbstractDay implements Callable<Integer> {
    protected final int day;
    protected final String name;

    @Option(names = "--debug", description = "Enabled debug output")
    public boolean debug;

    @Option(names = "--input", description = "Path to input file")
    private File inputFile;

    public AbstractDay() {
        Puzzle puzzle = Objects.requireNonNull(getPuzzle());
        day = puzzle.day();
        name = puzzle.name();
    }

    private Puzzle getPuzzle() {
        Class<?> curr = getClass();
        while (curr != AbstractDay.class) {
            Puzzle puzzle = curr.getAnnotation(Puzzle.class);
            if (puzzle != null)
                return puzzle;
            curr = curr.getSuperclass();
        }
        return null;
    }

    public String title() {
        return "Day " + day + ": " + name;
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

    private String formatTime(long time) {
        if (time >= 1000_000_000L) {
            return String.format("%d s %d ms", time / 1000_000_000L, time % 1000_000_000L / 1000_000L);
        } else {
            return String.format("%.3f ms", time * 1e-6);
        }
    }

    public String getInput() throws IOException {
        if (inputFile == null) {
            return Inputs.read(getClass(), getInputName());
        } else if (inputFile.toString().equals("-")) {
            return Inputs.read(System.in);
        } else {
            return Inputs.read(inputFile);
        }
    }

    public String getInputName() {
        return String.format("Day%d.txt", day);
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

    protected void debug(Object... args) {
        if (debug) {
            String message = Stream.of(args).map(Object::toString).collect(Collectors.joining(" "));
            System.out.println("  [DEBUG] " + message);
        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> mainClass = Class.forName(System.getProperty("sun.java.command"));
        main(mainClass.asSubclass(AbstractDay.class), args);
    }

    public static void main(Class<? extends AbstractDay> dayClass, String[] args) throws Exception {
        System.exit(new CommandLine(newInstance(dayClass)).execute(args));
    }

    public static AbstractDay newInstance(Class<? extends AbstractDay> dayClass) throws Exception {
        return Memoized.Interceptor.subclass(dayClass).getConstructor().newInstance();
    }
}
