package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.SuggestionService;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import java.lang.reflect.Parameter;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Sender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Async;
import java.lang.reflect.ParameterizedType;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.option.Option;
import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.OptArg;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Args;
import java.util.Objects;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Arg;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Executor;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.object.Duo;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.lang.reflect.Method;

public class CommandPathMeta
{
    private final CommandMeta commandMeta;
    private final Method method;
    private final Map<Integer, Annotation[]> argAnnotations;
    private final Map<Integer, Class<?>> argClasses;
    private final Map<Integer, CommandArgument> argNames;
    private final Map<Integer, CommandArgument> argDisplayNames;
    private final Map<Integer, Annotation[]> paramAnnotations;
    private final Map<Integer, Class<?>> paramArgs;
    private final Map<Integer, Class<?>> paramMultiArgs;
    private final Map<Integer, Duo<Class<?>, Boolean>> paramOptionalArgs;
    private final Map<Integer, Class<?>> paramBinds;
    private final String path;
    private final String description;
    private final boolean async;
    private final String[] pathPermissions;
    private final DreamSender.Type[] pathSenderTypes;
    private final CommandExecutor commandExecutor;
    
    public CommandPathMeta(@NonNull final CommandMeta commandMeta, @NonNull final Method method, @NonNull final Executor executor) {
        if (commandMeta == null) {
            throw new NullPointerException("commandMeta is marked non-null but is null");
        }
        if (method == null) {
            throw new NullPointerException("method is marked non-null but is null");
        }
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        this.commandMeta = commandMeta;
        this.method = method;
        this.paramAnnotations = (Map<Integer, Annotation[]>)new HashMap();
        for (int index = 0; index < this.method.getParameterAnnotations().length; ++index) {
            this.paramAnnotations.put((Object)index, (Object)this.method.getParameterAnnotations()[index]);
        }
        this.argAnnotations = (Map<Integer, Annotation[]>)new HashMap();
        this.argClasses = (Map<Integer, Class<?>>)new HashMap();
        this.argNames = (Map<Integer, CommandArgument>)new HashMap();
        this.argDisplayNames = (Map<Integer, CommandArgument>)new HashMap();
        final AtomicInteger atomicNameIndex = new AtomicInteger();
        for (int index2 = 0; index2 < this.method.getParameters().length; ++index2) {
            this.argAnnotations.put((Object)atomicNameIndex.get(), (Object)this.paramAnnotations.get((Object)index2));
            final Parameter parameter = this.method.getParameters()[index2];
            final Optional<Annotation> optionalArg = (Optional<Annotation>)Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).filter(annotation -> Arg.class.isAssignableFrom(annotation.annotationType())).findAny();
            if (optionalArg.isPresent()) {
                final Arg arg = (Arg)optionalArg.get();
                final String name = Objects.equals((Object)arg.value(), (Object)"") ? parameter.getName() : arg.value();
                this.argClasses.put((Object)atomicNameIndex.get(), (Object)parameter.getType());
                this.argNames.put((Object)atomicNameIndex.get(), (Object)new CommandArgument(CommandArgument.Type.ARG, name));
                this.argDisplayNames.put((Object)atomicNameIndex.getAndIncrement(), (Object)new CommandArgument(CommandArgument.Type.ARG, "<" + name + ">"));
            }
            else {
                final Optional<Annotation> optionalArgs = (Optional<Annotation>)Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).filter(annotation -> Args.class.isAssignableFrom(annotation.annotationType())).findAny();
                if (optionalArgs.isPresent()) {
                    final Args args = (Args)optionalArgs.get();
                    final String name2 = Objects.equals((Object)args.value(), (Object)"") ? parameter.getName() : args.value();
                    this.argClasses.put((Object)atomicNameIndex.get(), (Object)parameter.getType());
                    this.argNames.put((Object)atomicNameIndex.get(), (Object)new CommandArgument(CommandArgument.Type.ARGS, name2));
                    this.argDisplayNames.put((Object)atomicNameIndex.getAndIncrement(), (Object)new CommandArgument(CommandArgument.Type.ARGS, "(" + name2 + ")"));
                }
                else {
                    final Optional<Annotation> optionalOptArg = (Optional<Annotation>)Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).filter(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType())).findAny();
                    if (optionalOptArg.isPresent()) {
                        final OptArg optArg = (OptArg)optionalOptArg.get();
                        final String name3 = Objects.equals((Object)optArg.value(), (Object)"") ? parameter.getName() : optArg.value();
                        Class<?> rawType = parameter.getType();
                        if (Optional.class.isAssignableFrom(rawType) || Option.class.isAssignableFrom(rawType)) {
                            final ParameterizedType parameterizedType = (ParameterizedType)parameter.getParameterizedType();
                            if (parameterizedType.getActualTypeArguments().length == 1) {
                                final Type paramType = parameterizedType.getActualTypeArguments()[0];
                                if (paramType instanceof Class) {
                                    rawType = (Class)paramType;
                                }
                            }
                        }
                        this.argClasses.put((Object)atomicNameIndex.get(), (Object)rawType);
                        this.argNames.put((Object)atomicNameIndex.get(), (Object)new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, name3));
                        this.argDisplayNames.put((Object)atomicNameIndex.getAndIncrement(), (Object)new CommandArgument(CommandArgument.Type.OPTIONAL_ARG, "[" + name3 + "]"));
                    }
                }
            }
        }
        this.paramArgs = (Map<Integer, Class<?>>)new HashMap();
        this.paramMultiArgs = (Map<Integer, Class<?>>)new HashMap();
        this.paramOptionalArgs = (Map<Integer, Duo<Class<?>, Boolean>>)new HashMap();
        this.paramBinds = (Map<Integer, Class<?>>)new HashMap();
        for (int index2 = 0; index2 < this.method.getParameters().length; ++index2) {
            final Parameter parameter = this.method.getParameters()[index2];
            if (Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).anyMatch(annotation -> Arg.class.isAssignableFrom(annotation.annotationType()))) {
                final int finalIndex = index2;
                if (this.paramOptionalArgs.keySet().stream().anyMatch(argIndex -> argIndex < finalIndex)) {
                    throw new RuntimeException("@OptionalArg must be specified after @Arg params");
                }
                this.paramArgs.put((Object)index2, (Object)parameter.getType());
            }
            else if (Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).anyMatch(annotation -> Args.class.isAssignableFrom(annotation.annotationType()))) {
                this.paramMultiArgs.put((Object)index2, (Object)parameter.getType());
            }
            else if (Arrays.stream((Object[])this.paramAnnotations.get((Object)index2)).anyMatch(annotation -> OptArg.class.isAssignableFrom(annotation.annotationType()))) {
                final Class<?> rawOptionalType = parameter.getType();
                if (Optional.class.isAssignableFrom(rawOptionalType) || Option.class.isAssignableFrom(rawOptionalType)) {
                    final ParameterizedType parameterizedType2 = (ParameterizedType)parameter.getParameterizedType();
                    if (parameterizedType2.getActualTypeArguments().length == 1) {
                        final Type paramType2 = parameterizedType2.getActualTypeArguments()[0];
                        if (paramType2 instanceof Class) {
                            this.paramOptionalArgs.put((Object)index2, (Object)Duo.of((Class)paramType2, true));
                            continue;
                        }
                    }
                    throw new RuntimeException("Cannot resolve optional value by index " + index2);
                }
                this.paramOptionalArgs.put((Object)index2, (Object)Duo.of(rawOptionalType, false));
            }
            else {
                this.paramBinds.put((Object)index2, (Object)parameter.getType());
            }
        }
        this.path = executor.path();
        this.description = executor.description();
        this.async = (this.method.getAnnotation((Class)Async.class) != null);
        final Permission[] permissionsArray = (Permission[])this.method.getAnnotationsByType((Class)Permission.class);
        this.pathPermissions = (String[])Arrays.stream((Object[])permissionsArray).map(Permission::value).toArray(String[]::new);
        final Sender[] sendersArray = (Sender[])this.method.getAnnotationsByType((Class)Sender.class);
        this.pathSenderTypes = (DreamSender.Type[])Arrays.stream((Object[])sendersArray).map(Sender::value).toArray(DreamSender.Type[]::new);
        this.commandExecutor = new CommandExecutor(commandMeta, this);
    }
    
    public boolean isAsync() {
        return this.commandMeta.isAsync() || this.async;
    }
    
    public List<String> getPermissions() {
        final List<String> permissions = (List<String>)new ArrayList();
        Collections.addAll((Collection)permissions, (Object[])this.commandMeta.getBasePermissions());
        Collections.addAll((Collection)permissions, (Object[])this.pathPermissions);
        return permissions;
    }
    
    public List<DreamSender.Type> getSendersType() {
        final List<DreamSender.Type> senderTypes = (List<DreamSender.Type>)new ArrayList();
        Collections.addAll((Collection)senderTypes, (Object[])this.commandMeta.getBaseSenderTypes());
        Collections.addAll((Collection)senderTypes, (Object[])this.pathSenderTypes);
        return senderTypes;
    }
    
    public String getUsage() {
        final List<String> listBuilder = (List<String>)new ArrayList();
        listBuilder.add((Object)("/" + this.commandMeta.getCommandContext().getName()));
        if (!this.path.isEmpty()) {
            listBuilder.addAll((Collection)Arrays.asList((Object[])this.path.split(" ")));
        }
        final Stream map = this.argDisplayNames.values().stream().map(CommandArgument::getValue);
        final List<String> list = listBuilder;
        Objects.requireNonNull((Object)list);
        map.forEach(list::add);
        return StringUtil.join(listBuilder, " ");
    }
    
    public List<String> getSuggestion(@NonNull final SuggestionService suggestionService, @NonNull final CommandInput commandInput) {
        if (suggestionService == null) {
            throw new NullPointerException("suggestionService is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        final ListBuilder<String> listBuilder = new ListBuilder<String>();
        final String[] splitPath = this.path.split(" ");
        final int splitPathLength = this.path.isEmpty() ? 0 : splitPath.length;
        final String[] arguments = commandInput.getArguments();
        final String joinArguments = StringUtil.join(arguments, " ");
        if (arguments.length > splitPathLength && !joinArguments.startsWith(this.path)) {
            return listBuilder.build();
        }
        if (arguments.length <= splitPathLength && !this.path.startsWith(joinArguments)) {
            return listBuilder.build();
        }
        final int argumentLength = (arguments.length - 1 == -1) ? (commandInput.isSpaceAtTheEnd() ? 0 : -1) : (commandInput.isSpaceAtTheEnd() ? arguments.length : (arguments.length - 1));
        if (argumentLength == -1) {
            return listBuilder.build();
        }
        final int argumentParamLength = argumentLength - splitPathLength;
        if (argumentParamLength < 0) {
            listBuilder.add(splitPath[argumentLength]);
            return listBuilder.build();
        }
        this.paramMultiArgs.forEach((index, classType) -> {
            final Optional<Annotation> optionalAnnotation = (Optional<Annotation>)Arrays.stream((Object[])this.paramAnnotations.get((Object)index)).filter(annotation -> annotation.annotationType().equals(Args.class)).findAny();
            if (!optionalAnnotation.isPresent()) {
                throw new RuntimeException("Annotation @Args not found (critical bug)");
            }
            final Args args = (Args)optionalAnnotation.get();
            final int min = (args.min() == -1) ? 0 : args.min();
            final int max = (args.max() == -1) ? argumentParamLength : args.max();
            if (argumentParamLength >= min && argumentParamLength <= max) {
                final String displayName = ((CommandArgument)this.argDisplayNames.get((Object)(index - this.paramBinds.size()))).getValue();
                listBuilder.add(displayName);
            }
        });
        final String lastWord = commandInput.isSpaceAtTheEnd() ? "" : ((arguments.length == 0) ? "" : arguments[arguments.length - 1]);
        if (!this.argNames.containsKey((Object)argumentParamLength)) {
            return listBuilder.build();
        }
        final CommandArgument commandArgument = (CommandArgument)this.argNames.get((Object)argumentParamLength);
        final Optional<Completion> optionalCompletion = (Optional<Completion>)Arrays.stream((Object[])this.method.getAnnotationsByType((Class)Completion.class)).filter(completion -> Objects.equals((Object)completion.arg(), (Object)commandArgument.getValue())).findAny();
        if (!optionalCompletion.isPresent()) {
            final CommandArgument commandDisplayArgument = (CommandArgument)this.argDisplayNames.get((Object)argumentParamLength);
            if (!commandDisplayArgument.getType().equals((Object)CommandArgument.Type.ARGS)) {
                listBuilder.add(commandDisplayArgument.getValue());
            }
            return listBuilder.build();
        }
        final Completion completion = (Completion)optionalCompletion.get();
        final Class<?> suggestionParamType = (Class<?>)this.argClasses.get((Object)argumentParamLength);
        final Stream filter = suggestionService.getSuggestion(suggestionParamType, completion).stream().filter(text -> text.startsWith("[") || text.startsWith("(") || text.startsWith("<") || text.startsWith(lastWord));
        final ListBuilder<String> listBuilder2 = listBuilder;
        Objects.requireNonNull((Object)listBuilder2);
        filter.forEach(listBuilder2::add);
        return listBuilder.build();
    }
    
    @Generated
    public CommandMeta getCommandMeta() {
        return this.commandMeta;
    }
    
    @Generated
    public Method getMethod() {
        return this.method;
    }
    
    @Generated
    public Map<Integer, Annotation[]> getArgAnnotations() {
        return this.argAnnotations;
    }
    
    @Generated
    public Map<Integer, Class<?>> getArgClasses() {
        return this.argClasses;
    }
    
    @Generated
    public Map<Integer, CommandArgument> getArgNames() {
        return this.argNames;
    }
    
    @Generated
    public Map<Integer, CommandArgument> getArgDisplayNames() {
        return this.argDisplayNames;
    }
    
    @Generated
    public Map<Integer, Annotation[]> getParamAnnotations() {
        return this.paramAnnotations;
    }
    
    @Generated
    public Map<Integer, Class<?>> getParamArgs() {
        return this.paramArgs;
    }
    
    @Generated
    public Map<Integer, Class<?>> getParamMultiArgs() {
        return this.paramMultiArgs;
    }
    
    @Generated
    public Map<Integer, Duo<Class<?>, Boolean>> getParamOptionalArgs() {
        return this.paramOptionalArgs;
    }
    
    @Generated
    public Map<Integer, Class<?>> getParamBinds() {
        return this.paramBinds;
    }
    
    @Generated
    public String getPath() {
        return this.path;
    }
    
    @Generated
    public String getDescription() {
        return this.description;
    }
    
    @Generated
    public String[] getPathPermissions() {
        return this.pathPermissions;
    }
    
    @Generated
    public DreamSender.Type[] getPathSenderTypes() {
        return this.pathSenderTypes;
    }
    
    @Generated
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandPathMeta)) {
            return false;
        }
        final CommandPathMeta other = (CommandPathMeta)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isAsync() != other.isAsync()) {
            return false;
        }
        final Object this$commandMeta = this.getCommandMeta();
        final Object other$commandMeta = other.getCommandMeta();
        Label_0078: {
            if (this$commandMeta == null) {
                if (other$commandMeta == null) {
                    break Label_0078;
                }
            }
            else if (this$commandMeta.equals(other$commandMeta)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$method = this.getMethod();
        final Object other$method = other.getMethod();
        Label_0115: {
            if (this$method == null) {
                if (other$method == null) {
                    break Label_0115;
                }
            }
            else if (this$method.equals(other$method)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$argAnnotations = this.getArgAnnotations();
        final Object other$argAnnotations = other.getArgAnnotations();
        Label_0152: {
            if (this$argAnnotations == null) {
                if (other$argAnnotations == null) {
                    break Label_0152;
                }
            }
            else if (this$argAnnotations.equals(other$argAnnotations)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$argClasses = this.getArgClasses();
        final Object other$argClasses = other.getArgClasses();
        Label_0189: {
            if (this$argClasses == null) {
                if (other$argClasses == null) {
                    break Label_0189;
                }
            }
            else if (this$argClasses.equals(other$argClasses)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$argNames = this.getArgNames();
        final Object other$argNames = other.getArgNames();
        Label_0226: {
            if (this$argNames == null) {
                if (other$argNames == null) {
                    break Label_0226;
                }
            }
            else if (this$argNames.equals(other$argNames)) {
                break Label_0226;
            }
            return false;
        }
        final Object this$argDisplayNames = this.getArgDisplayNames();
        final Object other$argDisplayNames = other.getArgDisplayNames();
        Label_0263: {
            if (this$argDisplayNames == null) {
                if (other$argDisplayNames == null) {
                    break Label_0263;
                }
            }
            else if (this$argDisplayNames.equals(other$argDisplayNames)) {
                break Label_0263;
            }
            return false;
        }
        final Object this$paramAnnotations = this.getParamAnnotations();
        final Object other$paramAnnotations = other.getParamAnnotations();
        Label_0300: {
            if (this$paramAnnotations == null) {
                if (other$paramAnnotations == null) {
                    break Label_0300;
                }
            }
            else if (this$paramAnnotations.equals(other$paramAnnotations)) {
                break Label_0300;
            }
            return false;
        }
        final Object this$paramArgs = this.getParamArgs();
        final Object other$paramArgs = other.getParamArgs();
        Label_0337: {
            if (this$paramArgs == null) {
                if (other$paramArgs == null) {
                    break Label_0337;
                }
            }
            else if (this$paramArgs.equals(other$paramArgs)) {
                break Label_0337;
            }
            return false;
        }
        final Object this$paramMultiArgs = this.getParamMultiArgs();
        final Object other$paramMultiArgs = other.getParamMultiArgs();
        Label_0374: {
            if (this$paramMultiArgs == null) {
                if (other$paramMultiArgs == null) {
                    break Label_0374;
                }
            }
            else if (this$paramMultiArgs.equals(other$paramMultiArgs)) {
                break Label_0374;
            }
            return false;
        }
        final Object this$paramOptionalArgs = this.getParamOptionalArgs();
        final Object other$paramOptionalArgs = other.getParamOptionalArgs();
        Label_0411: {
            if (this$paramOptionalArgs == null) {
                if (other$paramOptionalArgs == null) {
                    break Label_0411;
                }
            }
            else if (this$paramOptionalArgs.equals(other$paramOptionalArgs)) {
                break Label_0411;
            }
            return false;
        }
        final Object this$paramBinds = this.getParamBinds();
        final Object other$paramBinds = other.getParamBinds();
        Label_0448: {
            if (this$paramBinds == null) {
                if (other$paramBinds == null) {
                    break Label_0448;
                }
            }
            else if (this$paramBinds.equals(other$paramBinds)) {
                break Label_0448;
            }
            return false;
        }
        final Object this$path = this.getPath();
        final Object other$path = other.getPath();
        Label_0485: {
            if (this$path == null) {
                if (other$path == null) {
                    break Label_0485;
                }
            }
            else if (this$path.equals(other$path)) {
                break Label_0485;
            }
            return false;
        }
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        Label_0522: {
            if (this$description == null) {
                if (other$description == null) {
                    break Label_0522;
                }
            }
            else if (this$description.equals(other$description)) {
                break Label_0522;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getPathPermissions(), (Object[])other.getPathPermissions())) {
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getPathSenderTypes(), (Object[])other.getPathSenderTypes())) {
            return false;
        }
        final Object this$commandExecutor = this.getCommandExecutor();
        final Object other$commandExecutor = other.getCommandExecutor();
        if (this$commandExecutor == null) {
            if (other$commandExecutor == null) {
                return true;
            }
        }
        else if (this$commandExecutor.equals(other$commandExecutor)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CommandPathMeta;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isAsync() ? 79 : 97);
        final Object $commandMeta = this.getCommandMeta();
        result = result * 59 + (($commandMeta == null) ? 43 : $commandMeta.hashCode());
        final Object $method = this.getMethod();
        result = result * 59 + (($method == null) ? 43 : $method.hashCode());
        final Object $argAnnotations = this.getArgAnnotations();
        result = result * 59 + (($argAnnotations == null) ? 43 : $argAnnotations.hashCode());
        final Object $argClasses = this.getArgClasses();
        result = result * 59 + (($argClasses == null) ? 43 : $argClasses.hashCode());
        final Object $argNames = this.getArgNames();
        result = result * 59 + (($argNames == null) ? 43 : $argNames.hashCode());
        final Object $argDisplayNames = this.getArgDisplayNames();
        result = result * 59 + (($argDisplayNames == null) ? 43 : $argDisplayNames.hashCode());
        final Object $paramAnnotations = this.getParamAnnotations();
        result = result * 59 + (($paramAnnotations == null) ? 43 : $paramAnnotations.hashCode());
        final Object $paramArgs = this.getParamArgs();
        result = result * 59 + (($paramArgs == null) ? 43 : $paramArgs.hashCode());
        final Object $paramMultiArgs = this.getParamMultiArgs();
        result = result * 59 + (($paramMultiArgs == null) ? 43 : $paramMultiArgs.hashCode());
        final Object $paramOptionalArgs = this.getParamOptionalArgs();
        result = result * 59 + (($paramOptionalArgs == null) ? 43 : $paramOptionalArgs.hashCode());
        final Object $paramBinds = this.getParamBinds();
        result = result * 59 + (($paramBinds == null) ? 43 : $paramBinds.hashCode());
        final Object $path = this.getPath();
        result = result * 59 + (($path == null) ? 43 : $path.hashCode());
        final Object $description = this.getDescription();
        result = result * 59 + (($description == null) ? 43 : $description.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getPathPermissions());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getPathSenderTypes());
        final Object $commandExecutor = this.getCommandExecutor();
        result = result * 59 + (($commandExecutor == null) ? 43 : $commandExecutor.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "CommandPathMeta(commandMeta=" + (Object)this.getCommandMeta() + ", method=" + (Object)this.getMethod() + ", argAnnotations=" + (Object)this.getArgAnnotations() + ", argClasses=" + (Object)this.getArgClasses() + ", argNames=" + (Object)this.getArgNames() + ", argDisplayNames=" + (Object)this.getArgDisplayNames() + ", paramAnnotations=" + (Object)this.getParamAnnotations() + ", paramArgs=" + (Object)this.getParamArgs() + ", paramMultiArgs=" + (Object)this.getParamMultiArgs() + ", paramOptionalArgs=" + (Object)this.getParamOptionalArgs() + ", paramBinds=" + (Object)this.getParamBinds() + ", path=" + this.getPath() + ", description=" + this.getDescription() + ", async=" + this.isAsync() + ", pathPermissions=" + Arrays.deepToString((Object[])this.getPathPermissions()) + ", pathSenderTypes=" + Arrays.deepToString((Object[])this.getPathSenderTypes()) + ", commandExecutor=" + (Object)this.getCommandExecutor() + ")";
    }
}
