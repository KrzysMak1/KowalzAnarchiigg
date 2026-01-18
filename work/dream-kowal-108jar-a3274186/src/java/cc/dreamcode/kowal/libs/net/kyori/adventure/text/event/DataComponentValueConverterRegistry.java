package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.Contract;
import java.util.Objects;
import java.util.function.BiFunction;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.stream.Collectors;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.Set;

public final class DataComponentValueConverterRegistry
{
    private static final Set<Provider> PROVIDERS;
    
    private DataComponentValueConverterRegistry() {
    }
    
    public static Set<Key> knownProviders() {
        return (Set<Key>)Collections.unmodifiableSet((Set)DataComponentValueConverterRegistry.PROVIDERS.stream().map(Provider::id).collect(Collectors.toSet()));
    }
    
    @NotNull
    public static <O extends DataComponentValue> O convert(@NotNull final Class<O> target, @NotNull final Key key, @NotNull final DataComponentValue in) {
        if (target.isInstance(in)) {
            return target.cast(in);
        }
        final RegisteredConversion converter = ConversionCache.converter(in.getClass(), target);
        if (converter == null) {
            throw new IllegalArgumentException("There is no data holder converter registered to convert from a " + (Object)in.getClass() + " instance to a " + (Object)target + " (on field " + (Object)key + ")");
        }
        try {
            return (O)converter.conversion.convert(key, in);
        }
        catch (final Exception ex) {
            throw new IllegalStateException("Failed to convert data component value of type " + (Object)in.getClass() + " to type " + (Object)target + " due to an error in a converter provided by " + converter.provider.asString() + "!", (Throwable)ex);
        }
    }
    
    static {
        PROVIDERS = Services.services((Class<? extends Provider>)Provider.class);
    }
    
    @ApiStatus.NonExtendable
    public interface Conversion<I, O> extends Examinable
    {
        @NotNull
        default <I1, O1> Conversion<I1, O1> convert(@NotNull final Class<I1> src, @NotNull final Class<O1> dst, @NotNull final BiFunction<Key, I1, O1> op) {
            return new DataComponentValueConversionImpl<I1, O1>((Class<I1>)Objects.requireNonNull((Object)src, "src"), (Class<O1>)Objects.requireNonNull((Object)dst, "dst"), (java.util.function.BiFunction<Key, I1, O1>)Objects.requireNonNull((Object)op, "op"));
        }
        
        @Contract(pure = true)
        @NotNull
        Class<I> source();
        
        @Contract(pure = true)
        @NotNull
        Class<O> destination();
        
        @NotNull
        O convert(@NotNull final Key key, @NotNull final I input);
    }
    
    static final class ConversionCache
    {
        private static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, RegisteredConversion>> CACHE;
        private static final Map<Class<?>, Set<RegisteredConversion>> CONVERSIONS;
        
        private static Map<Class<?>, Set<RegisteredConversion>> collectConversions() {
            final Map<Class<?>, Set<RegisteredConversion>> collected = (Map<Class<?>, Set<RegisteredConversion>>)new ConcurrentHashMap();
            for (final Provider provider : DataComponentValueConverterRegistry.PROVIDERS) {
                final Key id = (Key)Objects.requireNonNull((Object)provider.id(), () -> "ID of provider " + (Object)provider + " is null");
                for (final Conversion<?, ?> conv : provider.conversions()) {
                    ((Set)collected.computeIfAbsent((Object)conv.source(), $ -> ConcurrentHashMap.newKeySet())).add((Object)new RegisteredConversion(id, conv));
                }
            }
            for (final Map.Entry<Class<?>, Set<RegisteredConversion>> entry : collected.entrySet()) {
                entry.setValue((Object)Collections.unmodifiableSet((Set)entry.getValue()));
            }
            return (Map<Class<?>, Set<RegisteredConversion>>)new ConcurrentHashMap((Map)collected);
        }
        
        static RegisteredConversion compute(final Class<?> src, final Class<?> dst) {
            final Deque<Class<?>> sourceTypes = (Deque<Class<?>>)new ArrayDeque();
            sourceTypes.add((Object)src);
            Class<?> sourcePtr;
            while ((sourcePtr = (Class)sourceTypes.poll()) != null) {
                final Set<RegisteredConversion> conversions = (Set<RegisteredConversion>)ConversionCache.CONVERSIONS.get((Object)sourcePtr);
                if (conversions != null) {
                    RegisteredConversion nearest = null;
                    for (final RegisteredConversion potential : conversions) {
                        final Class<?> potentialDst = potential.conversion.destination();
                        if (dst.equals(potentialDst)) {
                            return potential;
                        }
                        if (!dst.isAssignableFrom(potentialDst)) {
                            continue;
                        }
                        if (nearest != null && !potentialDst.isAssignableFrom(nearest.conversion.destination())) {
                            continue;
                        }
                        nearest = potential;
                    }
                    if (nearest != null) {
                        return nearest;
                    }
                }
                addSupertypes(sourcePtr, sourceTypes);
            }
            return RegisteredConversion.NONE;
        }
        
        private static void addSupertypes(final Class<?> clazz, final Deque<Class<?>> queue) {
            if (clazz.getSuperclass() != null) {
                queue.add((Object)clazz.getSuperclass());
            }
            queue.addAll((Collection)Arrays.asList((Object[])clazz.getInterfaces()));
        }
        
        @Nullable
        static RegisteredConversion converter(final Class<? extends DataComponentValue> src, final Class<? extends DataComponentValue> dst) {
            final RegisteredConversion result = (RegisteredConversion)((ConcurrentMap)ConversionCache.CACHE.computeIfAbsent((Object)src, $ -> new ConcurrentHashMap())).computeIfAbsent((Object)dst, $$ -> compute(src, dst));
            if (result == RegisteredConversion.NONE) {
                return null;
            }
            return result;
        }
        
        static {
            CACHE = (ConcurrentMap)new ConcurrentHashMap();
            CONVERSIONS = collectConversions();
        }
    }
    
    static final class RegisteredConversion
    {
        static final RegisteredConversion NONE;
        final Key provider;
        final Conversion<?, ?> conversion;
        
        RegisteredConversion(final Key provider, final Conversion<?, ?> conversion) {
            this.provider = provider;
            this.conversion = conversion;
        }
        
        static {
            NONE = new RegisteredConversion(null, null);
        }
    }
    
    public interface Provider
    {
        @NotNull
        Key id();
        
        @NotNull
        Iterable<Conversion<?, ?>> conversions();
    }
}
