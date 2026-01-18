package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class DoubleArrayTransformer implements ArrayTransformer<Double>
{
    @Override
    public Class<?> getGeneric() {
        return Double[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Double[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Double[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Double[] array = new Double[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Double)objects[index];
        }
        return (Optional<Double[]>)Optional.of((Object)array);
    }
}
