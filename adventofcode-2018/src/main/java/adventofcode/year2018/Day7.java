package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.Counter;
import adventofcode.utils.collect.DefaultHashMap;
import lombok.AllArgsConstructor;
import picocli.CommandLine.Option;

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
        edges = new DefaultHashMap<>(HashSet::new);
        for (String line : input.split("\n")) {
            String[] tokens = line.split("\\s+");
            edges.get(tokens[1]).add(tokens[7]);
        }
    }

    @Override
    public String part1() {
        StepQueue queue = new StepQueue();
        List<String> seq = new ArrayList<>();
        while (!queue.isEmpty()) {
            String step = queue.poll();
            seq.add(step);
            queue.finished(step);
        }
        return String.join("", seq);
    }

    @Override
    public Integer part2() {
        int totalTime = 0;
        StepQueue queue = new StepQueue();
        List<Task> tasks = new ArrayList<>();
        addTasks(queue, tasks);
        while (!tasks.isEmpty()) {
            int timeStep = Fn.min(tasks, task -> task.timeRemaining);
            totalTime += timeStep;
            tasks.removeIf(task -> {
                task.timeRemaining -= timeStep;
                if (task.timeRemaining == 0) {
                    queue.finished(task.step);
                    return true;
                }
                return false;
            });
            addTasks(queue, tasks);
        }
        return totalTime;
    }

    private void addTasks(StepQueue queue, List<Task> tasks) {
        while (tasks.size() < workers && !queue.isEmpty()) {
            String step = queue.poll();
            tasks.add(new Task(step, baseDuration + step.charAt(0) - 'A' + 1));
        }
    }

    @AllArgsConstructor
    private static class Task {
        public final String step;
        public int timeRemaining;
    }

    //TODO: this is kind of ugly
    private class StepQueue extends PriorityQueue<String> {
        private final Counter<String> inDegree = new Counter<>();

        public StepQueue() {
            for (String a : edges.keySet())
                for (String b : edges.get(a))
                    inDegree.inc(b);

            for (String a : edges.keySet())
                if (!inDegree.containsKey(a))
                    add(a);
        }

        public void finished(String a) {
            for (String b : edges.getOrDefault(a, Collections.emptySet()))
                if (inDegree.dec(b) == 0)
                    add(b);
        }
    }
}
