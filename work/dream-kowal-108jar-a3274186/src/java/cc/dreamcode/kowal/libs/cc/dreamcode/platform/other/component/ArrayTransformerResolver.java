package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class ArrayTransformerResolver implements ComponentClassResolver<ArrayTransformer<?>>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public ArrayTransformerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<ArrayTransformer<?>> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ArrayTransformer.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-array-transformer";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final ArrayTransformer<?> arrayTransformer) {
        if (arrayTransformer == null) {
            throw new NullPointerException("arrayTransformer is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public ArrayTransformer<?> resolve(@NonNull final Injector injector, @NonNull final Class<ArrayTransformer<?>> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final ArrayTransformer<?> arrayTransformer = injector.createInstance(type);
        this.commandProvider.registerTransformer(arrayTransformer);
        return arrayTransformer;
    }
}
