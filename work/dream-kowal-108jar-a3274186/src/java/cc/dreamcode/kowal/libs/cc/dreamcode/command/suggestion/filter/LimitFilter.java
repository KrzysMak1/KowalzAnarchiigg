package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter;

import java.util.Optional;
import java.util.stream.Collectors;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidInputException;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ParseUtil;
import lombok.NonNull;
import java.util.List;

public class LimitFilter implements SuggestionFilter
{
    @Override
    public List<String> filter(@NonNull final List<String> suggestions, @NonNull final String data) {
        if (suggestions == null) {
            throw new NullPointerException("suggestions is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        final Optional<Integer> optionalInteger = ParseUtil.parseInteger(data);
        if (!optionalInteger.isPresent()) {
            throw new InvalidInputException(Integer.class, data, "Limit value is not Integer");
        }
        final int limit = (int)optionalInteger.get();
        return (List<String>)suggestions.stream().limit((long)limit).collect(Collectors.toList());
    }
}
