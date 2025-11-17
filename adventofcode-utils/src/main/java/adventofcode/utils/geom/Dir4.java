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

    public Dir4 left(int n) {
        return Dir4.values()[Math.floorMod(ordinal() - n, 4)];
    }

    public Dir4 left() {
        return left(1);
    }

    public Dir4 right(int n) {
        return Dir4.values()[Math.floorMod(ordinal() + n, 4)];
    }

    public Dir4 right() {
        return right(1);
    }
}