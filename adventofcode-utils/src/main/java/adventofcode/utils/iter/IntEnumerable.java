package adventofcode.utils.iter;

import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public interface IntEnumerable extends Enumerable<Integer> {
    default void forEach(Consumer<? super Integer> action) {
        forEachInt(val -> action.accept(Integer.valueOf(val)));
    }

    void forEachInt(IntConsumer action);

    default IntEnumerable map(IntUnaryOperator func) {
        return action -> forEach(val -> action.accept(func.applyAsInt(val)));
    }

    default IntEnumerable filter(IntPredicate pred) {
        return action -> forEach(t -> { if (pred.test(t)) action.accept(t); });
    }

    default int reduce(int identity, IntBinaryOperator op) {
        var reducer = new IntConsumer() {
            int acc = identity;

            @Override
            public void accept(int val) {
                acc = op.applyAsInt(acc, val);
            }
        };
        forEachInt(reducer);
        return reducer.acc;
    }

    default int sum() {
        return reduce(0, Integer::sum);
    }

    default int min() {
        return reduce(Integer.MAX_VALUE, Integer::min);
    }

    default int max() {
        return reduce(Integer.MIN_VALUE, Integer::max);
    }

    default <C extends IntCollection> C toIntCollection(Supplier<C> generator) {
        C coll = generator.get();
        forEachInt(coll::add);
        return coll;
    }

    default IntList toIntList() {
        return toIntCollection(IntArrayList::new);
    }

    default IntSet toIntSet() {
        return toIntCollection(IntOpenHashSet::new);
    }

    default int[] toIntArray() {
        return toIntList().toIntArray();
    }
}
