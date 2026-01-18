package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;

public class EnumSupplier implements SuggestionSupplier
{
    @Override
    public List<String> supply(@NonNull final Class<?> paramType) {
        if (paramType == null) {
            throw new NullPointerException("paramType is marked non-null but is null");
        }
        if (!Enum.class.isAssignableFrom(paramType)) {
            throw new RuntimeException("Parameter is not Enum class (" + paramType.getSimpleName() + ")");
        }
        final Class<? extends Enum> enumClass = (Class<? extends Enum>)paramType;
        return (List<String>)Arrays.stream((Object[])enumClass.getEnumConstants()).map(scan -> scan.name().toLowerCase()).collect(Collectors.toList());
    }
}
