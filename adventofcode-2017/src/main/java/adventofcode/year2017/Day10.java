package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.year2017.utils.KnotHash;

@Puzzle(day = 10, name = "Knot Hash")
public class Day10 extends AbstractDay {
    private String input;

    @Override
    public void parse(String input) {
        this.input = input;
    }

    @Override
    public Integer part1() {
        KnotHash hash = new KnotHash(Fn.parseBytes(input, ","));
        hash.round();
        return hash.hash[0] * hash.hash[1];
    }

    @Override
    public String part2() {
        return KnotHash.standard(input).toHexString();
    }
}
