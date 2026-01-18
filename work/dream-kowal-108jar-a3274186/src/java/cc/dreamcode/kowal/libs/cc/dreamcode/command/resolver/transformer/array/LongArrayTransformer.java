package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class LongArrayTransformer implements ArrayTransformer<Long>
{
    @Override
    public Class<?> getGeneric() {
        return Long[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Long[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Long[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Long[] array = new Long[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Long)objects[index];
        }
        return (Optional<Long[]>)Optional.of((Object)array);
    }
}
