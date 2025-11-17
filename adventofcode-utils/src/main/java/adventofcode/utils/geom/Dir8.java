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
    SOUTHWEST(-1, 0),
    WEST(-1, 0),
    NORTHWEST(-1, -1);

    public final int x;
    public final int y;
}
