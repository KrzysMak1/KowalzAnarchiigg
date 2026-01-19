package cc.dreamcode.kowal.citizens;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CitizensBypassService {
    private static final long BYPASS_DURATION_MS = 1500L;
    private final Map<UUID, BypassEntry> bypassEntries;
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;

    public void markBypass(final Player player) {
        if (player == null) {
            return;
        }
        final ItemStack snapshot = this.cloneSnapshot(player.getInventory().getItemInMainHand());
        this.bypassEntries.put(player.getUniqueId(), new BypassEntry(System.currentTimeMillis() + BYPASS_DURATION_MS, snapshot));
    }

    public void applyBypassIfActive(final Player player) {
        if (player == null) {
            return;
        }
        final UUID uuid = player.getUniqueId();
        final BypassEntry entry = this.bypassEntries.get(uuid);
        if (entry == null) {
            return;
        }
        if (System.currentTimeMillis() > entry.expiresAt()) {
            this.bypassEntries.remove(uuid);
            return;
        }
        this.bypassEntries.remove(uuid);
        if (this.isDebugEnabled()) {
            this.plugin.getLogger().info("Bypass auto-equip aktywny dla gracza " + player.getName() + ". Pomijam auto-equip.");
        }
        final ItemStack snapshot = entry.snapshot();
        if (this.isAir(snapshot)) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        final ItemStack currentMain = inventory.getItemInMainHand();
        if (!this.shouldRecoverAutoEquip(snapshot, currentMain)) {
            return;
        }
        this.recoverAutoEquippedItem(player, snapshot, currentMain);
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

    private boolean isDebugEnabled() {
        final PluginConfig.CitizensSettings citizensSettings = this.pluginConfig.citizens;
        return citizensSettings != null && citizensSettings.debug;
    }

    @Inject
    @Generated
    public CitizensBypassService(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.bypassEntries = new ConcurrentHashMap<>();
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
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

    private record BypassEntry(long expiresAt, ItemStack snapshot) {
    }
}
