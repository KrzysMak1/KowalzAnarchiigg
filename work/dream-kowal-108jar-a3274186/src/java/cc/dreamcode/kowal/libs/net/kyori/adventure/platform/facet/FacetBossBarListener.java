package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.Function;

class FacetBossBarListener<V> implements Facet.BossBar<V>
{
    private final Facet.BossBar<V> facet;
    private final Function<Component, Component> translator;
    
    FacetBossBarListener(final Facet.BossBar<V> facet, @NotNull final Function<Component, Component> translator) {
        this.facet = facet;
        this.translator = translator;
    }
    
    @Override
    public void bossBarInitialized(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar) {
        this.facet.bossBarInitialized(bar);
        this.bossBarNameChanged(bar, bar.name(), bar.name());
    }
    
    @Override
    public void bossBarNameChanged(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
        this.facet.bossBarNameChanged(bar, oldName, (Component)this.translator.apply((Object)newName));
    }
    
    @Override
    public void bossBarProgressChanged(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldPercent, final float newPercent) {
        this.facet.bossBarProgressChanged(bar, oldPercent, newPercent);
    }
    
    @Override
    public void bossBarColorChanged(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color oldColor, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color newColor) {
        this.facet.bossBarColorChanged(bar, oldColor, newColor);
    }
    
    @Override
    public void bossBarOverlayChanged(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay oldOverlay, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay newOverlay) {
        this.facet.bossBarOverlayChanged(bar, oldOverlay, newOverlay);
    }
    
    @Override
    public void bossBarFlagsChanged(@NotNull final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
        this.facet.bossBarFlagsChanged(bar, flagsAdded, flagsRemoved);
    }
    
    @Override
    public void addViewer(@NotNull final V viewer) {
        this.facet.addViewer(viewer);
    }
    
    @Override
    public void removeViewer(@NotNull final V viewer) {
        this.facet.removeViewer(viewer);
    }
    
    @Override
    public boolean isEmpty() {
        return this.facet.isEmpty();
    }
    
    @Override
    public void close() {
        this.facet.close();
    }
}
