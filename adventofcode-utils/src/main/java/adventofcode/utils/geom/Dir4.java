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

    public static Dir4 valueAt(int i) {
        return values()[Math.floorMod(i, 4)];
    }

    public final int x;
    public final int y;

    public Dir4 left(int n) {
        return valueAt(ordinal() - n);
    }

    public Dir4 right(int n) {
        return valueAt(ordinal() + n);
    }

    public Dir4 right() {
        return valueAt(ordinal() + 1);
    }

    public Dir4 flip() {
        return valueAt(ordinal() + 2);
    }

    public Dir4 left() {
        return valueAt(ordinal() + 3);
    }

    public Dir4 reflectVertical() {
        return valueAt(-ordinal());
    }

    public Dir4 reflectAntiDiagonal() {
        return valueAt(1 - ordinal());
    }

    public Dir4 reflectHorizontal() {
        return valueAt(2 - ordinal());
    }

    public Dir4 reflectDiagonal() {
        return valueAt(3 - ordinal());
    }
}