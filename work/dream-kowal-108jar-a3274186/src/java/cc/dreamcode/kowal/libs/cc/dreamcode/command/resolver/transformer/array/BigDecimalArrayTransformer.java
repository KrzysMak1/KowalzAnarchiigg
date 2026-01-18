package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;
import java.math.BigDecimal;

public class BigDecimalArrayTransformer implements ArrayTransformer<BigDecimal>
{
    @Override
    public Class<?> getGeneric() {
        return BigDecimal[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BigDecimal[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<BigDecimal[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final BigDecimal[] array = new BigDecimal[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (BigDecimal)objects[index];
        }
        return (Optional<BigDecimal[]>)Optional.of((Object)array);
    }
}
