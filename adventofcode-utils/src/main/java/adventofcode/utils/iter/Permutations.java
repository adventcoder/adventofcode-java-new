package adventofcode.utils.iter;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import adventofcode.utils.array.IntArrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Permutations<T> extends AbstractCollection<List<T>> {
    private final List<T> vals;

    @Override
    public int size() {
        int size = 1;
        for (int n = 1; n <= vals.size(); n++)
            size *= n;
        return size;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<>() {
            int[] perm = IntStream.range(0, vals.size()).toArray();
            boolean hasNext = true;
            List<T> curr = new ArrayList<>(vals.size());

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public List<T> next() {
                if (!hasNext) throw new NoSuchElementException();
                curr.clear();
                for (int i = 0; i < perm.length; i++)
                    curr.add(vals.get(perm[i]));
                hasNext = IntArrays.nextPermutation(perm);
                return curr; // the caller must copy if they wish to mutate
            }
        };
    }
}
