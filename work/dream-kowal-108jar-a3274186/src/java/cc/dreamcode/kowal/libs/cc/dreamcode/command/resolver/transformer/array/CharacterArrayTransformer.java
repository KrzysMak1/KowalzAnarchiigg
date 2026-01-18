package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;

public class CharacterArrayTransformer implements ArrayTransformer<Character>
{
    @Override
    public Class<?> getGeneric() {
        return Character[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Character[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Character[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Character[] array = new Character[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Character)objects[index];
        }
        return (Optional<Character[]>)Optional.of((Object)array);
    }
}
