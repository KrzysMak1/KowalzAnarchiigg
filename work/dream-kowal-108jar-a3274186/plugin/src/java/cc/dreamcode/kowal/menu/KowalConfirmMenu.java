package cc.dreamcode.kowal.menu;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.hook.VaultHook;
import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.kowal.effect.EffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;
import cc.dreamcode.utilities.RandomUtil;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.function.Consumer;
import java.util.Map;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import org.bukkit.entity.Player;
import cc.dreamcode.menu.adventure.base.BukkitMenu;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.kowal.level.Level;
import cc.dreamcode.platform.bukkit.hook.PluginHookManager;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.menu.adventure.setup.BukkitMenuPlayerSetup;
import cc.dreamcode.kowal.util.UpgradeUtil;
import java.util.Locale;
import org.bukkit.Sound;

public class KowalConfirmMenu implements BukkitMenuPlayerSetup
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PluginHookManager pluginHookManager;
    private Level level;
    private KowalMenuMode mode;
    
    @Override
    public BukkitMenu build(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        if (!(humanEntity instanceof Player)) {
            throw new RuntimeException("humanEntity must be Player");
        }
        final BukkitMenuBuilder builder = this.pluginConfig.confirmMenu;
        final BukkitMenu bukkitMenu = builder.buildEmpty();
        bukkitMenu.setDisposeWhenClose(true);
        builder.getItems().forEach((slot, item) -> {
            if (this.pluginConfig.confirmCancelSlot == slot) {
                bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> event.getWhoClicked().closeInventory()));
                return;
            }
            bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack());
        });
        if (this.mode.equals((Object)KowalMenuMode.METAL)) {
            bukkitMenu.setItem(this.pluginConfig.confirmAcceptSlot, ItemBuilder.of(this.pluginConfig.confirmModeMetal).fixColors(Map.of("chance", this.level.getChance())).toItemStack(), (Consumer<InventoryClickEvent>)this::handleClick);
        }
        else {
            bukkitMenu.setItem(this.pluginConfig.confirmAcceptSlot, ItemBuilder.of(this.pluginConfig.confirmModeKamien).fixColors(Map.of("chance", this.level.getChance())).toItemStack(), (Consumer<InventoryClickEvent>)this::handleClick);
        }
        return bukkitMenu;
    }
    
    private void handleClick(final InventoryClickEvent event) {
        final Player clicked = (Player)event.getWhoClicked();
        clicked.closeInventory();
        if (this.level == null) {
            return;
        }
        this.removeItems(clicked);
        final ItemStack hand = clicked.getInventory().getItemInMainHand();
        final Object levelValue = ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "upgrade-level").orElse("0");
        final int currentLevel = UpgradeUtil.parseLevel(levelValue);
        if (this.pluginConfig.kowalItems == null || !this.pluginConfig.kowalItems.containsKey((Object)hand.getType())) {
            return;
        }
        final String displayName = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "display-name").orElse(this.pluginConfig.kowalItems.get((Object)hand.getType()));
        final boolean success = RandomUtil.chance(this.level.getChance());
        final int newLevel = success ? (currentLevel + 1) : (currentLevel - 1);
        if (success) {
            this.messageConfig.upgradeSuccess.send((CommandSender)clicked);
            this.playConfiguredSound(clicked, this.pluginConfig.upgradeSuccess);
        }
        else {
            this.messageConfig.upgradeFailure.send((CommandSender)clicked);
            this.playConfiguredSound(clicked, this.pluginConfig.upgradeFailure);
        }
        if (this.mode.equals((Object)KowalMenuMode.KAMIEN_KOWALSKI)) {
            clicked.getInventory().removeItem(new ItemStack[] { ItemBuilder.of(this.pluginConfig.kamienKowalski).setAmount(1).fixColors().toItemStack() });
            if (!success) {
                return;
            }
        }
        if (newLevel < 0) {
            return;
        }
        final String currentLore = this.level.getItemLoreDisplay();
        final String previousLore = (newLevel > 0 && this.pluginConfig.kowalLevels != null && this.pluginConfig.kowalLevels.get((Object)newLevel) != null) ? ((Level)this.pluginConfig.kowalLevels.get((Object)newLevel)).getItemLoreDisplay() : "";
        final String colorSuffix = (this.pluginConfig.kowalColors != null) ? (String)this.pluginConfig.kowalColors.get((Object)hand.getType()) : "";
        String newItemName = displayName + " " + colorSuffix;
        if (newLevel == 0) {
            newItemName = colorSuffix.isEmpty() ? displayName : newItemName.substring(0, newItemName.length() - colorSuffix.length());
        }
        final ItemBuilder newItem = ItemBuilder.of(hand).setName(newItemName).setLore(success ? currentLore : previousLore).withNbt((Plugin)this.plugin, "upgrade-level", String.valueOf(newLevel));
        if (newLevel >= 7) {
            final EffectType[] effects = EffectType.values();
            final EffectType random = effects[RandomUtil.nextInteger(effects.length)];
            final Effect randomEffect = (this.pluginConfig.effects != null) ? (Effect)this.pluginConfig.effects.get((Object)random) : null;
            if (randomEffect != null) {
                newItem.withNbt((Plugin)this.plugin, "upgrade-effect", random.getData()).appendLore(randomEffect.getLore()).fixColors(Map.of("level", newLevel, "chance", randomEffect.getAmplifierChance()));
            }
        }
        else {
            newItem.fixColors(Map.of("level", newLevel));
        }
        final ItemStack upgradedItem = newItem.toItemStack();
        ItemNbtUtil.setValue((Plugin)this.plugin, upgradedItem, "upgrade-level", String.valueOf(newLevel));
        if (newLevel < 7) {
            ItemNbtUtil.setValue((Plugin)this.plugin, upgradedItem, "upgrade-effect", "none");
        }
        clicked.getInventory().setItemInMainHand(upgradedItem);
    }
    
    private void removeItems(final Player player) {
        this.pluginHookManager.get(VaultHook.class).map(vaultHook -> vaultHook.withdraw(player, this.level.getMoneyUpgrade()));
        this.level.getUpgradeItems().forEach((item, amount) -> player.getInventory().removeItem(new ItemStack[] { new ItemStack(item, (int)amount) }));
    }

    private void playConfiguredSound(final Player player, final String soundKey) {
        if (soundKey == null || soundKey.isBlank()) {
            return;
        }
        try {
            final Sound sound = Sound.valueOf(soundKey);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            return;
        }
        catch (IllegalArgumentException ignored) {
        }
        final String normalized = normalizeSoundKey(soundKey);
        try {
            player.playSound(player.getLocation(), normalized, 1.0f, 1.0f);
        }
        catch (RuntimeException ex) {
            this.plugin.getLogger().warning("Invalid sound key: " + soundKey + " (normalized: " + normalized + ")");
        }
    }

    private static String normalizeSoundKey(final String soundKey) {
        final String trimmed = soundKey.trim();
        if (trimmed.contains(":")) {
            final String[] parts = trimmed.split(":", 2);
            final String namespace = parts[0].toLowerCase(Locale.ROOT);
            final String path = parts[1].toLowerCase(Locale.ROOT).replace('_', '.');
            return namespace + ":" + path;
        }
        return trimmed.toLowerCase(Locale.ROOT).replace('_', '.');
    }
    
    @Inject
    @Generated
    public KowalConfirmMenu(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig, final PluginHookManager pluginHookManager) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.pluginHookManager = pluginHookManager;
    }
    
    @Generated
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    @Generated
    public void setMode(final KowalMenuMode mode) {
        this.mode = mode;
    }
}
