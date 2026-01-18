package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class StringArrayTransformer implements ArrayTransformer<String>
{
    @Override
    public Class<?> getGeneric() {
        return String[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return String[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<String[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final String[] array = new String[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (String)objects[index];
        }
        return (Optional<String[]>)Optional.of((Object)array);
    }
}
