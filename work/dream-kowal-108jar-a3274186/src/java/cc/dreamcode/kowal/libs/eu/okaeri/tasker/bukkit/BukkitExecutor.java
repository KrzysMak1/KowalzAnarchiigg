package cc.dreamcode.kowal.libs.eu.okaeri.tasker.bukkit;

import java.time.Duration;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerExecutor;

public class BukkitExecutor implements TaskerExecutor<BukkitTask>
{
    protected final Plugin plugin;
    
    @Override
    public boolean isMain() {
        return Bukkit.isPrimaryThread();
    }
    
    @Override
    public BukkitTask schedule(@NonNull final Runnable runnable, final boolean async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (async) {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, 1L, 1L);
        }
        return Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, 1L, 1L);
    }
    
    @Override
    public BukkitTask schedule(@NonNull final Runnable runnable, @NonNull final Duration delay, @NonNull final Duration rate, final boolean async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (delay == null) {
            throw new NullPointerException("delay is marked non-null but is null");
        }
        if (rate == null) {
            throw new NullPointerException("rate is marked non-null but is null");
        }
        final long delayTicks = (delay.toMillis() < 50L) ? 1L : (delay.toMillis() / 50L);
        final long rateTicks = (rate.toMillis() < 50L) ? 1L : (rate.toMillis() / 50L);
        if (async) {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delayTicks, rateTicks);
        }
        return Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, delayTicks, rateTicks);
    }
    
    @Override
    public BukkitTask run(@NonNull final Runnable runnable, final boolean async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (async) {
            return Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
        }
        return Bukkit.getScheduler().runTask(this.plugin, runnable);
    }
    
    @Override
    public BukkitTask runLater(@NonNull final Runnable runnable, @NonNull final Duration delay, final boolean async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (delay == null) {
            throw new NullPointerException("delay is marked non-null but is null");
        }
        if (delay.isZero()) {
            return this.run(runnable, async);
        }
        final long delayTicks = (delay.toMillis() < 50L) ? 1L : (delay.toMillis() / 50L);
        if (async) {
            return Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, delayTicks);
        }
        return Bukkit.getScheduler().runTaskLater(this.plugin, runnable, delayTicks);
    }
    
    @Override
    public void cancel(@NonNull final BukkitTask task) {
        if (task == null) {
            throw new NullPointerException("task is marked non-null but is null");
        }
        task.cancel();
    }
    
    public BukkitExecutor(final Plugin plugin) {
        this.plugin = plugin;
    }
}
