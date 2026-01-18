package cc.dreamcode.kowal.controller;

import lombok.Generated;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import cc.dreamcode.kowal.ParticleCache;
import org.bukkit.event.Listener;

public class ArmorEquipController implements Listener
{
    private final ParticleCache particleCache;
    
    @EventHandler
    public void onArmorEquip(final PlayerArmorChangeEvent event) {
        this.particleCache.check(event.getPlayer());
    }
    
    @Inject
    @Generated
    public ArmorEquipController(final ParticleCache particleCache) {
        this.particleCache = particleCache;
    }
}
