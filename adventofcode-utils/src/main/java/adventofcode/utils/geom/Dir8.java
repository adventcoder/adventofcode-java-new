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

    public static final Dir8[] VALUES = values();

    public final int x;
    public final int y;

    public Dir8 left45(int n) {
        return VALUES[(ordinal() - n) & 7];
    }

    public Dir8 right45(int n) {
        return VALUES[(ordinal() + n) & 7];
    }

    public Dir8 left90(int n) {
        return left45(2*n);
    }

    public Dir8 right90(int n) {
        return right45(2*n);
    }

    public Dir8 right45() {
        return VALUES[(ordinal() + 1) & 7];
    }

    public Dir8 right90() {
        return VALUES[(ordinal() + 2) & 7];
    }

    public Dir8 right135() {
        return VALUES[(ordinal() + 3) & 7];
    }

    public Dir8 opposite() {
        return VALUES[(ordinal() + 4) & 7];
    }

    public Dir8 left135() {
        return VALUES[(ordinal() + 5) & 7];
    }

    public Dir8 left90() {
        return VALUES[(ordinal() + 6) & 7];
    }

    public Dir8 left45() {
        return VALUES[(ordinal() + 7) & 7];
    }

    public Dir8 reflectVertical() {
        return VALUES[(-ordinal()) & 7];
    }

    public Dir8 reflectAntiDiagonal() {
        return VALUES[(2 - ordinal()) & 7];
    }

    public Dir8 reflectHorizontal() {
        return VALUES[(4 - ordinal()) & 7];
    }

    public Dir8 reflectMainDiagonal() {
        return VALUES[(6 - ordinal()) & 7];
    }
}
