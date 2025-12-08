package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Iterators {
    public static <T> Iterator<T> generate(Supplier<T> next, Predicate<? super T> cond) {
        return generate(() -> {
            T val = next.get();
            return cond.test(val) ? Maybe.present(val) : Maybe.empty();
        });
    }

    public static <T> Iterator<T> generate(Supplier<Maybe<T>> next) {
        return new Iterator<T>() {
            private Maybe<T> look = next.get();

            @Override
            public boolean hasNext() {
                return look.isPresent();
            }

            @Override
            public T next() {
                T curr = look.get();
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
