package adventofcode.year2018;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.year2018.Day16.Op;

@Puzzle(day = 19, name = "Go With The Flow")
public class Day19 extends AbstractDay {
    public static void main(String[] args) throws Exception{
        main(Day19.class, args);
    }

    private static Op[] ops = Stream.of(Op.Name.values())
        .map(Day16.ops::get)
        .toArray(Op[]::new);

    private List<int[]> program;
    private int ipReg;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");
        ipReg = Integer.parseInt(lines[0].split("\\s+")[1]);
        program = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] tokens = lines[i].trim().split("\\s+");
            int opCode = Op.Name.valueOf(tokens[0].toUpperCase()).ordinal();
            int a = Integer.parseInt(tokens[1]);
            int b = Integer.parseInt(tokens[2]);
            int c = Integer.parseInt(tokens[3]);
            program.add(new int[] { opCode, a, b, c });
        }
    }

    @Override
    public Integer part1() {
        int[] mem = new int[6];
        run(mem);
        return mem[0];
    }

    @Override
    public Integer part2() {
        int[] mem = new int[6];
        mem[0] = 1;
        run(mem);
        return mem[0];
    }

    private void run(int[] mem) {
        for (int ip = 0; ip < program.size(); ip++) {
            if (ip == 1) {
                mem[0] += divisorSum(mem[5]);
                ip = 15;
            } else {
                int[] instr = program.get(ip);
                mem[ipReg] = ip;
                ops[instr[0]].apply(mem, instr[1], instr[2], instr[3]);
                ip = mem[ipReg];
            }
        }
    }

    private int divisorSum(int n) {
        int total = 1;
        int p = 2;
        while (p*p <= n) {
            int a = 1;
            while (n % p == 0) {
                a *= p;
                n /= p;
            }
            if (a > 1)
                total *= (p*a - 1) / (p - 1);
            p++;
        }
        if (n > 1)
            total *= (n + 1);
        return total;
    }
}
