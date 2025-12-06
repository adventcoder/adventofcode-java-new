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
import adventofcode.year2018.utils.Program.Instruction;

@Puzzle(day = 21, name = "Chronal Conversion")
public class Day21 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day21.class, args);
    }

    private Program program;

    @Override
    public void parse(String input) {
        program = Program.parse(input);
        program.debug(this);
    }

    @Override
    public Integer part1() {
        return valueIterator().next();
    }

    @Override
    public Integer part2() {
        Set<Integer> seen = new HashSet<>();
        Integer lastVal = null;
        for (Iterator<Integer> it = valueIterator(); it.hasNext(); ) {
            Integer val = it.next();
            if (!seen.add(val)) break;
            lastVal = val;
        }
        return lastVal;
    }

    public Iterator<Integer> valueIterator() {
        return Iterators.generate(new Supplier<>() {
            private int[] mem = new int[6];
            private int ip = 0;

            @Override
            public Integer get() {
                Integer val = null;
                while (val == null && ip < program.instructions.size()) {
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
                        int src = program.instructions.get(20).b;
                        int dest = program.instructions.get(26).c;
                        mem[dest] = mem[src] / 256;
                        ip = 26;
                    } else {
                        Instruction i = program.instructions.get(ip);
                        if (i.op == Op.EQRR && (i.a == 0 || i.b == 0))
                            val = mem[i.a == 0 ? i.b : i.a];
                        mem[program.ipReg] = ip;
                        i.apply(mem);
                        ip = mem[program.ipReg];
                    }
                    ip++;
                }
                return val;
            }
        }, Objects::nonNull);
    }
}
