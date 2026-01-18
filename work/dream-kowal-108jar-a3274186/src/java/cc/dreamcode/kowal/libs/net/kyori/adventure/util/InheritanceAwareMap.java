package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public interface InheritanceAwareMap<C, V>
{
    @NotNull
    default <K, E> InheritanceAwareMap<K, E> empty() {
        return InheritanceAwareMapImpl.EMPTY;
    }
    
    default <K, E> Builder<K, E> builder() {
        return new InheritanceAwareMapImpl.BuilderImpl<K, E>();
    }
    
    default <K, E> Builder<K, E> builder(final InheritanceAwareMap<? extends K, ? extends E> existing) {
        return new InheritanceAwareMapImpl.BuilderImpl<K, E>().putAll(existing);
    }
    
    boolean containsKey(@NotNull final Class<? extends C> clazz);
    
    @Nullable
    V get(@NotNull final Class<? extends C> clazz);
    
    @CheckReturnValue
    @NotNull
    InheritanceAwareMap<C, V> with(@NotNull final Class<? extends C> clazz, @NotNull final V value);
    
    @CheckReturnValue
    @NotNull
    InheritanceAwareMap<C, V> without(@NotNull final Class<? extends C> clazz);
    
    public interface Builder<C, V> extends AbstractBuilder<InheritanceAwareMap<C, V>>
    {
        @NotNull
        Builder<C, V> strict(final boolean strict);
        
        @NotNull
        Builder<C, V> put(@NotNull final Class<? extends C> clazz, @NotNull final V value);
        
        @NotNull
        Builder<C, V> remove(@NotNull final Class<? extends C> clazz);
        
        @NotNull
        Builder<C, V> putAll(@NotNull final InheritanceAwareMap<? extends C, ? extends V> map);
    }
}
