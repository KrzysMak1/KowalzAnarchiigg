package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandExtension;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandExtensionResolver implements ComponentClassResolver<CommandExtension>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandExtensionResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<CommandExtension> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return CommandExtension.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "command-extension";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final CommandExtension commandExtension) {
        if (commandExtension == null) {
            throw new NullPointerException("commandExtension is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public CommandExtension resolve(@NonNull final Injector injector, @NonNull final Class<CommandExtension> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final CommandExtension commandExtension = injector.createInstance(type);
        this.commandProvider.registerExtension(commandExtension);
        return commandExtension;
    }
}
