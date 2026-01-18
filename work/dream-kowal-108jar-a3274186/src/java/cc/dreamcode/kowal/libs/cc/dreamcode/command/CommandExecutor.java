package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import java.lang.reflect.InvocationTargetException;
import lombok.Generated;
import java.util.Iterator;
import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Args;
import java.util.Arrays;
import java.lang.annotation.Annotation;
import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.object.Duo;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidInputException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidPermissionException;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidSenderException;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindService;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.ResolverService;
import lombok.NonNull;

public class CommandExecutor
{
    private final CommandMeta commandMeta;
    private final CommandPathMeta commandPathMeta;
    
    public void execute(@NonNull final CommandScheduler commandScheduler, @NonNull final ResolverService resolverService, @NonNull final BindService bindService, @NonNull final ResultService resultService, @NonNull final DreamSender<?> sender, @NonNull final CommandInput commandInput) {
        if (commandScheduler == null) {
            throw new NullPointerException("commandScheduler is marked non-null but is null");
        }
        if (resolverService == null) {
            throw new NullPointerException("resolverService is marked non-null but is null");
        }
        if (bindService == null) {
            throw new NullPointerException("bindService is marked non-null but is null");
        }
        if (resultService == null) {
            throw new NullPointerException("resultService is marked non-null but is null");
        }
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        final List<DreamSender.Type> senderTypes = this.commandPathMeta.getSendersType();
        if (!senderTypes.isEmpty() && !senderTypes.contains((Object)sender.getType())) {
            throw new InvalidSenderException(senderTypes, "Sender type is unacceptable (" + (Object)sender.getType() + ")");
        }
        for (final String permission : this.commandPathMeta.getPermissions()) {
            if (!sender.hasPermission(permission)) {
                throw new InvalidPermissionException(permission, "Sender permission not found (" + permission + ")");
            }
        }
        final String path = this.commandPathMeta.getPath();
        final ListBuilder<Object> objects = new ListBuilder<Object>();
        final int pathLength = path.isEmpty() ? 0 : path.split(" ").length;
        final String[] params = new String[commandInput.getArguments().length - pathLength];
        System.arraycopy((Object)commandInput.getArguments(), pathLength, (Object)params, 0, params.length);
        final AtomicInteger atomicArg = new AtomicInteger();
        for (int index = 0; index < this.commandPathMeta.getMethod().getParameterCount(); ++index) {
            if (this.getCommandPathMeta().getParamArgs().containsKey((Object)index)) {
                final String input = params[atomicArg.get()];
                final Class<?> paramType = (Class<?>)new ArrayList(this.getCommandPathMeta().getParamArgs().values()).get(atomicArg.get());
                final Optional<?> optionalObject = resolverService.resolve(paramType, input);
                if (!optionalObject.isPresent()) {
                    throw new InvalidInputException(paramType, input, "Cannot resolve param " + input + " as a " + paramType.getSimpleName());
                }
                objects.add(optionalObject.get());
                atomicArg.incrementAndGet();
            }
            else if (this.getCommandPathMeta().getParamOptionalArgs().containsKey((Object)index)) {
                final Duo<Class<?>, Boolean> paramOptDuo = (Duo<Class<?>, Boolean>)this.getCommandPathMeta().getParamOptionalArgs().get((Object)index);
                final Class<?> rawType = paramOptDuo.getFirst();
                final boolean optional = paramOptDuo.getSecond();
                if (params.length <= atomicArg.get()) {
                    objects.add(optional ? Optional.empty() : null);
                    atomicArg.incrementAndGet();
                }
                else {
                    final String input2 = params[atomicArg.get()];
                    final Optional<?> optionalObject2 = resolverService.resolve(rawType, input2);
                    if (!optionalObject2.isPresent()) {
                        throw new InvalidInputException(rawType, input2, "Cannot resolve optional-param " + input2 + " as a " + rawType.getSimpleName());
                    }
                    objects.add(optional ? optionalObject2 : optionalObject2.get());
                    atomicArg.incrementAndGet();
                }
            }
            else if (this.commandPathMeta.getParamMultiArgs().containsKey((Object)index)) {
                final Class<?> paramType2 = (Class<?>)this.commandPathMeta.getParamMultiArgs().get((Object)index);
                final Optional<Annotation> optionalAnnotation = (Optional<Annotation>)Arrays.stream((Object[])this.commandPathMeta.getParamAnnotations().get((Object)index)).filter(annotation -> annotation.annotationType().equals(Args.class)).findAny();
                if (!optionalAnnotation.isPresent()) {
                    throw new RuntimeException("Annotation @Args not found (critical bug)");
                }
                final Args args = (Args)optionalAnnotation.get();
                final String skip = StringUtil.join(params, " ", (args.min() == -1) ? 0 : Math.min(args.min(), params.length), (args.max() == -1) ? params.length : Math.min(args.max(), params.length));
                final Object[] array = Arrays.stream((Object[])skip.split(" ")).map(input -> {
                    final Optional<?> optionalObject = resolverService.resolve(paramType2.getComponentType(), input);
                    if (!optionalObject.isPresent()) {
                        throw new InvalidInputException(paramType2, input, "Cannot resolve param " + input + " as a " + paramType2.getSimpleName());
                    }
                    return optionalObject.get();
                }).toArray();
                objects.add(resolverService.resolveArray(paramType2, array).orElseThrow(() -> new InvalidInputException(paramType2, skip, "Cannot resolve part of array: " + (Object)paramType2)));
            }
            else if (this.commandPathMeta.getParamBinds().containsKey((Object)index)) {
                final Class<?> paramType2 = (Class<?>)this.commandPathMeta.getParamBinds().get((Object)index);
                final Optional<?> optionalObject3 = bindService.resolveBind(paramType2, sender);
                if (!optionalObject3.isPresent()) {
                    throw new RuntimeException("Cannot resolve bind: " + paramType2.getSimpleName());
                }
                objects.add(optionalObject3.get());
            }
        }
        final Runnable invoke = () -> {
            try {
                final Object object = this.commandPathMeta.getMethod().invoke(this.commandMeta.getCommandInstance(), objects.build().toArray());
                if (object != null) {
                    resultService.resolveResult(sender, object.getClass(), object);
                }
            }
            catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke command-path /" + this.commandMeta.getCommandContext().getName() + " " + this.commandPathMeta.getPath(), (Throwable)e);
            }
        };
        if (this.commandPathMeta.isAsync()) {
            commandScheduler.async(invoke);
        }
        else {
            commandScheduler.sync(invoke);
        }
    }
    
    @Generated
    public CommandExecutor(final CommandMeta commandMeta, final CommandPathMeta commandPathMeta) {
        this.commandMeta = commandMeta;
        this.commandPathMeta = commandPathMeta;
    }
    
    @Generated
    public CommandMeta getCommandMeta() {
        return this.commandMeta;
    }
    
    @Generated
    public CommandPathMeta getCommandPathMeta() {
        return this.commandPathMeta;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandExecutor)) {
            return false;
        }
        final CommandExecutor other = (CommandExecutor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$commandMeta = this.getCommandMeta();
        final Object other$commandMeta = other.getCommandMeta();
        Label_0065: {
            if (this$commandMeta == null) {
                if (other$commandMeta == null) {
                    break Label_0065;
                }
            }
            else if (this$commandMeta.equals(other$commandMeta)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$commandPathMeta = this.getCommandPathMeta();
        final Object other$commandPathMeta = other.getCommandPathMeta();
        if (this$commandPathMeta == null) {
            if (other$commandPathMeta == null) {
                return true;
            }
        }
        else if (this$commandPathMeta.equals(other$commandPathMeta)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CommandExecutor;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $commandMeta = this.getCommandMeta();
        result = result * 59 + (($commandMeta == null) ? 43 : $commandMeta.hashCode());
        final Object $commandPathMeta = this.getCommandPathMeta();
        result = result * 59 + (($commandPathMeta == null) ? 43 : $commandPathMeta.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "CommandExecutor(commandMeta=" + (Object)this.getCommandMeta() + ", commandPathMeta=" + (Object)this.getCommandPathMeta() + ")";
    }
}
