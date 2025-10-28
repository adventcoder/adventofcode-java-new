package adventofcode.year2017;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import lombok.RequiredArgsConstructor;

@Puzzle(day = 18, name = "Duet")
public class Day18 extends AbstractDay {
    private List<Object[]> ops;

    @Override
    public void parse(String input) {
        ops = new ArrayList<>();
        for (String line : input.split("\n")) {
            String[] tokens = line.trim().split("\\s+");
            Object[] op = new Object[tokens.length];
            op[0] = OpName.valueOf(tokens[0].toUpperCase());
            for (int i = 1; i < tokens.length; i++) {
                op[i] = tokens[i].matches("[a-z]+") ? tokens[i] : BigInteger.valueOf(Long.parseLong(tokens[i]));
            }
            ops.add(op);
        }
    }

    @Override
    public BigInteger part1() {
        FaultyVM vm = new FaultyVM(ops);
        while (vm.running() && vm.lastRecovered == null)
            vm.step();
        return vm.lastRecovered;
    }

    @Override
    public Integer part2() {
        Queue<BigInteger> inbox0 = new ArrayDeque<>();
        Queue<BigInteger> inbox1 = new ArrayDeque<>();
        DuetVM vm0 = new DuetVM(ops, inbox0, inbox1, 0);
        DuetVM vm1 = new DuetVM(ops, inbox1, inbox0, 1);
        while (true) {
            if (vm0.running() && !vm0.waiting()) {
                vm0.step();
            } else if (vm1.running() && !vm1.waiting()) {
                vm1.step();
            } else {
                // can't make progress
                break;
            }
        }
        return vm1.sendCount;
    }

    public static enum OpName { SET, ADD, MUL, MOD, JGZ, SND, RCV }

    @RequiredArgsConstructor
    private static abstract class AbstractVM {
        private static final Map<OpName, Consumer<AbstractVM>> opMap = Map.of(OpName.SET, AbstractVM::set, OpName.ADD, AbstractVM::add, OpName.MUL, AbstractVM::mul, OpName.MOD, AbstractVM::mod, OpName.JGZ, AbstractVM::jgz, OpName.SND, AbstractVM::snd, OpName.RCV, AbstractVM::rcv);

        private final List<Object[]> ops;
        protected final Map<String, BigInteger> mem = new HashMap<>();
        protected int pc = 0;

        public boolean running() {
            return pc >= 0 && pc < ops.size();
        }

        protected OpName opName() {
            return (OpName) ops.get(pc)[0];
        }

        protected String reg(int i) {
            return (String) ops.get(pc)[i];
        }

        protected BigInteger val(int i) {
            return ops.get(pc)[i] instanceof BigInteger ? (BigInteger) ops.get(pc)[i] : mem.getOrDefault(reg(i), BigInteger.ZERO);
        }

        public void step() {
            opMap.get(opName()).accept(this);
            pc++;
        }

        public void set() {
            mem.put(reg(1), val(2));
        }

        public void add() {
            mem.put(reg(1), val(1).add(val(2)));
        }

        public void mul() {
            mem.put(reg(1), val(1).multiply(val(2)));
        }

        public void mod() {
            mem.put(reg(1), val(1).mod(val(2)));
        }

        public void jgz() {
            if (val(1).signum() > 0)
                pc += val(2).intValueExact() - 1;
        }

        public abstract void snd();
        public abstract void rcv();
    }

    public static class FaultyVM extends AbstractVM {
        private BigInteger lastPlayed;
        public BigInteger lastRecovered;

        public FaultyVM(List<Object[]> ops) {
            super(ops);
        }

        @Override
        public void snd() {
            lastPlayed = val(1);
        }

        @Override
        public void rcv() {
            if (val(1).signum() != 0)
                lastRecovered = lastPlayed;
        }
    }

    public static class DuetVM extends AbstractVM {        
        private final Queue<BigInteger> inbox;
        private final Queue<BigInteger> outbox;
        public int sendCount;

        public DuetVM(List<Object[]> ops, Queue<BigInteger> inbox, Queue<BigInteger> outbox, int pid) {
            super(ops);
            this.inbox = inbox;
            this.outbox = outbox;
            mem.put("p", BigInteger.valueOf(pid));
        }

        public boolean waiting() {
            return opName() == OpName.RCV && inbox.isEmpty();
        }

        @Override
        public void snd() {
            outbox.add(val(1));
            sendCount++;
        }

        @Override
        public void rcv() {
            mem.put(reg(1), inbox.remove());
        }
    }
}
