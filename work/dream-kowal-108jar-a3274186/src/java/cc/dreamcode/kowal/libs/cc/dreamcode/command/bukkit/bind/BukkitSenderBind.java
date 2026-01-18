package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.BukkitSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;

public class BukkitSenderBind implements BindResolver<BukkitSender>
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BukkitSender.class.isAssignableFrom(type);
    }
    
    @NonNull
    @Override
    public BukkitSender resolveBind(@NonNull final DreamSender<?> sender) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        return (BukkitSender)sender;
    }
}
