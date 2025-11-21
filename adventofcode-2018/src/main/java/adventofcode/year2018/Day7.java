package adventofcode.year2018;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.Counter;
import picocli.CommandLine.Option;

//TODO: cleanup
@Puzzle(day = 7, name = "The Sum of Its Parts")
public class Day7 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day7.class, args);
    }

    @Option(names = "--workers", defaultValue = "5")
    private int workers;

    @Option(names = "--base-duration", defaultValue = "60")
    private int baseDuration;

    private Map<String, Set<String>> edges;

    @Override
    public void parse(String input) {
        edges = new HashMap<>();
        for (String line : input.split("\n")) {
            String[] tokens = line.split("\\s+");
            edges.computeIfAbsent(tokens[1], k -> new HashSet<>()).add(tokens[7]);
        }
    }

    @Override
    public String part1() {
        Counter<String> inDegree = new Counter<>();
        for (String a : edges.keySet())
            for (String b : edges.get(a))
                inDegree.add(b, 1);

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String step : edges.keySet())
            if (inDegree.getInt(step) == 0)
                queue.add(step);

        StringBuilder seq = new StringBuilder();
        while (!queue.isEmpty()) {
            String step = queue.poll();
            seq.append(step);

            for (String out : edges.getOrDefault(step, Collections.emptySet()))
                if (inDegree.subtract(out, 1) == 0)
                    queue.add(out);
        }

        return seq.toString();
    }

    @Override
    public Integer part2() {
        Counter<String> inDegree = new Counter<>();
        for (String a : edges.keySet())
            for (String b : edges.get(a))
                inDegree.add(b, 1);

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String step : edges.keySet())
            if (inDegree.getInt(step) == 0)
                queue.add(step);

        int totalTime = 0;

        Counter<String> tasks = new Counter<>();
        addTasks(queue, tasks);

        while (!tasks.isEmpty()) {
            int timeStep = Fn.min(tasks.values());
            totalTime += timeStep;
            for (String step : tasks.keySet()) {
                if (tasks.subtract(step, timeStep) == 0) {
                    tasks.removeInt(step);
                    for (String out : edges.getOrDefault(step, Collections.emptySet()))
                        if (inDegree.subtract(out, 1) == 0)
                            queue.add(out);
                }
            }
            addTasks(queue, tasks);
        }

        return totalTime;
    }

    private void addTasks(PriorityQueue<String> queue, Counter<String> tasks) {
        while (tasks.size() < workers && !queue.isEmpty()) {
            String step = queue.poll();
            tasks.put(step, baseDuration + step.charAt(0) - 'A' + 1);
        }
    }
}
