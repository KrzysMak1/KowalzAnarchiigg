package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Args;
import java.lang.annotation.Annotation;
import java.util.Map;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidInputException;
import java.util.ArrayList;
import lombok.Generated;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Collection;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.stream.Collectors;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Sender;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Async;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.ResolverService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.SuggestionService;
import java.util.List;

public class CommandMeta
{
    private final CommandContext commandContext;
    private final CommandBase commandBase;
    private final Object commandInstance;
    private final boolean async;
    private final String[] basePermissions;
    private final DreamSender.Type[] baseSenderTypes;
    private final List<CommandPathMeta> commandPaths;
    private final SuggestionService suggestionService;
    private final ResolverService resolverService;
    
    public CommandMeta(@NonNull final SuggestionService suggestionService, @NonNull final ResolverService resolverService, @NonNull final CommandContext commandContext, @NonNull final CommandBase commandBase, @NonNull final Object commandInstance) {
        if (suggestionService == null) {
            throw new NullPointerException("suggestionService is marked non-null but is null");
        }
        if (resolverService == null) {
            throw new NullPointerException("resolverService is marked non-null but is null");
        }
        if (commandContext == null) {
            throw new NullPointerException("commandContext is marked non-null but is null");
        }
        if (commandBase == null) {
            throw new NullPointerException("commandBase is marked non-null but is null");
        }
        if (commandInstance == null) {
            throw new NullPointerException("commandInstance is marked non-null but is null");
        }
        this.suggestionService = suggestionService;
        this.resolverService = resolverService;
        this.commandContext = commandContext;
        this.commandBase = commandBase;
        this.commandInstance = commandInstance;
        this.async = (commandBase.getClass().getAnnotation(Async.class) != null);
        final Permission[] permissionsArray = commandBase.getClass().getAnnotationsByType(Permission.class);
        this.basePermissions = (String[])Arrays.stream((Object[])permissionsArray).map(Permission::value).toArray(String[]::new);
        final Sender[] sendersArray = commandBase.getClass().getAnnotationsByType(Sender.class);
        this.baseSenderTypes = (DreamSender.Type[])Arrays.stream((Object[])sendersArray).map(Sender::value).toArray(DreamSender.Type[]::new);
        this.commandPaths = commandBase.getCommandPaths(this);
    }
    
    public List<CommandPathMeta> getFilteredCommandPaths(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        return (List<CommandPathMeta>)this.commandPaths.stream().filter(commandPathMeta -> {
            final List<DreamSender.Type> senderTypes = commandPathMeta.getSendersType();
            return senderTypes.isEmpty() || senderTypes.contains((Object)sender.getType());
        }).filter(commandPathMeta -> {
            for (final String permission : commandPathMeta.getPermissions()) {
                if (!sender.hasPermission(permission)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }
    
    public List<String> getSuggestion(@NonNull final DreamSender<?> sender, @NonNull final CommandInput commandInput) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        final ListBuilder<String> listBuilder = new ListBuilder<String>();
        for (final CommandPathMeta commandPath : this.getFilteredCommandPaths(sender)) {
            listBuilder.addAll((java.util.Collection<? extends String>)commandPath.getSuggestion(this.suggestionService, commandInput));
        }
        return (List<String>)listBuilder.build().stream().distinct().collect(Collectors.toList());
    }
    
    public Stream<CommandPathMeta> findExecutor(@NonNull final CommandInput commandInput, final boolean throwInvalidInput) {
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        return (Stream<CommandPathMeta>)this.commandPaths.stream().filter(commandPathMeta -> {
            final int pathLength = commandPathMeta.getPath().isEmpty() ? 0 : commandPathMeta.getPath().split(" ").length;
            if (commandInput.getArguments().length < pathLength) {
                return false;
            }
            if (commandPathMeta.getParamMultiArgs().isEmpty() && commandInput.getArguments().length > pathLength + commandPathMeta.getParamArgs().size() + commandPathMeta.getParamOptionalArgs().size()) {
                return false;
            }
            final String argumentEntry = StringUtil.join(commandInput.getArguments(), " ", 0, pathLength);
            return commandPathMeta.getPath().equalsIgnoreCase(argumentEntry);
        }).sorted((o1, o2) -> {
            final int firstPathLength = o1.getPath().isEmpty() ? 0 : (o1.getPath().split(" ").length + 1);
            final int secondPathLength = o2.getPath().isEmpty() ? 0 : (o2.getPath().split(" ").length + 1);
            return Integer.compare(secondPathLength + o2.getParamArgs().size(), firstPathLength + o1.getParamArgs().size());
        }).filter(commandPathMeta -> {
            final int pathLength = commandPathMeta.getPath().isEmpty() ? 0 : commandPathMeta.getPath().split(" ").length;
            final String[] params = new String[commandInput.getArguments().length - pathLength];
            System.arraycopy((Object)commandInput.getArguments(), pathLength, (Object)params, 0, params.length);
            if (params.length < commandPathMeta.getParamArgs().size()) {
                return false;
            }
            final List<Class<?>> argClasses = (List<Class<?>>)new ArrayList(commandPathMeta.getParamArgs().values());
            int index = 0;
            while (index < commandPathMeta.getParamArgs().size()) {
                final String input = params[index];
                final Class<?> paramType = (Class<?>)argClasses.get(index);
                if (!this.resolverService.support(paramType, input)) {
                    if (throwInvalidInput) {
                        throw new InvalidInputException(paramType, input, "Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                    }
                    return false;
                }
                else {
                    ++index;
                }
            }
            if (commandPathMeta.getParamMultiArgs().isEmpty()) {
                return true;
            }
            for (final Map.Entry<Integer, Class<?>> entry : commandPathMeta.getParamMultiArgs().entrySet()) {
                final int index2 = (int)entry.getKey();
                final Class<?> paramType2 = ((Class)entry.getValue()).getComponentType();
                final Optional<Annotation> optionalAnnotation = (Optional<Annotation>)Arrays.stream((Object[])commandPathMeta.getParamAnnotations().get((Object)index2)).filter(annotation -> annotation.annotationType().equals(Args.class)).findAny();
                if (!optionalAnnotation.isPresent()) {
                    throw new RuntimeException("Annotation @Args not found (critical bug)");
                }
                final Args args = (Args)optionalAnnotation.get();
                final String skip = StringUtil.join(params, " ", (args.min() == -1) ? 0 : Math.min(args.min(), params.length), (args.max() == -1) ? params.length : Math.min(args.max(), params.length));
                final String[] split = skip.split(" ");
                final int length = split.length;
                int i = 0;
                while (i < length) {
                    final String restParam = split[i];
                    if (!this.resolverService.support(paramType2, restParam)) {
                        if (throwInvalidInput) {
                            throw new InvalidInputException(paramType2, restParam, "Cannot resolve param " + restParam + " as a " + paramType2.getSimpleName());
                        }
                        return false;
                    }
                    else {
                        ++i;
                    }
                }
            }
            return true;
        });
    }
    
    @Generated
    public CommandContext getCommandContext() {
        return this.commandContext;
    }
    
    @Generated
    public CommandBase getCommandBase() {
        return this.commandBase;
    }
    
    @Generated
    public Object getCommandInstance() {
        return this.commandInstance;
    }
    
    @Generated
    public boolean isAsync() {
        return this.async;
    }
    
    @Generated
    public String[] getBasePermissions() {
        return this.basePermissions;
    }
    
    @Generated
    public DreamSender.Type[] getBaseSenderTypes() {
        return this.baseSenderTypes;
    }
    
    @Generated
    public List<CommandPathMeta> getCommandPaths() {
        return this.commandPaths;
    }
    
    @Generated
    public SuggestionService getSuggestionService() {
        return this.suggestionService;
    }
    
    @Generated
    public ResolverService getResolverService() {
        return this.resolverService;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandMeta)) {
            return false;
        }
        final CommandMeta other = (CommandMeta)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isAsync() != other.isAsync()) {
            return false;
        }
        final Object this$commandContext = this.getCommandContext();
        final Object other$commandContext = other.getCommandContext();
        Label_0078: {
            if (this$commandContext == null) {
                if (other$commandContext == null) {
                    break Label_0078;
                }
            }
            else if (this$commandContext.equals(other$commandContext)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$commandBase = this.getCommandBase();
        final Object other$commandBase = other.getCommandBase();
        Label_0115: {
            if (this$commandBase == null) {
                if (other$commandBase == null) {
                    break Label_0115;
                }
            }
            else if (this$commandBase.equals(other$commandBase)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$commandInstance = this.getCommandInstance();
        final Object other$commandInstance = other.getCommandInstance();
        Label_0152: {
            if (this$commandInstance == null) {
                if (other$commandInstance == null) {
                    break Label_0152;
                }
            }
            else if (this$commandInstance.equals(other$commandInstance)) {
                break Label_0152;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getBasePermissions(), (Object[])other.getBasePermissions())) {
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getBaseSenderTypes(), (Object[])other.getBaseSenderTypes())) {
            return false;
        }
        final Object this$commandPaths = this.getCommandPaths();
        final Object other$commandPaths = other.getCommandPaths();
        Label_0221: {
            if (this$commandPaths == null) {
                if (other$commandPaths == null) {
                    break Label_0221;
                }
            }
            else if (this$commandPaths.equals(other$commandPaths)) {
                break Label_0221;
            }
            return false;
        }
        final Object this$suggestionService = this.getSuggestionService();
        final Object other$suggestionService = other.getSuggestionService();
        Label_0258: {
            if (this$suggestionService == null) {
                if (other$suggestionService == null) {
                    break Label_0258;
                }
            }
            else if (this$suggestionService.equals(other$suggestionService)) {
                break Label_0258;
            }
            return false;
        }
        final Object this$resolverService = this.getResolverService();
        final Object other$resolverService = other.getResolverService();
        if (this$resolverService == null) {
            if (other$resolverService == null) {
                return true;
            }
        }
        else if (this$resolverService.equals(other$resolverService)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CommandMeta;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isAsync() ? 79 : 97);
        final Object $commandContext = this.getCommandContext();
        result = result * 59 + (($commandContext == null) ? 43 : $commandContext.hashCode());
        final Object $commandBase = this.getCommandBase();
        result = result * 59 + (($commandBase == null) ? 43 : $commandBase.hashCode());
        final Object $commandInstance = this.getCommandInstance();
        result = result * 59 + (($commandInstance == null) ? 43 : $commandInstance.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getBasePermissions());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getBaseSenderTypes());
        final Object $commandPaths = this.getCommandPaths();
        result = result * 59 + (($commandPaths == null) ? 43 : $commandPaths.hashCode());
        final Object $suggestionService = this.getSuggestionService();
        result = result * 59 + (($suggestionService == null) ? 43 : $suggestionService.hashCode());
        final Object $resolverService = this.getResolverService();
        result = result * 59 + (($resolverService == null) ? 43 : $resolverService.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "CommandMeta(commandContext=" + (Object)this.getCommandContext() + ", commandBase=" + (Object)this.getCommandBase() + ", commandInstance=" + this.getCommandInstance() + ", async=" + this.isAsync() + ", basePermissions=" + Arrays.deepToString((Object[])this.getBasePermissions()) + ", baseSenderTypes=" + Arrays.deepToString((Object[])this.getBaseSenderTypes()) + ", commandPaths=" + (Object)this.getCommandPaths() + ", suggestionService=" + (Object)this.getSuggestionService() + ", resolverService=" + (Object)this.getResolverService() + ")";
    }
}
