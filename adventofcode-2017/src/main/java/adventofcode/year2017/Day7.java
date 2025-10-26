package adventofcode.year2017;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.collect.DefaultHashMap;
import lombok.RequiredArgsConstructor;

@Puzzle(day = 7, name = "Recursive Circus")
public class Day7 extends AbstractDay {
    private static DefaultHashMap<String, Node> nodes = new DefaultHashMap<>(Node::new);
    private Node root;

    @Override
    public void parse(String input) {
        for (String line : input.split("\n")) {
            String[] tokens = line.trim().replaceAll("->", "").replace(",", "").split("\\s+");
            Node node = nodes.computeIfAbsent(tokens[0]);
            node.weight = Integer.parseInt(tokens[1].replaceAll("\\W", ""));
            for (int i = 2; i < tokens.length; i++) {
                node.children.add(nodes.computeIfAbsent(tokens[i]));
            }
        }
    }

    @Override
    public String part1() {
        Set<Node> roots = new HashSet<>(nodes.values());
        for (Node node : nodes.values())
            roots.removeAll(node.children);
        root = roots.iterator().next();
        return root.name;
    }

    @Override
    public Integer part2() {
        root.computeTotalWeight();
        return root.balance();
    }


    @RequiredArgsConstructor
    private static class Node {
        public final String name;
        private int weight;
        private int totalWeight;
        private List<Node> children = new ArrayList<>();

        private void computeTotalWeight() {
            totalWeight = weight;
            for (Node child : children) {
                child.computeTotalWeight();
                totalWeight += child.totalWeight;
            }
        }

        private Integer balance() {
            for (Node child : children) {
                var result = child.balance();
                if (result != null)
                    return result;
            }

            if (children.size() >= 3) {
                int correctChildTotalWeight = mostCommon(children.get(0).totalWeight, children.get(1).totalWeight, children.get(2).totalWeight);
                for (Node child : children) {
                    if (child.totalWeight != correctChildTotalWeight) {
                        int deviation = totalWeight - weight - correctChildTotalWeight * children.size();
                        return child.weight - deviation;
                    }
                }
            }

            return null;
        }

        private static int mostCommon(int a, int b, int c) {
            return a == b || a == c ? a : b;
        }
    }
}
