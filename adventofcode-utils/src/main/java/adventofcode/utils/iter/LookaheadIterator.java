package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface LookaheadIterator<T> extends Iterator<T> {
    T peek(); // the behavior when there are no more elements is defined by the iterator

    static <T> LookaheadIterator<T> iterate(Supplier<T> supplier, Predicate<? super T> pred) {
        return new LookaheadIterator<T>() {
            private T next = supplier.get();

            @Override
            public T peek() {
                return next;
            }

            @Override
            public boolean hasNext() {
                return pred.test(next);
            }

            @Override
            public T next() {
                if (!pred.test(next))
                    throw new NoSuchElementException();
                T curr = next;
                next = supplier.get();
                return curr;
            }
        };
    }

    static <T> LookaheadIterator<T> iterate(BooleanSupplier findNext, Supplier<T> get) {
        return new LookaheadIterator<T>() {
            private boolean hasNext = findNext.getAsBoolean();

            @Override
            public T peek() {
                return get.get();
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (!hasNext)
                    throw new NoSuchElementException();
                T curr = get.get();
                hasNext = findNext.getAsBoolean();
                return curr;
            }
        };
    }
}
