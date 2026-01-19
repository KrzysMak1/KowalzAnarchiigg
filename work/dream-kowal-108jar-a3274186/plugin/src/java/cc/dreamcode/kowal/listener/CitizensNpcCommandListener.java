package cc.dreamcode.kowal.listener;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import cc.dreamcode.kowal.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import net.citizensnpcs.api.event.NPCCommandDispatchEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class CitizensNpcCommandListener implements Listener {
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final CitizensBypassService bypassService;

    @EventHandler(ignoreCancelled = true)
    public void onNpcCommandDispatch(final NPCCommandDispatchEvent event) {
        final PluginConfig.CitizensSettings citizensSettings = this.pluginConfig.citizens;
        if (citizensSettings == null || !citizensSettings.enabled) {
            return;
        }
        final List<Integer> bypassIds = citizensSettings.bypassSetNpcIds;
        if (bypassIds == null || bypassIds.isEmpty()) {
            return;
        }
        final int npcId = event.getNPC().getId();
        if (!bypassIds.contains(npcId)) {
            return;
        }
        if (citizensSettings.debug) {
            this.plugin.getLogger().info("Wykryto NPC Citizens z listy bypass (ID: " + npcId + ").");
        }
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        this.bypassService.markBypass(player);
        if (citizensSettings.debug) {
            this.plugin.getLogger().info("Ustawiono bypass auto-equip dla gracza " + player.getName() + ".");
        }
    }

    @Inject
    @Generated
    public CitizensNpcCommandListener(final KowalPlugin plugin, final PluginConfig pluginConfig, final CitizensBypassService bypassService) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.bypassService = bypassService;
    }
}
