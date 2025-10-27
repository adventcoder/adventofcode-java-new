package adventofcode.utils.collect;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultHashMap<K, V> extends HashMap<K, V> {
    private final Function<? super K, ? extends V> defaultMapping;

    public static <K, V> DefaultHashMap<K, V> ofSupplier(Supplier<? extends V> defaultSupplier) {
        return new DefaultHashMap<>(k -> defaultSupplier.get());
    }

    public V computeIfAbsent(K key) {
        return computeIfAbsent(key, defaultMapping);
    }
}
