package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir4;
import lombok.AllArgsConstructor;

@Puzzle(day = 13, name = "Mine Cart Madness")
public class Day13 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day13.class, args);
    }

    private static final Map<Character, Dir4> dirMap = Map.of('^', Dir4.NORTH, '>', Dir4.EAST, 'v', Dir4.SOUTH, '<', Dir4.WEST);

    private String[] track;
    private List<Cart> carts = new ArrayList<>();
    private Set<Cart> dead = new LinkedHashSet<>();

    @Override
    public void parse(String input) {
        track = input.split("\n");
        for (int y = 0; y < track.length; y++) {
            for (int x = 0; x < track[y].length(); x++) {
                Dir4 dir = dirMap.get(track[y].charAt(x));
                if (dir != null)
                    carts.add(new Cart(x, y, dir, 0));
            }
        }
    }

    @Override
    public String part1() {
        while (dead.isEmpty())
            tick();
        Cart cart = dead.iterator().next();;
        return cart.x + "," + cart.y;
    }

    @Override
    public String part2() {
        while (carts.size() > 1)
            tick();
        Cart cart = carts.get(0);
        return cart.x + "," + cart.y;
    }

    private void tick() {
        carts.sort(Comparator.comparingInt(c -> c.x + track[0].length()*c.y));
        for (Cart cart : carts) {
            if (dead.contains(cart)) continue;
            cart.tick(track);
            for (Cart other : carts) {
                if (cart != other && cart.collides(other) && !dead.contains(other)) {
                    dead.add(cart);
                    dead.add(other);
                }
            }
        }
        carts.removeAll(dead);
    }

    @AllArgsConstructor
    public static class Cart {
        private int x;
        private int y;
        private Dir4 dir;
        private int state;

        public boolean collides(Cart other) {
            return x == other.x && y == other.y;
        }

        public void tick(String[] track) {
            x += dir.x;
            y += dir.y;
            switch (track[y].charAt(x)) {
                case '/' -> dir = dir.reflectAntiDiagonal();
                case '\\' -> dir = dir.reflectMainDiagonal();
                case '+' -> {
                    dir = dir.right90(state - 1);
                    state = (state + 1) % 3;
                }
            }
        }
    }
}
