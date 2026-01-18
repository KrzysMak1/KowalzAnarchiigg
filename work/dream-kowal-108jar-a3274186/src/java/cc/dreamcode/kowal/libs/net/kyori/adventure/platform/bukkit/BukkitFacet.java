package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translator;
import java.util.UUID;
import java.util.Locale;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetPointers;
import org.bukkit.command.ConsoleCommandSender;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.permission.PermissionChecker;
import java.util.function.Supplier;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointer;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.function.Function;
import java.util.Set;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import java.util.Collection;
import org.bukkit.SoundCategory;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.SoundStop;
import org.bukkit.Location;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import java.lang.invoke.MethodHandle;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetBase;
import org.bukkit.command.CommandSender;

class BukkitFacet<V extends CommandSender> extends FacetBase<V>
{
    protected BukkitFacet(@Nullable final Class<? extends V> viewerClass) {
        super(viewerClass);
    }
    
    static class Message<V extends CommandSender> extends BukkitFacet<V> implements Facet.Message<V, String>
    {
        protected Message(@Nullable final Class<? extends V> viewerClass) {
            super(viewerClass);
        }
        
        @NotNull
        @Override
        public String createMessage(@NotNull final V viewer, @NotNull final Component message) {
            return BukkitComponentSerializer.legacy().serialize(message);
        }
    }
    
    static class Chat extends BukkitFacet.Message<CommandSender> implements Facet.Chat<CommandSender, String>
    {
        protected Chat() {
            super(CommandSender.class);
        }
        
        @Override
        public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, @NotNull final String message, @NotNull final Object type) {
            viewer.sendMessage(message);
        }
    }
    
    static class Position extends BukkitFacet<Player> implements Facet.Position<Player, Vector>
    {
        protected Position() {
            super((Class<? extends CommandSender>)Player.class);
        }
        
        @NotNull
        @Override
        public Vector createPosition(@NotNull final Player viewer) {
            return viewer.getLocation().toVector();
        }
        
        @NotNull
        @Override
        public Vector createPosition(final double x, final double y, final double z) {
            return new Vector(x, y, z);
        }
    }
    
    static class Sound extends BukkitFacet.Position implements Facet.Sound<Player, Vector>
    {
        private static final boolean KEY_SUPPORTED;
        private static final boolean STOP_SUPPORTED;
        private static final MethodHandle STOP_ALL_SUPPORTED;
        
        @Override
        public void playSound(@NotNull final Player viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, @NotNull final Vector vector) {
            final String name = name(sound.name());
            final Location location = vector.toLocation(viewer.getWorld());
            viewer.playSound(location, name, sound.volume(), sound.pitch());
        }
        
        @Override
        public void stopSound(@NotNull final Player viewer, @NotNull final SoundStop stop) {
            if (Sound.STOP_SUPPORTED) {
                final String name = name(stop.sound());
                if (name.isEmpty() && Sound.STOP_ALL_SUPPORTED != null) {
                    try {
                        Sound.STOP_ALL_SUPPORTED.invoke(viewer);
                    }
                    catch (final Throwable error) {
                        Knob.logError(error, "Could not invoke stopAllSounds on %s", viewer);
                    }
                    return;
                }
                viewer.stopSound(name);
            }
        }
        
        @NotNull
        protected static String name(@Nullable final Key name) {
            if (name == null) {
                return "";
            }
            if (Sound.KEY_SUPPORTED) {
                return name.asString();
            }
            return name.value();
        }
        
        static {
            KEY_SUPPORTED = MinecraftReflection.hasClass("org.bukkit.NamespacedKey");
            STOP_SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class);
            STOP_ALL_SUPPORTED = MinecraftReflection.findMethod(Player.class, "stopAllSounds", Void.TYPE, (Class<?>[])new Class[0]);
        }
    }
    
    static class SoundWithCategory extends Sound
    {
        private static final boolean SUPPORTED;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && SoundWithCategory.SUPPORTED;
        }
        
        @Override
        public void playSound(@NotNull final Player viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, @NotNull final Vector vector) {
            final SoundCategory category = this.category(sound.source());
            if (category == null) {
                super.playSound(viewer, sound, vector);
            }
            else {
                final String name = Sound.name(sound.name());
                viewer.playSound(vector.toLocation(viewer.getWorld()), name, category, sound.volume(), sound.pitch());
            }
        }
        
        @Override
        public void stopSound(@NotNull final Player viewer, @NotNull final SoundStop stop) {
            final SoundCategory category = this.category(stop.source());
            if (category == null) {
                super.stopSound(viewer, stop);
            }
            else {
                final String name = Sound.name(stop.sound());
                viewer.stopSound(name, category);
            }
        }
        
        @Nullable
        private SoundCategory category(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source source) {
            if (source == null) {
                return null;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.MASTER) {
                return SoundCategory.MASTER;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.MUSIC) {
                return SoundCategory.MUSIC;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.RECORD) {
                return SoundCategory.RECORDS;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.WEATHER) {
                return SoundCategory.WEATHER;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.BLOCK) {
                return SoundCategory.BLOCKS;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.HOSTILE) {
                return SoundCategory.HOSTILE;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.NEUTRAL) {
                return SoundCategory.NEUTRAL;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.PLAYER) {
                return SoundCategory.PLAYERS;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.AMBIENT) {
                return SoundCategory.AMBIENT;
            }
            if (source == cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.VOICE) {
                return SoundCategory.VOICE;
            }
            Knob.logUnsupported(this, source);
            return null;
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class, MinecraftReflection.findClass("org.bukkit.SoundCategory"));
        }
    }
    
    static class BossBarBuilder extends BukkitFacet<Player> implements Facet.BossBar.Builder<Player, BukkitFacet.BossBar>
    {
        private static final boolean SUPPORTED;
        
        protected BossBarBuilder() {
            super((Class<? extends CommandSender>)Player.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && BossBarBuilder.SUPPORTED;
        }
        
        @Override
        public BukkitFacet.BossBar createBossBar(@NotNull final Collection<Player> viewers) {
            return new BukkitFacet.BossBar(viewers);
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasClass("org.bukkit.boss.BossBar");
        }
    }
    
    static class BossBar extends BukkitFacet.Message<Player> implements Facet.BossBar<Player>
    {
        protected final org.bukkit.boss.BossBar bar;
        
        protected BossBar(@NotNull final Collection<Player> viewers) {
            super(Player.class);
            (this.bar = Bukkit.createBossBar("", BarColor.PINK, BarStyle.SOLID, new BarFlag[0])).setVisible(false);
            for (final Player viewer : viewers) {
                this.bar.addPlayer(viewer);
            }
        }
        
        @Override
        public void bossBarInitialized(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar) {
            super.bossBarInitialized(bar);
            this.bar.setVisible(true);
        }
        
        @Override
        public void bossBarNameChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
            if (!this.bar.getPlayers().isEmpty()) {
                this.bar.setTitle(this.createMessage((Player)this.bar.getPlayers().get(0), newName));
            }
        }
        
        @Override
        public void bossBarProgressChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldPercent, final float newPercent) {
            this.bar.setProgress((double)newPercent);
        }
        
        @Override
        public void bossBarColorChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color oldColor, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color newColor) {
            final BarColor color = this.color(newColor);
            if (color != null) {
                this.bar.setColor(color);
            }
        }
        
        @Nullable
        private BarColor color(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color color) {
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.PINK) {
                return BarColor.PINK;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.BLUE) {
                return BarColor.BLUE;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.RED) {
                return BarColor.RED;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.GREEN) {
                return BarColor.GREEN;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.YELLOW) {
                return BarColor.YELLOW;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.PURPLE) {
                return BarColor.PURPLE;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.WHITE) {
                return BarColor.WHITE;
            }
            Knob.logUnsupported(this, color);
            return null;
        }
        
        @Override
        public void bossBarOverlayChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay oldOverlay, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay newOverlay) {
            final BarStyle style = this.style(newOverlay);
            if (style != null) {
                this.bar.setStyle(style);
            }
        }
        
        @Nullable
        private BarStyle style(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay overlay) {
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS) {
                return BarStyle.SOLID;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_6) {
                return BarStyle.SEGMENTED_6;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_10) {
                return BarStyle.SEGMENTED_10;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_12) {
                return BarStyle.SEGMENTED_12;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_20) {
                return BarStyle.SEGMENTED_20;
            }
            Knob.logUnsupported(this, overlay);
            return null;
        }
        
        @Override
        public void bossBarFlagsChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
            for (final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag removeFlag : flagsRemoved) {
                final BarFlag flag = this.flag(removeFlag);
                if (flag != null) {
                    this.bar.removeFlag(flag);
                }
            }
            for (final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag addFlag : flagsAdded) {
                final BarFlag flag = this.flag(addFlag);
                if (flag != null) {
                    this.bar.addFlag(flag);
                }
            }
        }
        
        @Nullable
        private BarFlag flag(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag flag) {
            if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
                return BarFlag.DARKEN_SKY;
            }
            if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
                return BarFlag.PLAY_BOSS_MUSIC;
            }
            if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
                return BarFlag.CREATE_FOG;
            }
            Knob.logUnsupported(this, flag);
            return null;
        }
        
        @Override
        public void addViewer(@NotNull final Player viewer) {
            this.bar.addPlayer(viewer);
        }
        
        @Override
        public void removeViewer(@NotNull final Player viewer) {
            this.bar.removePlayer(viewer);
        }
        
        @Override
        public boolean isEmpty() {
            return !this.bar.isVisible() || this.bar.getPlayers().isEmpty();
        }
        
        @Override
        public void close() {
            this.bar.removeAll();
        }
    }
    
    static final class ViaHook implements Function<Player, UserConnection>
    {
        public UserConnection apply(@NotNull final Player player) {
            return Via.getManager().getConnectionManager().getConnectedClient(player.getUniqueId());
        }
    }
    
    static final class TabList extends BukkitFacet.Message<Player> implements Facet.TabList<Player, String>
    {
        private static final boolean SUPPORTED;
        
        TabList() {
            super(Player.class);
        }
        
        @Override
        public boolean isSupported() {
            return TabList.SUPPORTED && super.isSupported();
        }
        
        @Override
        public void send(final Player viewer, @Nullable final String header, @Nullable final String footer) {
            if (header != null && footer != null) {
                viewer.setPlayerListHeaderFooter(header, footer);
            }
            else if (header != null) {
                viewer.setPlayerListHeader(header);
            }
            else if (footer != null) {
                viewer.setPlayerListFooter(footer);
            }
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasMethod(Player.class, "setPlayerListHeader", String.class);
        }
    }
    
    static final class CommandSenderPointers extends BukkitFacet<CommandSender> implements Facet.Pointers<CommandSender>
    {
        CommandSenderPointers() {
            super(CommandSender.class);
        }
        
        @Override
        public void contributePointers(final CommandSender viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
            final Pointer<String> name = Identity.NAME;
            Objects.requireNonNull((Object)viewer);
            builder.withDynamic((Pointer<Object>)name, (java.util.function.Supplier<Object>)viewer::getName);
            builder.withStatic(PermissionChecker.POINTER, perm -> {
                if (viewer.isPermissionSet(perm)) {
                    return viewer.hasPermission(perm) ? TriState.TRUE : TriState.FALSE;
                }
                else {
                    return TriState.NOT_SET;
                }
            });
        }
    }
    
    static final class ConsoleCommandSenderPointers extends BukkitFacet<ConsoleCommandSender> implements Facet.Pointers<ConsoleCommandSender>
    {
        ConsoleCommandSenderPointers() {
            super((Class<? extends CommandSender>)ConsoleCommandSender.class);
        }
        
        @Override
        public void contributePointers(final ConsoleCommandSender viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
            builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.CONSOLE);
        }
    }
    
    static final class PlayerPointers extends BukkitFacet<Player> implements Facet.Pointers<Player>
    {
        private static final MethodHandle LOCALE_SUPPORTED;
        
        PlayerPointers() {
            super((Class<? extends CommandSender>)Player.class);
        }
        
        @Override
        public void contributePointers(final Player viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
            final Pointer<UUID> uuid = Identity.UUID;
            Objects.requireNonNull((Object)viewer);
            builder.withDynamic((Pointer<Object>)uuid, (java.util.function.Supplier<Object>)viewer::getUniqueId);
            builder.withDynamic(Identity.DISPLAY_NAME, (java.util.function.Supplier<Component>)(() -> ((ComponentSerializer<I, Component, String>)BukkitComponentSerializer.legacy()).deserializeOrNull(viewer.getDisplayName())));
            builder.withDynamic(Identity.LOCALE, (java.util.function.Supplier<Locale>)(() -> {
                if (PlayerPointers.LOCALE_SUPPORTED != null) {
                    try {
                        return Translator.parseLocale(PlayerPointers.LOCALE_SUPPORTED.invoke(viewer));
                    }
                    catch (final Throwable error) {
                        Knob.logError(error, "Failed to call getLocale() for %s", viewer);
                    }
                }
                return Locale.getDefault();
            }));
            builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.PLAYER);
            builder.withDynamic(FacetPointers.WORLD, (java.util.function.Supplier<Key>)(() -> Key.key(viewer.getWorld().getName())));
        }
        
        static {
            LOCALE_SUPPORTED = MinecraftReflection.findMethod(Player.class, "getLocale", String.class, (Class<?>[])new Class[0]);
        }
    }
}
