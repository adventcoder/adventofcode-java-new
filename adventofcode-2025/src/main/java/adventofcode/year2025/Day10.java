package adventofcode.year2025;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.array.IntArrays;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

    @Override
    protected String getInput() {
        return "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
            "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
            "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}";
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
            int leastButtons = Integer.MAX_VALUE;
            var sol = solveGF2(m.buttons, m.diagram);
            int n = 1 << sol.right().length;
            for (int x = 0; x < n; x++) {
                int mask = getSol(sol, x);
                leastButtons = Math.min(leastButtons, Integer.bitCount(mask));
            }
            total += leastButtons;
        }
        return total;
    }

    @Override
    public Integer part2() {
        for (Machine m : machines) {
            var sol = solve(m.buttons, m.joltages, 1e-9);
            System.out.println("particular: " + Arrays.toString(sol.left()));
            System.out.println("nullspace: " + Arrays.deepToString(sol.right()));
        }
        return null;
    }

    public int getSol(IntObjectPair<int[]> sol, int x) {
        int result = sol.leftInt();
        for (int i = 0; i < sol.right().length; i++) {
            if ((x & (1 << i)) != 0)
                result ^= sol.right()[i];
        }
        return result;
    }

    private IntObjectPair<int[]> solveGF2(int[][] buttons, int[] diagram) {
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
                throw new IllegalArgumentException("no solution");

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

        return IntObjectPair.of(particular, nullspace);
    }

       private Pair<double[], double[][]> solve(int[][] buttons, int[] joltages, double epsilon) {
        // Build augmented matrix, rows are bit vectors
        double[][] lhs = new double[joltages.length][buttons.length];
        for (int x = 0; x < buttons.length; x++)
            for (int y : buttons[x])
                lhs[y][x] = 1.0;
        double[] rhs = new double[joltages.length];
        for (int y = 0; y < joltages.length; y++)
            rhs[y] = joltages[y];

        // Row reduce
        int[] pivotCols = new int[Math.min(joltages.length, buttons.length)];
        int[] freeCols = new int[buttons.length];
        int rank = 0;
        int nullity = 0;
        for (int x = 0; x < buttons.length; x++) {
            boolean foundPivot = false;
            for (int y = rank; y < joltages.length; y++) {
                if (Math.abs(lhs[y][x]) > epsilon) {
                    double[] lhsTemp = lhs[rank]; lhs[rank] = lhs[y]; lhs[y] = lhsTemp;
                    double rhsTemp = rhs[rank]; rhs[rank] = rhs[y]; rhs[y] = rhsTemp;
                    foundPivot = true;
                    break;
                }
            }
            if (!foundPivot) {
                freeCols[nullity++] = x;
            } else {
                double factor = lhs[rank][x];
                for (int i = x; i < buttons.length; i++)
                    lhs[rank][i] /= factor;
                rhs[rank] /= factor;

                for (int y = 0; y < joltages.length; y++) {
                    if (y == rank) continue;
                    if (Math.abs(lhs[y][x]) > epsilon) {
                        for (int i = x; i < buttons.length; i++)
                            lhs[y][i] -= lhs[rank][i];
                        rhs[y] -= rhs[rank];
                    }
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < joltages.length; y++)
            if (DoubleStream.of(lhs[y]).allMatch(x -> Math.abs(x) <= epsilon) && Math.abs(rhs[y]) > epsilon)
                throw new IllegalArgumentException("no solution");

        // Build solution space
        double[] particular = new double[buttons.length];
        double[][] nullspace = new double[nullity][buttons.length];
        for (int y = 0; y < rank; y++) {
            particular[pivotCols[y]] = rhs[y];
            for (int nx = 0; nx < nullity; nx++)
                nullspace[nx][pivotCols[y]] = Math.abs(lhs[y][freeCols[nx]]);
        }
        for (int nx = 0; nx < nullity; nx++)
            nullspace[nx][freeCols[nx]] = 1;

        return Pair.of(particular, nullspace);
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
}
