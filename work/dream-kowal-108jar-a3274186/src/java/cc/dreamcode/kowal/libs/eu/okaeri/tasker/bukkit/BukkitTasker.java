package cc.dreamcode.kowal.libs.eu.okaeri.tasker.bukkit;

import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.delayer.Delayer;
import java.time.Duration;
import org.bukkit.plugin.Plugin;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerExecutor;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.Tasker;

public class BukkitTasker extends Tasker
{
    protected BukkitTasker(@NonNull final TaskerExecutor<?> executor) {
        super(executor);
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
    }
    
    public static BukkitTasker newPool(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return new BukkitTasker(new BukkitExecutor(plugin));
    }
    
    public Delayer newDelayer(@NonNull final Duration duration, final long checkRateTicks) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return this.newDelayer(duration, Duration.ofMillis(50L * checkRateTicks));
    }
}
