package adventofcode.utils.iter;

import java.util.NoSuchElementException;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntIterator;

public interface IntGenerator extends IntIterable, IntEnumerable {
    boolean next(IntConsumer action);

    default IntStream stream() {
        return StreamSupport.intStream(intSpliterator(), false);
    }

    @Override
    default void forEach(IntConsumer action) {
        int[] box = new int[1];
        while (next(t -> box[0] = t))
            action.accept(box[0]);
    }

    @Override
    default IntIterator iterator() {
        int[] box = new int[1];
        return new IntIterator() {
            private boolean hasNext = IntGenerator.this.next(t -> box[0] = t);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public int nextInt() {
                if (!hasNext) throw new NoSuchElementException();
                int curr = box[0];
                hasNext = IntGenerator.this.next(t -> box[0] = t);
                return curr;
            }
        };
    }

    @Override
    default IntGenerator map(IntUnaryOperator func) {
        return action -> next(val -> action.accept(func.applyAsInt(val)));
    }

    @Override
    default <T> Generator<T> mapToObj(IntFunction<? extends T> func) {
        int[] box = new int[1];
        return () -> next(val -> box[0] = val) ? func.apply(box[0]): null;
    }

    @Override
    default IntGenerator filter(IntPredicate pred) {
        int[] box = new int[1];
        return action -> {
            while (next(val -> box[0] = val)) {
                if (pred.test(box[0])) {
                    action.accept(box[0]);
                    return true;
                }
            }
            return false;
        };
    }
}
