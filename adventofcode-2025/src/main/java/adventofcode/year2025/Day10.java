package adventofcode.year2025;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.array.IntArrays;
import adventofcode.utils.array.ObjectArrays;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.ints.IntList;

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
        for (Machine m : machines) {
            AffineSpaceGF2 sol = solveGF2(m.buttons, m.diagram);
            if (sol != null)
                total += sol.minimizeBitCount();
        }
        return total;
    }

    @Override
    public Integer part2() {
        int total = 0;
        for (Machine m : machines) {
            System.out.println("buttons: " + Arrays.deepToString(m.buttons));
            System.out.println("joltages: " + Arrays.toString(m.joltages()));
            AffineSpace sol = solve(m.buttons, m.joltages);
            if (sol == null) System.out.println("OH NO>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"); //TODO
            if (sol != null) {
                System.out.println("particular: " + Arrays.toString(sol.origin));
                System.out.println("nullspace: " + Arrays.deepToString(sol.basis));
                total += sol.minimizeSum();
            }
            System.out.println();
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
                if (lhs[rank][x] < 0) {
                    for (int i = x; i < buttons.length; i++) lhs[rank][i] *= -1;
                    rhs[rank] *= -1;
                }
                for (int y = 0; y < joltages.length; y++) {
                    if (y == rank) continue;
                    if (lhs[y][x] < 0) {
                        for (int i = x; i < buttons.length; i++) lhs[y][i] += lhs[rank][i];
                        rhs[y] += rhs[rank];
                    } else if (lhs[y][x] > 0) {
                        for (int i = x; i < buttons.length; i++) lhs[y][i] -= lhs[rank][i];
                        rhs[y] -= rhs[rank];
                    }
                }
                pivotCols[rank++] = x;
            }
        }

        // Check valid solution
        for (int y = rank; y < joltages.length; y++)
            if (IntStream.of(lhs[y]).allMatch(n -> n == 0) && rhs[y] != 0)
                return null;

        // Build solution space
        int[] particular = new int[buttons.length];
        int[][] nullspace = new int[nullity][buttons.length];
        for (int y = 0; y < rank; y++) {
            particular[pivotCols[y]] = rhs[y];
            for (int nx = 0; nx < nullity; nx++)
                nullspace[nx][pivotCols[y]] = -lhs[y][freeCols[nx]];
        }
        for (int nx = 0; nx < nullity; nx++)
            nullspace[nx][freeCols[nx]] = 1;

        return new AffineSpace(particular, nullspace);
    }

    public record AffineSpace(int[] origin, int[][] basis) {
        public int minimizeSum() {
            return minimizeSum(new IntArrayList(), IntArrays.sum(origin));
        }

        private int minimizeSum(IntList params, int partialSum) {
            if (params.size() == basis.length) {
                return partialSum;
            }
            int minSum = Integer.MAX_VALUE;
            int basisSum = IntArrays.sum(basis[params.size()]);
            IntIntPair bounds = computeNextBounds(params);
            for (int t = bounds.leftInt(); t <= bounds.rightInt(); t++) {
                if (partialSum + t*basisSum >= minSum) continue; // prune branch
                params.add(t);
                minSum = Math.min(minSum, minimizeSum(params, partialSum + t*basisSum));
                params.removeInt(params.size() - 1);
            }
            return minSum;
        }

        private IntIntPair computeNextBounds(IntList prefix) {
            //TODO: this is from chat gpt
            int k = prefix.size();
            int n = origin.length;

            int lo = Integer.MIN_VALUE;
            int hi = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                // compute remainingOrigin[i]
                int remaining = origin[i];
                for (int j = 0; j < prefix.size(); j++)
                    remaining += basis[j][i] * prefix.getInt(j);

                int a = basis[k][i]; // coefficient of next parameter

                if (a == 0) continue;

                // remaining + a*t >= 0
                int rhs = -remaining;

                if (a > 0) {
                    int lb = -Math.floorDiv(-rhs, a);
                    lo = Math.max(lo, lb);
                } else {
                    int ub = Math.floorDiv(rhs, a);
                    hi = Math.min(hi, ub);
                }
            }

            return IntIntPair.of(lo, hi);
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
