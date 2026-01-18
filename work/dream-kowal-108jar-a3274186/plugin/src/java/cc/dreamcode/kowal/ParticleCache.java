package cc.dreamcode.kowal;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import lombok.NonNull;
import java.util.UUID;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.util.UpgradeDataUtil;

public class ParticleCache
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final Set<UUID> particleEffect;
    
    public void add(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.particleEffect.add(uuid);
    }
    
    public void remove(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.particleEffect.remove(uuid);
    }
    
    public boolean hasArmor(@NonNull final Player player, final int level) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (this.pluginConfig.kowalItems == null || this.pluginConfig.kowalItems.isEmpty()) {
            return false;
        }
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) {
                return false;
            }
            if (!this.pluginConfig.kowalItems.containsKey(armor.getType())) {
                return false;
            }
            final int currentLevel = UpgradeDataUtil.getLevel((Plugin)this.plugin, armor);
            if (currentLevel < level) {
                return false;
            }
        }
        return true;
    }
    
    public void check(@NonNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        final boolean has = this.hasArmor(player, 6);
        if (has) {
            this.add(player.getUniqueId());
        }
        else {
            this.remove(player.getUniqueId());
        }
    }
    
    public void checkOnline() {
        Bukkit.getOnlinePlayers().forEach(this::check);
    }
    
    public Collection<UUID> values() {
        return (Collection<UUID>)Collections.unmodifiableSet(this.particleEffect);
    }
    
    @Inject
    @Generated
    public ParticleCache(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.particleEffect = ConcurrentHashMap.newKeySet();
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }

    public void clear() {
        this.particleEffect.clear();
    }
}
