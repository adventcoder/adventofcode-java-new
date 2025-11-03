package adventofcode.utils.collect;

import java.util.function.Consumer;
import java.util.stream.Collector;

@FunctionalInterface
public interface Traversable<T> {

    void forEach(Consumer<? super T> action);

    default <A, R> R collect(Collector<? super T, A, R> coll) {
        A acc = coll.supplier().get();
        forEach(t -> coll.accumulator().accept(acc, t));
        return coll.finisher().apply(acc);
    }
}
