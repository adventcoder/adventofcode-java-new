package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vec2 implements Comparable<Vec2> {
    public static Vec2 NORTH = new Vec2(0, -1);
    public static Vec2 EAST = new Vec2(1, 0);
    public static Vec2 SOUTH = new Vec2(0, 1);
    public static Vec2 WEST = new Vec2(-1, 0);

    public final int x;
    public final int y;

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 multiply(int n) {
        return new Vec2(x * n, y * n);
    }

    public int dot(Vec2 other) {
        return x*other.x + y*other.y;
    }

    public int cross(Vec2 other) {
        return x*other.y - y*other.x;
    }

    public Vec2 perpLeft() {
        return new Vec2(y, -x);
    }

    public Vec2 perpRight() {
        return new Vec2(-y, x);
    }

    @Override
    public int compareTo(Vec2 other) {
        int cmp = Integer.compare(y, other.y);
        if (cmp == 0)
            cmp = Integer.compare(x, other.x);
        return cmp;
    }
}
