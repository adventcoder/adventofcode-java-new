package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Rot2 {
    public static final Rot2 RIGHT = new Rot2(0, 1);
    public static final Rot2 LEFT = new Rot2(0, -1);

    public final int a;
    public final int b;

    public Rot2(int x) {
        this(x, 0);
    }

    public int radiusSquared() {
        return a*a + b*b;
    }

    public double angle() {
        return Math.atan2(b, a);
    }

    public Rot2 conjugate() {
        return new Rot2(a, -b);
    }

    public Rot2 negate() {
        return new Rot2(-a, -b);
    }

    public Rot2 add(int n) {
        return new Rot2(a + n, b);
    }

    public Rot2 add(Rot2 r) {
        return new Rot2(a + r.a, b + r.b);
    }

    public Rot2 subtract(int n) {
        return new Rot2(a - n, b);
    }

    public Rot2 subtract(Rot2 r) {
        return new Rot2(a - r.a, b - r.b);
    }

    public Rot2 multiply(int n) {
        return new Rot2(a * n, b * n);
    }

    public Rot2 multiply(Rot2 r) {
        return new Rot2(a*r.a - b*r.b, a*r.b + b*r.a);
    }

    public Rot2 pow(int n) {
        Rot2 acc = new Rot2(1);
        Rot2 base = this;
        while (n > 0) {
            if ((n & 1) != 0)
                acc = acc.multiply(base);
            base = base.multiply(base);
            n >>= 1;
        }
        return acc;
    }
}
