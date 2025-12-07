package adventofcode.utils.iter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
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

    default long count() {
        return collect(Collectors.counting());
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

    default T reduce(T identity, BinaryOperator<T> op) {
        return collect(Collectors.reducing(identity, op));
    }

    default T reduce(BinaryOperator<T> op) {
        return collect(Collectors.reducing(op)).orElseThrow();
    }

    default T minBy(Comparator<? super T> cmp) {
        return collect(Collectors.minBy(cmp)).orElseThrow();
    }

    default T maxBy(Comparator<? super T> cmp) {
        return collect(Collectors.maxBy(cmp)).orElseThrow();
    }

    default <C extends Collection<T>> C toCollection(Supplier<C> generator) {
        C coll = generator.get();
        forEach(coll::add);
        return coll;
    }

    default List<T> toList() {
        return toCollection(ArrayList::new);
    }

    default <E> E[] toArray(IntFunction<E[]> generator) {
        return toList().toArray(generator);
    }

    // default Iterator<T> iterator() {
    //     Object NIL = new Object();
    //     Fiber<Object, Object> fiber = new Fiber<>(self -> ignored -> {
    //         forEach(i -> self.yield(i));
    //         return NIL;
    //     });
    //     return Iterators.generate(() -> (T) fiber.resume(NIL), o -> o != NIL);
    // }
}
