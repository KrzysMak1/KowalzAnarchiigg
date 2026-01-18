package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class FloatArrayTransformer implements ArrayTransformer<Float>
{
    @Override
    public Class<?> getGeneric() {
        return Float[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Float[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Float[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Float[] array = new Float[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Float)objects[index];
        }
        return (Optional<Float[]>)Optional.of((Object)array);
    }
}
