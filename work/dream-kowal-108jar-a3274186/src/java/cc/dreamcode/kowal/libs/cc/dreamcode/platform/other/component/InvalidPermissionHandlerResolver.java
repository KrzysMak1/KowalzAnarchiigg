package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class InvalidPermissionHandlerResolver implements ComponentClassResolver<InvalidPermissionHandler>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public InvalidPermissionHandlerResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<InvalidPermissionHandler> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return InvalidPermissionHandler.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-invalid-permission-handler";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final InvalidPermissionHandler invalidPermissionHandler) {
        if (invalidPermissionHandler == null) {
            throw new NullPointerException("invalidPermissionHandler is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public InvalidPermissionHandler resolve(@NonNull final Injector injector, @NonNull final Class<InvalidPermissionHandler> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final InvalidPermissionHandler invalidPermissionHandler = injector.createInstance(type);
        this.commandProvider.setInvalidPermissionHandler(invalidPermissionHandler);
        return invalidPermissionHandler;
    }
}
