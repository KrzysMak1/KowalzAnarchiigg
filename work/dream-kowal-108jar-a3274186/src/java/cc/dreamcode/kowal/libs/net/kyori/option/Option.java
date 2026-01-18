package cc.dreamcode.kowal.libs.net.kyori.option;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface Option<V>
{
    default Option<Boolean> booleanOption(final String id, final boolean defaultValue) {
        return OptionImpl.option(id, Boolean.class, defaultValue);
    }
    
    default <E extends Enum<E>> Option<E> enumOption(final String id, final Class<E> enumClazz, final E defaultValue) {
        return OptionImpl.option(id, enumClazz, defaultValue);
    }
    
    @NotNull
    String id();
    
    @NotNull
    Class<V> type();
    
    @Nullable
    V defaultValue();
}
