package adventofcode.year2018;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.IntArrays;
import lombok.AllArgsConstructor;

@Puzzle(day = 4, name = "Repose Record")
public class Day4 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day4.class, args);
    }

    private Map<Integer, int[]> counts = new HashMap<>();

    @Override
    public void parse(String input) {
        List<Log> logs = Fn.parseVals(input, "\n", Log::parse).sorted(Comparator.comparing(log -> log.ts)).toList();
        Integer guardId = null;
        Integer startMinute = null;
        for (Log log : logs) {
            switch (log.type) {
                case BEGINS_SHIFT -> {
                    guardId = log.guardId;
                }
                case FALLS_ASLEEP -> {
                    startMinute = log.ts.getMinute();
                }
                case WAKES_UP -> {
                    addNap(Objects.requireNonNull(guardId), Objects.requireNonNull(startMinute), log.ts.getMinute());
                    startMinute = null;
                }
            }
        }
    }

    private void addNap(Integer guardId, int startMinute, int endMinute) {
        int[] guardCounts = counts.computeIfAbsent(guardId, k -> new int[60]);
        for (int m = startMinute; m < endMinute; m++)
            guardCounts[m]++;
    }

    @Override
    public Integer part1() {
        Integer guardId = Fn.argMax(counts.keySet(), gid -> IntArrays.sum(counts.get(gid)));
        return guardId * maxGuardMinute(guardId);
    }

    @Override
    public Integer part2() {
        Integer guardId = Fn.argMax(counts.keySet(), gid -> IntArrays.max(counts.get(gid)));
        return guardId * maxGuardMinute(guardId);
    }

    private int maxGuardMinute(Integer gid) {
        int[] guardCounts = counts.get(gid);
        return IntArrays.index(guardCounts, IntArrays.max(guardCounts));
    }

    @AllArgsConstructor
    private static class Log {
        public static enum Type { BEGINS_SHIFT, FALLS_ASLEEP, WAKES_UP }

        public final LocalDateTime ts;
        public final Type type;
        public final Integer guardId;

        public static Log parse(String line) {
            LocalDateTime ts = LocalDateTime.parse(line.substring(1, 17), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String msg = line.substring(19);
            if (msg.matches("Guard #\\d+ begins shift")) {
                int guardId = Integer.parseInt(msg.substring(7, msg.length() - 13));
                return new Log(ts, Type.BEGINS_SHIFT, guardId);
            } else if (msg.equals("falls asleep")) {
                return new Log(ts, Type.FALLS_ASLEEP, null);
            } else if (msg.equals("wakes up")) {
                return new Log(ts, Type.WAKES_UP, null);
            } else {
                throw new InputMismatchException(msg);
            }
        }
    }
}
