package cc.dreamcode.kowal.citizens;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class CitizensBypassService {
    private static final long BYPASS_DURATION_MS = 1500L;
    private static final long DEBOUNCE_MS = 150L;
    private final Map<UUID, BypassEntry> bypassEntries;
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;

    public void markBypass(final Player player) {
        if (player == null) {
            return;
        }
        final UUID uuid = player.getUniqueId();
        final long now = System.currentTimeMillis();
        final BypassEntry existing = this.bypassEntries.get(uuid);
        if (existing != null && now - existing.createdAt() < DEBOUNCE_MS) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        final int heldSlot = inventory.getHeldItemSlot();
        final ItemStack handBefore = this.cloneSnapshot(inventory.getItem(heldSlot));
        final ArmorSlot armorSlot = this.resolveArmorSlot(handBefore);
        final ItemStack armorBefore = armorSlot != null ? this.cloneSnapshot(this.getArmorItem(inventory, armorSlot)) : null;
        this.bypassEntries.put(uuid, new BypassEntry(now + BYPASS_DURATION_MS, now, heldSlot, handBefore, armorSlot, armorBefore));
    }

    public boolean scheduleNpcOpenIfActive(final Player player, final Consumer<ItemStack> openAction) {
        if (player == null) {
            return false;
        }
        final UUID uuid = player.getUniqueId();
        final BypassEntry entry = this.bypassEntries.get(uuid);
        if (entry == null) {
            return false;
        }
        if (System.currentTimeMillis() > entry.expiresAt()) {
            this.bypassEntries.remove(uuid);
            return false;
        }
        this.bypassEntries.remove(uuid);
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> this.applyBypassAfterTick(player, entry, openAction));
        return true;
    }

    private void applyBypassAfterTick(final Player player, final BypassEntry entry, final Consumer<ItemStack> openAction) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack handBefore = entry.handBefore();
        if (this.isAir(handBefore)) {
            return;
        }
        final int heldSlot = entry.heldSlot();
        final ItemStack handNow = inventory.getItem(heldSlot);
        final ArmorSlot expectedArmorSlot = entry.armorSlot();
        ItemStack armorNow = expectedArmorSlot != null ? this.getArmorItem(inventory, expectedArmorSlot) : null;
        Source source = null;
        if (expectedArmorSlot != null && this.isSimilarItem(armorNow, handBefore)) {
            source = new Source(SourceType.ARMOR, expectedArmorSlot, armorNow);
        } else if (this.isSimilarItem(handNow, handBefore)) {
            source = new Source(SourceType.HAND, null, handNow);
        } else {
            final ArmorSlot fallbackSlot = this.findArmorSlot(inventory, handBefore);
            if (fallbackSlot != null) {
                armorNow = this.getArmorItem(inventory, fallbackSlot);
                source = new Source(SourceType.ARMOR, fallbackSlot, armorNow);
            }
        }
        if (source == null || this.isAir(source.item())) {
            return;
        }
        if (source.type() == SourceType.ARMOR) {
            this.logDebug("Wykryto auto-equip (zrodlo=ARMOR) dla gracza " + player.getName() + ".");
            this.setArmorItem(inventory, source.armorSlot(), entry.armorBefore());
            inventory.setItem(heldSlot, new ItemStack(Material.AIR));
        } else {
            this.logDebug("Wykryto wejscie z reki (zrodlo=HAND) dla gracza " + player.getName() + ".");
            inventory.setItem(heldSlot, new ItemStack(Material.AIR));
        }
        openAction.accept(source.item());
        player.updateInventory();
    }

    private ArmorSlot resolveArmorSlot(final ItemStack itemStack) {
        if (this.isAir(itemStack)) {
            return null;
        }
        final String name = itemStack.getType().name();
        if (name.endsWith("_HELMET")) {
            return ArmorSlot.HELMET;
        }
        if (name.endsWith("_CHESTPLATE") || itemStack.getType() == Material.ELYTRA) {
            return ArmorSlot.CHESTPLATE;
        }
        if (name.endsWith("_LEGGINGS")) {
            return ArmorSlot.LEGGINGS;
        }
        if (name.endsWith("_BOOTS")) {
            return ArmorSlot.BOOTS;
        }
        return null;
    }

    private ArmorSlot findArmorSlot(final PlayerInventory inventory, final ItemStack snapshot) {
        if (this.isSimilarItem(snapshot, inventory.getHelmet())) {
            return ArmorSlot.HELMET;
        }
        if (this.isSimilarItem(snapshot, inventory.getChestplate())) {
            return ArmorSlot.CHESTPLATE;
        }
        if (this.isSimilarItem(snapshot, inventory.getLeggings())) {
            return ArmorSlot.LEGGINGS;
        }
        if (this.isSimilarItem(snapshot, inventory.getBoots())) {
            return ArmorSlot.BOOTS;
        }
        return null;
    }

    private ItemStack getArmorItem(final PlayerInventory inventory, final ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> inventory.getHelmet();
            case CHESTPLATE -> inventory.getChestplate();
            case LEGGINGS -> inventory.getLeggings();
            case BOOTS -> inventory.getBoots();
        };
    }

    private void setArmorItem(final PlayerInventory inventory, final ArmorSlot slot, final ItemStack itemStack) {
        final ItemStack item = this.isAir(itemStack) ? new ItemStack(Material.AIR) : itemStack;
        switch (slot) {
            case HELMET -> inventory.setHelmet(item);
            case CHESTPLATE -> inventory.setChestplate(item);
            case LEGGINGS -> inventory.setLeggings(item);
            case BOOTS -> inventory.setBoots(item);
        }
    }

    private boolean isSimilarItem(final ItemStack first, final ItemStack second) {
        if (this.isAir(first) || this.isAir(second)) {
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

    private void logDebug(final String message) {
        final PluginConfig.CitizensSettings citizensSettings = this.pluginConfig.citizens;
        if (citizensSettings != null && citizensSettings.debug) {
            this.plugin.getLogger().info(message);
        }
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

    private enum SourceType {
        HAND,
        ARMOR
    }

    private record Source(SourceType type, ArmorSlot armorSlot, ItemStack item) {
    }

    private record BypassEntry(long expiresAt, long createdAt, int heldSlot, ItemStack handBefore, ArmorSlot armorSlot, ItemStack armorBefore) {
    }
}
