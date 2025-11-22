package adventofcode.utils;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntMath {
    public static int sgn(int x) {
        return Integer.compare(x, 0);
    }

    public static int product(int x, int y) {
        return x * y;
    }

    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (a > 0) {
            int tmp = a;
            a = b % a;
            b = tmp;
        }
        return b;
    }

    public static int lcm(int a, int b) {
        int g = gcd(a, b);
        return g == 0 ? 0 : Math.abs(a * (b / g));
    }

    public static int modInverse(int x, int m) {
        var triple = extendedGcd(x, m);
        if (triple.gcd != 1)
            throw new ArithmeticException(x + " not invertible mod " + m);
        return Math.floorMod(triple.x, m);
    }

    public static BezoutTriple extendedGcd(int a, int b) {
        int nextG = Math.abs(a), nextX = sgn(a), nextY = 0;
        int g = Math.abs(b), x = 0, y = sgn(b);
        while (nextG > 0) {
            int q = g / nextG;
            int oldG = g; g = nextG; nextG = oldG - q*nextG;
            int oldX = x; x = nextX; nextX = oldX - q*nextX;
            int oldY = y; y = nextY; nextY = oldY - q*nextY;
        }
        return new BezoutTriple(g, x, y);
    }

    @AllArgsConstructor
    public static class BezoutTriple {
        public final int gcd;
        public final int x;
        public final int y;
    }

    public static int rowReduce(int[][] M, int maxRank) {
        for (int r = 0; r < maxRank; r++) {
            boolean foundPivot = false;
            for (int i = r; i < M.length; i++) {
                if (M[i][r] != 0) {
                    int[] t = M[r];
                    M[r] = M[i];
                    M[i] = t;
                    foundPivot = true;
                    break;
                }
            }
            if (!foundPivot)
                return r;
            for (int i = 0; i < M.length; i++) {
                if (i != r && M[i][r] != 0) {
                    int g = gcd(M[r][r], M[i][r]);
                    int a = M[r][r] / g;
                    int b = M[i][r] / g;
                    if (i < r)
                        M[i][i] *= a;
                    for (int j = r; j < M[i].length; j++)
                        M[i][j] = a * M[i][j] - b * M[r][j];
                }
            }
        }
        return maxRank;
    }
}
