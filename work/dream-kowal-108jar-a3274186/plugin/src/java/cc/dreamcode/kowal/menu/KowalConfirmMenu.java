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
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import cc.dreamcode.menu.adventure.setup.BukkitMenuPlayerSetup;
import cc.dreamcode.kowal.util.UpgradeUtil;
import cc.dreamcode.kowal.util.MiniMessageUtil;
import java.util.Locale;
import org.bukkit.Sound;
import cc.dreamcode.kowal.economy.PaymentMode;

public class KowalConfirmMenu implements BukkitMenuPlayerSetup
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PluginHookManager pluginHookManager;
    private final CitizensBypassService bypassService;
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
        final BukkitMenuBuilder builder = this.pluginConfig.menus.confirm;
        final BukkitMenu bukkitMenu = builder.buildEmpty();
        bukkitMenu.setDisposeWhenClose(true);
        builder.getItems().forEach((slot, item) -> {
            if (this.pluginConfig.slots.confirmCancel == slot) {
                bukkitMenu.setItem((int)slot, MiniMessageUtil.applyItemColors(item).toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                    final KowalMenu kowalMenu = this.plugin.createInstance(KowalMenu.class);
                    kowalMenu.setMode(this.mode);
                    this.bypassService.markMenuOpen((Player)event.getWhoClicked());
                    kowalMenu.build(event.getWhoClicked()).open(event.getWhoClicked());
                }));
                return;
            }
            bukkitMenu.setItem((int)slot, MiniMessageUtil.applyItemColors(item).toItemStack());
        });
        if (this.mode.equals((Object)KowalMenuMode.METAL)) {
            bukkitMenu.setItem(this.pluginConfig.slots.confirmAccept, MiniMessageUtil.applyItemColors(this.pluginConfig.menus.confirmMetalItem, Map.of("chance", this.level.getChance())).toItemStack(), (Consumer<InventoryClickEvent>)this::handleClick);
        }
        else {
            bukkitMenu.setItem(this.pluginConfig.slots.confirmAccept, MiniMessageUtil.applyItemColors(this.pluginConfig.menus.confirmKamienItem, Map.of("chance", this.level.getChance())).toItemStack(), (Consumer<InventoryClickEvent>)this::handleClick);
        }
        return bukkitMenu;
    }
    
    private void handleClick(final InventoryClickEvent event) {
        final Player clicked = (Player)event.getWhoClicked();
        final CitizensBypassService.PendingInput pending = this.bypassService.consumePendingInput(clicked);
        clicked.closeInventory();
        if (this.level == null) {
            if (pending != null) {
                this.bypassService.returnPendingInput(clicked, pending);
            }
            return;
        }
        this.removeItems(clicked);
        final ItemStack hand = pending != null ? pending.item() : clicked.getInventory().getItemInMainHand();
        final Object levelValue = ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "upgrade-level").orElse("0");
        final int currentLevel = UpgradeUtil.parseLevel(levelValue);
        if (this.pluginConfig.items == null || this.pluginConfig.items.names == null
                || !this.pluginConfig.items.names.containsKey((Object)hand.getType())) {
            if (pending != null) {
                this.bypassService.returnPendingInput(clicked, pending);
            }
            return;
        }
        final String displayName = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "display-name")
                .orElse(this.pluginConfig.items.names.get((Object)hand.getType()));
        final boolean success = RandomUtil.chance(this.level.getChance());
        final int newLevel = success ? (currentLevel + 1) : (currentLevel - 1);
        if (success) {
            this.messageConfig.upgradeSuccess.send((CommandSender)clicked);
            this.playConfiguredSound(clicked, this.pluginConfig.sounds.upgradeSuccess);
        }
        else {
            this.messageConfig.upgradeFailure.send((CommandSender)clicked);
            this.playConfiguredSound(clicked, this.pluginConfig.sounds.upgradeFailure);
        }
        if (this.mode.equals((Object)KowalMenuMode.KAMIEN_KOWALSKI)) {
            clicked.getInventory().removeItem(new ItemStack[] { MiniMessageUtil.applyItemColors(ItemBuilder.of(this.pluginConfig.items.kamienKowalski).setAmount(1), Map.of()).toItemStack() });
            if (!success) {
                if (pending != null) {
                    this.bypassService.returnPendingInput(clicked, pending);
                }
                return;
            }
        }
        if (newLevel < 0) {
            if (pending != null) {
                this.bypassService.returnPendingInput(clicked, pending);
            }
            return;
        }
        final String currentLore = this.level.getItemLoreDisplay();
        final String previousLore = (newLevel > 0 && this.pluginConfig.levels != null && this.pluginConfig.levels.get((Object)newLevel) != null)
                ? ((Level)this.pluginConfig.levels.get((Object)newLevel)).getItemLoreDisplay()
                : "";
        final String colorSuffix = (this.pluginConfig.items.colors != null)
                ? (String)this.pluginConfig.items.colors.get((Object)hand.getType())
                : "";
        final String newItemName = UpgradeUtil.buildUpgradeName(displayName, colorSuffix, newLevel);
        final ItemBuilder newItem = ItemBuilder.of(hand).setName(newItemName).setLore(success ? currentLore : previousLore).withNbt((Plugin)this.plugin, "upgrade-level", String.valueOf(newLevel));
        if (newLevel >= 7) {
            final EffectType[] effects = EffectType.values();
            final EffectType random = effects[RandomUtil.nextInteger(effects.length)];
            final Effect randomEffect = (this.pluginConfig.effects != null && this.pluginConfig.effects.list != null)
                    ? (Effect)this.pluginConfig.effects.list.get((Object)random)
                    : null;
            if (randomEffect != null) {
                MiniMessageUtil.applyItemColors(newItem.withNbt((Plugin)this.plugin, "upgrade-effect", random.getData()).appendLore(randomEffect.getLore()), Map.of("level", newLevel, "chance", randomEffect.getAmplifierChance()));
            }
        }
        else {
            MiniMessageUtil.applyItemColors(newItem, Map.of("level", newLevel));
        }
        final ItemStack upgradedItem = newItem.toItemStack();
        ItemNbtUtil.setValue((Plugin)this.plugin, upgradedItem, "upgrade-level", String.valueOf(newLevel));
        if (newLevel < 7) {
            ItemNbtUtil.setValue((Plugin)this.plugin, upgradedItem, "upgrade-effect", "none");
        }
        if (pending != null) {
            this.bypassService.placePendingItem(clicked, new CitizensBypassService.PendingInput(upgradedItem, pending.originSlot()));
            this.bypassService.logUpgradeConsumed(clicked);
        }
        else {
            clicked.getInventory().setItemInMainHand(upgradedItem);
            clicked.updateInventory();
        }
    }
    
    private void removeItems(final Player player) {
        final PaymentMode paymentMode = this.pluginConfig.resolvePaymentMode(this.plugin.getLogger());
        if (paymentMode != null && paymentMode.usesMoney() && this.level.hasMoneyUpgrade()) {
            this.pluginHookManager.get(VaultHook.class).map(vaultHook -> vaultHook.withdraw(player, this.level.getMoneyUpgrade()));
        }
        if (paymentMode == null || !paymentMode.usesItems() || !this.level.hasUpgradeItems()) {
            return;
        }
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
    public KowalConfirmMenu(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig, final PluginHookManager pluginHookManager, final CitizensBypassService bypassService) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.pluginHookManager = pluginHookManager;
        this.bypassService = bypassService;
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
