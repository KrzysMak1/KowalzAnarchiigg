package cc.dreamcode.kowal.tasks;

import org.bukkit.Bukkit;
import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.ParticleCache;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

@Scheduler(async = false, delay = 20L, interval = 20L)
public class ParticleTask implements Runnable
{
    private final PluginConfig pluginConfig;
    private final ParticleCache particleCache;
    
    public void run() {
        if (this.pluginConfig.particle == null && (this.pluginConfig.particlesLevelSeven == null || this.pluginConfig.particlesLevelSeven.isEmpty())) {
            return;
        }
        this.particleCache.entries().forEach(entry -> {
            final Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null || !player.isOnline()) {
                return;
            }
            final Particle particle = entry.getValue();
            if (particle == null) {
                return;
            }
            player.getWorld().spawnParticle(particle, player.getLocation().add(0.0, 2.0, 0.0), 4, 0.5, 1.0, 0.5);
        });
    }
    
    @Inject
    @Generated
    public ParticleTask(final PluginConfig pluginConfig, final ParticleCache particleCache) {
        this.pluginConfig = pluginConfig;
        this.particleCache = particleCache;
    }
}
