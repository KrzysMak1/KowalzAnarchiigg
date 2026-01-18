package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ParseUtil;
import java.util.Optional;
import lombok.NonNull;

public class CharacterTransformer implements ObjectTransformer<Character>
{
    @Override
    public Class<?> getGeneric() {
        return Character.class;
    }
    
    @Override
    public Optional<Character> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        if (input.length() != 1) {
            return (Optional<Character>)Optional.empty();
        }
        return ParseUtil.parseChar(input);
    }
}
