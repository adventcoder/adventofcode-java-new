package adventofcode.year2018;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.DefaultHashMap;
import lombok.AllArgsConstructor;

@Puzzle(day = 16, name = "Chronal Classification")
public class Day16 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day16.class, args);
    }

    private List<Sample> samples;
    private int[] code;

    @Override
    public void parse(String input) {
        String[] sections = input.split("\n\n\n\n");

        samples = new ArrayList<>();
        for (String chunk : sections[0].split("\n\n")) {
            String[] lines = chunk.split("\n");
            int[] before = Fn.parseInts(Fn.strip(lines[0], "Before: [", "]"), ",");
            int[] instr = Fn.parseInts(lines[1], "\\s+");
            int[] after = Fn.parseInts(Fn.strip(lines[2], "After:  [", "]"), ",");
            samples.add(new Sample(before, instr, after));
        }

        String[] lines = sections[1].split("\n");
        code = new int[lines.length * 4];
        for (int i = 0; i < lines.length; i++)
            System.arraycopy(Fn.parseInts(lines[i], "\\s+"), 0, code, i * 4, 4);
    }

    @Override
    public Integer part1() {
        return Fn.count(samples, sample -> sample.candidates().size() >= 3);
    }

    @Override
    public Integer part2() {
        Op[] opMapping = resolveOpMapping();
        int[] mem = new int[4];
        for (int i = 0; i < code.length; i += 4)
            opMapping[code[i]].apply(mem, code[i+1], code[i+2], code[i+3]);
        return mem[0];
    }

    private Op[] resolveOpMapping() {
        Map<Integer, EnumSet<Op.Name>> candidates = new DefaultHashMap<>(() -> EnumSet.allOf(Op.Name.class));
        for (Sample sample : samples) {
            int opCode = sample.code[0];
            candidates.get(opCode).retainAll(sample.candidates());
        }

        Op[] opMapping = new Op[ops.size()];

        Queue<Integer> queue = new ArrayDeque<>();
        for (var entry : candidates.entrySet())
            if (entry.getValue().size() == 1)
                queue.add(entry.getKey());

        while (!queue.isEmpty()) {
            int opCode = queue.poll();

            Op.Name opName = candidates.remove(opCode).iterator().next();
            opMapping[opCode] = ops.get(opName);

            for (var entry : candidates.entrySet())
                if (entry.getValue().remove(opName) && entry.getValue().size() == 1)
                    queue.add(entry.getKey());
        }

        return opMapping;
    }

    @AllArgsConstructor
    public static class Sample {
        public int[] before;
        public int[] code;
        public int[] after;

        public EnumSet<Op.Name> candidates() {
            EnumSet<Op.Name> result = EnumSet.noneOf(Op.Name.class);
            for (var entry : ops.entrySet()) {
                int[] mem = before.clone();
                entry.getValue().apply(mem, code[1], code[2], code[3]);
                if (Arrays.equals(mem, after))
                    result.add(entry.getKey());
            }
            return result;
        }
    }

    public static EnumMap<Op.Name, Op> ops = new EnumMap<>(Op.Name.class);

    static {
        ops.put(Op.Name.ADDR, (mem, a, b, c) -> mem[c] = mem[a] + mem[b]);
        ops.put(Op.Name.ADDI, (mem, a, b, c) -> mem[c] = mem[a] + b);
        ops.put(Op.Name.MULR, (mem, a, b, c) -> mem[c] = mem[a] * mem[b]);
        ops.put(Op.Name.MULI, (mem, a, b, c) -> mem[c] = mem[a] * b);
        ops.put(Op.Name.BANR, (mem, a, b, c) -> mem[c] = mem[a] & mem[b]);
        ops.put(Op.Name.BANI, (mem, a, b, c) -> mem[c] = mem[a] & b);
        ops.put(Op.Name.BORR, (mem, a, b, c) -> mem[c] = mem[a] | mem[b]);
        ops.put(Op.Name.BORI, (mem, a, b, c) -> mem[c] = mem[a] | b);
        ops.put(Op.Name.SETR, (mem, a, b, c) -> mem[c] = mem[a]);
        ops.put(Op.Name.SETI, (mem, a, b, c) -> mem[c] = a);
        ops.put(Op.Name.GTIR, (mem, a, b, c) -> mem[c] = a > mem[b] ? 1 : 0);
        ops.put(Op.Name.GTRI, (mem, a, b, c) -> mem[c] = mem[a] > b ? 1 : 0);
        ops.put(Op.Name.GTRR, (mem, a, b, c) -> mem[c] = mem[a] > mem[b] ? 1 : 0);
        ops.put(Op.Name.EQIR, (mem, a, b, c) -> mem[c] = a == mem[b] ? 1 : 0);
        ops.put(Op.Name.EQRI, (mem, a, b, c) -> mem[c] = mem[a] == b ? 1 : 0);
        ops.put(Op.Name.EQRR, (mem, a, b, c) -> mem[c] = mem[a] == mem[b] ? 1 : 0);
    }

    @FunctionalInterface
    private static interface Op {
        enum Name {
            ADDR, ADDI,
            MULR, MULI,
            BANR, BANI,
            BORR, BORI,
            SETR, SETI,
            GTIR, GTRI, GTRR,
            EQIR, EQRI, EQRR
        }

        void apply(int[] mem, int a, int b, int c);
    }
}
