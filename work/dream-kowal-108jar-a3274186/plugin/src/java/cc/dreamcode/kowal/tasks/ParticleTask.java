package cc.dreamcode.kowal.tasks;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.UUID;
import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.ParticleCache;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;

@Scheduler(async = false, delay = 20L, interval = 20L)
public class ParticleTask implements Runnable
{
    private final PluginConfig pluginConfig;
    private final ParticleCache particleCache;
    
    public void run() {
        if (this.pluginConfig.particle == null) {
            return;
        }
        this.particleCache.values().forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                return;
            }
            player.getWorld().spawnParticle(this.pluginConfig.particle, player.getLocation().add(0.0, 2.0, 0.0), 4, 0.5, 1.0, 0.5);
        });
    }
    
    @Inject
    @Generated
    public ParticleTask(final PluginConfig pluginConfig, final ParticleCache particleCache) {
        this.pluginConfig = pluginConfig;
        this.particleCache = particleCache;
    }
}
