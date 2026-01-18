package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class InvalidSenderHandlerResolver implements ComponentClassResolver<InvalidSenderHandler>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public InvalidSenderHandlerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<InvalidSenderHandler> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return InvalidSenderHandler.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-invalid-sender-handler";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final InvalidSenderHandler invalidSenderHandler) {
        if (invalidSenderHandler == null) {
            throw new NullPointerException("invalidSenderHandler is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public InvalidSenderHandler resolve(@NonNull final Injector injector, @NonNull final Class<InvalidSenderHandler> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final InvalidSenderHandler invalidSenderHandler = injector.createInstance(type);
        this.commandProvider.setInvalidSenderHandler(invalidSenderHandler);
        return invalidSenderHandler;
    }
}
