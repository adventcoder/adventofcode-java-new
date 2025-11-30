package adventofcode.utils.iter;

import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntEnumerable extends Enumerable<Integer> {
    default void forEach(Consumer<? super Integer> action) {
        forEachInt(val -> action.accept(Integer.valueOf(val)));
    }

    void forEachInt(IntConsumer action);

    default IntEnumerable mapInt(IntUnaryOperator func) {
        return action -> forEach(val -> action.accept(func.applyAsInt(val)));
    }

    default IntEnumerable filterInt(IntPredicate pred) {
        return action -> forEach(t -> { if (pred.test(t)) action.accept(t); });
    }

    default int reduceInt(int identity, IntBinaryOperator op) {
        var action = new IntConsumer() {
            int acc = identity;

            @Override
            public void accept(int val) {
                acc = op.applyAsInt(acc, val);
            }
        };
        forEachInt(action);
        return action.acc;
    }

    default int sum() {
        return reduceInt(0, Integer::sum);
    }
}
