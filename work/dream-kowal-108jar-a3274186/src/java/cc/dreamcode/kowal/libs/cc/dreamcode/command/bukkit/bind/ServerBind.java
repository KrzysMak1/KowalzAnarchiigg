package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind;

import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import org.bukkit.Server;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;

public class ServerBind implements BindResolver<Server>
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Server.class.isAssignableFrom(type);
    }
    
    @NonNull
    @Override
    public Server resolveBind(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        final CommandSender commandSender = (CommandSender)sender.getHandler();
        return commandSender.getServer();
    }
}
