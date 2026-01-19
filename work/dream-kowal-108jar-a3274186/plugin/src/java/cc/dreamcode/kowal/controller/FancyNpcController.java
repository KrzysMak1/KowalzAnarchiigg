package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.menu.KowalMenu;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import java.util.UUID;

public class FancyNpcController implements Listener {
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;

    @EventHandler(ignoreCancelled = true)
    public void onNpcInteract(final PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!this.isFancyNpcEnabled()) {
            return;
        }
        final String configuredUuid = this.pluginConfig.fancyNpcUuid;
        if (configuredUuid == null || configuredUuid.isBlank()) {
            return;
        }
        final UUID npcUuid;
        try {
            npcUuid = UUID.fromString(configuredUuid.trim());
        }
        catch (IllegalArgumentException ex) {
            return;
        }
        if (!event.getRightClicked().getUniqueId().equals(npcUuid)) {
            return;
        }
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final KowalMenu menu = this.plugin.createInstance(KowalMenu.class);
        menu.build(player).open(player);
    }

    private boolean isFancyNpcEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("FancyNpcs");
    }

    @Inject
    @Generated
    public FancyNpcController(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }
}
