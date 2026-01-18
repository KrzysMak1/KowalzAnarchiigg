package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.NonNull;

public interface CommandRegistry
{
    void register(@NonNull final CommandContext commandContext, @NonNull final CommandMeta commandMeta);
    
    void unregister(@NonNull final CommandContext commandContext);
}
