package adventofcode.year2018.utils;

import java.util.ArrayList;
import java.util.List;

import adventofcode.Logger;
import lombok.AllArgsConstructor;

public class Program {
    public int ipReg = -1;
    public List<Instruction> instructions = new ArrayList<>();

    public Program(String input) {
        for (String line : input.split("\n")) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens[0].equals("#ip")) {
                ipReg = Integer.parseInt(tokens[1]);
            } else {
                Op op = Op.valueOf(tokens[0].toUpperCase());
                int a = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);
                int c = Integer.parseInt(tokens[3]);
                instructions.add(new Instruction(op, a, b, c));
            }
        }
    }

    public void debug(Logger logger) {
        for (int ip = 0; ip < instructions.size(); ip++) {
            Instruction i = instructions.get(ip);
            logger.debug(String.format("%2d:", ip), switch (i.op) {
                case ADDR -> String.format("%s = %s + %s", reg(i.c), reg(i.a), reg(i.b));
                case ADDI -> String.format("%s = %s + %d", reg(i.c), reg(i.a), i.b);
                case MULR -> String.format("%s = %s * %s", reg(i.c), reg(i.a), reg(i.b));
                case MULI -> String.format("%s = %s * %d", reg(i.c), reg(i.a), i.b);
                case BANR -> String.format("%s = %s & %s", reg(i.c), reg(i.a), reg(i.b));
                case BANI -> String.format("%s = %s & %d", reg(i.c), reg(i.a), i.b);
                case BORR -> String.format("%s = %s | %s", reg(i.c), reg(i.a), reg(i.b));
                case BORI -> String.format("%s = %s | %d", reg(i.c), reg(i.a), i.b);
                case SETR -> String.format("%s = %s", reg(i.c), reg(i.a));
                case SETI -> String.format("%s = %d", reg(i.c), i.a);
                case GTIR -> String.format("%s = (%d > %s) ? 1 : 0", reg(i.c), i.a, reg(i.b));
                case GTRI -> String.format("%s = (%s > %d) ? 1 : 0", reg(i.c), reg(i.a), i.b);
                case GTRR -> String.format("%s = (%s > %s) ? 1 : 0", reg(i.c), reg(i.a), reg(i.b));
                case EQIR -> String.format("%s = (%d == %s) ? 1 : 0", reg(i.c), i.a, reg(i.b));
                case EQRI -> String.format("%s = (%s == %d) ? 1 : 0", reg(i.c), reg(i.a), i.b);
                case EQRR -> String.format("%s = (%s == %s) ? 1 : 0", reg(i.c), reg(i.a), reg(i.b));
            });
        }
    }

    private String reg(int reg) {
        return reg == ipReg ? "ip" : Character.toString('a' + reg);
    }

    @AllArgsConstructor
    public static class Instruction {
        public final Op op;
        public final int a;
        public final int b;
        public final int c;

        public void apply(int[] mem) {
            op.apply(mem, a, b, c);
        }
    }
}
