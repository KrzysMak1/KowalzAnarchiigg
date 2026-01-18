package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class InvalidUsageHandlerResolver implements ComponentClassResolver<InvalidUsageHandler>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public InvalidUsageHandlerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<InvalidUsageHandler> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return InvalidUsageHandler.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-invalid-usage-handler";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final InvalidUsageHandler invalidUsageHandler) {
        if (invalidUsageHandler == null) {
            throw new NullPointerException("invalidUsageHandler is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public InvalidUsageHandler resolve(@NonNull final Injector injector, @NonNull final Class<InvalidUsageHandler> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final InvalidUsageHandler invalidUsageHandler = injector.createInstance(type);
        this.commandProvider.setInvalidUsageHandler(invalidUsageHandler);
        return invalidUsageHandler;
    }
}
