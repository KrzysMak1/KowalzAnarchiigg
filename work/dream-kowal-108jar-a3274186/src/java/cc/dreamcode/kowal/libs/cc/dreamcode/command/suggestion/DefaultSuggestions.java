package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.EnumSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.LimitFilter;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandExtension;

public class DefaultSuggestions implements CommandExtension
{
    @Override
    public void register(@NonNull final CommandProvider commandProvider) {
        if (commandProvider == null) {
            throw new NullPointerException("commandProvider is marked non-null but is null");
        }
        commandProvider.registerSuggestionFilter("limit", new LimitFilter());
        commandProvider.registerSuggestion("@enum", new EnumSupplier());
    }
}
