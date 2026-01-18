package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;

public interface ObjectTransformer<T>
{
    Class<?> getGeneric();
    
    default boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return this.getGeneric().isAssignableFrom(type);
    }
    
    Optional<T> transform(@NonNull final Class<?> type, @NonNull final String input);
}
