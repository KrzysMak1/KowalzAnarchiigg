package cc.dreamcode.kowal.controller;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import cc.dreamcode.kowal.ParticleCache;
import org.bukkit.event.Listener;

public class PlayerController implements Listener
{
    private final ParticleCache particleCache;
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.particleCache.check(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.particleCache.remove(event.getPlayer().getUniqueId());
    }
    
    @Inject
    @Generated
    public PlayerController(final ParticleCache particleCache) {
        this.particleCache = particleCache;
    }
}
