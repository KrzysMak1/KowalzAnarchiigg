package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind;

import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception.InvalidSenderException;
import java.util.Collections;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;

public class PlayerBind implements BindResolver<Player>
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Player.class.isAssignableFrom(type);
    }
    
    @NonNull
    @Override
    public Player resolveBind(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (!(sender.getHandler() instanceof Player)) {
            throw new InvalidSenderException((List<DreamSender.Type>)Collections.singletonList((Object)DreamSender.Type.CLIENT), "Sender type is unacceptable (" + (Object)sender.getType() + ")");
        }
        return (Player)sender.getHandler();
    }
}
