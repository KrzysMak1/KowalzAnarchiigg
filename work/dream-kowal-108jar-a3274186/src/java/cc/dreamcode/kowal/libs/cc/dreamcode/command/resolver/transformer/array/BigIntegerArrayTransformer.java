package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;
import java.math.BigInteger;

public class BigIntegerArrayTransformer implements ArrayTransformer<BigInteger>
{
    @Override
    public Class<?> getGeneric() {
        return BigInteger[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BigInteger[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<BigInteger[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final BigInteger[] array = new BigInteger[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (BigInteger)objects[index];
        }
        return (Optional<BigInteger[]>)Optional.of((Object)array);
    }
}
