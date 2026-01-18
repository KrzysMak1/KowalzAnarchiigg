package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ParseUtil;
import java.util.Optional;
import lombok.NonNull;

public class IntegerTransformer implements ObjectTransformer<Integer>
{
    @Override
    public Class<?> getGeneric() {
        return Integer.class;
    }
    
    @Override
    public Optional<Integer> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return ParseUtil.parseInteger(input);
    }
}
