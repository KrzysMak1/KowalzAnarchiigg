package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class IntegerArrayTransformer implements ArrayTransformer<Integer>
{
    @Override
    public Class<?> getGeneric() {
        return Integer[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Integer[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Integer[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Integer[] array = new Integer[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Integer)objects[index];
        }
        return (Optional<Integer[]>)Optional.of((Object)array);
    }
}
