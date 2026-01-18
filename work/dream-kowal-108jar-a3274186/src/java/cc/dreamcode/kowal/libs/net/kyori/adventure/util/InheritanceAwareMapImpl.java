package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;

final class InheritanceAwareMapImpl<C, V> implements InheritanceAwareMap<C, V>
{
    private static final Object NONE;
    static final InheritanceAwareMapImpl EMPTY;
    private final Map<Class<? extends C>, V> declaredValues;
    private final boolean strict;
    private final transient ConcurrentMap<Class<? extends C>, Object> cache;
    
    InheritanceAwareMapImpl(final boolean strict, final Map<Class<? extends C>, V> declaredValues) {
        this.cache = (ConcurrentMap<Class<? extends C>, Object>)new ConcurrentHashMap();
        this.strict = strict;
        this.declaredValues = declaredValues;
    }
    
    @Override
    public boolean containsKey(@NotNull final Class<? extends C> clazz) {
        return this.get(clazz) != null;
    }
    
    @Nullable
    @Override
    public V get(@NotNull final Class<? extends C> clazz) {
        final Object ret = this.cache.computeIfAbsent((Object)clazz, c -> {
            final V value = (V)this.declaredValues.get((Object)c);
            if (value != null) {
                return value;
            }
            for (final Map.Entry<Class<? extends C>, V> entry : this.declaredValues.entrySet()) {
                if (((Class)entry.getKey()).isAssignableFrom(c)) {
                    return entry.getValue();
                }
            }
            return InheritanceAwareMapImpl.NONE;
        });
        return (V)((ret == InheritanceAwareMapImpl.NONE) ? null : ret);
    }
    
    @NotNull
    @Override
    public InheritanceAwareMap<C, V> with(@NotNull final Class<? extends C> clazz, @NotNull final V value) {
        if (Objects.equals(this.declaredValues.get((Object)clazz), (Object)value)) {
            return this;
        }
        if (this.strict) {
            validateNoneInHierarchy(clazz, this.declaredValues);
        }
        final Map<Class<? extends C>, V> newValues = (Map<Class<? extends C>, V>)new LinkedHashMap((Map)this.declaredValues);
        newValues.put((Object)clazz, (Object)value);
        return new InheritanceAwareMapImpl(this.strict, (java.util.Map<Class<?>, Object>)Collections.unmodifiableMap((Map)newValues));
    }
    
    @NotNull
    @Override
    public InheritanceAwareMap<C, V> without(@NotNull final Class<? extends C> clazz) {
        if (!this.declaredValues.containsKey((Object)clazz)) {
            return this;
        }
        final Map<Class<? extends C>, V> newValues = (Map<Class<? extends C>, V>)new LinkedHashMap((Map)this.declaredValues);
        newValues.remove((Object)clazz);
        return new InheritanceAwareMapImpl(this.strict, (java.util.Map<Class<?>, Object>)Collections.unmodifiableMap((Map)newValues));
    }
    
    private static void validateNoneInHierarchy(final Class<?> beingRegistered, final Map<? extends Class<?>, ?> entries) {
        for (final Class<?> clazz : entries.keySet()) {
            testHierarchy(clazz, beingRegistered);
        }
    }
    
    private static void testHierarchy(final Class<?> existing, final Class<?> beingRegistered) {
        if (!existing.equals(beingRegistered) && (existing.isAssignableFrom(beingRegistered) || beingRegistered.isAssignableFrom(existing))) {
            throw new IllegalArgumentException("Conflict detected between already registered type " + (Object)existing + " and newly registered type " + (Object)beingRegistered + "! Types in a strict inheritance-aware map must not share a common hierarchy!");
        }
    }
    
    static {
        NONE = new Object();
        EMPTY = new InheritanceAwareMapImpl(false, (Map<Class<? extends C>, V>)Collections.emptyMap());
    }
    
    static final class BuilderImpl<C, V> implements Builder<C, V>
    {
        private boolean strict;
        private final Map<Class<? extends C>, V> values;
        
        BuilderImpl() {
            this.values = (Map<Class<? extends C>, V>)new LinkedHashMap();
        }
        
        @NotNull
        @Override
        public InheritanceAwareMap<C, V> build() {
            return new InheritanceAwareMapImpl<C, V>(this.strict, (java.util.Map<Class<? extends C>, V>)Collections.unmodifiableMap((Map)new LinkedHashMap((Map)this.values)));
        }
        
        @NotNull
        @Override
        public Builder<C, V> strict(final boolean strict) {
            if (strict && !this.strict) {
                for (final Class<? extends C> clazz : this.values.keySet()) {
                    validateNoneInHierarchy(clazz, this.values);
                }
            }
            this.strict = strict;
            return this;
        }
        
        @NotNull
        @Override
        public Builder<C, V> put(@NotNull final Class<? extends C> clazz, @NotNull final V value) {
            if (this.strict) {
                validateNoneInHierarchy(clazz, this.values);
            }
            this.values.put((Object)Objects.requireNonNull((Object)clazz, "clazz"), Objects.requireNonNull((Object)value, "value"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder<C, V> remove(@NotNull final Class<? extends C> clazz) {
            this.values.remove(Objects.requireNonNull((Object)clazz, "clazz"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder<C, V> putAll(@NotNull final InheritanceAwareMap<? extends C, ? extends V> map) {
            final InheritanceAwareMapImpl<?, V> impl = (InheritanceAwareMapImpl)map;
            if (this.strict && (!this.values.isEmpty() || !((InheritanceAwareMapImpl<Object, Object>)impl).strict)) {
                for (final Map.Entry<? extends Class<?>, V> entry : ((InheritanceAwareMapImpl<Object, Object>)impl).declaredValues.entrySet()) {
                    validateNoneInHierarchy((Class)entry.getKey(), this.values);
                    this.values.put((Object)entry.getKey(), entry.getValue());
                }
                return this;
            }
            this.values.putAll(((InheritanceAwareMapImpl<Object, Object>)impl).declaredValues);
            return this;
        }
    }
}
