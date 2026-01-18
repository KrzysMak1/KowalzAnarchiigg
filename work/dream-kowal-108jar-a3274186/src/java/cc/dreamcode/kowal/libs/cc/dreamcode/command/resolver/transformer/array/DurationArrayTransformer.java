package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array;

import java.util.Optional;
import lombok.NonNull;
import java.time.Duration;

public class DurationArrayTransformer implements ArrayTransformer<Duration>
{
    @Override
    public Class<?> getGeneric() {
        return Duration[].class;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Duration[].class.isAssignableFrom(type);
    }
    
    @Override
    public Optional<Duration[]> transform(@NonNull final Class<?> type, @NonNull final Object[] objects) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (objects == null) {
            throw new NullPointerException("objects is marked non-null but is null");
        }
        final Duration[] array = new Duration[objects.length];
        for (int index = 0; index < objects.length; ++index) {
            array[index] = (Duration)objects[index];
        }
        return (Optional<Duration[]>)Optional.of((Object)array);
    }
}
