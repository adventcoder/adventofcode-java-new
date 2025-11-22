package adventofcode.utils.collect;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class DefaultHashMap<K, V> extends HashMap<K, V> {
    @NonNull
    private final Supplier<? extends V> supplier;

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        V val = super.get(key);
        if (val == null) {
            val = Objects.requireNonNull(supplier.get());
            put((K) key, val);
        }
        return val;
    }
}
