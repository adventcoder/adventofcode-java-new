package adventofcode.utils.math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntRot3 {
    public final int a;
    public final IntVec3 B;

    public IntRot3(int x) {
        this(x, IntVec3.ZERO);
    }

    public int radiusSquared() {
        // The value r in:
        //
        // a + b i j k = r e^(v i j k) = r (cos|v| + sin|v| v/|v| i j k)
        //
        return a*a + B.dot(B);
    }

    public double absoluteAngle() {
        // The value |v| in:
        //
        // a + b i j k = r e^(v i j k) = r (cos|v| + sin|v| v/|v| i j k)
        //
        return Math.atan2(Math.sqrt(B.dot(B)), a);
    }

    public IntRot3 conjugate() {
        return new IntRot3(a, B.negate());
    }

    public IntRot3 negate() {
        return new IntRot3(-a, B.negate());
    }

    public IntRot3 add(int n) {
        return new IntRot3(a + n, B);
    }

    public IntRot3 add(IntRot3 r) {
        return new IntRot3(a + r.a, B.add(r.B));
    }

    public IntRot3 subtract(int n) {
        return new IntRot3(a - n, B);
    }

    public IntRot3 subtract(IntRot3 r) {
        return new IntRot3(a - r.a, B.subtract(r.B));
    }

    public IntRot3 multiply(int n) {
        return new IntRot3(a * n, B.multiply(n));
    }

    public IntRot3 multiply(IntRot3 r) {
        return new IntRot3(a*r.a - B.dot(r.B), r.B.multiply(a).add(B.multiply(r.a)).subtract(B.cross(r.B)));
    }

    public IntRot3 multiplySelf() {
        return new IntRot3(a*a - B.dot(B), B.multiply(2*a));
    }

    public IntRot3 pow(int n) {
        IntRot3 acc = new IntRot3(1);
        IntRot3 base = this;
        while (n > 0) {
            if ((n & 1) != 0)
                acc = acc.multiply(base);
            base = base.multiplySelf();
            n >>= 1;
        }
        return acc;
    }
}
