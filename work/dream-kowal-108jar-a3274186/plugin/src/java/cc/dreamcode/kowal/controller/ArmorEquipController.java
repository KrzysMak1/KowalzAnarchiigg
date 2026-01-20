package cc.dreamcode.kowal.controller;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import cc.dreamcode.kowal.ParticleCache;
import cc.dreamcode.kowal.config.PluginConfig;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorEquipController implements Listener
{
    private final ParticleCache particleCache;
    private final PluginConfig pluginConfig;
    
    @EventHandler(ignoreCancelled = true)
    public void onArmorEquip(final PlayerArmorChangeEvent event) {
        if (this.isSetItem(event.getOldItem()) || this.isSetItem(event.getNewItem())) {
            return;
        }
        this.particleCache.check(event.getPlayer());
    }

    private boolean isSetItem(final ItemStack item) {
        return item != null
                && this.pluginConfig.items != null
                && this.pluginConfig.items.names != null
                && this.pluginConfig.items.names.containsKey(item.getType());
    }
    
    @Inject
    @Generated
    public ArmorEquipController(final ParticleCache particleCache, final PluginConfig pluginConfig) {
        this.particleCache = particleCache;
        this.pluginConfig = pluginConfig;
    }
}
