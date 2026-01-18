package cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar;

import org.jetbrains.annotations.NotNull;

public interface BossBarViewer
{
    @NotNull
    Iterable<? extends BossBar> activeBossBars();
}
