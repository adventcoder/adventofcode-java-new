package adventofcode.year2025;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.decorators.Memoized;
import picocli.CommandLine.Option;

@Puzzle(day = 11, name = "Reactor")
public class Day11 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day11.class, args);
    }

    @Option(names = "--dot-file")
    private File dotFile;

    private Map<String, Set<String>> graph;

    @Override
    public void parse(String input) {
        graph = new HashMap<>();
        for (String line : input.split("\n")) {
            String[] pair = line.split(":");
            graph.put(pair[0].trim(), Set.of(pair[1].trim().split("\\s+")));
        }
        writeGraph();
    }

    @Override
    public Long part1() {
        return paths("you", "out");
    }

    @Override
    public Long part2() {
        return paths("svr", "fft") * paths("fft", "dac") * paths("dac", "out") +
            paths("svr", "dac") * paths("dac", "fft") * paths("fft", "out");
    }

    @Memoized
    protected long paths(String start, String end) {
        if (start.equals(end)) return 1;
        return Fn.sumLong(graph.getOrDefault(start, Set.of()), next -> paths(next, end));
    }

    private void writeGraph() {
        if (dotFile == null) return;
        try (PrintStream out = new PrintStream(dotFile)) {
            out.println("digraph {");
            for (String a : graph.keySet())
                for (String b : graph.get(a))
                    out.println(a + " -> " + b + ";");
            out.println();
            out.println("}");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
