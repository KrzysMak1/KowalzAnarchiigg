package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component.annotation.SuggestionKey;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandSuggestionResolver implements ComponentClassResolver<SuggestionSupplier>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandSuggestionResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<SuggestionSupplier> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return SuggestionSupplier.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "suggestion-supplier";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final SuggestionSupplier suggestionSupplier) {
        if (suggestionSupplier == null) {
            throw new NullPointerException("suggestionSupplier is marked non-null but is null");
        }
        final SuggestionKey suggestionKey = suggestionSupplier.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionSupplier must have @SuggestionKey annotation.");
        }
        return (Map<String, Object>)MapBuilder.of("key", suggestionKey.value());
    }
    
    @Override
    public SuggestionSupplier resolve(@NonNull final Injector injector, @NonNull final Class<SuggestionSupplier> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final SuggestionSupplier suggestionSupplier = injector.createInstance(type);
        final SuggestionKey suggestionKey = suggestionSupplier.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionSupplier must have @SuggestionKey annotation.");
        }
        this.commandProvider.registerSuggestion(suggestionKey.value(), suggestionSupplier);
        return suggestionSupplier;
    }
}
