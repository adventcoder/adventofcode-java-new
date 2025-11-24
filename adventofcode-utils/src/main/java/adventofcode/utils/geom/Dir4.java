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

    public static final Dir4[] VALUES = values();

    public final int x;
    public final int y;

    public Dir4 left90(int n) {
        return VALUES[(ordinal() - n) & 3];
    }

    public Dir4 right90(int n) {
        return VALUES[(ordinal() + n) & 3];
    }

    public Dir4 right90() {
        return VALUES[(ordinal() + 1) & 3];
    }

    public Dir4 opposite() {
        return VALUES[(ordinal() + 2) & 3];
    }

    public Dir4 left90() {
        return VALUES[(ordinal() + 3) & 3];
    }

    public Dir4 reflectVertical() {
        return VALUES[(-ordinal()) & 3];
    }

    public Dir4 reflectAntiDiagonal() {
        return VALUES[(1 - ordinal()) & 3];
    }

    public Dir4 reflectHorizontal() {
        return VALUES[(2 - ordinal()) & 3];
    }

    public Dir4 reflectMainDiagonal() {
        return VALUES[(3 - ordinal()) & 3];
    }
}