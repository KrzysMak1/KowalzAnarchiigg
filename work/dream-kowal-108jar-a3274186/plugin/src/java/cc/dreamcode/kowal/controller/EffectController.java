package cc.dreamcode.kowal.controller;

import org.bukkit.potion.PotionEffect;
import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.kowal.effect.EffectType;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import org.bukkit.inventory.ItemStack;
import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import cc.dreamcode.utilities.RandomUtil;
import org.bukkit.event.player.PlayerItemDamageEvent;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionType;

public class EffectController implements Listener
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    
    @EventHandler
    public void onArmorDamage(final PlayerItemDamageEvent event) {
        final Player player = event.getPlayer();
        if (this.pluginConfig.kowalItems == null || !this.pluginConfig.kowalItems.containsKey((Object)event.getItem().getType())) {
            return;
        }
        if (event.getPlayer().getInventory().getChestplate() == null) {
            return;
        }
        int chance = 0;
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) {
                continue;
            }
            final String upgrade = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, armor, "upgrade-effect").orElse("none");
            if (upgrade.equals((Object)EffectType.ARMOR_DAMAGE.getData()) && this.pluginConfig.effects != null) {
                final Effect effect = (Effect)this.pluginConfig.effects.get((Object)EffectType.ARMOR_DAMAGE);
                if (effect != null) {
                    chance += effect.getAmplifierChance();
                }
            }
        }
        if (chance > 0 && RandomUtil.chance(chance)) {
            event.setDamage(0);
        }
    }
    
    @EventHandler
    public void onPotionConsume(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        if (!(event.getItem().getItemMeta() instanceof PotionMeta)) {
            return;
        }
        double amplifier = 1.0;
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) {
                continue;
            }
            final String upgrade = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, armor, "upgrade-effect").orElse("none");
            if (upgrade.equals((Object)EffectType.POTION_DURATION.getData()) && this.pluginConfig.effects != null) {
                final Effect effect = (Effect)this.pluginConfig.effects.get((Object)EffectType.POTION_DURATION);
                if (effect != null) {
                    amplifier += effect.getAmplifierChance() / 100.0;
                }
            }
        }
        final PotionMeta meta = (PotionMeta)event.getItem().getItemMeta();
        final PotionType baseType = meta.getBasePotionType();
        if (baseType == null) {
            return;
        }
        final PotionEffectType type = baseType.getEffectType();
        if (type == null) {
            return;
        }
        final double multiplier = amplifier;
        this.plugin.getServer().getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            if (!player.isOnline()) {
                return;
            }
            final PotionEffect activeEffect = player.getPotionEffect(type);
            if (activeEffect != null) {
                final int newDuration = (int)(activeEffect.getDuration() * multiplier);
                player.removePotionEffect(type);
                player.addPotionEffect(new PotionEffect(type, newDuration, activeEffect.getAmplifier()));
            }
        }, 20L);
    }
    
    @EventHandler
    public void onArrowDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        int chance = 0;
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) {
                continue;
            }
            final String upgrade = (String)ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, armor, "upgrade-effect").orElse("none");
            if (upgrade.equals((Object)EffectType.ARROW.getData()) && this.pluginConfig.effects != null) {
                final Effect effect = (Effect)this.pluginConfig.effects.get((Object)EffectType.ARROW);
                if (effect != null) {
                    chance += effect.getAmplifierChance();
                }
            }
        }
        if (chance > 0 && RandomUtil.chance(chance)) {
            this.messageConfig.arrowUse.send((CommandSender)player);
            event.setCancelled(true);
        }
    }
    
    @Inject
    @Generated
    public EffectController(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
    }
}
