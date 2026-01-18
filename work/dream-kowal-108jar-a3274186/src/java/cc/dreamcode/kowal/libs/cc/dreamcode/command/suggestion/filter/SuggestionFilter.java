package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter;

import lombok.NonNull;
import java.util.List;

public interface SuggestionFilter
{
    List<String> filter(@NonNull final List<String> suggestions, @NonNull final String data);
}
