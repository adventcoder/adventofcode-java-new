package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vec2 {
    public static Vec2 NORTH = new Vec2(0, -1);
    public static Vec2 EAST = new Vec2(1, 0);
    public static Vec2 SOUTH = new Vec2(0, 1);
    public static Vec2 WEST = new Vec2(-1, 0);

    public final int x;
    public final int y;

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 rotateLeft() {
        return new Vec2(y, -x);
    }

    public Vec2 rotateRight() {
        return new Vec2(-y, x);
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

    public Vec2 multiply(Rot2 r) {
        return new Vec2(x*r.a - y*r.b, x*r.b + y*r.a);
    }

    public Rot2 multiply(Vec2 other) {
        return new Rot2(dot(other), cross(other));
    }

    public int dot(Vec2 other) {
        return x*other.x + y*other.y;
    }

    public int cross(Vec2 other) {
        return x*other.y - y*other.x;
    }
}
