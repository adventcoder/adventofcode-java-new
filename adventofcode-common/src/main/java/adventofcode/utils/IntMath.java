package adventofcode.utils;

import adventofcode.utils.collect.IntArray;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntMath {
    public static int sgn(int x) {
        return Integer.compare(x, 0);
    }

    public static int gcd(int a, int b) {
        while (a != 0) {
            int tmp = a;
            a = b % a;
            b = tmp;
        }
        return b;
    }

    public static int lcm(int a, int b) {
        return (a / gcd(a, b)) * b;
    }

    // a x + b y = gcd(a,b)
    public static BezoutTriple extendedGcd(int a, int b) {
        BezoutTriple A = new BezoutTriple(a, 1, 0);
        BezoutTriple B = new BezoutTriple(b, 0, 1);
        while (A.gcd != 0) {
            BezoutTriple tmp = A;
            A = B.subMul(A, B.gcd / A.gcd);
            B = tmp;
        }
        return B;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class BezoutTriple {
        public final int gcd;
        public final int x;
        public final int y;

        public BezoutTriple subMul(BezoutTriple other, int q) {
            return new BezoutTriple(gcd - other.gcd*q, x - other.x*q, y - other.y*q);
        }
    }

    public static IntArray solveQuadratic(int a, int b, int c) {
        // Solve for x in: a x^2 + b x + c = 0
        if (a == 0)
            return solveLinear(b, c);
        long disc = (long) b*b - 4L*a*c;
        if (disc == 0)
            return solveLinear(2*a, b);
        if (disc > 0) {
            int k = (int) Math.sqrt(disc);
            if (k * k == disc)
                return solveLinear(2*a, b + k).add(solveLinear(2*a, b - k));
        }
        return IntArray.of();
    }

    public static IntArray solveLinear(int a, int b) {
        // Solve for x in: a x + b = 0
        if (a == 0)
            return solveConstant(b);
        if (-b % a == 0)
            return IntArray.of(-b / a);
        return IntArray.of();
    }

    public static IntArray solveConstant(int a) {
        // Solve for x in: a = 0
        if (a == 0)
            throw new IllegalStateException("free variable");
        return IntArray.of();
    }
}
