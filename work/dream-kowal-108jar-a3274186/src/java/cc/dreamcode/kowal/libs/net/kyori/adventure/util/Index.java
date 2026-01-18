package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import java.util.EnumMap;
import org.jetbrains.annotations.Contract;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.function.IntFunction;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;
import java.util.Map;

public final class Index<K, V>
{
    private final Map<K, V> keyToValue;
    private final Map<V, K> valueToKey;
    
    private Index(final Map<K, V> keyToValue, final Map<V, K> valueToKey) {
        this.keyToValue = keyToValue;
        this.valueToKey = valueToKey;
    }
    
    @NotNull
    public static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, @NotNull final Function<? super V, ? extends K> keyFunction) {
        return create(type, keyFunction, (V[])type.getEnumConstants());
    }
    
    @SafeVarargs
    @NotNull
    public static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, @NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final V... values) {
        return create(values, (java.util.function.IntFunction<java.util.Map<V, K>>)(length -> new EnumMap(type)), keyFunction);
    }
    
    @SafeVarargs
    @NotNull
    public static <K, V> Index<K, V> create(@NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final V... values) {
        return create(values, (java.util.function.IntFunction<java.util.Map<V, K>>)HashMap::new, keyFunction);
    }
    
    @NotNull
    public static <K, V> Index<K, V> create(@NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final List<V> constants) {
        return create(constants, (java.util.function.IntFunction<java.util.Map<V, K>>)HashMap::new, keyFunction);
    }
    
    @NotNull
    private static <K, V> Index<K, V> create(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, @NotNull final Function<? super V, ? extends K> keyFunction) {
        return create((java.util.List<V>)Arrays.asList((Object[])values), valueToKeyFactory, keyFunction);
    }
    
    @NotNull
    private static <K, V> Index<K, V> create(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, @NotNull final Function<? super V, ? extends K> keyFunction) {
        final int length = values.size();
        final Map<K, V> keyToValue = (Map<K, V>)new HashMap(length);
        final Map<V, K> valueToKey = (Map<V, K>)valueToKeyFactory.apply(length);
        for (int i = 0; i < length; ++i) {
            final V value = (V)values.get(i);
            final K key = (K)keyFunction.apply((Object)value);
            if (keyToValue.putIfAbsent((Object)key, (Object)value) != null) {
                throw new IllegalStateException(String.format("Key %s already mapped to value %s", new Object[] { key, keyToValue.get((Object)key) }));
            }
            if (valueToKey.putIfAbsent((Object)value, (Object)key) != null) {
                throw new IllegalStateException(String.format("Value %s already mapped to key %s", new Object[] { value, valueToKey.get((Object)value) }));
            }
        }
        return new Index<K, V>((java.util.Map<K, V>)Collections.unmodifiableMap((Map)keyToValue), (java.util.Map<V, K>)Collections.unmodifiableMap((Map)valueToKey));
    }
    
    @NotNull
    public Set<K> keys() {
        return (Set<K>)Collections.unmodifiableSet(this.keyToValue.keySet());
    }
    
    @Nullable
    public K key(@NotNull final V value) {
        return (K)this.valueToKey.get((Object)value);
    }
    
    @NotNull
    public K keyOrThrow(@NotNull final V value) {
        final K key = this.key(value);
        if (key == null) {
            throw new NoSuchElementException("There is no key for value " + (Object)value);
        }
        return key;
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    public K keyOr(@NotNull final V value, @Nullable final K defaultKey) {
        final K key = this.key(value);
        return (key == null) ? defaultKey : key;
    }
    
    @NotNull
    public Set<V> values() {
        return (Set<V>)Collections.unmodifiableSet(this.valueToKey.keySet());
    }
    
    @Nullable
    public V value(@NotNull final K key) {
        return (V)this.keyToValue.get((Object)key);
    }
    
    @NotNull
    public V valueOrThrow(@NotNull final K key) {
        final V value = this.value(key);
        if (value == null) {
            throw new NoSuchElementException("There is no value for key " + (Object)key);
        }
        return value;
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    public V valueOr(@NotNull final K key, @Nullable final V defaultValue) {
        final V value = this.value(key);
        return (value == null) ? defaultValue : value;
    }
    
    @NotNull
    public Map<K, V> keyToValue() {
        return (Map<K, V>)Collections.unmodifiableMap((Map)this.keyToValue);
    }
    
    @NotNull
    public Map<V, K> valueToKey() {
        return (Map<V, K>)Collections.unmodifiableMap((Map)this.valueToKey);
    }
}
