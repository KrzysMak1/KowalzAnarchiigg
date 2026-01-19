package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.npc.NpcSelectionService;
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

    @EventHandler(ignoreCancelled = true)
    public void onNpcSelect(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        if (!this.npcSelectionService.consumeSelection(player)) {
            return;
        }
        this.pluginConfig.fancyNpcUuid = event.getRightClicked().getUniqueId().toString();
        this.pluginConfig.save();
        this.messageConfig.npcSet.send(player, java.util.Map.of("uuid", this.pluginConfig.fancyNpcUuid));
        event.setCancelled(true);
    }

    @Inject
    @Generated
    public NpcSelectionController(final PluginConfig pluginConfig, final MessageConfig messageConfig, final NpcSelectionService npcSelectionService) {
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.npcSelectionService = npcSelectionService;
    }
}
