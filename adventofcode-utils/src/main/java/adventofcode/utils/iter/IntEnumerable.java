package adventofcode.utils.iter;

import java.util.NoSuchElementException;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;

public interface IntEnumerable {
    void forEach(IntConsumer action);

    default IntEnumerable map(IntUnaryOperator func) {
        return action -> forEach(val -> action.accept(func.applyAsInt(val)));
    }

    default <T> Enumerable<T> mapToObj(IntFunction<? extends T> func) {
        return action -> forEach(val -> action.accept(func.apply(val)));
    }

    default IntEnumerable filter(IntPredicate pred) {
        return action -> forEach(t -> { if (pred.test(t)) action.accept(t); });
    }

    default int reduce(int identity, IntBinaryOperator op) {
        int[] acc = { identity };
        forEach(t -> acc[0] = op.applyAsInt(acc[0], t));
        return acc[0];
    }

    default int reduce(IntBinaryOperator op) {
        IntList acc = new IntArrayList(1);
        forEach(t -> {
            if (acc.isEmpty())
                acc.add(t);
            else
                acc.set(0, op.applyAsInt(acc.getInt(0), t));
        });
        if (acc.isEmpty())
            throw new NoSuchElementException();
        return acc.getInt(0);
    }

    default int sum() {
        return reduce(0, Integer::sum);
    }

    default int min() {
        return reduce(Integer::min);
    }

    default int max() {
        return reduce(Integer::max);
    }

    default <C extends IntCollection> C toCollection(Supplier<C> generator) {
        C coll = generator.get();
        forEach(coll::add);
        return coll;
    }

    default IntList toList() {
        return toCollection(IntArrayList::new);
    }

    default int[] toArray() {
        return toList().toIntArray();
    }
}
