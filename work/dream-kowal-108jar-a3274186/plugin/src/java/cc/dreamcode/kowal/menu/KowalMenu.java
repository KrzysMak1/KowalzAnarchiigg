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
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import cc.dreamcode.menu.adventure.setup.BukkitMenuPlayerSetup;
import cc.dreamcode.kowal.util.UpgradeUtil;
import cc.dreamcode.kowal.economy.PaymentMode;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class KowalMenu implements BukkitMenuPlayerSetup
{
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)[&§][0-9A-FK-OR]");
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final PluginHookManager pluginHookManager;
    private final CitizensBypassService bypassService;
    private KowalMenuMode mode;
    private @Nullable ItemStack input;
    
    @Override
    public BukkitMenu build(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        if (!(humanEntity instanceof Player)) {
            throw new RuntimeException("humanEntity must be Player");
        }
        final Player player = (Player)humanEntity;
        final BukkitMenuBuilder builder = this.pluginConfig.menus.kowal;
        final BukkitMenu bukkitMenu = builder.buildEmpty();
        bukkitMenu.setDisposeWhenClose(true);
        final ItemStack hand = this.input != null ? this.input : humanEntity.getInventory().getItemInMainHand();
        final Object levelValue = ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, hand, "upgrade-level").orElse("0");
        final int currentLevel = UpgradeUtil.parseLevel(levelValue);
        if (hand.getType().equals((Object)Material.AIR)
                || this.pluginConfig.items == null
                || this.pluginConfig.items.names == null
                || !this.pluginConfig.items.names.containsKey((Object)hand.getType())
                || currentLevel >= 7) {
            bukkitMenu.setItem(this.pluginConfig.slots.upgradeItem, ItemBuilder.of(this.pluginConfig.menus.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        if (this.mode.equals((Object)KowalMenuMode.METAL)) {
            bukkitMenu.setItem(this.pluginConfig.slots.mode, ItemBuilder.of(this.pluginConfig.menus.modeMetal).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                if (!event.getWhoClicked().getInventory().containsAtLeast(ItemBuilder.of(this.pluginConfig.items.kamienKowalski).fixColors().toItemStack(), 1)) {
                    event.getWhoClicked().closeInventory();
                    this.messageConfig.kamienRequired.send((CommandSender)event.getWhoClicked());
                    return;
                }
                this.mode = KowalMenuMode.KAMIEN_KOWALSKI;
                this.bypassService.markMenuOpen((Player)event.getWhoClicked());
                this.build(event.getWhoClicked()).open(event.getWhoClicked());
            }));
        }
        else {
            bukkitMenu.setItem(this.pluginConfig.slots.mode, ItemBuilder.of(this.pluginConfig.menus.modeKamien).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                this.mode = KowalMenuMode.METAL;
                this.bypassService.markMenuOpen((Player)event.getWhoClicked());
                this.build(event.getWhoClicked()).open(event.getWhoClicked());
            }));
        }
        if (this.pluginConfig.levels == null) {
            bukkitMenu.setItem(this.pluginConfig.slots.upgradeItem, ItemBuilder.of(this.pluginConfig.menus.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        final Level level = (Level)this.pluginConfig.levels.get((Object)(currentLevel + 1));
        if (level == null) {
            bukkitMenu.setItem(this.pluginConfig.slots.upgradeItem, ItemBuilder.of(this.pluginConfig.menus.notUpgradeable).fixColors().toItemStack());
            return bukkitMenu;
        }
        builder.getItems().forEach((slot, item) -> {
            if (this.pluginConfig.slots.upgradeCancel == slot) {
                bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack(), (Consumer<InventoryClickEvent>)(event -> event.getWhoClicked().closeInventory()));
                return;
            }
            if (this.pluginConfig.slots.upgradeAccept == slot) {
                final PaymentMode paymentMode = this.pluginConfig.resolvePaymentMode(this.plugin.getLogger());
                final boolean hasRequiredItems = this.hasRequiredItems(player, level, paymentMode);
                final boolean hasRequiredMoney = this.hasRequiredMoney(player, level, paymentMode);
                final String missingItems = this.buildMissingItems(player, level, paymentMode);
                final String missingMoney = this.buildMissingMoney(player, level, paymentMode);
                final String status = this.resolveStatus(hasRequiredItems, hasRequiredMoney, paymentMode, missingItems, missingMoney);
                final String costLine = this.buildCostLine(level, paymentMode, hasRequiredMoney);
                final List<String> itemsLore = this.buildRequirementsLore(player, level, paymentMode);
                final Map<String, String> placeholders = Map.of(
                        "level", String.valueOf(currentLevel),
                        "new", String.valueOf(currentLevel + 1),
                        "cost", costLine == null ? "" : costLine,
                        "status", status == null ? "" : status,
                        "missingItems", missingItems,
                        "missingMoney", missingMoney);
                bukkitMenu.setItem((int)slot, this.applyUpgradePlaceholders(item, placeholders, itemsLore).toItemStack(), (Consumer<InventoryClickEvent>)(event -> {
                    if (!this.canAfford(player, level, paymentMode)) {
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
                        this.bypassService.markMenuOpen(player);
                        kowalConfirmMenu.build(event.getWhoClicked()).open(event.getWhoClicked());
                    }
                }));
                return;
            }
            bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack());
        });
        final ItemStack inputItem = this.input != null ? this.input : humanEntity.getInventory().getItemInMainHand().clone();
        bukkitMenu.setItem(this.pluginConfig.slots.upgradeItem, inputItem, (Consumer<InventoryClickEvent>)(event -> event.setCancelled(true)));
        return bukkitMenu;
    }
    
    private boolean canAfford(final Player player, final Level level, final PaymentMode paymentMode) {
        return this.hasRequiredItems(player, level, paymentMode) && this.hasRequiredMoney(player, level, paymentMode);
    }

    private boolean hasRequiredItems(final Player player, final Level level, final PaymentMode paymentMode) {
        if (paymentMode == null || !paymentMode.usesItems()) {
            return true;
        }
        if (!level.hasUpgradeItems()) {
            return true;
        }
        for (final Map.Entry<Material, Integer> entry : level.getUpgradeItems().entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null || entry.getValue() <= 0) {
                continue;
            }
            if (!player.getInventory().containsAtLeast(new ItemStack((Material)entry.getKey()), (int)entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRequiredMoney(final Player player, final Level level, final PaymentMode paymentMode) {
        if (paymentMode == null || !paymentMode.usesMoney()) {
            return true;
        }
        if (!level.hasMoneyUpgrade()) {
            return true;
        }
        final double money = (double)this.pluginHookManager.get(VaultHook.class).map(vaultHook -> (Double)vaultHook.getMoney(player).orElse(0.0)).orElse(0.0);
        return money >= level.getMoneyUpgrade();
    }

    private String colorizeLoreLine(final String line, final boolean canAfford) {
        if (line == null || line.isBlank() || canAfford) {
            return line;
        }
        return "&c" + COLOR_PATTERN.matcher(line).replaceAll("");
    }

    private String resolveStatus(final boolean hasRequiredItems, final boolean hasRequiredMoney, final PaymentMode paymentMode, final String missingItems, final String missingMoney) {
        if (hasRequiredItems && hasRequiredMoney) {
            return this.applyStatusPlaceholders(this.pluginConfig.messages.status.canUpgrade, missingItems, missingMoney);
        }
        if (paymentMode == PaymentMode.MONEY_ONLY) {
            return this.applyStatusPlaceholders(this.pluginConfig.messages.status.missingRequirementsMoneyOnly, missingItems, missingMoney);
        }
        if (paymentMode == PaymentMode.ITEMS_ONLY) {
            return this.applyStatusPlaceholders(this.pluginConfig.messages.status.missingRequirementsItemsOnly, missingItems, missingMoney);
        }
        if (!hasRequiredItems && !hasRequiredMoney) {
            return this.applyStatusPlaceholders(this.pluginConfig.messages.status.missingRequirementsBoth, missingItems, missingMoney);
        }
        if (!hasRequiredItems) {
            return this.applyStatusPlaceholders(this.pluginConfig.messages.status.missingRequirements, missingItems, missingMoney);
        }
        return this.applyStatusPlaceholders(this.pluginConfig.messages.status.missingRequirementsMoneyOnly, missingItems, missingMoney);
    }

    private String applyStatusPlaceholders(final String status, final String missingItems, final String missingMoney) {
        if (status == null) {
            return null;
        }
        return status.replace("{missingItems}", missingItems == null ? "" : missingItems)
                .replace("{missingMoney}", missingMoney == null ? "" : missingMoney);
    }

    private String buildCostLine(final Level level, final PaymentMode paymentMode, final boolean hasRequiredMoney) {
        if (paymentMode == null || !paymentMode.usesMoney()) {
            return "";
        }
        if (!level.hasMoneyUpgrade()) {
            return "";
        }
        String costLine = level.getCostLore();
        if (costLine == null || costLine.isBlank()) {
            costLine = this.pluginConfig.messages.requirements.costLine;
        }
        if (costLine == null || costLine.isBlank()) {
            return "";
        }
        final String formatted = formatMoney(level.getMoneyUpgrade());
        final String replaced = costLine.replace("{cost}", formatted);
        return this.colorizeLoreLine(replaced, hasRequiredMoney);
    }

    private List<String> buildRequirementsLore(final Player player, final Level level, final PaymentMode paymentMode) {
        if (paymentMode == null || !paymentMode.usesItems() || !level.hasUpgradeItems()) {
            return List.of();
        }
        final List<String> lore = new ArrayList<>();
        level.getUpgradeItems().forEach((material, required) -> {
            if (material == null || required == null || required <= 0) {
                return;
            }
            final int have = countItems(player, material);
            final int missing = Math.max(0, required - have);
            final String itemName = humanizeMaterial(material);
            final Map<String, String> itemPlaceholders = Map.of(
                    "itemName", itemName,
                    "required", String.valueOf(required),
                    "have", String.valueOf(have),
                    "missing", String.valueOf(missing));
            final String line = missing <= 0
                    ? this.pluginConfig.messages.requirements.itemLineHave
                    : this.pluginConfig.messages.requirements.itemLineMissing;
            if (line != null && !line.isBlank()) {
                lore.add(applyPlaceholders(line, itemPlaceholders));
            }
            if (missing > 0 && this.pluginConfig.messages.requirements.itemMissingHint != null
                    && !this.pluginConfig.messages.requirements.itemMissingHint.isBlank()) {
                lore.add(applyPlaceholders(this.pluginConfig.messages.requirements.itemMissingHint, itemPlaceholders));
            }
            if (missing <= 0 && this.pluginConfig.messages.requirements.itemHaveHint != null
                    && !this.pluginConfig.messages.requirements.itemHaveHint.isBlank()) {
                lore.add(applyPlaceholders(this.pluginConfig.messages.requirements.itemHaveHint, itemPlaceholders));
            }
        });
        return lore;
    }

    private String buildMissingItems(final Player player, final Level level, final PaymentMode paymentMode) {
        if (paymentMode == null || !paymentMode.usesItems() || !level.hasUpgradeItems()) {
            return "";
        }
        return level.getUpgradeItems().entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null && entry.getValue() > 0)
                .map(entry -> {
                    final int have = countItems(player, entry.getKey());
                    final int missing = Math.max(0, entry.getValue() - have);
                    if (missing <= 0) {
                        return null;
                    }
                    return humanizeMaterial(entry.getKey()) + " x" + missing;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    private String buildMissingMoney(final Player player, final Level level, final PaymentMode paymentMode) {
        if (paymentMode == null || !paymentMode.usesMoney() || !level.hasMoneyUpgrade()) {
            return "";
        }
        final double money = (double)this.pluginHookManager.get(VaultHook.class).map(vaultHook -> (Double)vaultHook.getMoney(player).orElse(0.0)).orElse(0.0);
        final double missing = Math.max(0.0, level.getMoneyUpgrade() - money);
        return formatMoney(missing);
    }

    private ItemBuilder applyUpgradePlaceholders(final ItemStack item, final Map<String, String> placeholders, final List<String> itemsLore) {
        final ItemBuilder builder = ItemBuilder.of(item);
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            final String name = meta.getDisplayName();
            if (name != null) {
                builder.setName(applyPlaceholders(name, placeholders));
            }
            final List<String> lore = meta.getLore();
            if (lore != null) {
                final List<String> updated = new ArrayList<>();
                for (final String line : lore) {
                    if (line == null) {
                        continue;
                    }
                    if (line.contains("{items}")) {
                        if (!itemsLore.isEmpty()) {
                            updated.addAll(itemsLore);
                        }
                        continue;
                    }
                    final String replaced = applyPlaceholders(line, placeholders);
                    if (replaced != null) {
                        updated.add(replaced);
                    }
                }
                builder.setLore(updated);
            }
        }
        return builder.fixColors();
    }

    private static String applyPlaceholders(final String input, final Map<String, String> placeholders) {
        if (input == null) {
            return null;
        }
        String result = input;
        for (final Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue() == null ? "" : entry.getValue());
        }
        return result;
    }

    private static int countItems(final Player player, final Material material) {
        int count = 0;
        for (final ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() != material) {
                continue;
            }
            count += itemStack.getAmount();
        }
        return count;
    }

    private static String humanizeMaterial(final Material material) {
        final String[] parts = material.name().toLowerCase(Locale.ROOT).split("_");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            final String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }

    private static String formatMoney(final double amount) {
        final DecimalFormat format = new DecimalFormat("0.##");
        return format.format(amount);
    }
    
    @Inject
    @Generated
    public KowalMenu(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig, final PluginHookManager pluginHookManager, final CitizensBypassService bypassService) {
        this.mode = KowalMenuMode.METAL;
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.pluginHookManager = pluginHookManager;
        this.bypassService = bypassService;
    }

    @Generated
    public void setMode(final KowalMenuMode mode) {
        this.mode = mode;
    }

    public void setInput(@Nullable final ItemStack input) {
        this.input = input;
    }
}
