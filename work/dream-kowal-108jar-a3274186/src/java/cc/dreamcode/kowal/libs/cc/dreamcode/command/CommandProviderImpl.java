package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import java.util.stream.Stream;
import java.util.Objects;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Command;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidSenderException;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidPermissionException;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidInputException;
import java.util.Optional;
import java.util.stream.Collectors;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidUsageException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.DefaultSuggestions;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.DefaultTransformers;
import java.util.HashMap;
import java.util.Map;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.SuggestionService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.SuggestionCache;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultCache;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.ResolverCache;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindCache;

public class CommandProviderImpl implements CommandProvider
{
    private final BindCache bindCache;
    private final BindService bindService;
    private final ResolverCache resolverCache;
    private final ResolverService resolverService;
    private final ResultCache resultCache;
    private final ResultService resultService;
    private final SuggestionCache suggestionCache;
    private final SuggestionService suggestionService;
    private CommandRegistry commandRegistry;
    private CommandScheduler commandScheduler;
    private InvalidPermissionHandler invalidPermissionHandler;
    private InvalidSenderHandler invalidSenderHandler;
    private InvalidUsageHandler invalidUsageHandler;
    private InvalidInputHandler invalidInputHandler;
    private final Map<String, CommandMeta> commandMap;
    
    public CommandProviderImpl(final boolean registerDefaults) {
        this.commandMap = (Map<String, CommandMeta>)new HashMap();
        this.bindCache = new BindCache();
        this.bindService = new BindService(this.bindCache);
        this.resolverCache = new ResolverCache();
        this.resolverService = new ResolverService(this.resolverCache);
        this.resultCache = new ResultCache();
        this.resultService = new ResultService(this.resultCache);
        this.suggestionCache = new SuggestionCache();
        this.suggestionService = new SuggestionService(this.suggestionCache);
        this.commandScheduler = (runnable -> {
            throw new RuntimeException("Cannot invoke async method without async command-scheduler implementation");
        });
        if (registerDefaults) {
            this.registerExtension(new DefaultTransformers());
            this.registerExtension(new DefaultSuggestions());
        }
    }
    
    @Override
    public List<String> getSuggestion(@NonNull final DreamSender<?> sender, @NonNull final String input) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return this.getSuggestion(sender, new CommandInput(input));
    }
    
    @Override
    public List<String> getSuggestion(@NonNull final DreamSender<?> sender, @NonNull final CommandInput commandInput) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        return (List<String>)this.commandMap.entrySet().stream().filter(entry -> commandInput.getLabel().equalsIgnoreCase((String)entry.getKey())).map(entry -> ((CommandMeta)entry.getValue()).getSuggestion(sender, commandInput)).findAny().orElse((Object)new ArrayList());
    }
    
    @Override
    public CommandProviderImpl call(@NonNull final DreamSender<?> dreamSender, @NonNull final String input) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return this.call(dreamSender, new CommandInput(input));
    }
    
    @Override
    public CommandProviderImpl call(@NonNull final DreamSender<?> dreamSender, @NonNull final CommandInput commandInput) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        try {
            final Optional<CommandMeta> optionalCommandMeta = (Optional<CommandMeta>)this.commandMap.entrySet().stream().filter(entry -> commandInput.getLabel().equalsIgnoreCase((String)entry.getKey())).map(Map.Entry::getValue).findAny();
            if (!optionalCommandMeta.isPresent()) {
                throw new InvalidUsageException(null, commandInput, "Cannot find any method with input: " + Arrays.toString((Object[])commandInput.getParams()));
            }
            final CommandMeta commandMeta = (CommandMeta)optionalCommandMeta.get();
            Optional<CommandPathMeta> optionalCommandPathMeta;
            try {
                optionalCommandPathMeta = (Optional<CommandPathMeta>)commandMeta.findExecutor(commandInput, true).findFirst();
            }
            catch (final InvalidInputException e) {
                final List<CommandPathMeta> commandPathMetas = (List<CommandPathMeta>)commandMeta.findExecutor(commandInput, false).collect(Collectors.toList());
                if (commandPathMetas.isEmpty()) {
                    throw e;
                }
                optionalCommandPathMeta = (Optional<CommandPathMeta>)Optional.of((Object)commandPathMetas.get(0));
            }
            if (!optionalCommandPathMeta.isPresent()) {
                throw new InvalidUsageException(commandMeta, commandInput, "Cannot find any path with input: " + Arrays.toString((Object[])commandInput.getParams()));
            }
            final CommandPathMeta commandPathMeta = (CommandPathMeta)optionalCommandPathMeta.get();
            final CommandExecutor commandExecutor = commandPathMeta.getCommandExecutor();
            commandExecutor.execute(this.commandScheduler, this.resolverService, this.bindService, this.resultService, dreamSender, commandInput);
        }
        catch (final InvalidInputException e2) {
            if (this.invalidInputHandler != null) {
                this.invalidInputHandler.handle(dreamSender, e2.getRequiringClass(), e2.getInput());
                return this;
            }
            throw e2;
        }
        catch (final InvalidPermissionException e3) {
            if (this.invalidPermissionHandler != null) {
                this.invalidPermissionHandler.handle(dreamSender, e3.getPermission());
                return this;
            }
            throw e3;
        }
        catch (final InvalidSenderException e4) {
            if (this.invalidSenderHandler != null) {
                this.invalidSenderHandler.handle(dreamSender, e4.getRequireType());
                return this;
            }
            throw e4;
        }
        catch (final InvalidUsageException e5) {
            if (this.invalidUsageHandler != null) {
                this.invalidUsageHandler.handle(dreamSender, (Optional<CommandMeta>)Optional.ofNullable((Object)e5.getCommandMeta()), e5.getCommandInput());
                return this;
            }
            throw e5;
        }
        return this;
    }
    
    @Override
    public CommandProviderImpl register(@NonNull final CommandBase commandBase) {
        if (commandBase == null) {
            throw new NullPointerException("commandBase is marked non-null but is null");
        }
        final Command command = commandBase.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new RuntimeException("Cannot find @Command annotation in class " + commandBase.getClass().getSimpleName());
        }
        return this.register(new CommandContext(command), commandBase);
    }
    
    @Override
    public CommandProviderImpl register(@NonNull final CommandContext commandContext, @NonNull final CommandBase commandBase) {
        if (commandContext == null) {
            throw new NullPointerException("commandContext is marked non-null but is null");
        }
        if (commandBase == null) {
            throw new NullPointerException("commandBase is marked non-null but is null");
        }
        final CommandMeta commandMeta = new CommandMeta(this.suggestionService, this.resolverService, commandContext, commandBase, commandBase);
        this.commandMap.put((Object)commandContext.getName(), (Object)commandMeta);
        Arrays.stream((Object[])commandContext.getAliases()).forEach(label -> this.commandMap.put((Object)label, (Object)commandMeta));
        if (this.commandRegistry != null) {
            this.commandRegistry.register(commandContext, commandMeta);
        }
        return this;
    }
    
    @Override
    public CommandProviderImpl register(@NonNull final CommandContext commandContext, @NonNull final CommandBase commandBase, @NonNull final Object instance) {
        if (commandContext == null) {
            throw new NullPointerException("commandContext is marked non-null but is null");
        }
        if (commandBase == null) {
            throw new NullPointerException("commandBase is marked non-null but is null");
        }
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        final CommandMeta commandMeta = new CommandMeta(this.suggestionService, this.resolverService, commandContext, commandBase, instance);
        this.commandMap.put((Object)commandContext.getName(), (Object)commandMeta);
        Arrays.stream((Object[])commandContext.getAliases()).forEach(label -> this.commandMap.put((Object)label, (Object)commandMeta));
        if (this.commandRegistry != null) {
            this.commandRegistry.register(commandContext, commandMeta);
        }
        return this;
    }
    
    @Override
    public CommandProviderImpl unregister(@NonNull final CommandContext commandContext) {
        if (commandContext == null) {
            throw new NullPointerException("commandContext is marked non-null but is null");
        }
        this.commandMap.remove((Object)commandContext.getName());
        final Stream stream = Arrays.stream((Object[])commandContext.getAliases());
        final Map<String, CommandMeta> commandMap = this.commandMap;
        Objects.requireNonNull((Object)commandMap);
        stream.forEach(commandMap::remove);
        if (this.commandRegistry != null) {
            this.commandRegistry.unregister(commandContext);
        }
        return this;
    }
    
    @Override
    public CommandProviderImpl registerExtension(@NonNull final CommandExtension commandExtension) {
        if (commandExtension == null) {
            throw new NullPointerException("commandExtension is marked non-null but is null");
        }
        commandExtension.register(this);
        return this;
    }
    
    @Override
    public CommandProviderImpl registerTransformer(@NonNull final ObjectTransformer<?> objectTransformer) {
        if (objectTransformer == null) {
            throw new NullPointerException("objectTransformer is marked non-null but is null");
        }
        this.resolverCache.add(objectTransformer);
        return this;
    }
    
    @Override
    public CommandProviderImpl registerTransformer(@NonNull final ArrayTransformer<?> arrayTransformer) {
        if (arrayTransformer == null) {
            throw new NullPointerException("arrayTransformer is marked non-null but is null");
        }
        this.resolverCache.add(arrayTransformer);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterTransformer(@NonNull final Class<?> classTransformer, final boolean array) {
        if (classTransformer == null) {
            throw new NullPointerException("classTransformer is marked non-null but is null");
        }
        this.resolverCache.remove(classTransformer);
        if (array) {
            this.resolverCache.removeArray(classTransformer);
        }
        return this;
    }
    
    @Override
    public CommandProviderImpl registerBind(@NonNull final BindResolver<?> bindResolver) {
        if (bindResolver == null) {
            throw new NullPointerException("bindResolver is marked non-null but is null");
        }
        this.bindCache.registerBind(bindResolver);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterBind(@NonNull final Class<?> bindClass) {
        if (bindClass == null) {
            throw new NullPointerException("bindClass is marked non-null but is null");
        }
        this.bindCache.unregisterBind(bindClass);
        return this;
    }
    
    @Override
    public CommandProviderImpl registerResult(@NonNull final ResultResolver resultResolver) {
        if (resultResolver == null) {
            throw new NullPointerException("resultResolver is marked non-null but is null");
        }
        this.resultCache.registerResult(resultResolver);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterResult(@NonNull final Class<?> resultClass) {
        if (resultClass == null) {
            throw new NullPointerException("resultClass is marked non-null but is null");
        }
        this.resultCache.unregisterResult(resultClass);
        return this;
    }
    
    @Override
    public CommandProviderImpl registerSuggestion(@NonNull final String key, @NonNull final SuggestionSupplier suggestionSupplier) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (suggestionSupplier == null) {
            throw new NullPointerException("suggestionSupplier is marked non-null but is null");
        }
        this.suggestionCache.addSuggestion(key, suggestionSupplier);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterSuggestion(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.suggestionCache.removeSuggestion(key);
        return this;
    }
    
    @Override
    public CommandProviderImpl registerSuggestionFilter(@NonNull final String key, @NonNull final SuggestionFilter suggestionFilter) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (suggestionFilter == null) {
            throw new NullPointerException("suggestionFilter is marked non-null but is null");
        }
        this.suggestionCache.addSuggestionFilter(key, suggestionFilter);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterSuggestionFilter(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.suggestionCache.removeSuggestionFilter(key);
        return this;
    }
    
    @Override
    public InvalidPermissionHandler getInvalidPermissionHandler() {
        return this.invalidPermissionHandler;
    }
    
    @Override
    public CommandProviderImpl setInvalidPermissionHandler(@NonNull final InvalidPermissionHandler invalidPermissionHandler) {
        if (invalidPermissionHandler == null) {
            throw new NullPointerException("invalidPermissionHandler is marked non-null but is null");
        }
        this.invalidPermissionHandler = invalidPermissionHandler;
        return this;
    }
    
    @Override
    public InvalidSenderHandler getInvalidSenderHandler() {
        return this.invalidSenderHandler;
    }
    
    @Override
    public CommandProviderImpl setInvalidSenderHandler(@NonNull final InvalidSenderHandler invalidSenderHandler) {
        if (invalidSenderHandler == null) {
            throw new NullPointerException("invalidSenderHandler is marked non-null but is null");
        }
        this.invalidSenderHandler = invalidSenderHandler;
        return this;
    }
    
    @Override
    public InvalidUsageHandler getInvalidUsageHandler() {
        return this.invalidUsageHandler;
    }
    
    @Override
    public CommandProviderImpl setInvalidUsageHandler(@NonNull final InvalidUsageHandler invalidUsageHandler) {
        if (invalidUsageHandler == null) {
            throw new NullPointerException("invalidUsageHandler is marked non-null but is null");
        }
        this.invalidUsageHandler = invalidUsageHandler;
        return this;
    }
    
    @Override
    public InvalidInputHandler getInvalidInputHandler() {
        return this.invalidInputHandler;
    }
    
    @Override
    public CommandProviderImpl setInvalidInputHandler(@NonNull final InvalidInputHandler invalidInputHandler) {
        if (invalidInputHandler == null) {
            throw new NullPointerException("invalidInputHandler is marked non-null but is null");
        }
        this.invalidInputHandler = invalidInputHandler;
        return this;
    }
    
    @Override
    public CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }
    
    @Override
    public CommandProviderImpl setCommandRegistry(@NonNull final CommandRegistry commandRegistry) {
        if (commandRegistry == null) {
            throw new NullPointerException("commandRegistry is marked non-null but is null");
        }
        this.commandRegistry = commandRegistry;
        return this;
    }
    
    @Override
    public CommandProviderImpl registerAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        this.resolverCache.addAssignableClass(from, to);
        return this;
    }
    
    @Override
    public CommandProviderImpl unregisterAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        this.resolverCache.removeAssignableClass(from, to);
        return this;
    }
    
    @Override
    public CommandScheduler getCommandScheduler() {
        return this.commandScheduler;
    }
    
    @Override
    public CommandProviderImpl setCommandScheduler(@NonNull final CommandScheduler commandScheduler) {
        if (commandScheduler == null) {
            throw new NullPointerException("commandScheduler is marked non-null but is null");
        }
        this.commandScheduler = commandScheduler;
        return this;
    }
}
