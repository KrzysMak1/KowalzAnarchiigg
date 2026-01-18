package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import java.util.function.Predicate;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.AudienceProvider;

public interface BukkitAudiences extends AudienceProvider
{
    @NotNull
    default BukkitAudiences create(@NotNull final Plugin plugin) {
        return BukkitAudiencesImpl.instanceFor(plugin);
    }
    
    @NotNull
    default Builder builder(@NotNull final Plugin plugin) {
        return BukkitAudiencesImpl.builder(plugin);
    }
    
    default Sound.Emitter asEmitter(@NotNull final Entity entity) {
        return new BukkitEmitter(entity);
    }
    
    @NotNull
    Audience sender(@NotNull final CommandSender sender);
    
    @NotNull
    Audience player(@NotNull final Player player);
    
    @NotNull
    Audience filter(@NotNull final Predicate<CommandSender> filter);
    
    public interface Builder extends AudienceProvider.Builder<BukkitAudiences, Builder>
    {
    }
}
