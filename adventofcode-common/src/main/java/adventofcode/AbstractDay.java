package adventofcode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        Puzzle puzzle = Objects.requireNonNull(getClass().getAnnotation(Puzzle.class));
        day = puzzle.day();
        name = puzzle.name();
    }

    public String title() {
        return "Day " + day + ": " + name;
    }

    public Integer call() throws Exception {
        return new DayCallable(this).call();
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
        return getClass().getSimpleName() + ".txt";
    }

    public void parse(String input) {
    }

    public Object part1() {
        throw new UnsupportedOperationException();
    }

    public Object part2() {
        throw new UnsupportedOperationException();
    }

    protected void debug(Object... args) {
        if (debug) {
            String message = Stream.of(args).map(Object::toString).collect(Collectors.joining(" "));
            System.out.println("  [DEBUG] " + message);
        }
    }

    public int[] getAllParts() {
        //TODO: add target parameter
        return IntStream.rangeClosed(1, 2)
            .filter(this::supportsPart)
            .toArray();
    }

    public boolean supportsPart(int part) {
        //TODO: target parts to run should be driven by the command line, then this is not necessary (...except to determine all parts if that's an option)
        try {
            Method method = this.getClass().getMethod("part" + part);
            return !method.getDeclaringClass().equals(AbstractDay.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public Callable<Object> partFunction(int part) {
        if (part == 1)
            return this::part1;
        if (part == 2)
            return this::part2;
        throw new NoSuchElementException();
    }

    public static void main(Class<? extends AbstractDay> dayClass, String[] args) throws Exception {
        AbstractDay day = dayClass.getConstructor().newInstance();
        System.exit(new CommandLine(day).execute(args));
    }

    public static void main(String[] args) throws Exception {
        main(getMainClass(), args);
    }

    private static Class<? extends AbstractDay> getMainClass() throws ClassNotFoundException {
        return Class.forName(System.getProperty("sun.java.command")).asSubclass(AbstractDay.class);
    }
}
