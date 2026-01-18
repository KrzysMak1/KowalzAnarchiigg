package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Command;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandBase;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandBaseResolver implements ComponentClassResolver<CommandBase>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandBaseResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<CommandBase> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return CommandBase.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "command";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final CommandBase commandBase) {
        if (commandBase == null) {
            throw new NullPointerException("commandBase is marked non-null but is null");
        }
        final Command command = commandBase.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new RuntimeException("Cannot find @Command annotation in class " + commandBase.getClass().getSimpleName());
        }
        return (Map<String, Object>)new MapBuilder<String, String>().put("name", command.name()).put("aliases", StringUtil.join(command.aliases(), ", ")).build();
    }
    
    @Override
    public CommandBase resolve(@NonNull final Injector injector, @NonNull final Class<CommandBase> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final CommandBase commandBase = injector.createInstance(type);
        this.commandProvider.register(commandBase);
        return commandBase;
    }
}
