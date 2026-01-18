package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.effect.Effect;
import cc.dreamcode.kowal.effect.EffectType;
import cc.dreamcode.kowal.level.Level;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.ItemStack;
import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.command.CommandSender;
import cc.dreamcode.utilities.RandomUtil;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.util.UpgradeDataUtil;
import org.bukkit.event.Listener;

public class DamageController implements Listener
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    
    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        double reduce = 0.0;
        int chance = 0;
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) {
                continue;
            }
            final int currentLevel = UpgradeDataUtil.getLevel((Plugin)this.plugin, armor);
            final String upgrade = UpgradeDataUtil.getEffect((Plugin)this.plugin, armor);
            if (currentLevel >= 1 && this.pluginConfig.kowalLevels != null) {
                final Level level = (Level)this.pluginConfig.kowalLevels.get((Object)currentLevel);
                if (level != null) {
                    reduce += level.getHpReduce();
                }
            }
            if (upgrade.equals((Object)EffectType.DAMAGE.getData()) && this.pluginConfig.effects != null) {
                final Effect effect = (Effect)this.pluginConfig.effects.get((Object)EffectType.DAMAGE);
                if (effect != null) {
                    chance += effect.getAmplifierChance();
                }
            }
        }
        if (chance > 0 && RandomUtil.chance(chance) && event.getDamager().getType().equals((Object)EntityType.PLAYER)) {
            final Player damager = (Player)event.getDamager();
            damager.damage(event.getFinalDamage(), player);
            this.messageConfig.damageUsePlayer.send((CommandSender)player);
            this.messageConfig.damageUseDamager.send((CommandSender)damager);
            return;
        }
        event.setDamage(Math.max(0.0, event.getDamage() - reduce));
    }
    
    @Inject
    @Generated
    public DamageController(final KowalPlugin plugin, final PluginConfig pluginConfig, final MessageConfig messageConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
    }
}
