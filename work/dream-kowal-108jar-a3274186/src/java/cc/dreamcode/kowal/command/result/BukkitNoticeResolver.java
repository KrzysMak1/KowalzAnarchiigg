package cc.dreamcode.kowal.command.result;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.BukkitSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure.BukkitNotice;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultResolver;

public class BukkitNoticeResolver implements ResultResolver
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BukkitNotice.class.isAssignableFrom(type);
    }
    
    @Override
    public void resolveResult(@NonNull final DreamSender<?> sender, @NonNull final Class<?> type, @NonNull final Object object) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        final BukkitSender bukkitSender = (BukkitSender)sender;
        final BukkitNotice BukkitNotice = (BukkitNotice)object;
        BukkitNotice.send(bukkitSender.getHandler());
    }
}
