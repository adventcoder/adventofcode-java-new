package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.array.IntArrays;
import adventofcode.utils.array.ObjectArrays;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

    @Override
    protected String getInput() {
        List<String> lines = new ArrayList<>();
        // lines.add("[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}");
        // lines.add("[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}");
        // lines.add("[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}");
        lines.add("[....] (1,2) (2,3) (1,2,3) (0,2) (0,1) (0,1,3) {31,43,187,161}");
        return String.join("\n", lines);
    }

    private List<Machine> machines;

    @Override
    public void parse(String input) {
        machines = Fn.parseVals(input, "\n", Machine::parse).toList();
    }

    @Override
    public Integer part1() {
        int total = 0;
        for (Machine m : machines)
            total += findMinimumCount(m.buttons, m.diagram);
        return total;
    }

    @Override
    public Integer part2() {
        int total = 0;
        for (Machine m : machines) {
            double[] sol = solveLP(m.buttons, m.joltages);
            if (DoubleStream.of(sol).allMatch(x -> Math.floor(x) == Math.ceil(x))) {
                total += DoubleStream.of(sol).mapToInt(x -> (int) x).sum();
            } else {
                System.out.println("buttons: " + Arrays.deepToString(m.buttons));
                System.out.println("joltages: " + Arrays.toString(m.joltages));
                System.out.println("solLP: " + Arrays.toString(sol));
                System.out.println("sumLP: " + DoubleStream.of(sol).sum());
                System.out.println();
            }
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

    // private int solveILP(int[][] buttons, int[] joltages) {
    //     int leastIntegerSum = Integer.MAX_VALUE;

    //     Deque<List<Constraint>> queue = new ArrayDeque<>();
    //     queue.add(List.of());
    //     while (!queue.isEmpty()) {
    //         List<Constraint> cons = queue.pop();

    //         double[] sol = solveLP(buttons, joltages, cons);
    //         if (sol == null) continue; // infeasible

    //         double sum = DoubleStream.of(sol).sum();
    //         if (sum >= leastIntegerSum) continue; // prune

    //         int k = chooseFractionalVariable(sol);
    //         if (k < 0) {
    //             leastIntegerSum = (int) sum;
    //         } else {
    //             List<int[]> newCons = new ArrayList<>(cons);
    //             newCons.add(Constraint.upperBound(k, (int) Math.floor(sol[k])));
    //             queue.add(newCons);
    //             cons.add(Constraint.lowerBound(k, (int) Math.ceil(sol[k])));
    //             queue.add(cons);
    //         }
    //     }

    //     return leastIntegerSum;
    // }

    private double[] solveLP(int[][] buttons, int[] joltages) {
        // Build augmented matrix
        int[][] M = new int[joltages.length + 2][buttons.length + 1];
        for (int x = 0; x < buttons.length; x++) {
            for (int y : buttons[x])
                M[y][x] = 1;
            M[joltages.length][x] = -1; // Phase II objective
            M[joltages.length + 1][x] = buttons[x].length; // Phase I objective
        }
        for (int y = 0; y < joltages.length; y++) {
            M[y][buttons.length] = joltages[y];
        }
        M[joltages.length + 1][buttons.length] = IntArrays.sum(joltages);

        // Phase I: Find BFS
        int[] pivotCols = new int[Math.min(joltages.length, buttons.length)];
        int rank = 0;
        while (M[joltages.length + 1][buttons.length] > 0) {
            int pivotCol = findPivotCol(M[joltages.length + 1], buttons.length);
            if (pivotCol < 0)
                throw new IllegalArgumentException("infeasible");
            int pivotRow = findPivotRow(M, pivotCol, buttons.length, joltages.length);
            if (pivotRow < 0)
                throw new IllegalArgumentException("unbounded");
            if (pivotRow >= rank) {
                ObjectArrays.swap(M, rank, pivotRow);
                pivotRow = rank++;
            }
            pivotCols[pivotRow] = pivotCol;
            pivot(M, pivotCol, pivotRow, buttons.length + 1, joltages.length + 2);
        }

        // Phase II: Minimise sum
        while (true) {
            int pivotCol = findPivotCol(M[joltages.length], buttons.length);
            if (pivotCol < 0)
                break;
            int pivotRow = findPivotRow(M, pivotCol, buttons.length, rank);
            if (pivotRow < 0)
                throw new IllegalArgumentException("unbounded");
            pivotCols[pivotRow] = pivotCol;
            pivot(M, pivotCol, pivotRow, buttons.length + 1, joltages.length + 1);
        }

        // All nearest integer solutions
        double[] result = new double[buttons.length];
        for (int y = 0; y < rank; y++) {
            int x = pivotCols[y];
            result[x] = (double) M[y][buttons.length] / M[y][x];
        }
        return result;
    }

    private int findPivotCol(int[] obj, int width) {
        for (int x = 0; x < width; x++)
            if (obj[x] > 0) return x;
        return -1;
    }

    private int findPivotRow(int[][] M, int pivotCol, int width, int height) {
        int pivotRow = -1;
        double minRatio = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height; y++) {
            if (M[y][pivotCol] <= 0) continue;
            double ratio = (double) M[y][width] / M[y][pivotCol];
            if (pivotRow < 0 || ratio < minRatio) {
                pivotRow = y;
                minRatio = ratio;
            }
        }
        return pivotRow;
    }

    private void pivot(int[][] M, int pivotCol, int pivotRow, int width, int height) {
        for (int y = 0; y < height; y++) {
            if (y == pivotRow || M[y][pivotCol] == 0) continue;
            int c = IntMath.gcd(M[pivotRow][pivotCol], M[y][pivotCol]);
            int a = M[pivotRow][pivotCol] / c;
            int b = M[y][pivotCol] / c;
            for (int x = 0; x < width; x++)
                M[y][x] = a*M[y][x] - b*M[pivotRow][x];
        }
    }

    private void print(int[][] M, int width) {
        for (int[] row : M) {
            String[] lhs = IntStream.of(row)
                .limit(width)
                .mapToObj(x -> String.format("%2d", x))
                .toArray(String[]::new);
            String rhs = String.format("%2d", row[width]);
            System.out.println(String.join(" ", lhs) + " | " + rhs);
        }
    }

    private int findMinimumCount(int[][] buttons, int[] diagram) {
        AffineSpaceGF2 sol = solveGF2(buttons, diagram);
        int leastCount = Integer.MAX_VALUE;
        int n = 1 << sol.basis.length;
        for (int i = 0; i < n; i++)
            leastCount = Math.min(leastCount, Integer.bitCount(sol.get(i)));
        return leastCount;
    }

    public AffineSpaceGF2 solveGF2(int[][] buttons, int[] diagram) {
        // Build augmented matrix, rows are bit vectors
        int[] lhs = new int[diagram.length];
        for (int x = 0; x < buttons.length; x++)
            for (int y : buttons[x])
                lhs[y] |= (1 << x);
        int[] rhs = diagram.clone();

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
                    if (y == rank || (lhs[y] & (1 << x)) == 0) continue;
                    lhs[y] ^= lhs[rank];
                    rhs[y] ^= rhs[rank];
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < diagram.length; y++)
            if (rhs[y] != 0)
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
