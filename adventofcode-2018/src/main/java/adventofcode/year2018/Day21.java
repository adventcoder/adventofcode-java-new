package adventofcode.year2018;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.iter.Iterators;
import adventofcode.year2018.utils.Op;
import adventofcode.year2018.utils.Program;

@Puzzle(day = 21, name = "Chronal Conversion")
public class Day21 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day21.class, args);
    }

    private ActivationSystem program;

    @Override
    public void parse(String input) {
        program = new ActivationSystem(input);
        program.debug(this);
    }

    @Override
    public Integer part1() {
        return program.iterator().next();
    }

    @Override
    public Integer part2() {
        Set<Integer> seen = new HashSet<>();
        Integer lastVal = null;
        for (Integer val : program) {
            if (!seen.add(val)) break;
            lastVal = val;
        }
        return lastVal;
    }

    private static class ActivationSystem extends Program implements Iterable<Integer> {
        public ActivationSystem(String input) {
            super(input);
        }

        public Iterator<Integer> iterator() {
            Supplier<Integer> next = new Supplier<>() {
                int[] mem = new int[6];
                int ip = 0;

                @Override
                public Integer get() {
                    Integer val = null;
                    while (val == null && ip < instructions.size()) {
                        if (ip == 17) {
                            // 17: f = 0
                            // 18: c = f + 1
                            // 19: c = c * 256
                            // 20: c = (c > d) ? 1 : 0
                            // 21: ip = c + ip
                            // 22: ip = ip + 1
                            // 23: ip = 25
                            // 24: f = f + 1
                            // 25: ip = 17
                            // 26: d = f
                            int src = instructions.get(20).b;
                            int dest = instructions.get(26).c;
                            mem[dest] = mem[src] / 256;
                            ip = 26;
                        } else {
                            Instruction i = instructions.get(ip);
                            if (i.op == Op.EQRR && (i.a == 0 || i.b == 0))
                                val = mem[i.a == 0 ? i.b : i.a];
                            mem[ipReg] = ip;
                            i.apply(mem);
                            ip = mem[ipReg];
                        }
                        ip++;
                    }
                    return val;
                }
            };
            return Iterators.generate(next, Objects::nonNull);
        }
    }
}
