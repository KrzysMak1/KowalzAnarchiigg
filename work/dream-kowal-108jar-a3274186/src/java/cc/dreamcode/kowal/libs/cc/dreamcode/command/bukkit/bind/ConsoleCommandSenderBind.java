package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind;

import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidSenderException;
import java.util.Collections;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import org.bukkit.command.CommandSender;
import lombok.NonNull;
import org.bukkit.command.ConsoleCommandSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;

public class ConsoleCommandSenderBind implements BindResolver<ConsoleCommandSender>
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return CommandSender.class.isAssignableFrom(type);
    }
    
    @NonNull
    @Override
    public ConsoleCommandSender resolveBind(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (!(sender.getHandler() instanceof ConsoleCommandSender)) {
            throw new InvalidSenderException((List<DreamSender.Type>)Collections.singletonList((Object)DreamSender.Type.CONSOLE), "Sender type is unacceptable (" + (Object)sender.getType() + ")");
        }
        return (ConsoleCommandSender)sender.getHandler();
    }
}
