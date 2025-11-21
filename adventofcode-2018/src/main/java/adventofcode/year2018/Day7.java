package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
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
        edges = new HashMap<>();
        for (String line : input.split("\n")) {
            String[] tokens = line.split("\\s+");
            edges.computeIfAbsent(tokens[1], k -> new HashSet<>()).add(tokens[7]);
        }
    }

    @Override
    public String part1() {
        List<String> seq = new ArrayList<>();
        StepQueue queue = new StepQueue();
        while (!queue.isEmpty()) {
            String step = queue.poll();
            seq.add(step);
            queue.finished(step);
        }
        return String.join("", seq);
    }

    @Override
    public Integer part2() {
        StepQueue queue = new StepQueue();
        int totalTime = 0;

        List<Job> jobs = new ArrayList<>();
        addJobs(queue, jobs);

        while (!jobs.isEmpty()) {
            int timeStep = Fn.min(jobs, t -> t.timeRemaining);
            totalTime += timeStep;
            jobs.removeIf(job -> {
                job.timeRemaining -= timeStep;
                if (job.timeRemaining == 0) {
                    queue.finished(job.step);
                    return true;
                }
                return false;
            });
            addJobs(queue, jobs);
        }

        return totalTime;
    }

    private void addJobs(StepQueue queue, List<Job> jobs) {
        while (jobs.size() < workers && !queue.isEmpty()) {
            String step = queue.poll();
            jobs.add(new Job(step, baseDuration + step.charAt(0) - 'A' + 1));
        }
    }

    @AllArgsConstructor
    public static class Job {
        public final String step;
        public int timeRemaining;
    }

    public class StepQueue extends PriorityQueue<String> {
        private Map<String, Integer> inDegree = new HashMap<>();

        public StepQueue() {
            for (String in : edges.keySet())
                for (String out : edges.get(in))
                    inDegree.merge(out, 1, (a, b) -> a + b);
            for (String step : edges.keySet())
                if (!inDegree.containsKey(step))
                    add(step);
        }

        public void finished(String step) {
            for (String out : edges.getOrDefault(step, Collections.emptySet())) {
                if (inDegree.merge(out, -1, (a, b) -> a  + b) == 0)
                    add(out);
            }
        }
    }
}
