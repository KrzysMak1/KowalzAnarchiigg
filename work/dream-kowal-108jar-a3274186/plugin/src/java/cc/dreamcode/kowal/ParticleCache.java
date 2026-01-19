package cc.dreamcode.kowal;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import org.bukkit.entity.Player;
import lombok.NonNull;
import java.util.UUID;
import cc.dreamcode.utilities.RandomUtil;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.util.UpgradeUtil;
import org.bukkit.Particle;

public class ParticleCache
{
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private final Map<UUID, Particle> particleEffect;
    
    private void add(@NonNull final UUID uuid, @NonNull final Particle particle) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (particle == null) {
            throw new NullPointerException("particle is marked non-null but is null");
        }
        this.particleEffect.put(uuid, particle);
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
            final Object levelValue = ItemNbtUtil.getValueByPlugin((Plugin)this.plugin, armor, "upgrade-level").orElse("0");
            final int currentLevel = UpgradeUtil.parseLevel(levelValue);
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
        final UUID uuid = player.getUniqueId();
        if (this.hasArmor(player, 7)) {
            final Particle currentParticle = this.particleEffect.get(uuid);
            if (currentParticle == null || !this.isLevelSevenParticle(currentParticle)) {
                this.selectLevelSevenParticle().ifPresent(particle -> this.add(uuid, particle));
            }
            return;
        }
        this.remove(uuid);
        if (this.hasArmor(player, 6)) {
            if (this.pluginConfig.particle != null) {
                this.add(uuid, this.pluginConfig.particle);
                return;
            }
        }
    }
    
    public void checkOnline() {
        Bukkit.getOnlinePlayers().forEach(this::check);
    }
    
    public Collection<UUID> values() {
        return (Collection<UUID>)Collections.unmodifiableSet(this.particleEffect.keySet());
    }

    public Collection<Map.Entry<UUID, Particle>> entries() {
        return Collections.unmodifiableSet(this.particleEffect.entrySet());
    }
    
    @Inject
    @Generated
    public ParticleCache(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.particleEffect = new ConcurrentHashMap<UUID, Particle>();
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }

    public void clear() {
        this.particleEffect.clear();
    }

    private Optional<Particle> selectLevelSevenParticle() {
        final List<Particle> particles = this.pluginConfig.particlesLevelSeven;
        if (particles == null || particles.isEmpty()) {
            return Optional.ofNullable(this.pluginConfig.particle);
        }
        return Optional.of(particles.get(RandomUtil.nextInteger(particles.size())));
    }

    private boolean isLevelSevenParticle(@NonNull final Particle particle) {
        if (particle == null) {
            throw new NullPointerException("particle is marked non-null but is null");
        }
        final List<Particle> particles = this.pluginConfig.particlesLevelSeven;
        if (particles == null || particles.isEmpty()) {
            return particle == this.pluginConfig.particle;
        }
        return particles.contains(particle);
    }
}
