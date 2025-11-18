package adventofcode.utils.geom;

import it.unimi.dsi.fastutil.ints.AbstractIntList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Vector2 extends AbstractIntList {
    public final int x;
    public final int y;

    @Override
    public int size() {
        return 2;
    }

    @Override
    public int getInt(int i) {
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    public Vector2 addMul(Vector2 v, int n) {
        return new Vector2(x + v.x*n, y + v.y*n);
    }

    public Vector2 subtract(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public Vector2 subtractMul(Vector2 v, int n) {
        return new Vector2(x - v.x*n, y - v.y*n);
    }

    public Vector2 multiply(int n) {
        return new Vector2(x * n, y * n);
    }

    public int dot(Vector2 v) {
        return x*v.x + y*v.y;
    }

    public int cross(Vector2 v) {
        return x*v.y - y*v.x;
    }
}
