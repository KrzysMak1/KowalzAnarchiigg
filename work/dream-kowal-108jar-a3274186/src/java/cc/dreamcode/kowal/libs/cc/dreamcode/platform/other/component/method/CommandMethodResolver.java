package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component.method;

import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandContext;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandPathMeta;
import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandMeta;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandBase;
import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component.annotation.SingleCommand;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentMethodResolver;

public class CommandMethodResolver implements ComponentMethodResolver<SingleCommand>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandMethodResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<SingleCommand> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return SingleCommand.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "command";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final SingleCommand singleCommand) {
        if (singleCommand == null) {
            throw new NullPointerException("singleCommand is marked non-null but is null");
        }
        return (Map<String, Object>)new MapBuilder<String, String>().put("name", singleCommand.name()).put("aliases", StringUtil.join(singleCommand.aliases(), ", ")).build();
    }
    
    @Override
    public void apply(@NonNull final Injector injector, @NonNull final SingleCommand singleCommand, @NonNull final Method method, @NonNull final Object instance) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (singleCommand == null) {
            throw new NullPointerException("singleCommand is marked non-null but is null");
        }
        if (method == null) {
            throw new NullPointerException("method is marked non-null but is null");
        }
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        final CommandBase commandBase = new CommandBase() {
            @Override
            public List<CommandPathMeta> getCommandPaths(@NonNull final CommandMeta commandMeta) {
                if (commandMeta == null) {
                    throw new NullPointerException("commandMeta is marked non-null but is null");
                }
                method.setAccessible(true);
                final Executor executor = (Executor)method.getAnnotation((Class)Executor.class);
                if (executor == null) {
                    throw new RuntimeException("Executor annotation not found");
                }
                return ListBuilder.of(new CommandPathMeta(commandMeta, method, executor));
            }
        };
        final CommandContext commandContext = new CommandContext(singleCommand.name(), singleCommand.aliases(), singleCommand.description());
        this.commandProvider.register(commandContext, commandBase, instance);
    }
}
