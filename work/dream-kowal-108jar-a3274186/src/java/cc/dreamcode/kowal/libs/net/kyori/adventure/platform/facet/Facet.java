package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.SoundStop;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.Set;
import java.util.Collections;
import java.io.Closeable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import java.time.Duration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.MessageType;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface Facet<V>
{
    @SafeVarargs
    @NotNull
    default <V, F extends Facet<? extends V>> Collection<F> of(@NotNull final Supplier<F>... suppliers) {
        final List<F> facets = (List<F>)new LinkedList();
        for (final Supplier<F> supplier : suppliers) {
            Label_0139: {
                F facet;
                try {
                    facet = (F)supplier.get();
                }
                catch (final NoClassDefFoundError error) {
                    Knob.logMessage("Skipped facet: %s", supplier.getClass().getName());
                    break Label_0139;
                }
                catch (final Throwable error2) {
                    Knob.logError(error2, "Failed facet: %s", supplier);
                    break Label_0139;
                }
                if (!facet.isSupported()) {
                    Knob.logMessage("Skipped facet: %s", facet);
                }
                else {
                    facets.add((Object)facet);
                    Knob.logMessage("Added facet: %s", facet);
                }
            }
        }
        return (Collection<F>)facets;
    }
    
    @Nullable
    default <V, F extends Facet<V>> F of(@Nullable final Collection<F> facets, @Nullable final V viewer) {
        if (facets == null || viewer == null) {
            return null;
        }
        for (final F facet : facets) {
            try {
                if (facet.isApplicable(viewer)) {
                    Knob.logMessage("Selected facet: %s for %s", facet, viewer);
                    return facet;
                }
                if (!Knob.DEBUG) {
                    continue;
                }
                Knob.logMessage("Not selecting %s for %s", facet, viewer);
            }
            catch (final ClassCastException error) {
                if (!Knob.DEBUG) {
                    continue;
                }
                Knob.logMessage("Exception while getting facet %s for %s: %s", facet, viewer, error.getMessage());
            }
        }
        return null;
    }
    
    default boolean isSupported() {
        return true;
    }
    
    default boolean isApplicable(@NotNull final V viewer) {
        return true;
    }
    
    public interface ChatPacket<V, M> extends Chat<V, M>
    {
        public static final byte TYPE_CHAT = 0;
        public static final byte TYPE_SYSTEM = 1;
        public static final byte TYPE_ACTION_BAR = 2;
        
        default byte createMessageType(@NotNull final MessageType type) {
            if (type == MessageType.CHAT) {
                return 0;
            }
            if (type == MessageType.SYSTEM) {
                return 1;
            }
            Knob.logUnsupported(this, type);
            return 0;
        }
    }
    
    public interface Title<V, M, C, T> extends Message<V, M>
    {
        public static final int PROTOCOL_ACTION_BAR = 310;
        public static final long MAX_SECONDS = 461168601842738790L;
        
        @NotNull
        C createTitleCollection();
        
        void contributeTitle(@NotNull final C coll, @NotNull final M title);
        
        void contributeSubtitle(@NotNull final C coll, @NotNull final M subtitle);
        
        void contributeTimes(@NotNull final C coll, final int inTicks, final int stayTicks, final int outTicks);
        
        @Nullable
        T completeTitle(@NotNull final C coll);
        
        void showTitle(@NotNull final V viewer, @NotNull final T title);
        
        void clearTitle(@NotNull final V viewer);
        
        void resetTitle(@NotNull final V viewer);
        
        default int toTicks(@Nullable final Duration duration) {
            if (duration == null || duration.isNegative()) {
                return -1;
            }
            if (duration.getSeconds() > 461168601842738790L) {
                return Integer.MAX_VALUE;
            }
            return (int)(duration.getSeconds() * 20L + duration.getNano() / 50000000);
        }
    }
    
    public interface BossBar<V> extends cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Listener, Closeable
    {
        public static final int PROTOCOL_BOSS_BAR = 356;
        
        default void bossBarInitialized(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar) {
            this.bossBarNameChanged(bar, bar.name(), bar.name());
            this.bossBarColorChanged(bar, bar.color(), bar.color());
            this.bossBarProgressChanged(bar, bar.progress(), bar.progress());
            this.bossBarFlagsChanged(bar, bar.flags(), (Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag>)Collections.emptySet());
            this.bossBarOverlayChanged(bar, bar.overlay(), bar.overlay());
        }
        
        void addViewer(@NotNull final V viewer);
        
        void removeViewer(@NotNull final V viewer);
        
        boolean isEmpty();
        
        void close();
        
        @FunctionalInterface
        public interface Builder<V, B extends BossBar<V>> extends Facet<V>
        {
            @NotNull
            B createBossBar(@NotNull final Collection<V> viewer);
        }
    }
    
    public interface BossBarPacket<V> extends BossBar<V>
    {
        public static final int ACTION_ADD = 0;
        public static final int ACTION_REMOVE = 1;
        public static final int ACTION_HEALTH = 2;
        public static final int ACTION_TITLE = 3;
        public static final int ACTION_STYLE = 4;
        public static final int ACTION_FLAG = 5;
        
        default int createColor(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color color) {
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.PURPLE) {
                return 5;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.PINK) {
                return 0;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.BLUE) {
                return 1;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.RED) {
                return 2;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.GREEN) {
                return 3;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.YELLOW) {
                return 4;
            }
            if (color == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color.WHITE) {
                return 6;
            }
            Knob.logUnsupported(this, color);
            return 5;
        }
        
        default int createOverlay(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay overlay) {
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS) {
                return 0;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_6) {
                return 1;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_10) {
                return 2;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_12) {
                return 3;
            }
            if (overlay == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_20) {
                return 4;
            }
            Knob.logUnsupported(this, overlay);
            return 0;
        }
        
        default byte createFlag(final byte flagBit, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
            byte bit = flagBit;
            for (final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag flag : flagsAdded) {
                if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
                    bit |= 0x1;
                }
                else if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
                    bit |= 0x2;
                }
                else if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
                    bit |= 0x4;
                }
                else {
                    Knob.logUnsupported(this, flag);
                }
            }
            for (final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag flag : flagsRemoved) {
                if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
                    bit &= 0xFFFFFFFE;
                }
                else if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
                    bit &= 0xFFFFFFFD;
                }
                else if (flag == cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
                    bit &= 0xFFFFFFFB;
                }
                else {
                    Knob.logUnsupported(this, flag);
                }
            }
            return bit;
        }
    }
    
    public interface BossBarEntity<V, P> extends BossBar<V>, FakeEntity<V, P>
    {
        public static final int OFFSET_PITCH = 30;
        public static final int OFFSET_YAW = 0;
        public static final int OFFSET_MAGNITUDE = 40;
        public static final int INVULNERABLE_KEY = 20;
        public static final int INVULNERABLE_TICKS = 890;
        
        default void bossBarProgressChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldProgress, final float newProgress) {
            this.health(newProgress);
        }
        
        default void bossBarNameChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
            this.name(newName);
        }
        
        default void addViewer(@NotNull final V viewer) {
            this.teleport(viewer, this.createPosition(viewer));
        }
        
        default void removeViewer(@NotNull final V viewer) {
            this.teleport(viewer, null);
        }
    }
    
    public interface Pointers<V> extends Facet<V>
    {
        void contributePointers(final V viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers.Builder builder);
    }
    
    public interface TabList<V, M> extends Message<V, M>
    {
        void send(final V viewer, @Nullable final M header, @Nullable final M footer);
    }
    
    public interface Message<V, M> extends Facet<V>
    {
        public static final int PROTOCOL_HEX_COLOR = 713;
        public static final int PROTOCOL_JSON = 5;
        
        @Nullable
        M createMessage(@NotNull final V viewer, @NotNull final Component message);
    }
    
    public interface FakeEntity<V, P> extends Position<V, P>, Closeable
    {
        void teleport(@NotNull final V viewer, @Nullable final P position);
        
        void metadata(final int position, @NotNull final Object data);
        
        void invisible(final boolean invisible);
        
        void health(final float health);
        
        void name(@NotNull final Component name);
        
        void close();
    }
    
    public interface Position<V, P> extends Facet<V>
    {
        @Nullable
        P createPosition(@NotNull final V viewer);
        
        @NotNull
        P createPosition(final double x, final double y, final double z);
    }
    
    public interface Book<V, M, B> extends Message<V, M>
    {
        @Nullable
        B createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<M> pages);
        
        void openBook(@NotNull final V viewer, @NotNull final B book);
    }
    
    public interface EntitySound<V, M> extends Facet<V>
    {
        M createForSelf(final V viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound);
        
        M createForEmitter(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Emitter emitter);
        
        void playSound(@NotNull final V viewer, final M message);
    }
    
    public interface Sound<V, P> extends Position<V, P>
    {
        void playSound(@NotNull final V viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, @NotNull final P position);
        
        void stopSound(@NotNull final V viewer, @NotNull final SoundStop sound);
    }
    
    public interface TitlePacket<V, M, C, T> extends Title<V, M, C, T>
    {
        public static final int ACTION_TITLE = 0;
        public static final int ACTION_SUBTITLE = 1;
        public static final int ACTION_ACTIONBAR = 2;
        public static final int ACTION_TIMES = 3;
        public static final int ACTION_CLEAR = 4;
        public static final int ACTION_RESET = 5;
    }
    
    public interface ActionBar<V, M> extends Message<V, M>
    {
        void sendMessage(@NotNull final V viewer, @NotNull final M message);
    }
    
    public interface Chat<V, M> extends Message<V, M>
    {
        void sendMessage(@NotNull final V viewer, @NotNull final Identity source, @NotNull final M message, @NotNull final Object type);
    }
}
