package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class InvalidInputHandlerResolver implements ComponentClassResolver<InvalidInputHandler>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public InvalidInputHandlerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<InvalidInputHandler> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return InvalidInputHandler.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-invalid-input-handler";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final InvalidInputHandler invalidInputHandler) {
        if (invalidInputHandler == null) {
            throw new NullPointerException("invalidInputHandler is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public InvalidInputHandler resolve(@NonNull final Injector injector, @NonNull final Class<InvalidInputHandler> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final InvalidInputHandler invalidInputHandler = injector.createInstance(type);
        this.commandProvider.setInvalidInputHandler(invalidInputHandler);
        return invalidInputHandler;
    }
}
