package adventofcode.utils.collect;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

// A version of HashMap that doesn't throw ConcurrentModificationException on compute methods.
public class ReentrantHashMap<K, V> extends HashMap<K, V> {
    @Override
    public V computeIfAbsent(K k, Function<? super K, ? extends V> mappingFunction) {
        V v = get(k);
        if (v == null) {
            v = mappingFunction.apply(k);
            if (v != null)
                put(k, v);
        }
        return v;
    }

    @Override
    public V computeIfPresent(K k, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V v = get(k);
        if (v != null) {
            v = remappingFunction.apply(k, v);
            if (v == null)
                remove(k);
            else
                put(k, v);
        }
        return v;
    }

    @Override
    public V compute(K k, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V v = remappingFunction.apply(k, get(k));
        if (v == null)
            remove(k);
        else
            put(k, v);
        return v;
    }

    @Override
    public V merge(K k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V oldV = get(k);
        if (oldV == null) {
            put(k, Objects.requireNonNull(v));
        } else {
            v = remappingFunction.apply(oldV, Objects.requireNonNull(v));
            if (v == null)
                remove(k);
            else
                put(k, v);
        }
        return v;
    }
}
