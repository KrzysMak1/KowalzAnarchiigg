package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class ShortArrayTransformer implements ArrayTransformer<Short>
{
    @Override
    public Class<?> getGeneric() {
        return Short[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Short[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Short[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Short[] array = new Short[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Short)objects[index];
        }
        return (Optional<Short[]>)Optional.of((Object)array);
    }
}
