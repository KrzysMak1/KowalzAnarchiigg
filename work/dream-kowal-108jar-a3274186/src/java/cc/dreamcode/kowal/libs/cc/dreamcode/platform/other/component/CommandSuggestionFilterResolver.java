package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component.annotation.SuggestionKey;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandSuggestionFilterResolver implements ComponentClassResolver<SuggestionFilter>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandSuggestionFilterResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<SuggestionFilter> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return SuggestionFilter.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "suggestion-filter";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final SuggestionFilter suggestionFilter) {
        if (suggestionFilter == null) {
            throw new NullPointerException("suggestionFilter is marked non-null but is null");
        }
        final SuggestionKey suggestionKey = suggestionFilter.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionFilter must have @SuggestionKey annotation.");
        }
        return (Map<String, Object>)MapBuilder.of("key", suggestionKey.value());
    }
    
    @Override
    public SuggestionFilter resolve(@NonNull final Injector injector, @NonNull final Class<SuggestionFilter> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final SuggestionFilter suggestionFilter = injector.createInstance(type);
        final SuggestionKey suggestionKey = suggestionFilter.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionFilter must have @SuggestionKey annotation.");
        }
        this.commandProvider.registerSuggestionFilter(suggestionKey.value(), suggestionFilter);
        return suggestionFilter;
    }
}
