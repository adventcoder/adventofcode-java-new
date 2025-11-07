package adventofcode.utils.math;

import adventofcode.utils.collect.IntArrays;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntMath {
    public static int sgn(int x) {
        return Integer.compare(x, 0);
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

    public static int modInverse(int x, int m) {
        var triple = extendedGcd(x, m);
        if (triple.gcd != 1)
            throw new ArithmeticException(x + " not invertible mod " + m);
        return Math.floorMod(triple.x, m);
    }

    public static int[] findRoots(int a, int b, int c) {
        if (a == 0)
            return findRoots(b, c);
        int disc = b*b - 4*a*c;
        if (disc == 0)
            // (2 a x + b)^2 = 0
            return findRoots(2*a, b);
        if (disc > 0) {
            int k = (int) Math.sqrt(disc);
            if (k * k == disc)
                // (2 a x + b + k)(2 a x + b - k) = 0
                return IntArrays.concat(findRoots(2*a, b + k), findRoots(2*a, b - k));
        }
        return new int[0];
    }

    public static int[] findRoots(int a, int b) {
        if (a == 0)
            return findRoots(b);
        if (b % a == 0)
            return new int[] { -b / a };
        return new int[0];
    }

    public static int[] findRoots(int a) {
        if (a == 0)
            throw new ArithmeticException("zero polynomial");
        return new int[0];
    }
}
