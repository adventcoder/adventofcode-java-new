package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Iterators {
    public static <T> Iterator<T> generate(Supplier<T> next, Predicate<? super T> cond) {
        return new Iterator<>() {
            private T look = next.get();

            @Override
            public boolean hasNext() {
                return cond.test(look);
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                T curr = look;
                look = next.get();
                return curr;
            }
        };
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
}
