package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public interface ArrayTransformer<T>
{
    Class<?> getGeneric();
    
    boolean isAssignableFrom(@NonNull final Class<?> type);
    
    Optional<T[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects);
}
