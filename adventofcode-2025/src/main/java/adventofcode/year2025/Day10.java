package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.array.IntArrays;
import adventofcode.utils.array.ObjectArrays;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

//     @Override
//     protected String getInput() throws IOException {
//         return """
// [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
// [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
// [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
// """;
//     }

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
        // buttons: [[1, 2, 4, 6, 7], [1, 3, 4, 5, 6, 7], [1, 4, 6], [0, 3, 4, 5, 6], [0, 4, 7], [0, 5, 6], [0, 1, 2, 4, 5, 7], [0, 2, 3, 4, 5, 6, 7], [2, 3, 5], [0, 5]]
        // joltages: [45, 36, 41, 26, 53, 54, 45, 36]
        // particular: [78, 8, -28, 96, -28, 26, 86, 0, 0, 0]
        // basis: [[-4, 0, 4, -4, 0, 0, 0, 4, 0, 0], [-2, 0, 4, -4, 4, 2, -2, 0, 4, 0], [2, 0, 0, 0, 0, -2, -2, 0, 0, 4]]
        // scale: 4
        //
        // -4 -2  2 |-78     -4 t - 2 u + 2 v >= -78
        //  0  0  0 | -8                    0 >= -8
        //  4  4  0 | 28      4 t + 4 u       >= 28
        // -4 -4  0 |-96     -4 t - 4 u       >= 96
        //  0  4  0 | 28            4 u       >= 28
        //  0  2 -2 |-26            2 u - 2 v >= -26
        //  0 -2 -2 |-86           -2 u - 2 v >= -86
        //  4  0 -2 |  0      4 t       - 2 v >= 0
        //  0  4  0 |  0            4 u       >= 0
        //  0  0  4 |  0                  4 v >= 0
        //
        //  4  4  0 | 28
        //  4  0 -2 |  0
        // -4 -2  2 |-78
        // -4 -4  0 |-96
        //  0  4  0 | 28
        //  0  2 -2 |-26
        //  0 -2 -2 |-86
        //  0  4  0 |  0
        //  0  0  4 |  0
        //  0  0  0 | -8
        //
        //  1  0  0 |  0   t>=0
        // -1  0  0 |-17   t<=17
        //  0  1  0 |  7   u>=7
        //  0 -1  0 |-43   u<=43
        //  0  0  1 |  0   v>=0
        //
        //             t
        //  4  0 | 28 -4
        //  0 -2 |  0 -4
        // -2  2 |-78  4
        // -4  0 |-96  4
        //  4  0 | 28  0
        //  2 -2 |-26  0
        // -2 -2 |-86  0
        //  4  0 |  0  0
        //  0  4 |  0  0
        //  0  0 | -8  0
        //
        //  1  0 |-13  0   u >= max(-13,7-t,7,0)
        //  1  0 |  7 -1
        //  1  0 |  7  0
        //  1  0 |  0  0
        // -1  0 |-41  1   u <=min(-41+t,-39,-28+t,-43)
        // -1  0 |-39  0
        // -1  0 |-28  1
        // -1  0 |-43  0
        //  0 -1 |  0 -2
        //  0  1 |  0  0
        //  0  0 | -8  0
        //
        int total = 0;
        for (Machine m : machines) {
            AffineSpace sol = solve(m.buttons, m.joltages);
            if (sol == null) continue;
            if (sol.basis.length < 3) continue;
            System.out.println("buttons: " + Arrays.deepToString(m.buttons));
            System.out.println("joltages: " + Arrays.toString(m.joltages()));
            System.out.println("particular: " + Arrays.toString(sol.origin));
            System.out.println("nullspace: " + Arrays.deepToString(sol.basis));
            System.out.println("scale: " + sol.scale);
            System.out.println();
            // Integer minSum = sol.minimizeSum();
            // if (minSum != null)
            //     total += minSum;
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

    public AffineSpace solve(int[][] buttons, int[] joltages) {
        // Build augmented matrix, rows are bit vectors
        int[][] lhs = new int[joltages.length][buttons.length];
        for (int x = 0; x < buttons.length; x++)
            for (int y : buttons[x])
                lhs[y][x] = 1;
        int[] rhs = new int[joltages.length];
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
                if (lhs[y][x] != 0) {
                    ObjectArrays.swap(lhs, rank, y);
                    IntArrays.swap(rhs, rank, y);
                    foundPivot = true;
                    break;
                }
            }
            if (!foundPivot) {
                freeCols[nullity++] = x;
            } else {
                for (int y = 0; y < joltages.length; y++) {
                    if (y == rank) continue;
                    if (lhs[y][x] != 0) {
                        int d = IntMath.gcd(lhs[rank][x], lhs[y][x]);
                        int a = lhs[rank][x] / d;
                        int b = lhs[y][x] / d;
                        if (y < rank)
                            lhs[y][pivotCols[y]] *= a;
                        for (int i = x; i < buttons.length; i++)
                            lhs[y][i] = a*lhs[y][i] - b*lhs[rank][i];
                        rhs[y] = a*rhs[y] - b*rhs[rank];
                    }
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < joltages.length; y++)
            if (IntStream.of(lhs[y]).allMatch(n -> n == 0) && rhs[y] != 0)
                return null;

        // normalise pivots
        int m = 1;
        for (int y = 0; y < rank; y++)
            m = IntMath.lcm(m, lhs[y][pivotCols[y]]);
        for (int y = 0; y < rank; y++) {
            int factor = m / lhs[y][pivotCols[y]];
            lhs[y][pivotCols[y]] *= factor;
            for (int nx = 0; nx < nullity; nx++)
                lhs[y][freeCols[nx]] *= factor;
            rhs[y] *= factor;
        }

        // Build solution space
        int[] particular = new int[buttons.length];
        int[][] nullspace = new int[nullity][buttons.length];
        for (int y = 0; y < rank; y++) {
            particular[pivotCols[y]] = rhs[y];
            for (int nx = 0; nx < nullity; nx++)
                nullspace[nx][pivotCols[y]] = -lhs[y][freeCols[nx]];
        }
        for (int nx = 0; nx < nullity; nx++)
            nullspace[nx][freeCols[nx]] = m;

        return new AffineSpace(particular, nullspace, m);
    }

    public record AffineSpace(int[] origin, int[][] basis, int scale) {
        // buttons: [[3], [1, 3], [2], [2, 3], [0, 2], [0, 1]]
        // joltages: [3, 5, 4, 7]
        // particular: [2, 5, 1, 0, 3, 0]
        // nullspace: [[-1, 0, -1, 1, 0, 0], [1, -1, 1, 0, -1, 1]]
        //
        // m [a] = [2] + [-1  1] [d], m=1
        //   [b]   [5]   [ 0 -1] [f]
        //   [c]   [1]   [-1  1]
        //   [d]   [0]   [ 1  0]
        //   [e]   [3]   [ 0 -1]
        //   [f]   [0]   [ 0  1]
        //
        // 2 - d + f >= 0
        // 5     - f >= 0
        // 1 - d + f >= 0
        //     d     >= 0
        // 3     - f >= 0
        //         f >= 0
        //
        // sum = 11 - d + f
        //
        // Eliminate f by combining all lo/hi pairs:
        //
        // lo            hi
        // f >= d - 2    f <= 5
        // f >= d - 1    f <= 3
        // f >= 0
        //
        // combined                 inequalities without f
        // d - 2 <= 5     d <= 7    d >= 0
        // d - 1 <= 5     d <= 6
        //     0 <= 5  => d <= 5
        // d - 2 <= 3     d <= 4
        // d - 1 <= 3
        //     0 <= 3
        //
        // Since d is negative in the sum to minimize choose the min upper bound: d=4
        //
        // Back substitude to find f:
        //
        // f >= 4 - 2 = 2    f <= 5
        // f >= 4 - 1 = 3    f <= 3
        // f >= 0
        //
        // Since f is positive in the sum to minimize choose the max lower bound: f=3
        //
        // sum = 11 - 4 + 3 = 10
        //
        public void minimizeSum(int[] params) {
            if (basis.length == 0) return;
            int xLast = basis.length - 1;
            eliminate(xLast).minimizeSum(params);
            int val = IntArrays.sum(basis[xLast]);
            if (val > 0) {
                // find the max lower bound doing back subtitution of params
                int max = Integer.MIN_VALUE;
                for (int y = 0; y < origin.length; y++) {
                    if (basis[xLast][y] > 0) {
                        // 1 + 2 d + 3 f >= 0
                        //             f >= ceil((-1 - 2 d)/3)
                        int lb = origin[y];
                        for (int x = 0; x < xLast; x++)
                            lb += params[x] * basis[x][y];
                        max = Math.max(max, -Math.floorDiv(lb, basis[xLast][y]));
                    }
                }
                params[xLast] = max;
            } else if (val < 0) {
                // find the min upper bound doing back subtitution of params
                int min = Integer.MAX_VALUE;
                for (int y = 0; y < origin.length; y++) {
                    if (basis[xLast][y] < 0) {
                        // 1 + 2 d - 3 f >= 0
                        //             f <= floor((1 + 2 d)/3)
                        int ub = origin[y];
                        for (int x = 0; x < xLast; x++)
                            ub += params[x] * basis[x][y];
                        min = Math.min(min, Math.floorDiv(ub, basis[xLast][y]));
                    }
                }
                params[xLast] = min;
            }
        }

        private AffineSpace eliminate(int xElim) {
            IntList newOrigin = new IntArrayList();
            List<int[]> newBasis = new ArrayList<>();
            // add derived constrains from the eliminated column
            for (int y1 = 0; y1 < basis.length; y1++) {
                for (int y2 = 0; y2 < basis.length; y2++) {
                    // y1 is a lower bound, y2 is an upper bound
                    //   lower:  2 - d + f >= 0   (f >= d - 2)
                    //   upper: -5     - f >= 0   (f <= 5)
                    // derived:  7 - d     >= 0   (d <= 7)
                    if (basis[xElim][y1] > 0 && basis[xElim][y2] < 0) {
                        newBasis.add(null); //TODO
                        newOrigin.add(origin[y1] - origin[y2]);
                    }
                }
            }
            // also add constraints not referencing the eliminated colum
            for (int y = 0; y < basis.length; y++) {
                if (basis[xElim][y] == 0) {
                    newBasis.add(null); //TODO
                    newOrigin.add(origin[y]);
                }
            }
            return new AffineSpace(newOrigin.toIntArray(), newBasis.toArray(int[][]::new), scale);
        }
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
