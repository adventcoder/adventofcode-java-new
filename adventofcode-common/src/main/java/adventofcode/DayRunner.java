package adventofcode;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Callable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DayRunner {
    private final AbstractDay day;
    private final Stopwatch stopwatch = new Stopwatch();

    public void intro() {
        System.out.println(day.title());
        System.out.println();
    }

    public void parse(String input) {
        System.out.println("- Parsing input");
        long parseTime = stopwatch.resumeFor(() -> day.parse(input));
        System.out.println("  Took " + formatTime(parseTime));
        System.out.println();
    }

    public void common() {
        System.out.println("- Common");
        long commonTime = stopwatch.resumeFor(day::common);
        System.out.println("  Took " + formatTime(commonTime));
        System.out.println();
    }

    public void part(int part, Callable<Object> partFunction) throws Exception {
        System.out.println("- Part " + part);
        var pair = stopwatch.resumeFor(partFunction);
        System.out.println(formatAnswer("  Answer: ", pair.left()));
        System.out.println("  Took " + formatTime(pair.rightLong()));
        System.out.println();
    }

    public void outro() {
        System.out.println("Took in total " + formatTime(stopwatch.time()));
    }

    public void log(Logger.Level level, String message) {
        stopwatch.pauseFor(() -> System.out.println("  [" + level + "]" + message));
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
}
