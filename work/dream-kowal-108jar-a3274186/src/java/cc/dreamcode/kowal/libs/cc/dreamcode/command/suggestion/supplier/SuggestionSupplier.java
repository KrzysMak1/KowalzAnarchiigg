package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier;

import java.util.List;
import lombok.NonNull;

public interface SuggestionSupplier
{
    List<String> supply(@NonNull final Class<?> paramType);
}
