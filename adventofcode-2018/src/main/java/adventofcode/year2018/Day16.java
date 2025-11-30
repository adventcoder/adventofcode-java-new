package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.DefaultHashMap;
import adventofcode.year2018.utils.Op;
import lombok.AllArgsConstructor;

@Puzzle(day = 16, name = "Chronal Classification")
public class Day16 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day16.class, args);
    }

    private List<Sample> samples;
    private List<int[]> instructions;

    @Override
    public void parse(String input) {
        String[] sections = input.split("\n\n\n\n");

        samples = new ArrayList<>();
        for (String chunk : sections[0].split("\n\n")) {
            String[] lines = chunk.split("\n");
            int[] before = Fn.parseInts(Fn.strip(lines[0], "Before: [", "]"), ",");
            int[] instruction = Fn.parseInts(lines[1], "\\s+");
            int[] after = Fn.parseInts(Fn.strip(lines[2], "After:  [", "]"), ",");
            samples.add(new Sample(before, instruction, after));
        }

        instructions = new ArrayList<>();
        for (String line : sections[1].split("\n"))
            instructions.add(Fn.parseInts(line, "\\s+"));
    }

    @Override
    public Integer part1() {
        return Fn.count(samples, sample -> sample.candidates().size() >= 3);
    }

    @Override
    public Integer part2() {
        Op[] ops = resolveOps();
        int[] mem = new int[4];
        for (int[] i : instructions)
            ops[i[0]].apply(mem, i[1], i[2], i[3]);
        return mem[0];
    }

    private Op[] resolveOps() {
        Map<Integer, EnumSet<Op>> candidates = new DefaultHashMap<>(() -> EnumSet.allOf(Op.class));
        for (Sample sample : samples) {
            int opCode = sample.instruction[0];
            candidates.get(opCode).retainAll(sample.candidates());
        }

        Op[] ops = new Op[Op.values().length];
        EnumSet<Op> resolved = EnumSet.noneOf(Op.class);
        while (resolved.size() < candidates.size()) {
            for (var entry : candidates.entrySet()) {
                entry.getValue().removeAll(resolved);
                if (entry.getValue().size() == 1) {
                    Op op = entry.getValue().iterator().next();
                    ops[entry.getKey()] = op;
                    resolved.add(op);
                }
            }
        }

        return ops;
    }

    @AllArgsConstructor
    public static class Sample {
        public int[] before;
        public int[] instruction;
        public int[] after;

        public EnumSet<Op> candidates() {
            EnumSet<Op> result = EnumSet.noneOf(Op.class);
            for (Op op : Op.values()) {
                int[] mem = before.clone();
                op.apply(mem, instruction[1], instruction[2], instruction[3]);
                if (Arrays.equals(mem, after))
                    result.add(op);
            }
            return result;
        }
    }
}
