package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.array.IntArrays;
import adventofcode.utils.iter.Enumerable;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

    @Override
    protected String getInput() {
        return """
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
""";
    }

    private List<Machine> machines;

    @Override
    public void parse(String input) {
        machines = Fn.parseVals(input, "\n", Machine::parse).toList();
    }

    @Override
    public Integer part1() {
        int total = 0;
        for (Machine m : machines) {
            AffineSpaceGF2 sol = solveGF2(m.buttons, m.diagram);
            if (sol == null) continue;
            total += sol.minimizeBitCount();
        }
        return total;
    }

    @Override
    public Integer part2() {
        int total = 0;
        for (Machine machine : machines) {
            int n = minimizeSum(machine.buttons, machine.joltages);
            System.out.println("answer: " + n);
            System.out.println();
            total += n;
        }
        return total;
    }

    public record Machine(int[] diagram, int[][] buttons, int[] joltages) {
        public static Machine parse(String s) {
            String[] tokens = s.trim().split("\\s+");
            int[] diagram = parseDiagram(tokens[0]);
            int[][] buttons = Arrays.stream(tokens, 1, tokens.length - 1)
                .map(tok -> Fn.parseInts(Fn.strip(tok, "(", ")"), ","))
                .toArray(int[][]::new);
            int[] joltages = Fn.parseInts(Fn.strip(tokens[tokens.length - 1], "{", "}"), ",");
            return new Machine(diagram, buttons, joltages);
        }

        private static int[] parseDiagram(String s) {
            return Fn.strip(s, "[", "]").chars()
                .map(c -> c == '#' ? 1 : 0)
                .toArray();
        }
    }

    public int minimizeSum(int[][] buttons, int[] joltages) {
        // Build constraints
        List<Inequality> ineqs = new ArrayList<>();
        for (int x = 0; x < buttons.length; x++) {
            int[] lhs = new int[buttons.length];
            lhs[x] = 1;
            ineqs.add(new Inequality(lhs, 0));
        }
        int[][] lower = new int[joltages.length][buttons.length];
        int[][] upper = new int[joltages.length][buttons.length];
        for (int x = 0; x < buttons.length; x++) {
            for (int y : buttons[x]) {
                lower[y][x] = 1;
                upper[y][x] = -1;
            }
        }
        for (int y = 0; y < joltages.length; y++) {
            ineqs.add(new Inequality(lower[y], joltages[y]));
            ineqs.add(new Inequality(upper[y], -joltages[y]));
        }

        Set<Integer> remaining = IntStream.range(0, buttons.length).boxed().collect(Collectors.toSet());
        return solutions(ineqs, buttons.length, remaining)
            .mapToInt(sol -> IntArrays.sum(sol))
            .min();
    }

    private Enumerable<int[]> solutions(List<Inequality> ineqs, int n, Set<Integer> remaining) {
        if (remaining.isEmpty())
            return action -> action.accept(new int[n]);

        int i = Fn.argMin(remaining, j -> countPairs(ineqs, j));
        Set<Integer> newRemaining = new HashSet<>(remaining);
        newRemaining.remove(i);

        List<Inequality> lower = new ArrayList<>();
        List<Inequality> upper = new ArrayList<>();
        List<Inequality> free = new ArrayList<>();
        for (Inequality ineq : ineqs) {
            if (ineq.left[i] > 0) {
                lower.add(ineq);
            } else if (ineq.left[i] < 0) {
                upper.add(ineq);
            } else {
                free.add(ineq);
            }
        }

        // Eliminate variable i, and add derived constraints
        for (Inequality a : lower) {
            for (Inequality b : upper) {
                // a1 x + b1 y + c1 z >= d1, c1>0
                // a2 x + b2 y + c2 z >= d2, c2<0
                //
                // -c1 c2 z >= -c2 d1 + c2 a1 x + c2 b1 y
                // -c1 c2 z <= -c1 d2 + c1 a2 x + c1 b2 y
                //
                // (c1 a2-c2 a1) x + (c1 b2-c2 b1) y >= (c1 d2-c2 d1)
                //
                int d = IntMath.gcd(a.left[i], b.left[i]);
                int A = a.left[i] / d;
                int B = b.left[i] / d;
                int[] left = new int[n];
                for (int j : newRemaining)
                    left[j] = A*b.left[j] - B*a.left[j];
                int right = A*b.right - B*a.right;
                if (IntStream.of(left).allMatch(val -> val == 0)) {
                    // Check consistency
                    if (right > 0)
                        return Enumerable.empty();
                } else {
                    boolean found = false;
                    for (int k = 0; k < free.size(); k++) {
                        Inequality existing = free.get(k);
                        if (Arrays.equals(existing.left, left)) {
                            if (right > existing.right)
                                free.set(k, new Inequality(left, right));
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        free.add(new Inequality(left, right));
                }
            }
        }

        // Recursive solve
        return action -> {
            // Back substitute the solution for variables < i.
            solutions(free, n, newRemaining).forEach(sol -> {
                int lb = Integer.MIN_VALUE;
                for (Inequality ineq : lower) {
                    int total = ineq.right;
                    for (int j : newRemaining)
                        total -= ineq.left[j]*sol[j];
                    lb = Math.max(lb, -Math.floorDiv(-total, ineq.left[i]));
                }

                int ub = Integer.MAX_VALUE;
                for (Inequality ineq : upper) {
                    int total = -ineq.right;
                    for (int j : newRemaining)
                        total += ineq.left[j]*sol[j];
                    ub = Math.min(ub, Math.floorDiv(total, -ineq.left[i]));
                }

                for (int x = lb; x <= ub; x++) {
                    sol[i] = x;
                    action.accept(sol);
                }
            });
        };
    }

    private int countPairs(List<Inequality> ineqs, int i) {
        int lower = 0;
        int upper = 0;
        for (Inequality ineq : ineqs) {
            if (ineq.left[i] > 0) {
                lower++;
            } else if (ineq.left[i] < 0) {
                upper++;
            }
        }
        return lower * upper;
    }

    public record Inequality(int[] left, int right) {
    }

    public AffineSpaceGF2 solveGF2(int[][] buttons, int[] diagram) {
        // Build augmented matrix, rows are bit vectors
        int[] rhs = diagram.clone();
        int[] lhs = new int[diagram.length];
        for (int x = 0; x < buttons.length; x++)
            for (int y : buttons[x])
                lhs[y] |= (1 << x);

        // Row reduce
        int[] pivotCols = new int[Math.min(diagram.length, buttons.length)];
        int[] freeCols = new int[buttons.length];
        int rank = 0;
        int nullity = 0;
        for (int x = 0; x < buttons.length; x++) {
            boolean foundPivot = false;
            for (int y = rank; y < diagram.length; y++) {
                if ((lhs[y] & (1 << x)) != 0) {
                    IntArrays.swap(lhs, rank, y);
                    IntArrays.swap(rhs, rank, y);
                    foundPivot = true;
                    break;
                }
            }
            if (!foundPivot) {
                freeCols[nullity++] = x;
            } else {
                for (int y = 0; y < diagram.length; y++) {
                    if (y == rank) continue;
                    if ((lhs[y] & (1 << x)) != 0) {
                        lhs[y] ^= lhs[rank];
                        rhs[y] ^= rhs[rank];
                    }
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < diagram.length; y++)
            if (lhs[y] == 0 && rhs[y] != 0)
                return null;

        // Build solution space
        int particular = 0;
        int[] nullspace = new int[nullity];
        for (int y = 0; y < rank; y++) {
            particular |= rhs[y] << pivotCols[y];
            for (int nx = 0; nx < nullity; nx++) {
                if ((lhs[y] & (1 << freeCols[nx])) != 0)
                    nullspace[nx] |= 1 << pivotCols[y];
            }
        }
        for (int nx = 0; nx < nullity; nx++)
            nullspace[nx] |= (1 << freeCols[nx]);

        return new AffineSpaceGF2(particular, nullspace);
    }

    public record AffineSpaceGF2(int origin, int[] basis) {
        public int minimizeBitCount() {
            int leastCount = Integer.MAX_VALUE;
            int n = 1 << basis.length;
            for (int i = 0; i < n; i++)
                leastCount = Math.min(leastCount, Integer.bitCount(get(i)));
            return leastCount;
        }

        public int get(int x) {
            int result = origin;
            for (int i = 0; i < basis.length; i++) {
                if ((x & (1 << i)) != 0)
                    result ^= basis[i];
            }
            return result;
        }
    }
}
