package cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion;

import java.util.Optional;
import lombok.NonNull;
import java.util.HashMap;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import java.util.Map;

public class SuggestionCache
{
    private final Map<String, SuggestionSupplier> suggestionMap;
    private final Map<String, SuggestionFilter> suggestionFilterMap;
    
    public SuggestionCache() {
        this.suggestionMap = (Map<String, SuggestionSupplier>)new HashMap();
        this.suggestionFilterMap = (Map<String, SuggestionFilter>)new HashMap();
    }
    
    public Optional<SuggestionSupplier> getSuggestion(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (Optional<SuggestionSupplier>)Optional.ofNullable((Object)this.suggestionMap.get((Object)key));
    }
    
    public Optional<SuggestionFilter> getSuggestionFilter(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (Optional<SuggestionFilter>)Optional.ofNullable((Object)this.suggestionFilterMap.get((Object)key));
    }
    
    public void addSuggestion(@NonNull final String key, @NonNull final SuggestionSupplier supplier) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        this.suggestionMap.put((Object)key, (Object)supplier);
    }
    
    public void addSuggestionFilter(@NonNull final String key, @NonNull final SuggestionFilter suggestionFilter) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (suggestionFilter == null) {
            throw new NullPointerException("suggestionFilter is marked non-null but is null");
        }
        this.suggestionFilterMap.put((Object)key, (Object)suggestionFilter);
    }
    
    public void removeSuggestion(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (!this.suggestionMap.containsKey((Object)key)) {
            return;
        }
        this.suggestionMap.remove((Object)key);
    }
    
    public void removeSuggestionFilter(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (!this.suggestionFilterMap.containsKey((Object)key)) {
            return;
        }
        this.suggestionFilterMap.remove((Object)key);
    }
}
