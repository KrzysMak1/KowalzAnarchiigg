package cc.dreamcode.kowal.libs.cc.dreamcode.command.handler;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandInput;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandMeta;
import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;

public interface InvalidUsageHandler
{
    void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final Optional<CommandMeta> commandMeta, @NonNull final CommandInput commandInput);
}
