package adventofcode.utils.iter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@FunctionalInterface
public interface Enumerable<T> {
    void forEach(Consumer<? super T> action);

    default <A, R> R collect(Collector<? super T, A, R> coll) {
        A acc = coll.supplier().get();
        forEach(t -> coll.accumulator().accept(acc, t));
        return coll.finisher().apply(acc);
    }

    default <U> Enumerable<U> map(Function<? super T, ? extends U> func) {
        return action -> forEach(t -> action.accept(func.apply(t)));
    }

    default IntEnumerable mapToInt(ToIntFunction<? super T> func) {
        return action -> forEach(t -> action.accept(func.applyAsInt(t)));
    }

    default Enumerable<T> filter(Predicate<? super T> pred) {
        return action -> forEach(t -> { if (pred.test(t)) action.accept(t); });
    }

    default long count() {
        return collect(Collectors.counting());
    }

    default <C extends Collection<T>> C toCollection(Supplier<C> generator) {
        C coll = generator.get();
        forEach(coll::add);
        return coll;
    }

    default List<T> toList() {
        return toCollection(ArrayList::new);
    }

    default Set<T> toSet() {
        return toCollection(HashSet::new);
    }

    default T[] toArray(IntFunction<T[]> generator) {
        return toList().toArray(generator);
    }
}
