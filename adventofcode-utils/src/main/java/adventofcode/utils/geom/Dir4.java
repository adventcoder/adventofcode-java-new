package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum Dir4 {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    public final int x;
    public final int y;

    public static final Dir4[] values = values();

    public Dir4 rotate(int n) {
        return values[(ordinal() + n) & 3];
    }

    public Dir4 reflect(int n) {
        return values[(n - ordinal()) & 3];
    }

    public Dir4 right90() {
        return rotate(1);
    }

    public Dir4 opposite() {
        return rotate(2);
    }

    public Dir4 left90() {
        return rotate(3);
    }

    public Dir4 reflectVertical() {
        return reflect(0);
    }

    public Dir4 reflectAntiDiagonal() {
        return reflect(1);
    }

    public Dir4 reflectHorizontal() {
        return reflect(2);
    }

    public Dir4 reflectMainDiagonal() {
        return reflect(3);
    }
}