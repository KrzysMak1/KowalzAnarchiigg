package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.menu.KowalMenu;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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
        final Player player = event.getPlayer();
        final ItemStack handSnapshot = player.getInventory().getItemInMainHand().clone();
        final EquipmentSlot armorSlot = resolveArmorSlot(handSnapshot.getType());
        final ItemStack previousArmor = armorSlot != null ? getArmorItem(player, armorSlot) : null;
        event.setCancelled(true);
        final KowalMenu menu = this.plugin.createInstance(KowalMenu.class);
        menu.build(player).open(player);
        if (armorSlot != null && previousArmor == null) {
            Bukkit.getScheduler().runTask(this.plugin, () -> restoreHandItem(player, handSnapshot, armorSlot));
        }
    }

    private boolean isFancyNpcEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("FancyNpcs");
    }

    private void restoreHandItem(final Player player, final ItemStack handSnapshot, final EquipmentSlot armorSlot) {
        final ItemStack currentHand = player.getInventory().getItemInMainHand();
        final ItemStack currentArmor = getArmorItem(player, armorSlot);
        if (currentArmor == null || currentHand.getType() != Material.AIR) {
            return;
        }
        if (!currentArmor.isSimilar(handSnapshot)) {
            return;
        }
        player.getInventory().setItemInMainHand(handSnapshot);
        setArmorItem(player, armorSlot, null);
    }

    private static EquipmentSlot resolveArmorSlot(final Material material) {
        if (material == null) {
            return null;
        }
        final String name = material.name();
        if (name.endsWith("_HELMET") || name.equals("TURTLE_HELMET")) {
            return EquipmentSlot.HEAD;
        }
        if (name.endsWith("_CHESTPLATE") || name.equals("ELYTRA")) {
            return EquipmentSlot.CHEST;
        }
        if (name.endsWith("_LEGGINGS")) {
            return EquipmentSlot.LEGS;
        }
        if (name.endsWith("_BOOTS")) {
            return EquipmentSlot.FEET;
        }
        return null;
    }

    private static ItemStack getArmorItem(final Player player, final EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> player.getInventory().getHelmet();
            case CHEST -> player.getInventory().getChestplate();
            case LEGS -> player.getInventory().getLeggings();
            case FEET -> player.getInventory().getBoots();
            default -> null;
        };
    }

    private static void setArmorItem(final Player player, final EquipmentSlot slot, final ItemStack item) {
        switch (slot) {
            case HEAD -> player.getInventory().setHelmet(item);
            case CHEST -> player.getInventory().setChestplate(item);
            case LEGS -> player.getInventory().setLeggings(item);
            case FEET -> player.getInventory().setBoots(item);
            default -> {
            }
        }
    }

    @Inject
    @Generated
    public FancyNpcController(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }
}
