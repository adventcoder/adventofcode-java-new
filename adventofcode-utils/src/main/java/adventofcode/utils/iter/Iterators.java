package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Iterators {
    public static <T> Iterator<T> generate(Supplier<T> next, Predicate<? super T> cond) {
        return generate(action -> {
            T val = next.get();
            if (cond.test(val)) {
                action.accept(val);
                return true;
            }
            return false;
        });
    }

    public static <T> Iterator<T> generate(Predicate<? super Consumer<? super T>> tryAdvance) {
        class Itr implements Iterator<T>, Consumer<T> {
            private T look;
            private boolean hasNext = tryAdvance.test(this);

            @Override
            public void accept(T next) {
                this.look = next;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
                if (hasNext)
                    throw new NoSuchElementException();
                T curr = look;
                hasNext = tryAdvance.test(this);
                return curr;
            }
        };
        return new Itr();
    }

    public static <T, U> Iterator<U> map(Iterator<T> it, Function<? super T, ? extends U> func) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public U next() {
                return func.apply(it.next());
            }
        };
    }

    public static <T> Iterator<T> filter(Iterator<T> it, Predicate<? super T> pred) {
        return generate(action -> {
            while (it.hasNext()) {
                T curr = it.next();
                if (pred.test(curr)) {
                    action.accept(curr);
                    return true;
                }
            }
            return false;
        });
    }
}
