package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.Optional;
import lombok.NonNull;

public class EnumTransformer implements ObjectTransformer<Enum>
{
    @Override
    public Class<?> getGeneric() {
        return Enum.class;
    }
    
    @Override
    public Optional<Enum> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        final Class<? extends Enum> enumClass = (Class<? extends Enum>)type;
        try {
            return (Optional<Enum>)Optional.of((Object)Enum.valueOf((Class)enumClass, input));
        }
        catch (final Exception e) {
            for (final Enum enumConstant : (Enum[])enumClass.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(input)) {
                    return (Optional<Enum>)Optional.of((Object)enumConstant);
                }
            }
            return (Optional<Enum>)Optional.empty();
        }
    }
}
