package cc.dreamcode.kowal.listener;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.menu.KowalMenu;
import cc.dreamcode.kowal.npc.NpcUuidRegistry;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class NpcInteractListener implements Listener {
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final NpcUuidRegistry npcUuidRegistry;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onNpcInteract(final PlayerInteractEntityEvent event) {
        if (this.pluginConfig.npc == null || !this.pluginConfig.npc.enabled) {
            return;
        }
        if (this.pluginConfig.npc.mainHandOnly && event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        final UUID clicked = event.getRightClicked().getUniqueId();
        if (!this.npcUuidRegistry.isNpc(clicked)) {
            return;
        }
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final KowalMenu kowalMenu = this.plugin.createInstance(KowalMenu.class);
        kowalMenu.build(player).open(player);
        Bukkit.getScheduler().runTask(this.plugin, player::updateInventory);
    }

    @Inject
    @Generated
    public NpcInteractListener(final KowalPlugin plugin, final PluginConfig pluginConfig, final NpcUuidRegistry npcUuidRegistry) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.npcUuidRegistry = npcUuidRegistry;
    }
}
