package adventofcode.utils.collect;

import java.util.stream.IntStream;

public class DisjointSet {
    private final int[] parent;
    private final int[] rank;
    private int size;

    public DisjointSet(int size) {
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int size() {
        return size;
    }

    public int rank(int x) {
        return rank[findRoot(x)];
    }

    public boolean merge(int x, int y) {
        int rootX = findRoot(x);
        int rootY = findRoot(y);
        if (rootX == rootY) return false;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            rank[rootY] += rank[rootX];
        } else {
            parent[rootY] = rootX;
            rank[rootX] += rank[rootY];
        }
        size--;
        return true;
    }

    public int findRoot(int x) {
        if (parent[x] != x)
            parent[x] = findRoot(parent[x]);
        return parent[x];
    }

    public IntStream roots() {
        return IntStream.range(0, parent.length)
            .map(this::findRoot)
            .distinct();
    }

    public IntStream ranks() {
        return roots().map(this::rank);
    }
}