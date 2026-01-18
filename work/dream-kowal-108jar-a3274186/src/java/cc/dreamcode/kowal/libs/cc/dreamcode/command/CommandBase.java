package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.List;
import lombok.NonNull;

public interface CommandBase
{
    default List<CommandPathMeta> getCommandPaths(@NonNull final CommandMeta commandMeta) {
        if (commandMeta == null) {
            throw new NullPointerException("commandMeta is marked non-null but is null");
        }
        final ListBuilder<CommandPathMeta> executors = new ListBuilder<CommandPathMeta>();
        for (final Method declaredMethod : this.getClass().getDeclaredMethods()) {
            declaredMethod.setAccessible(true);
            final Executor executor = (Executor)declaredMethod.getAnnotation((Class)Executor.class);
            if (executor != null) {
                final CommandPathMeta commandPathMeta = new CommandPathMeta(commandMeta, declaredMethod, executor);
                executors.add(commandPathMeta);
            }
        }
        return executors.build();
    }
}
