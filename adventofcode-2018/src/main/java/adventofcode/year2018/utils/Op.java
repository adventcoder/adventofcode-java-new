package adventofcode.year2018.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Op {
    ADDR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] + mem[b]; }
    },
    ADDI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] + b; }
    },
    MULR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] * mem[b]; }
    },
    MULI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] * b; }
    },
    BANR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] & mem[b]; }
    },
    BANI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] & b; }
    },
    BORR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] | mem[b]; }
    },
    BORI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a] | b; }
    },
    SETR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = mem[a]; }
    },
    SETI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = a; }
    },
    GTIR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (a > mem[b]) ? 1 : 0; }
    },
    GTRI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (mem[a] > b) ? 1 : 0; }
    },
    GTRR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (mem[a] > mem[b]) ? 1 : 0; }
    },
    EQIR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (a == mem[b]) ? 1 : 0; }
    },
    EQRI {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (mem[a] == b) ? 1 : 0; }
    },
    EQRR {
        public void apply(int[] mem, int a, int b, int c) { mem[c] = (mem[a] == mem[b]) ? 1 : 0; }
    };

    public abstract void apply(int[] mem, int a, int b, int c);
}
