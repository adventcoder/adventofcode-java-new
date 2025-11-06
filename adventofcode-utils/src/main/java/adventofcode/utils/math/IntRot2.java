package adventofcode.utils.math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntRot2 {
    public static final IntRot2 RIGHT = new IntRot2(0, 1);
    public static final IntRot2 LEFT = new IntRot2(0, -1);

    public final int a;
    public final int b;

    public IntRot2(int x) {
        this(x, 0);
    }

    public int radiusSquared() {
        return a*a + b*b;
    }

    public double angle() {
        return Math.atan2(b, a);
    }

    public IntRot2 conjugate() {
        return new IntRot2(a, -b);
    }

    public IntRot2 negate() {
        return new IntRot2(-a, -b);
    }

    public IntRot2 add(int n) {
        return new IntRot2(a + n, b);
    }

    public IntRot2 add(IntRot2 r) {
        return new IntRot2(a + r.a, b + r.b);
    }

    public IntRot2 subtract(int n) {
        return new IntRot2(a - n, b);
    }

    public IntRot2 subtract(IntRot2 r) {
        return new IntRot2(a - r.a, b - r.b);
    }

    public IntRot2 multiply(int n) {
        return new IntRot2(a * n, b * n);
    }

    public IntRot2 multiply(IntRot2 r) {
        return new IntRot2(a*r.a - b*r.b, a*r.b + b*r.a);
    }

    public IntRot2 pow(int n) {
        IntRot2 acc = new IntRot2(1);
        IntRot2 base = this;
        while (n > 0) {
            if ((n & 1) != 0)
                acc = acc.multiply(base);
            base = base.multiply(base);
            n >>= 1;
        }
        return acc;
    }
}
