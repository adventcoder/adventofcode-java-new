package adventofcode.year2018;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.year2018.utils.Program;

@Puzzle(day = 19, name = "Go With The Flow")
public class Day19 extends AbstractDay {
    public static void main(String[] args) throws Exception{
        main(Day19.class, args);
    }

    private Program program;

    @Override
    public void parse(String input) {
        program = Program.parse(input);
        program.debug(this);
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
        for (int ip = 0; ip < program.instructions.size(); ip++) {
            if (ip == 1) {
                //  1: c = 1
                //  2: e = 1
                //  3: d = c * e
                //  4: d = (d == f) ? 1 : 0
                //  5: ip = d + ip
                //  6: ip = ip + 1
                //  7: a = c + a
                //  8: e = e + 1
                //  9: d = (e > f) ? 1 : 0
                // 10: ip = ip + d
                // 11: ip = 2
                // 12: c = c + 1
                // 13: d = (c > f) ? 1 : 0
                // 14: ip = d + ip
                // 15: ip = 1
                mem[0] += divisorSum(mem[5]);
                ip = 15;
            } else {
                mem[program.ipReg] = ip;
                program.instructions.get(ip).apply(mem);
                ip = mem[program.ipReg];
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
