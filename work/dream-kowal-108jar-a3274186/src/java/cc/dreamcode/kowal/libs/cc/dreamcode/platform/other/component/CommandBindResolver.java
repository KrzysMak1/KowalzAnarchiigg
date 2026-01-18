package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandBindResolver implements ComponentClassResolver<BindResolver<?>>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandBindResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<BindResolver<?>> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BindResolver.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-bind-resolver";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final BindResolver<?> bindResolver) {
        if (bindResolver == null) {
            throw new NullPointerException("bindResolver is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public BindResolver<?> resolve(@NonNull final Injector injector, @NonNull final Class<BindResolver<?>> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final BindResolver<?> bindResolver = injector.createInstance(type);
        this.commandProvider.registerBind(bindResolver);
        return bindResolver;
    }
}
