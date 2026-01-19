package cc.dreamcode.kowal.listener;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class KowalMenuCloseListener implements Listener {
    private final KowalPlugin plugin;
    private final CitizensBypassService bypassService;

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        if (!this.bypassService.hasPendingInput(player)) {
            return;
        }
        this.bypassService.markMenuClosed(player);
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            if (this.bypassService.isMenuOpen(player)) {
                return;
            }
            this.bypassService.returnPendingInputIfExists(player);
        });
    }

    @Inject
    @Generated
    public KowalMenuCloseListener(final KowalPlugin plugin, final CitizensBypassService bypassService) {
        this.plugin = plugin;
        this.bypassService = bypassService;
    }
}
