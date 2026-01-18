package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure;

import lombok.Generated;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;

public class BukkitNoticeProvider
{
    private static BukkitNoticeProvider instance;
    private final Plugin plugin;
    private final BukkitAudiences bukkitAudiences;
    
    public BukkitNoticeProvider(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        BukkitNoticeProvider.instance = this;
        this.plugin = plugin;
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }
    
    public static BukkitNoticeProvider create(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return new BukkitNoticeProvider(plugin);
    }
    
    @Generated
    public static BukkitNoticeProvider getInstance() {
        return BukkitNoticeProvider.instance;
    }
    
    @Generated
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    @Generated
    public BukkitAudiences getBukkitAudiences() {
        return this.bukkitAudiences;
    }
}
