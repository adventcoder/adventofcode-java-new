package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum Dir8 {
    NORTH(0, -1),
    NORTHEAST(1, -1),
    EAST(1, 0),
    SOUTHEAST(1, 1),
    SOUTH(0, 1),
    SOUTHWEST(-1, 1),
    WEST(-1, 0),
    NORTHWEST(-1, -1);

    public final int x;
    public final int y;

    public static final Dir8[] values = values();

    public Dir8 rotate(int n) {
        return values[(ordinal() + n) & 7];
    }

    public Dir8 reflect(int n) {
        return values[(n - ordinal()) & 7];
    }

    public Dir8 right45() {
        return rotate(1);
    }

    public Dir8 right90() {
        return rotate(2);
    }

    public Dir8 right135() {
        return rotate(3);
    }

    public Dir8 opposite() {
        return rotate(4);
    }

    public Dir8 left135() {
        return rotate(5);
    }

    public Dir8 left90() {
        return rotate(6);
    }

    public Dir8 left45() {
        return rotate(7);
    }

    public Dir8 reflectVertical() {
        return reflect(0);
    }

    public Dir8 reflectAntiDiagonal() {
        return reflect(2);
    }

    public Dir8 reflectHorizontal() {
        return reflect(4);
    }

    public Dir8 reflectMainDiagonal() {
        return reflect(6);
    }
}
