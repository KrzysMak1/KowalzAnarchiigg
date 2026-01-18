package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;

public class CommandSenderBind implements BindResolver<CommandSender>
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
    public CommandSender resolveBind(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        return (CommandSender)sender.getHandler();
    }
}
