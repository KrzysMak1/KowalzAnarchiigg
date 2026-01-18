package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure;

import lombok.NonNull;
import java.util.Map;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.NoticeSender;

public interface BukkitSender extends NoticeSender<CommandSender>
{
    void sendAll();
    
    void sendAll(@NonNull final Map<String, Object> p0);
    
    void sendBroadcast();
    
    void sendBroadcast(@NonNull final Map<String, Object> p0);
    
    void sendPermitted(@NonNull final String p0);
    
    void sendPermitted(@NonNull final String p0, @NonNull final Map<String, Object> p1);
}
