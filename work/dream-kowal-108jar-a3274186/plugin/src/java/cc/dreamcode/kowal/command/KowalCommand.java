package cc.dreamcode.kowal.command;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.configs.exception.OkaeriException;
import cc.dreamcode.utilities.TimeUtil;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.utilities.RandomUtil;
import cc.dreamcode.kowal.effect.EffectType;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import cc.dreamcode.kowal.level.Level;
import org.bukkit.Material;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.utilities.bukkit.InventoryUtil;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import java.util.Map;
import cc.dreamcode.command.annotation.Arg;
import org.bukkit.command.CommandSender;
import cc.dreamcode.command.annotation.Executor;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.kowal.menu.KowalMenu;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.ParticleCache;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.citizens.CitizensBypassService;
import cc.dreamcode.kowal.util.UpgradeUtil;
import cc.dreamcode.kowal.util.MiniMessageUtil;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.CommandBase;

@Command(name = "kowal")
public class KowalCommand implements CommandBase
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final CitizensBypassService citizensBypassService;
    
    @Executor(description = "Otwiera gui kowala.")
    void gui(final Player sender) {
        final boolean handledBypass = this.citizensBypassService.scheduleNpcOpenIfActive(sender, input -> {
            final KowalMenu kowalMenu = this.plugin.createInstance(KowalMenu.class);
            kowalMenu.setInput(input);
            this.citizensBypassService.markMenuOpen(sender);
            kowalMenu.build((HumanEntity)sender).open((HumanEntity)sender);
        });
        if (handledBypass) {
            return;
        }
        final KowalMenu kowalMenu = this.plugin.createInstance(KowalMenu.class);
        this.citizensBypassService.markMenuOpen(sender);
        kowalMenu.build((HumanEntity)sender).open((HumanEntity)sender);
    }
    
    @Permission("dream-kowal.give")
    @Completion(arg = "target", value = { "all", "@all-players" })
    @Executor(path = "give", description = "Nadaje kamien kowalski.")
    void give(final CommandSender sender, @Arg final String target, @Arg final int amount) {
        if (amount <= 0) {
            this.messageConfig.invalidFormat.send(sender, Map.of("input", "amount"));
            return;
        }
        final ItemStack kamien = MiniMessageUtil.applyItemColors(ItemBuilder.of(this.pluginConfig.items.kamienKowalski).setAmount(amount), Map.of()).toItemStack();
        if (target.equals((Object)"all")) {
            this.plugin.getServer().getOnlinePlayers().forEach(player -> {
                InventoryUtil.giveItem(player, kamien.clone());
                this.messageConfig.giveAllPlayer.send((CommandSender)player, Map.of("amount", amount));
            });
            this.messageConfig.giveAll.send(sender, Map.of("amount", amount));
            return;
        }
        final Player targetPlayer = this.plugin.getServer().getPlayerExact(target);
        if (targetPlayer == null) {
            this.messageConfig.playerNotFound.send(sender);
            return;
        }
        InventoryUtil.giveItem(targetPlayer, kamien.clone());
        this.messageConfig.givePlayer.send((CommandSender)targetPlayer, Map.of("amount", amount));
        this.messageConfig.give.send(sender, Map.of("player", targetPlayer.getName(), "amount", amount));
    }
    
    @Permission("dream-kowal.set")
    @Executor(path = "set", description = "Ustawia przedmiot kamienia kowalskiego.")
    BukkitNotice set(final Player sender) {
        if (sender.getInventory().getItemInMainHand().getType().equals((Object)Material.AIR)) {
            return this.messageConfig.kamienAir;
        }
        this.pluginConfig.items.kamienKowalski = ItemBuilder.of(sender.getInventory().getItemInMainHand()).breakColors().toItemStack();
        this.pluginConfig.items.applyKamienKowalskiCustomModelData();
        this.pluginConfig.save();
        return this.messageConfig.kamienSet;
    }
    
    @Permission("dream-kowal.upgrade")
    @Executor(path = "ulepsz", description = "Ulepsza przedmiot w rece na podany poziom.")
    BukkitNotice ulepsz(final Player sender, @Arg final int level) {
        final ItemStack item = sender.getInventory().getItemInMainHand();
        if (item.getType().equals((Object)Material.AIR)) {
            return this.messageConfig.commandUpgradeAir;
        }
        if (!this.pluginConfig.items.names.containsKey((Object)item.getType())) {
            return this.messageConfig.commandUpgradeNotArmor;
        }
        if (level > 7 || level <= 0) {
            return this.messageConfig.commandUpgradeLevelError;
        }
        if (this.pluginConfig.levels == null || !this.pluginConfig.levels.containsKey((Object)level)) {
            return this.messageConfig.commandUpgradeLevelError;
        }
        final Level found = (Level)this.pluginConfig.levels.get((Object)level);
        final String displayName = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, item, "display-name")
                .orElse(this.pluginConfig.items.names.get((Object)item.getType()));
        String baseName = displayName;
        if (baseName == null || baseName.isBlank()) {
            baseName = (this.pluginConfig.items != null && this.pluginConfig.items.names != null)
                    ? (String)this.pluginConfig.items.names.get((Object)item.getType())
                    : null;
        }
        final String colorSuffix = (this.pluginConfig.items.colors != null)
                ? (String)this.pluginConfig.items.colors.get((Object)item.getType())
                : "";
        final String newName = UpgradeUtil.buildUpgradeName(baseName, colorSuffix, level);
        final ItemBuilder newItem = ItemBuilder.of(item.getType()).setName(newName).setLore(found.getItemLoreDisplay()).withNbt((Plugin)this.plugin, "upgrade-level", String.valueOf(level));
        if (level == 7) {
            final EffectType[] effects = EffectType.values();
            final EffectType random = effects[RandomUtil.nextInteger(effects.length)];
            final Effect randomEffect = (this.pluginConfig.effects != null && this.pluginConfig.effects.list != null)
                    ? (Effect)this.pluginConfig.effects.list.get((Object)random)
                    : null;
            if (randomEffect != null) {
                MiniMessageUtil.applyItemColors(newItem.withNbt((Plugin)this.plugin, "upgrade-effect", random.getData()).appendLore(randomEffect.getLore()), Map.of("level", level, "chance", randomEffect.getAmplifierChance()));
            }
        }
        else {
            MiniMessageUtil.applyItemColors(newItem, Map.of("level", level));
        }
        sender.getInventory().setItemInMainHand(newItem.toItemStack());
        return this.messageConfig.commandUpgradeSuccess.with("level", level);
    }

    @Permission("dream-kowal.reload")
    @Executor(path = "reload", description = "Przeladowuje konfiguracje.")
    BukkitNotice reload() {
        final long time = System.currentTimeMillis();
        try {
            this.messageConfig.load();
            this.pluginConfig.load();
            this.pluginConfig.items.applyKamienKowalskiCustomModelData();
            this.plugin.getInject(ParticleCache.class).ifPresent(particleCache -> {
                particleCache.clear();
                particleCache.checkOnline();
            });
            return this.messageConfig.reloaded.with("time", TimeUtil.format(System.currentTimeMillis() - time));
        }
        catch (final NullPointerException | OkaeriException e) {
            e.printStackTrace();
            return this.messageConfig.reloadError.with("error", e.getMessage());
        }
    }
    
    @Inject
    @Generated
    public KowalCommand(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig, final CitizensBypassService citizensBypassService) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.citizensBypassService = citizensBypassService;
    }
}
