package adventofcode.year2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import lombok.AllArgsConstructor;

@Puzzle(day = 8, name = "I Heard You Like Registers")
public class Day8 extends AbstractDay {
    private List<Instr> instrs = new ArrayList<>();
    private int runningMax = 0;

    @Override
    public void parse(String input) {
        instrs = Fn.parseVals(input, "\n", Instr::parse).toList();
    }

    @Override
    public Integer part1() {
        Map<String, Integer> mem = new HashMap<>();
        for (Instr instr : instrs) {
            if (instr.test(mem)) {
                instr.apply(mem);
                runningMax = Math.max(runningMax, mem.get(instr.reg));
            }
        }
        return Fn.max(mem.keySet(), mem::get);
    }

    @Override
    public Integer part2() {
        return runningMax;
    }

    @AllArgsConstructor
    public static class Instr {
        private static final Map<String, IntBinaryOperator> ops = Map.of(
            "inc", (x, y) -> x + y,
            "dec", (x, y) -> x - y
        );

        private static final Map<String, TestOp> testOps = Map.of(
            "==", (x, y) -> x == y,
            "!=", (x, y) -> x != y,
            "<=", (x, y) -> x <= y,
            ">=", (x, y) -> x >= y,
            "<", (x, y) -> x < y,
            ">", (x, y) -> x > y
        );

        public final String reg;
        public final IntBinaryOperator op;
        private final int amount;
        private final String testReg;
        public final TestOp testOp;
        private final int testAmount;

        public static Instr parse(String s) {
            String[] tokens = s.trim().split("\\s+");
            return new Instr(
                tokens[0], ops.get(tokens[1]), Integer.parseInt(tokens[2]),
                tokens[4], testOps.get(tokens[5]), Integer.parseInt(tokens[6])
            );
        }

        public void apply(Map<String, Integer> mem) {
            mem.put(reg, op.applyAsInt(mem.getOrDefault(reg, 0), amount));
        }

        public boolean test(Map<String, Integer> mem) {
            return testOp.test(mem.getOrDefault(testReg, 0), testAmount);
        }
    }

    @FunctionalInterface
    public interface TestOp {
        boolean test(int x, int y);
    }
}
