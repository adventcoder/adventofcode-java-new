package adventofcode.utils.iter;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import adventofcode.utils.array.IntArrays;
import adventofcode.utils.array.ObjectArrays;
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
            T[] arr = ObjectArrays.newInstance(Object.class, vals.size());

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public List<T> next() {
                for (int i = 0; i < perm.length; i++)
                    arr[i] = vals.get(perm[i]);
                hasNext = IntArrays.nextPermutation(perm);
                return List.of(arr);
            }
        };
    }
}
