package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class ByteArrayTransformer implements ArrayTransformer<Byte>
{
    @Override
    public Class<?> getGeneric() {
        return Byte[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Byte[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Byte[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Byte[] array = new Byte[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Byte)objects[index];
        }
        return (Optional<Byte[]>)Optional.of((Object)array);
    }
}
