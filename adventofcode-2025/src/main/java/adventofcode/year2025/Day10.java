package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.array.IntArrays;
import adventofcode.utils.array.ObjectArrays;
import adventofcode.utils.iter.Enumerable;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
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
        for (Machine m : machines)
            total += findMinimumSum(m.buttons, m.joltages);
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

    private int findMinimumSum(int[][] buttons, int[] joltages) {
        AffineSpace space = solve(buttons, joltages);
        // I just do a brute force search over the feasible region, though I could probably branch and bound.
        // I already do recursive branching, but I have figure out a lower bound on the sum over each for loop for pruning.
        return space.nonnegative()
            .map(space::getIntegral)
            .filter(Objects::nonNull)
            .mapToInt(IntArrays::sum)
            .min();
    }

    public AffineSpace solve(int[][] buttons, int[] joltages) {
        // Build augmented matrix
        int[][] mat = new int[joltages.length][buttons.length + 1];
        for (int x = 0; x < buttons.length; x++)
            for (int y : buttons[x])
                mat[y][x] = 1;
        for (int y = 0; y < joltages.length; y++)
            mat[y][buttons.length] = joltages[y];

        // Row reduce
        int[] pivotCols = new int[Math.min(joltages.length, buttons.length)];
        int[] freeCols = new int[buttons.length];
        int rank = 0;
        int nullity = 0;
        for (int x = 0; x < buttons.length; x++) {
            boolean foundPivot = false;
            for (int y = rank; y < joltages.length; y++) {
                if (mat[y][x] != 0) {
                    ObjectArrays.swap(mat, rank, y);
                    foundPivot = true;
                    break;
                }
            }
            if (!foundPivot) {
                freeCols[nullity++] = x;
            } else {
                if (mat[rank][x] < 0) {
                    for (int i = x; i < buttons.length + 1; i++)
                        mat[rank][i] *= -1;
                }
                for (int y = 0; y < joltages.length; y++) {
                    if (y == rank || mat[y][x] == 0) continue;
                    int c = IntMath.gcd(mat[rank][x], mat[y][x]);
                    int a = mat[rank][x] / c;
                    int b = mat[y][x] / c;
                    for (int i = 0; i < buttons.length + 1; i++)
                        mat[y][i] = a*mat[y][i] - b*mat[rank][i];
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < joltages.length; y++)
            if (mat[y][buttons.length] != 0)
                return null;

        // Build solution space
        int[] factors = new int[buttons.length];
        int[] particular = new int[buttons.length];
        int[][] nullspace = new int[nullity][buttons.length];
        for (int y = 0; y < rank; y++) {
            factors[pivotCols[y]] = mat[y][pivotCols[y]];
            particular[pivotCols[y]] = mat[y][buttons.length];
            for (int nx = 0; nx < nullity; nx++)
                nullspace[nx][pivotCols[y]] = -mat[y][freeCols[nx]];
        }
        for (int nx = 0; nx < nullity; nx++) {
            factors[freeCols[nx]] = 1;
            nullspace[nx][freeCols[nx]] = 1;
        }

        return new AffineSpace(particular, nullspace, factors);
    }

    // diag(factors) x = origin + dot(basis, t)
    public record AffineSpace(int[] origin, int[][] basis, int[] factors) {
        public int[] getIntegral(int[] params) {
            int[] vec = origin.clone();
            for (int y = 0; y < basis.length; y++)
                for (int x = 0; x < basis[y].length; x++)
                    vec[x] += params[y]*basis[y][x];
            for (int x = 0; x < factors.length; x++) {
                if (vec[x] % factors[x] != 0)
                    return null;
                vec[x] /= factors[x];
            }
            return vec;
        }

        public Enumerable<int[]> nonnegative() {
            List<int[]> ineqs = new ArrayList<>();
            for (int x = 0; x < origin.length; x++) {
                int[] ineq = new int[basis.length + 1];
                for (int y = 0; y < basis.length; y++)
                    ineq[y] = basis[y][x];
                ineq[basis.length] = -origin[x];
                ineqs.add(ineq);
            }
            return feasibleRegion(ineqs, basis.length);
        }
    }

    private static Enumerable<int[]> feasibleRegion(List<int[]> ineqs, int size) {
        int pivotCol = findPivotCol(ineqs, size);
        if (pivotCol < 0) {
            for (int[] row : ineqs)
                if (row[size] > 0)
                    return Enumerable.empty();
            return Enumerable.of(new int[size]);
        } else {
            List<int[]> lower = new ArrayList<>();
            List<int[]> upper = new ArrayList<>();
            List<int[]> free = new ArrayList<>();
            for (int[] ineq : ineqs) {
                if (ineq[pivotCol] > 0) {
                    lower.add(ineq);
                } else if (ineq[pivotCol] < 0) {
                    upper.add(ineq);
                } else {
                    free.add(ineq);
                }
            }
            for (int[] a : lower) {
                for (int[] b : upper) {
                    // a1 x + b1 y + c1 z >= d1, c1>0
                    // a2 x + b2 y + c2 z >= d2, c2<0
                    //
                    // C1 = c1/gcd(c1,c2)
                    // C2 = c2/gcd(c1,c2)
                    //
                    // (C1 a2-C2 a1) x + (C1 b2-C2 b1) y >= (C1 d2-C2 d1)
                    //
                    int d = IntMath.gcd(a[pivotCol], b[pivotCol]);
                    int A = a[pivotCol] / d;
                    int B = b[pivotCol] / d;
                    int[] c = new int[size + 1];
                    for (int i = 0; i < size + 1; i++)
                        c[i] = A*b[i] - B*a[i];
                    free.add(c);
                }
            }
            Enumerable<int[]> parent = feasibleRegion(free, size);
            return action -> {
                parent.forEach(params -> {
                    int lb = Integer.MIN_VALUE;
                    for (int[] ineq : lower) {
                        int n = ineq[size] - IntArrays.dot(ineq, params);
                        int d = ineq[pivotCol];
                        lb = Math.max(lb, -Math.floorDiv(-n, d));
                    }
                    int ub = Integer.MAX_VALUE;
                    for (int[] ineq : upper) {
                        int n = IntArrays.dot(ineq, params) - ineq[size];
                        int d = -ineq[pivotCol];
                        ub = Math.min(ub, Math.floorDiv(n, d));
                    }
                    // System.out.println("params[" + pivotCol + "] : " + lb + " - " + ub);
                    for (int x = lb; x <= ub; x++) {
                        params[pivotCol] = x;
                        action.accept(params);
                        params[pivotCol] = 0;
                    }
                });
            };
        }
    }

    private static int findPivotCol(List<int[]> ineqs, int size) {
        int pivotCol = -1;
        int leastPairs = Integer.MAX_VALUE;
        for (int x = 0; x < size; x++) {
            int lower = 0;
            int upper = 0;
            for (int[] ineq : ineqs) {
                if (ineq[x] > 0) lower++;
                else if (ineq[x] < 0) upper++;
            }
            int pairs = lower * upper;
            if (pairs != 0 && pairs < leastPairs) {
                pivotCol = x;
                leastPairs = pairs;
            }
        }
        return pivotCol;
    }

    private int findMinimumCount(int[][] buttons, int[] diagram) {
        AffineSpaceGF2 space = solveGF2(buttons, diagram);
        int leastCount = Integer.MAX_VALUE;
        int n = 1 << space.basis.length;
        for (int i = 0; i < n; i++)
            leastCount = Math.min(leastCount, Integer.bitCount(space.get(i)));
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

    /*
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
    */
}
