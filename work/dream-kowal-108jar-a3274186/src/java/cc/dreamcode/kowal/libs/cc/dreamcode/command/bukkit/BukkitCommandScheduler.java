package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit;

import lombok.Generated;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandScheduler;

public class BukkitCommandScheduler implements CommandScheduler
{
    private final Plugin plugin;
    
    @Override
    public void async(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
    
    @Generated
    public BukkitCommandScheduler(final Plugin plugin) {
        this.plugin = plugin;
    }
}
