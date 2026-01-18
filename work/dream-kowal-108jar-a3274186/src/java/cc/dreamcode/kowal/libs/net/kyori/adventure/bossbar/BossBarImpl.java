package cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.Collection;
import java.util.Objects;
import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.List;

final class BossBarImpl extends HackyBossBarPlatformBridge implements BossBar
{
    private final List<Listener> listeners;
    private Component name;
    private float progress;
    private Color color;
    private Overlay overlay;
    private final Set<Flag> flags;
    @Nullable
    BossBarImplementation implementation;
    
    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final Color color, @NotNull final Overlay overlay) {
        this.listeners = (List<Listener>)new CopyOnWriteArrayList();
        this.flags = (Set<Flag>)EnumSet.noneOf((Class)Flag.class);
        this.name = (Component)Objects.requireNonNull((Object)name, "name");
        this.progress = progress;
        this.color = (Color)Objects.requireNonNull((Object)color, "color");
        this.overlay = (Overlay)Objects.requireNonNull((Object)overlay, "overlay");
    }
    
    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final Color color, @NotNull final Overlay overlay, @NotNull final Set<Flag> flags) {
        this(name, progress, color, overlay);
        this.flags.addAll((Collection)flags);
    }
    
    @NotNull
    @Override
    public Component name() {
        return this.name;
    }
    
    @NotNull
    @Override
    public BossBar name(@NotNull final Component newName) {
        Objects.requireNonNull((Object)newName, "name");
        final Component oldName = this.name;
        if (!Objects.equals((Object)newName, (Object)oldName)) {
            this.name = newName;
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarNameChanged(this, oldName, newName)));
        }
        return this;
    }
    
    @Override
    public float progress() {
        return this.progress;
    }
    
    @NotNull
    @Override
    public BossBar progress(final float newProgress) {
        checkProgress(newProgress);
        final float oldProgress = this.progress;
        if (newProgress != oldProgress) {
            this.progress = newProgress;
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarProgressChanged(this, oldProgress, newProgress)));
        }
        return this;
    }
    
    static void checkProgress(final float progress) {
        if (progress < 0.0f || progress > 1.0f) {
            throw new IllegalArgumentException("progress must be between 0.0 and 1.0, was " + progress);
        }
    }
    
    @NotNull
    @Override
    public Color color() {
        return this.color;
    }
    
    @NotNull
    @Override
    public BossBar color(@NotNull final Color newColor) {
        Objects.requireNonNull((Object)newColor, "color");
        final Color oldColor = this.color;
        if (newColor != oldColor) {
            this.color = newColor;
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarColorChanged(this, oldColor, newColor)));
        }
        return this;
    }
    
    @NotNull
    @Override
    public Overlay overlay() {
        return this.overlay;
    }
    
    @NotNull
    @Override
    public BossBar overlay(@NotNull final Overlay newOverlay) {
        Objects.requireNonNull((Object)newOverlay, "overlay");
        final Overlay oldOverlay = this.overlay;
        if (newOverlay != oldOverlay) {
            this.overlay = newOverlay;
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarOverlayChanged(this, oldOverlay, newOverlay)));
        }
        return this;
    }
    
    @NotNull
    @Override
    public Set<Flag> flags() {
        return (Set<Flag>)Collections.unmodifiableSet((Set)this.flags);
    }
    
    @NotNull
    @Override
    public BossBar flags(@NotNull final Set<Flag> newFlags) {
        if (newFlags.isEmpty() && !this.flags.isEmpty()) {
            final Set<Flag> oldFlags = (Set<Flag>)EnumSet.copyOf((Collection)this.flags);
            this.flags.clear();
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarFlagsChanged(this, (Set<Flag>)Collections.emptySet(), oldFlags)));
        }
        else if (!this.flags.equals((Object)newFlags)) {
            final Set<Flag> oldFlags = (Set<Flag>)EnumSet.copyOf((Collection)this.flags);
            this.flags.clear();
            this.flags.addAll((Collection)newFlags);
            final EnumSet copy;
            final Set<Flag> added = (Set<Flag>)(copy = EnumSet.copyOf((Collection)newFlags));
            final Set<Flag> set = oldFlags;
            Objects.requireNonNull((Object)set);
            ((Set)copy).removeIf(set::contains);
            final EnumSet copy2;
            final Set<Flag> removed = (Set<Flag>)(copy2 = EnumSet.copyOf((Collection)oldFlags));
            final Set<Flag> flags = this.flags;
            Objects.requireNonNull((Object)flags);
            ((Set)copy2).removeIf(flags::contains);
            this.forEachListener((Consumer<Listener>)(listener -> listener.bossBarFlagsChanged(this, added, removed)));
        }
        return this;
    }
    
    @Override
    public boolean hasFlag(@NotNull final Flag flag) {
        return this.flags.contains((Object)flag);
    }
    
    @NotNull
    @Override
    public BossBar addFlag(@NotNull final Flag flag) {
        return this.editFlags(flag, (BiPredicate<Set<Flag>, Flag>)Set::add, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsAdded);
    }
    
    @NotNull
    @Override
    public BossBar removeFlag(@NotNull final Flag flag) {
        return this.editFlags(flag, (BiPredicate<Set<Flag>, Flag>)Set::remove, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsRemoved);
    }
    
    @NotNull
    private BossBar editFlags(@NotNull final Flag flag, @NotNull final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        if (predicate.test((Object)this.flags, (Object)flag)) {
            onChange.accept((Object)this, (Object)Collections.singleton((Object)flag));
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addFlags(@NotNull final Flag... flags) {
        return this.editFlags(flags, (BiPredicate<Set<Flag>, Flag>)Set::add, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsAdded);
    }
    
    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final Flag... flags) {
        return this.editFlags(flags, (BiPredicate<Set<Flag>, Flag>)Set::remove, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsRemoved);
    }
    
    @NotNull
    private BossBar editFlags(final Flag[] flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        if (flags.length == 0) {
            return this;
        }
        Set<Flag> changes = null;
        for (int i = 0, length = flags.length; i < length; ++i) {
            if (predicate.test((Object)this.flags, (Object)flags[i])) {
                if (changes == null) {
                    changes = (Set<Flag>)EnumSet.noneOf((Class)Flag.class);
                }
                changes.add((Object)flags[i]);
            }
        }
        if (changes != null) {
            onChange.accept((Object)this, (Object)changes);
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addFlags(@NotNull final Iterable<Flag> flags) {
        return this.editFlags(flags, (BiPredicate<Set<Flag>, Flag>)Set::add, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsAdded);
    }
    
    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final Iterable<Flag> flags) {
        return this.editFlags(flags, (BiPredicate<Set<Flag>, Flag>)Set::remove, (BiConsumer<BossBarImpl, Set<Flag>>)BossBarImpl::onFlagsRemoved);
    }
    
    @NotNull
    private BossBar editFlags(final Iterable<Flag> flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        Set<Flag> changes = null;
        for (final Flag flag : flags) {
            if (predicate.test((Object)this.flags, (Object)flag)) {
                if (changes == null) {
                    changes = (Set<Flag>)EnumSet.noneOf((Class)Flag.class);
                }
                changes.add((Object)flag);
            }
        }
        if (changes != null) {
            onChange.accept((Object)this, (Object)changes);
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addListener(@NotNull final Listener listener) {
        this.listeners.add((Object)listener);
        return this;
    }
    
    @NotNull
    @Override
    public BossBar removeListener(@NotNull final Listener listener) {
        this.listeners.remove((Object)listener);
        return this;
    }
    
    @NotNull
    @Override
    public Iterable<? extends BossBarViewer> viewers() {
        if (this.implementation != null) {
            return this.implementation.viewers();
        }
        return (Iterable<? extends BossBarViewer>)Collections.emptyList();
    }
    
    private void forEachListener(@NotNull final Consumer<Listener> consumer) {
        for (final Listener listener : this.listeners) {
            consumer.accept((Object)listener);
        }
    }
    
    private static void onFlagsAdded(final BossBarImpl bar, final Set<Flag> flagsAdded) {
        bar.forEachListener((Consumer<Listener>)(listener -> listener.bossBarFlagsChanged(bar, flagsAdded, (Set<Flag>)Collections.emptySet())));
    }
    
    private static void onFlagsRemoved(final BossBarImpl bar, final Set<Flag> flagsRemoved) {
        bar.forEachListener((Consumer<Listener>)(listener -> listener.bossBarFlagsChanged(bar, (Set<Flag>)Collections.emptySet(), flagsRemoved)));
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("name", this.name), ExaminableProperty.of("progress", this.progress), ExaminableProperty.of("color", this.color), ExaminableProperty.of("overlay", this.overlay), ExaminableProperty.of("flags", this.flags) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @ApiStatus.Internal
    static final class ImplementationAccessor
    {
        private static final Optional<BossBarImplementation.Provider> SERVICE;
        
        private ImplementationAccessor() {
        }
        
        @NotNull
        static <I extends BossBarImplementation> I get(@NotNull final BossBar bar, @NotNull final Class<I> type) {
            BossBarImplementation implementation = ((BossBarImpl)bar).implementation;
            if (implementation == null) {
                implementation = ((BossBarImplementation.Provider)ImplementationAccessor.SERVICE.get()).create(bar);
                ((BossBarImpl)bar).implementation = implementation;
            }
            return type.cast(implementation);
        }
        
        static {
            SERVICE = Services.service(BossBarImplementation.Provider.class);
        }
    }
}
