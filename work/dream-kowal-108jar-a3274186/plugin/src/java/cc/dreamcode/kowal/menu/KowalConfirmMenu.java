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
import cc.dreamcode.kowal.util.UpgradeDataUtil;

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
        final int currentLevel = UpgradeDataUtil.getLevel((Plugin)this.plugin, hand);
        if (this.pluginConfig.kowalItems == null || !this.pluginConfig.kowalItems.containsKey((Object)hand.getType())) {
            return;
        }
        final String displayName = (String)UpgradeDataUtil.getDisplayName((Plugin)this.plugin, hand).orElse(this.pluginConfig.kowalItems.get((Object)hand.getType()));
        final boolean success = RandomUtil.chance(this.level.getChance());
        final int newLevel = success ? (currentLevel + 1) : (currentLevel - 1);
        if (success) {
            this.messageConfig.upgradeSuccess.send((CommandSender)clicked);
            clicked.playSound(clicked.getLocation(), this.pluginConfig.upgradeSuccess, 1.0f, 1.0f);
        }
        else {
            this.messageConfig.upgradeFailure.send((CommandSender)clicked);
            clicked.playSound(clicked.getLocation(), this.pluginConfig.upgradeFailure, 1.0f, 1.0f);
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
        final ItemBuilder newItem = ItemBuilder.of(hand).setName(newItemName).setLore(success ? currentLore : previousLore);
        String selectedEffect = "none";
        if (newLevel >= 7 && this.pluginConfig.effects != null) {
            final EffectType[] effects = EffectType.values();
            final EffectType random = effects[RandomUtil.nextInteger(effects.length)];
            final Effect randomEffect = (Effect)this.pluginConfig.effects.get((Object)random);
            if (randomEffect != null) {
                selectedEffect = random.getData();
                newItem.appendLore(randomEffect.getLore()).fixColors(Map.of("level", newLevel, "chance", randomEffect.getAmplifierChance()));
            }
        }
        if ("none".equals(selectedEffect)) {
            newItem.fixColors(Map.of("level", newLevel));
        }
        final ItemStack upgradedItem = newItem.toItemStack();
        UpgradeDataUtil.setLevel((Plugin)this.plugin, upgradedItem, newLevel);
        UpgradeDataUtil.setEffect((Plugin)this.plugin, upgradedItem, selectedEffect);
        clicked.getInventory().setItemInMainHand(upgradedItem);
    }
    
    private void removeItems(final Player player) {
        this.pluginHookManager.get(VaultHook.class).map(vaultHook -> vaultHook.withdraw(player, this.level.getMoneyUpgrade()));
        this.level.getUpgradeItems().forEach((item, amount) -> player.getInventory().removeItem(new ItemStack[] { new ItemStack(item, (int)amount) }));
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
