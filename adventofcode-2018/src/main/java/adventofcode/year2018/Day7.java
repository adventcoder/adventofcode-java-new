package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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

@Puzzle(day = 7, name = "The Sum of Its Parts")
public class Day7 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day7.class, args);
    }

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
        Scheduler scheduler = new Scheduler(1);
        scheduler.run();
        return String.join("", scheduler.finished);
    }

    @Override
    public Integer part2() {
        Scheduler scheduler = new Scheduler(5);
        scheduler.run();
        return scheduler.totalTime;
    }

    private class Scheduler {
        private final int workers;
        private final Counter<String> inDegree = new Counter<>();
        private final PriorityQueue<String> available = new PriorityQueue<>();
        private final List<Task> inProgress = new ArrayList<>();
        private final List<String> finished = new ArrayList<>();
        private int totalTime = 0;

        public Scheduler(int workers) {
            this.workers = workers;

            for (String a : edges.keySet())
                for (String b : edges.get(a))
                    inDegree.inc(b);

            for (String a : edges.keySet())
                if (!inDegree.containsKey(a))
                    available.add(a);
        }

        public void run() {
            addTasks();
            while (!inProgress.isEmpty()) {
                int timeStep = Fn.min(inProgress, t -> t.timeRemaining);
                totalTime += timeStep;
                for (Iterator<Task> it = inProgress.iterator(); it.hasNext(); ) {
                    Task task = it.next();
                    task.timeRemaining -= timeStep;
                    if (task.timeRemaining == 0) {
                        finish(task.step);
                        it.remove();
                    }
                }
                addTasks();
            }
        }

        public void addTasks() {
            while (inProgress.size() < workers && !available.isEmpty()) {
                String step = available.poll();
                inProgress.add(new Task(step, 60 + (step.charAt(0) - 'A' + 1)));
            }
        }

        public void finish(String step) {
            for (String b : edges.getOrDefault(step, Collections.emptySet()))
                if (inDegree.dec(b) == 0)
                    available.add(b);
            finished.add(step);
        }
    }

    @AllArgsConstructor
    private static class Task {
        public final String step;
        public int timeRemaining;
    }
}
