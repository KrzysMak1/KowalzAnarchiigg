package cc.dreamcode.kowal.menu;

import cc.dreamcode.utilities.bukkit.StringColorUtil;
import org.bukkit.command.CommandSender;
import java.util.Map;
import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.hook.VaultHook;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import cc.dreamcode.kowal.level.Level;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.function.Consumer;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import org.bukkit.entity.Player;
import cc.dreamcode.menu.adventure.base.BukkitMenu;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.platform.bukkit.hook.PluginHookManager;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.menu.adventure.setup.BukkitMenuPlayerSetup;
import cc.dreamcode.kowal.util.UpgradeUtil;

public class KowalMenu implements BukkitMenuPlayerSetup
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PluginHookManager pluginHookManager;
    private KowalMenuMode mode;
    
    @Override
    public BukkitMenu build(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        if (!(humanEntity instanceof Player)) {
            throw new RuntimeException("humanEntity must be Player");
        }
        final Player player = (Player)humanEntity;
        final BukkitMenuBuilder builder = this.pluginConfig.kowalMenu;
        final BukkitMenu bukkitMenu = builder.buildEmpty();
        bukkitMenu.setDisposeWhenClose(true);
        final ItemStack hand = humanEntity.getInventory().getItemInMainHand();
        final String levelString = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "upgrade-level").orElse("0");
        final int currentLevel = UpgradeUtil.parseLevel(levelString);
        if (hand.getType().equals((Object)Material.AIR) || this.pluginConfig.kowalItems == null || !this.pluginConfig.kowalItems.containsKey((Object)hand.getType()) || currentLevel >= 7) {
            bukkitMenu.setItem(this.pluginConfig.upgradeItemSlot, ItemBuilder.of(this.pluginConfig.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        if (this.mode.equals((Object)KowalMenuMode.METAL)) {
            bukkitMenu.setItem(this.pluginConfig.modeSlot, ItemBuilder.of(this.pluginConfig.modeMetal).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                if (!event.getWhoClicked().getInventory().containsAtLeast(ItemBuilder.of(this.pluginConfig.kamienKowalski).fixColors().toItemStack(), 1)) {
                    event.getWhoClicked().closeInventory();
                    this.messageConfig.kamienRequired.send((CommandSender)event.getWhoClicked());
                    return;
                }
                this.mode = KowalMenuMode.KAMIEN_KOWALSKI;
                this.build(event.getWhoClicked()).open(event.getWhoClicked());
            }));
        }
        else {
            bukkitMenu.setItem(this.pluginConfig.modeSlot, ItemBuilder.of(this.pluginConfig.modeKamien).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                this.mode = KowalMenuMode.METAL;
                this.build(event.getWhoClicked()).open(event.getWhoClicked());
            }));
        }
        if (this.pluginConfig.kowalLevels == null) {
            bukkitMenu.setItem(this.pluginConfig.upgradeItemSlot, ItemBuilder.of(this.pluginConfig.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        final Level level = (Level)this.pluginConfig.kowalLevels.get((Object)(currentLevel + 1));
        if (level == null) {
            bukkitMenu.setItem(this.pluginConfig.upgradeItemSlot, ItemBuilder.of(this.pluginConfig.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        builder.getItems().forEach((slot, item) -> {
            if (this.pluginConfig.upgradeCancelSlot == slot) {
                bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> event.getWhoClicked().closeInventory()));
                return;
            }
            if (this.pluginConfig.upgradeAcceptSlot == slot) {
                bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors(Map.of("level", currentLevel, "new", currentLevel + 1, "items", level.getUpgradeItemsLore(), "cost", level.getCostLore(), "status", this.canAfford(player, level) ? this.pluginConfig.canUpgradeStatus : this.pluginConfig.cannotUpgradeStatus)).toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                    if (!this.canAfford(player, level)) {
                        player.closeInventory();
                        this.messageConfig.cannotAfford.send((CommandSender)player);
                    }
                    else {
                        final KowalConfirmMenu kowalConfirmMenu = this.plugin.createInstance(KowalConfirmMenu.class);
                        kowalConfirmMenu.setLevel(level);
                        kowalConfirmMenu.setMode(this.mode);
                        if (currentLevel == 0 && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && !ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "display-name").isPresent()) {
                            ItemNbtUtil.setValue((Plugin)this.plugin, hand, "display-name", StringColorUtil.breakColor(hand.getItemMeta().getDisplayName()));
                        }
                        kowalConfirmMenu.build(event.getWhoClicked()).open(event.getWhoClicked());
                    }
                }));
                return;
            }
            bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack());
        });
        bukkitMenu.setItem(this.pluginConfig.upgradeItemSlot, humanEntity.getInventory().getItemInMainHand());
        return bukkitMenu;
    }
    
    private boolean canAfford(final Player player, final Level level) {
        boolean afford = true;
        for (final Map.Entry<Material, Integer> entry : level.getUpgradeItems().entrySet()) {
            if (!player.getInventory().containsAtLeast(new ItemStack((Material)entry.getKey()), (int)entry.getValue())) {
                afford = false;
                break;
            }
        }
        final double money = (double)this.pluginHookManager.get(VaultHook.class).map(vaultHook -> (Double)vaultHook.getMoney(player).orElse(0.0)).orElse(0.0);
        if (money < level.getMoneyUpgrade()) {
            afford = false;
        }
        return afford;
    }
    
    @Inject
    @Generated
    public KowalMenu(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig, final PluginHookManager pluginHookManager) {
        this.mode = KowalMenuMode.METAL;
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.pluginHookManager = pluginHookManager;
    }
}
