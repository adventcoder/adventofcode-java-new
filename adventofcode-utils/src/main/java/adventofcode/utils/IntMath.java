package adventofcode.utils;

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

    public static int floorSqrt(int x) {
        return (int) Math.sqrt(x);
    }
}
