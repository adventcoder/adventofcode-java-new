package adventofcode.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IntMath {
    public static int getBit(int x, int i) {
        return (x >>> i) & 1;
    }

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

    public static void main(String[] args) {
        System.out.println(extendedGcd(24504480, 38));
    }
}
