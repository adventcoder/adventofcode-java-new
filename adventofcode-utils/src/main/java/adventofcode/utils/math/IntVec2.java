package adventofcode.utils.math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntVec2 {
    public static IntVec2 ZERO = new IntVec2(0, 0);

    public static IntVec2 NORTH = new IntVec2(0, -1);
    public static IntVec2 EAST = new IntVec2(1, 0);
    public static IntVec2 SOUTH = new IntVec2(0, 1);
    public static IntVec2 WEST = new IntVec2(-1, 0);

    public final int x;
    public final int y;

    public static IntVec2 parse(String s, String sep) {
        String[] tokens = s.split(sep);
        int x = Integer.parseInt(tokens[0].trim());
        int y = Integer.parseInt(tokens[1].trim());
        return new IntVec2(x, y);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public IntVec2 negate() {
        return new IntVec2(-x, -y);
    }

    public IntVec2 rotateLeft() {
        return new IntVec2(y, -x);
    }

    public IntVec2 rotateRight() {
        return new IntVec2(-y, x);
    }

    public IntVec2 add(IntVec2 other) {
        return new IntVec2(x + other.x, y + other.y);
    }

    public IntVec2 subtract(IntVec2 other) {
        return new IntVec2(x - other.x, y - other.y);
    }

    public IntVec2 multiply(int n) {
        return new IntVec2(x * n, y * n);
    }

    public IntVec2 multiply(IntRot2 r) {
        return new IntVec2(x*r.a - y*r.b, x*r.b + y*r.a);
    }

    public IntRot2 multiply(IntVec2 other) {
        return new IntRot2(dot(other), cross(other));
    }

    public int dot(IntVec2 other) {
        return x*other.x + y*other.y;
    }

    public int cross(IntVec2 other) {
        return x*other.y - y*other.x;
    }
}
