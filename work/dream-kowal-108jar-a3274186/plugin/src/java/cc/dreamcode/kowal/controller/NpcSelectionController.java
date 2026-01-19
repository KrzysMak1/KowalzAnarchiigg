package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.npc.NpcSelectionService;
import cc.dreamcode.kowal.npc.NpcUuidRegistry;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NpcSelectionController implements Listener {
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final NpcSelectionService npcSelectionService;
    private final NpcUuidRegistry npcUuidRegistry;

    @EventHandler(ignoreCancelled = true)
    public void onNpcSelect(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        if (!this.npcSelectionService.consumeSelection(player)) {
            return;
        }
        final String uuidString = event.getRightClicked().getUniqueId().toString();
        if (this.pluginConfig.npc != null) {
            if (this.pluginConfig.npc.uuids == null) {
                this.pluginConfig.npc.uuids = new java.util.ArrayList<>();
            }
            if (!this.pluginConfig.npc.uuids.contains(uuidString)) {
                this.pluginConfig.npc.uuids.add(uuidString);
            }
        }
        this.pluginConfig.save();
        this.npcUuidRegistry.reload();
        this.messageConfig.npcSet.send(player, java.util.Map.of("uuid", uuidString));
        event.setCancelled(true);
    }

    @Inject
    @Generated
    public NpcSelectionController(final PluginConfig pluginConfig, final MessageConfig messageConfig, final NpcSelectionService npcSelectionService, final NpcUuidRegistry npcUuidRegistry) {
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.npcSelectionService = npcSelectionService;
        this.npcUuidRegistry = npcUuidRegistry;
    }
}
