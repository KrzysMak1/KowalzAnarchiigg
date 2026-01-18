package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.CompletionFilter;
import java.util.Optional;
import java.util.Collection;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import java.util.concurrent.atomic.AtomicReference;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Completion;
import lombok.NonNull;

public class SuggestionService
{
    private final SuggestionCache suggestionCache;
    
    public List<String> getSuggestion(@NonNull final Class<?> paramType, @NonNull final Completion completion) {
        if (paramType == null) {
            throw new NullPointerException("paramType is marked non-null but is null");
        }
        if (completion == null) {
            throw new NullPointerException("completion is marked non-null but is null");
        }
        final List<String> suggestions = (List<String>)new ArrayList();
        for (final String value : completion.value()) {
            final String[] split = value.split(" ");
            if (split.length != 0) {
                final String firstValue = split[0];
                final Optional<SuggestionSupplier> optionalSupplier = this.suggestionCache.getSuggestion(firstValue);
                if (!optionalSupplier.isPresent()) {
                    suggestions.add((Object)firstValue);
                }
                else {
                    final SuggestionSupplier supplier = (SuggestionSupplier)optionalSupplier.get();
                    final AtomicReference<List<String>> reference = (AtomicReference<List<String>>)new AtomicReference((Object)supplier.supply(paramType));
                    final CompletionFilter[] filter;
                    final CompletionFilter[] completionFilterArray = filter = completion.filter();
                    for (final CompletionFilter completionFilter : filter) {
                        final Optional<SuggestionFilter> optionalFilter = this.suggestionCache.getSuggestionFilter(completionFilter.name());
                        if (!optionalFilter.isPresent()) {
                            throw new RuntimeException("Cannot resolve suggestion-filter by key: " + completionFilter.name());
                        }
                        final SuggestionFilter suggestionFilter = (SuggestionFilter)optionalFilter.get();
                        reference.set((Object)suggestionFilter.filter((List<String>)reference.get(), completionFilter.value()));
                    }
                    suggestions.addAll((Collection)reference.get());
                }
            }
        }
        return suggestions;
    }
    
    @Generated
    public SuggestionService(final SuggestionCache suggestionCache) {
        this.suggestionCache = suggestionCache;
    }
}
