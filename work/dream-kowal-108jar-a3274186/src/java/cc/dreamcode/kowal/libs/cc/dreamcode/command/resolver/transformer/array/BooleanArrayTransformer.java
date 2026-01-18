package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class BooleanArrayTransformer implements ArrayTransformer<Boolean>
{
    @Override
    public Class<?> getGeneric() {
        return Boolean[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Boolean[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Boolean[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Boolean[] array = new Boolean[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Boolean)objects[index];
        }
        return (Optional<Boolean[]>)Optional.of((Object)array);
    }
}
