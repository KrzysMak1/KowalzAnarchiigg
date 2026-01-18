package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;

public class StringTransformer implements ObjectTransformer<String>
{
    @Override
    public Class<?> getGeneric() {
        return String.class;
    }
    
    @Override
    public Optional<String> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return (Optional<String>)Optional.of((Object)input);
    }
}
