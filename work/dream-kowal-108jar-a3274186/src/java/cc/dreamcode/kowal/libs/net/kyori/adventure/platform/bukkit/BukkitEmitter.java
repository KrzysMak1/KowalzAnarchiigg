package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import org.bukkit.entity.Entity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;

final class BukkitEmitter implements Sound.Emitter
{
    final Entity entity;
    
    BukkitEmitter(final Entity entity) {
        this.entity = entity;
    }
}
