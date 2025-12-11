package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.PriorityQueue;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.collect.Counter;
import adventofcode.utils.collect.DefaultHashMap;

@Puzzle(day = 7, name = "The Sum of Its Parts")
public class Day7 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day7.class, args);
    }

    private Map<Character, Set<Character>> graph;

    @Override
    public void parse(String input) {
        graph = new DefaultHashMap<>(HashSet::new);
        for (String line : input.split("\n")) {
            String[] tokens = line.split("\\s+");
            graph.get(tokens[1].charAt(0)).add(tokens[7].charAt(0));
        }
    }

    @Override
    public String part1() {
        Scheduler scheduler = new Scheduler(1);
        scheduler.run();
        return scheduler.finished.toString();
    }

    @Override
    public Integer part2() {
        Scheduler scheduler = new Scheduler(5);
        scheduler.run();
        return scheduler.totalTime;
    }

    //TODO: this is still ugly
    private class Scheduler {
        private final List<Worker> workers = new ArrayList<>();
        private final Counter<Character> inDegree = new Counter<>();
        private final PriorityQueue<Character> ready = new PriorityQueue<>();
        private final StringBuilder finished = new StringBuilder();
        private int totalTime = 0;

        public Scheduler(int numWorkers) {
            while (workers.size() < numWorkers)
                workers.add(new Worker());
            for (Character step : graph.keySet())
                for (Character nextStep : graph.get(step))
                    inDegree.inc(nextStep);
            for (Character step : graph.keySet())
                if (!inDegree.containsKey(step))
                    ready.add(step);
        }

        public void run() {
            assignWork();
            while (work())
                assignWork();
        }

        private void assignWork() {
            for (Worker worker : workers) {
                if (ready.isEmpty()) break;
                if (worker.step != null) continue;
                worker.step = ready.poll();
                worker.timeRemaining = 60 + (worker.step - 'A' + 1);
            }
        }

        private boolean work() {
            OptionalInt timeStep = leastTimeStep();
            if (timeStep.isEmpty()) return false;
            totalTime += timeStep.getAsInt();
            for (Worker worker : workers) {
                if (worker.step == null) continue;
                worker.timeRemaining -= timeStep.getAsInt();
                if (worker.timeRemaining == 0) {
                    finish(worker.step);
                    worker.step = null;
                }
            }
            return true;
        }

        private OptionalInt leastTimeStep() {
            return workers.stream()
                .filter(worker -> worker.step != null)
                .mapToInt(worker -> worker.timeRemaining)
                .min();
        }

        private void finish(Character step) {
            for (Character nextStep : graph.getOrDefault(step, Collections.emptySet()))
                if (inDegree.dec(nextStep) == 0)
                    ready.add(nextStep);
            finished.append(step.charValue());
        }
    }

    public static class Worker {
        public Character step;
        public int timeRemaining;
    }
}
