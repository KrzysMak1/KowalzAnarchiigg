package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class ObjectTransformerResolver implements ComponentClassResolver<ObjectTransformer<?>>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public ObjectTransformerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<ObjectTransformer<?>> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ObjectTransformer.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-object-transformer";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final ObjectTransformer<?> objectTransformer) {
        if (objectTransformer == null) {
            throw new NullPointerException("objectTransformer is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public ObjectTransformer<?> resolve(@NonNull final Injector injector, @NonNull final Class<ObjectTransformer<?>> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final ObjectTransformer<?> objectTransformer = injector.createInstance(type);
        this.commandProvider.registerTransformer(objectTransformer);
        return objectTransformer;
    }
}
