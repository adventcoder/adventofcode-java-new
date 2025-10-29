package adventofcode;

import java.util.List;
import java.util.concurrent.Callable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DayCallable implements Callable<Integer> {
    private final AbstractDay day;

    public Integer call() throws Exception {
        System.out.println(day.title());

        String input = day.getInput();
        Stopwatch stopwatch = new Stopwatch();

        System.out.println();
        System.out.println("- Parsing input");
        long parseTime = stopwatch.resumeFor(day::parse, input);
        System.out.println("  Took " + formatTime(parseTime));

        for (int part : day.getAllParts()) {
            System.out.println();
            System.out.println("- Part " + part);
            PartResult partResult = stopwatch.resumeFor(day.partFunction(part));
            System.out.println(formatAnswer("  Answer: ", partResult.answer));
            System.out.println("  Took " + formatTime(partResult.time));
        }

        System.out.println();
        System.out.println("Took in total " + formatTime(stopwatch.time()));

        return 0;
    }

    private String formatAnswer(String prefix, Object answer) {
        String answerString = answer == null ? "<No answer>" : answer.toString();
        return prefix + String.join("\n" + " ".repeat(prefix.length()), answerString.split("\n"));
    }

    private String formatTime(long time) {
        String unit = "ns";
        if (time < 1000L)
            return String.format("%d %s", time, unit);
        for (String nextUnit : List.of("us", "ms", "s")) {
            if (time < 1000000L)
                return String.format("%d %s %d %s", time / 1000L, nextUnit, time % 1000L, unit);
            time /= 1000L;
            unit = nextUnit;
        }
        return String.format("%d %s", time, unit);
    }
}
