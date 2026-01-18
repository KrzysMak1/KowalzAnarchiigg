package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder;

import lombok.NonNull;
import java.util.Arrays;
import java.util.WeakHashMap;
import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V>
{
    private final Map<K, V> map;
    
    public MapBuilder() {
        this.map = (Map<K, V>)new HashMap();
    }
    
    public MapBuilder(final boolean weak) {
        if (weak) {
            this.map = (Map<K, V>)new WeakHashMap();
        }
        else {
            this.map = (Map<K, V>)new HashMap();
        }
    }
    
    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<K, V>();
    }
    
    public static <K, V> MapBuilder<K, V> builder(final boolean weak) {
        return new MapBuilder<K, V>(weak);
    }
    
    public static <K, V> Map<K, V> of() {
        return (Map<K, V>)new HashMap();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1) {
        return new MapBuilder<K, V>().put(k1, v1).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).build();
    }
    
    public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return new MapBuilder<K, V>().put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).build();
    }
    
    public static <K, V> Map<K, V> ofWeak() {
        return (Map<K, V>)new WeakHashMap();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1) {
        return new MapBuilder<K, V>(true).put(k1, v1).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).build();
    }
    
    public static <K, V> Map<K, V> ofWeak(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {
        return new MapBuilder<K, V>(true).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).put(k6, v6).put(k7, v7).put(k8, v8).build();
    }
    
    public MapBuilder<K, V> put(final K k, final V v) {
        this.map.put((Object)k, (Object)v);
        return this;
    }
    
    public MapBuilder<K, V> put(final K[] array, final V v) {
        Arrays.stream((Object[])array).forEach(k -> this.put(k, v));
        return this;
    }
    
    public MapBuilder<K, V> putAll(@NonNull final Map<? extends K, ? extends V> map) {
        if (map == null) {
            throw new NullPointerException("map is marked non-null but is null");
        }
        this.map.putAll((Map)map);
        return this;
    }
    
    public Map<K, V> build() {
        return this.map;
    }
}
