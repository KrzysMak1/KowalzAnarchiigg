package cc.dreamcode.kowal.listener;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.menu.KowalMenu;
import de.oliver.fancynpcs.api.actions.ActionTrigger;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import de.oliver.fancynpcs.api.events.NpcInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcInteractListener implements Listener {
    private static final long CLICK_DEBOUNCE_MS = 150L;
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final Map<UUID, Long> lastClickMillis;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onNpcInteract(final NpcInteractEvent event) {
        final PluginConfig.Settings settings = this.pluginConfig.settings;
        if (settings == null || settings.npcId == null || settings.npcId.isBlank()) {
            return;
        }
        if (event.getInteractionType() != ActionTrigger.RIGHT_CLICK) {
            return;
        }
        if (!settings.npcId.equalsIgnoreCase(event.getNpc().getData().getId())) {
            return;
        }
        final Player player = event.getPlayer();
        if (this.isDebounced(player)) {
            return;
        }
        event.setCancelled(true);
        final ItemStack snapshot = this.cloneSnapshot(player.getInventory().getItemInMainHand());
        Bukkit.getScheduler().runTask(this.plugin, () -> this.openMenuSafely(player, snapshot));
    }

    private void openMenuSafely(final Player player, final ItemStack snapshot) {
        if (!player.isOnline()) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        final ItemStack currentMain = inventory.getItemInMainHand();
        if (this.shouldRecoverAutoEquip(snapshot, currentMain)) {
            this.recoverAutoEquippedItem(player, snapshot, currentMain);
        }
        final KowalMenu kowalMenu = this.plugin.createInstance(KowalMenu.class);
        kowalMenu.build(player).open(player);
        player.updateInventory();
    }

    private void recoverAutoEquippedItem(final Player player, final ItemStack snapshot, final ItemStack currentMain) {
        if (!this.isKowalSetItem(snapshot)) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        final ArmorMatch match = this.findArmorMatch(inventory, snapshot);
        if (match == null) {
            return;
        }
        match.clear(inventory);
        if (!this.isAir(currentMain) && !this.isSameItem(currentMain, match.item())) {
            inventory.addItem(currentMain).values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        }
        inventory.setItemInMainHand(match.item());
    }

    private ArmorMatch findArmorMatch(final PlayerInventory inventory, final ItemStack snapshot) {
        if (this.isSameItem(snapshot, inventory.getHelmet()) && this.isKowalSetItem(inventory.getHelmet())) {
            return new ArmorMatch(ArmorSlot.HELMET, inventory.getHelmet());
        }
        if (this.isSameItem(snapshot, inventory.getChestplate()) && this.isKowalSetItem(inventory.getChestplate())) {
            return new ArmorMatch(ArmorSlot.CHESTPLATE, inventory.getChestplate());
        }
        if (this.isSameItem(snapshot, inventory.getLeggings()) && this.isKowalSetItem(inventory.getLeggings())) {
            return new ArmorMatch(ArmorSlot.LEGGINGS, inventory.getLeggings());
        }
        if (this.isSameItem(snapshot, inventory.getBoots()) && this.isKowalSetItem(inventory.getBoots())) {
            return new ArmorMatch(ArmorSlot.BOOTS, inventory.getBoots());
        }
        return null;
    }

    private boolean shouldRecoverAutoEquip(final ItemStack snapshot, final ItemStack currentMain) {
        if (this.isAir(snapshot)) {
            return false;
        }
        if (this.isSameItem(snapshot, currentMain)) {
            return false;
        }
        return this.isAir(currentMain) || !this.isSameItem(snapshot, currentMain);
    }

    private boolean isKowalSetItem(final ItemStack itemStack) {
        if (this.isAir(itemStack)) {
            return false;
        }
        return ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, itemStack, "upgrade-level").isPresent()
            || ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, itemStack, "upgrade-effect").isPresent()
            || ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, itemStack, "display-name").isPresent();
    }

    private boolean isSameItem(final ItemStack first, final ItemStack second) {
        if (this.isAir(first) || this.isAir(second)) {
            return false;
        }
        if (first.getType() != second.getType()) {
            return false;
        }
        return first.isSimilar(second);
    }

    private boolean isAir(final ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    private ItemStack cloneSnapshot(final ItemStack itemStack) {
        if (this.isAir(itemStack)) {
            return null;
        }
        return itemStack.clone();
    }

    private boolean isDebounced(final Player player) {
        final long now = System.currentTimeMillis();
        final Long lastClick = this.lastClickMillis.get(player.getUniqueId());
        if (lastClick != null && now - lastClick < CLICK_DEBOUNCE_MS) {
            return true;
        }
        this.lastClickMillis.put(player.getUniqueId(), now);
        return false;
    }

    @Inject
    @Generated
    public NpcInteractListener(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.lastClickMillis = new HashMap<>();
    }

    private enum ArmorSlot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

    private record ArmorMatch(ArmorSlot slot, ItemStack item) {
        void clear(final PlayerInventory inventory) {
            switch (this.slot) {
                case HELMET -> inventory.setHelmet(new ItemStack(Material.AIR));
                case CHESTPLATE -> inventory.setChestplate(new ItemStack(Material.AIR));
                case LEGGINGS -> inventory.setLeggings(new ItemStack(Material.AIR));
                case BOOTS -> inventory.setBoots(new ItemStack(Material.AIR));
            }
        }
    }
}
