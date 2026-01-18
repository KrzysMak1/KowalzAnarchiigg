package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.title.TitlePart;
import cc.dreamcode.kowal.libs.net.kyori.adventure.title.Title;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import java.util.List;
import java.util.LinkedList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.inventory.Book;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.SoundStop;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.SignedMessage;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.ChatType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.MessageType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import java.util.Iterator;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Objects;
import java.util.Collection;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import java.io.Closeable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;

@ApiStatus.Internal
public class FacetAudience<V> implements Audience, Closeable
{
    @NotNull
    protected final FacetAudienceProvider<V, FacetAudience<V>> provider;
    @NotNull
    private final Set<V> viewers;
    @Nullable
    private V viewer;
    private volatile Pointers pointers;
    private final Facet.Chat<V, Object> chat;
    private final Facet.ActionBar<V, Object> actionBar;
    private final Facet.Title<V, Object, Object, Object> title;
    private final Facet.Sound<V, Object> sound;
    private final Facet.EntitySound<V, Object> entitySound;
    private final Facet.Book<V, Object, Object> book;
    private final Facet.BossBar.Builder<V, Facet.BossBar<V>> bossBar;
    @Nullable
    private final Map<BossBar, Facet.BossBar<V>> bossBars;
    private final Facet.TabList<V, Object> tabList;
    @NotNull
    private final Collection<? extends Facet.Pointers<V>> pointerProviders;
    
    public FacetAudience(@NotNull final FacetAudienceProvider provider, @NotNull final Collection<? extends V> viewers, @Nullable final Collection<? extends Facet.Chat> chat, @Nullable final Collection<? extends Facet.ActionBar> actionBar, @Nullable final Collection<? extends Facet.Title> title, @Nullable final Collection<? extends Facet.Sound> sound, @Nullable final Collection<? extends Facet.EntitySound> entitySound, @Nullable final Collection<? extends Facet.Book> book, @Nullable final Collection<? extends Facet.BossBar.Builder> bossBar, @Nullable final Collection<? extends Facet.TabList> tabList, @Nullable final Collection<? extends Facet.Pointers> pointerProviders) {
        this.provider = (FacetAudienceProvider)Objects.requireNonNull((Object)provider, "audience provider");
        this.viewers = (Set<V>)new CopyOnWriteArraySet();
        for (final V viewer : (Collection)Objects.requireNonNull((Object)viewers, "viewers")) {
            this.addViewer(viewer);
        }
        this.refresh();
        this.chat = Facet.of(chat, this.viewer);
        this.actionBar = Facet.of(actionBar, this.viewer);
        this.title = Facet.of(title, this.viewer);
        this.sound = Facet.of(sound, this.viewer);
        this.entitySound = Facet.of(entitySound, this.viewer);
        this.book = Facet.of(book, this.viewer);
        this.bossBar = Facet.of(bossBar, this.viewer);
        this.bossBars = (Map<BossBar, Facet.BossBar<V>>)((this.bossBar == null) ? null : Collections.synchronizedMap((Map)new IdentityHashMap(4)));
        this.tabList = Facet.of(tabList, this.viewer);
        this.pointerProviders = (Collection<? extends Facet.Pointers<V>>)((pointerProviders == null) ? Collections.emptyList() : pointerProviders);
    }
    
    public void addViewer(@NotNull final V viewer) {
        if (this.viewers.add((Object)viewer) && this.viewer == null) {
            this.viewer = viewer;
            this.refresh();
        }
    }
    
    public void removeViewer(@NotNull final V viewer) {
        if (this.viewers.remove((Object)viewer) && this.viewer == viewer) {
            this.viewer = (V)(this.viewers.isEmpty() ? null : this.viewers.iterator().next());
            this.refresh();
        }
        if (this.bossBars == null) {
            return;
        }
        for (final Facet.BossBar<V> listener : this.bossBars.values()) {
            listener.removeViewer(viewer);
        }
    }
    
    public void refresh() {
        synchronized (this) {
            this.pointers = null;
        }
        if (this.bossBars == null) {
            return;
        }
        for (final Map.Entry<BossBar, Facet.BossBar<V>> entry : this.bossBars.entrySet()) {
            final BossBar bar = (BossBar)entry.getKey();
            final Facet.BossBar<V> listener = (Facet.BossBar<V>)entry.getValue();
            listener.bossBarNameChanged(bar, bar.name(), bar.name());
        }
    }
    
    @Override
    public void sendMessage(@NotNull final Identity source, @NotNull final Component original, @NotNull final MessageType type) {
        if (this.chat == null) {
            return;
        }
        final Object message = this.createMessage(original, this.chat);
        if (message == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.chat.sendMessage(viewer, source, message, type);
        }
    }
    
    @Override
    public void sendMessage(@NotNull final Component original, final ChatType.Bound boundChatType) {
        if (this.chat == null) {
            return;
        }
        final Object message = this.createMessage(original, this.chat);
        if (message == null) {
            return;
        }
        final Component name = this.provider.componentRenderer.render(boundChatType.name(), this);
        Component target = null;
        if (boundChatType.target() != null) {
            target = this.provider.componentRenderer.render(boundChatType.target(), this);
        }
        final Object renderedType = boundChatType.type().bind(name, target);
        for (final V viewer : this.viewers) {
            this.chat.sendMessage(viewer, Identity.nil(), message, renderedType);
        }
    }
    
    @Override
    public void sendMessage(@NotNull final SignedMessage signedMessage, final ChatType.Bound boundChatType) {
        if (signedMessage.isSystem()) {
            final Component content = (signedMessage.unsignedContent() != null) ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
            this.sendMessage(content, boundChatType);
        }
        else {
            super.sendMessage(signedMessage, boundChatType);
        }
    }
    
    @Override
    public void sendActionBar(@NotNull final Component original) {
        if (this.actionBar == null) {
            return;
        }
        final Object message = this.createMessage(original, this.actionBar);
        if (message == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.actionBar.sendMessage(viewer, message);
        }
    }
    
    @Override
    public void playSound(final Sound original) {
        if (this.sound == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            final Object position = this.sound.createPosition(viewer);
            if (position == null) {
                continue;
            }
            this.sound.playSound(viewer, original, position);
        }
    }
    
    @Override
    public void playSound(@NotNull final Sound sound, final Sound.Emitter emitter) {
        if (this.entitySound == null) {
            return;
        }
        if (emitter == Sound.Emitter.self()) {
            for (final V viewer : this.viewers) {
                final Object message = this.entitySound.createForSelf(viewer, sound);
                if (message == null) {
                    continue;
                }
                this.entitySound.playSound(viewer, message);
            }
        }
        else {
            final Object message2 = this.entitySound.createForEmitter(sound, emitter);
            if (message2 == null) {
                return;
            }
            for (final V viewer2 : this.viewers) {
                this.entitySound.playSound(viewer2, message2);
            }
        }
    }
    
    @Override
    public void playSound(final Sound original, final double x, final double y, final double z) {
        if (this.sound == null) {
            return;
        }
        final Object position = this.sound.createPosition(x, y, z);
        for (final V viewer : this.viewers) {
            this.sound.playSound(viewer, original, position);
        }
    }
    
    @Override
    public void stopSound(@NotNull final SoundStop original) {
        if (this.sound == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.sound.stopSound(viewer, original);
        }
    }
    
    @Override
    public void openBook(final Book original) {
        if (this.book == null) {
            return;
        }
        final String title = this.toPlain(original.title());
        final String author = this.toPlain(original.author());
        final List<Object> pages = (List<Object>)new LinkedList();
        for (final Component originalPage : original.pages()) {
            final Object page = this.createMessage(originalPage, this.book);
            if (page != null) {
                pages.add(page);
            }
        }
        if (title == null || author == null || pages.isEmpty()) {
            return;
        }
        final Object book = this.book.createBook(title, author, (java.lang.Iterable<Object>)pages);
        if (book == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.book.openBook(viewer, book);
        }
    }
    
    private String toPlain(final Component comp) {
        if (comp == null) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        final ComponentFlattener basic = ComponentFlattener.basic();
        final Component render = this.provider.componentRenderer.render(comp, this);
        final StringBuilder sb = builder;
        Objects.requireNonNull((Object)sb);
        basic.flatten(render, sb::append);
        return builder.toString();
    }
    
    @Override
    public void showTitle(final Title original) {
        if (this.title == null) {
            return;
        }
        final Object mainTitle = this.createMessage(original.title(), this.title);
        final Object subTitle = this.createMessage(original.subtitle(), this.title);
        final Title.Times times = original.times();
        final int inTicks = (times == null) ? -1 : this.title.toTicks(times.fadeIn());
        final int stayTicks = (times == null) ? -1 : this.title.toTicks(times.stay());
        final int outTicks = (times == null) ? -1 : this.title.toTicks(times.fadeOut());
        final Object collection = this.title.createTitleCollection();
        if (inTicks != -1 || stayTicks != -1 || outTicks != -1) {
            this.title.contributeTimes(collection, inTicks, stayTicks, outTicks);
        }
        this.title.contributeSubtitle(collection, subTitle);
        this.title.contributeTitle(collection, mainTitle);
        final Object title = this.title.completeTitle(collection);
        if (title == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.title.showTitle(viewer, title);
        }
    }
    
    @Override
    public <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
        if (this.title == null) {
            return;
        }
        Objects.requireNonNull((Object)value, "value");
        final Object collection = this.title.createTitleCollection();
        if (part == TitlePart.TITLE) {
            final Object message = this.createMessage((Component)value, this.title);
            if (message != null) {
                this.title.contributeTitle(collection, message);
            }
        }
        else if (part == TitlePart.SUBTITLE) {
            final Object message = this.createMessage((Component)value, this.title);
            if (message != null) {
                this.title.contributeSubtitle(collection, message);
            }
        }
        else {
            if (part != TitlePart.TIMES) {
                throw new IllegalArgumentException("Unknown TitlePart '" + (Object)part + "'");
            }
            final Title.Times times = (Title.Times)value;
            final int inTicks = this.title.toTicks(times.fadeIn());
            final int stayTicks = this.title.toTicks(times.stay());
            final int outTicks = this.title.toTicks(times.fadeOut());
            if (inTicks != -1 || stayTicks != -1 || outTicks != -1) {
                this.title.contributeTimes(collection, inTicks, stayTicks, outTicks);
            }
        }
        final Object title = this.title.completeTitle(collection);
        if (title == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.title.showTitle(viewer, title);
        }
    }
    
    @Override
    public void clearTitle() {
        if (this.title == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.title.clearTitle(viewer);
        }
    }
    
    @Override
    public void resetTitle() {
        if (this.title == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            this.title.resetTitle(viewer);
        }
    }
    
    @Override
    public void showBossBar(@NotNull final BossBar bar) {
        if (this.bossBar == null || this.bossBars == null) {
            return;
        }
        Facet.BossBar<V> listener;
        synchronized (this.bossBars) {
            listener = (Facet.BossBar)this.bossBars.get((Object)bar);
            if (listener == null) {
                listener = new FacetBossBarListener<V>(this.bossBar.createBossBar((java.util.Collection<V>)this.viewers), (Function<Component, Component>)(message -> this.provider.componentRenderer.render(message, this)));
                this.bossBars.put((Object)bar, (Object)listener);
            }
        }
        if (listener.isEmpty()) {
            listener.bossBarInitialized(bar);
            bar.addListener(listener);
        }
        for (final V viewer : this.viewers) {
            listener.addViewer(viewer);
        }
    }
    
    @Override
    public void hideBossBar(@NotNull final BossBar bar) {
        if (this.bossBars == null) {
            return;
        }
        final Facet.BossBar<V> listener = (Facet.BossBar<V>)this.bossBars.get((Object)bar);
        if (listener == null) {
            return;
        }
        for (final V viewer : this.viewers) {
            listener.removeViewer(viewer);
        }
        if (listener.isEmpty() && this.bossBars.remove((Object)bar) != null) {
            bar.removeListener(listener);
            listener.close();
        }
    }
    
    @Override
    public void sendPlayerListHeader(@NotNull final Component header) {
        if (this.tabList != null) {
            final Object headerFormatted = this.createMessage(header, this.tabList);
            if (headerFormatted == null) {
                return;
            }
            for (final V viewer : this.viewers) {
                this.tabList.send(viewer, headerFormatted, null);
            }
        }
    }
    
    @Override
    public void sendPlayerListFooter(@NotNull final Component footer) {
        if (this.tabList != null) {
            final Object footerFormatted = this.createMessage(footer, this.tabList);
            if (footerFormatted == null) {
                return;
            }
            for (final V viewer : this.viewers) {
                this.tabList.send(viewer, null, footerFormatted);
            }
        }
    }
    
    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
        if (this.tabList != null) {
            final Object headerFormatted = this.createMessage(header, this.tabList);
            final Object footerFormatted = this.createMessage(footer, this.tabList);
            if (headerFormatted == null || footerFormatted == null) {
                return;
            }
            for (final V viewer : this.viewers) {
                this.tabList.send(viewer, headerFormatted, footerFormatted);
            }
        }
    }
    
    @NotNull
    public Pointers pointers() {
        if (this.pointers == null) {
            synchronized (this) {
                if (this.pointers == null) {
                    final V viewer = this.viewer;
                    if (viewer == null) {
                        return Pointers.empty();
                    }
                    final Pointers.Builder builder = Pointers.builder();
                    this.contributePointers(builder);
                    for (final Facet.Pointers<V> provider : this.pointerProviders) {
                        if (provider.isApplicable(viewer)) {
                            provider.contributePointers(viewer, builder);
                        }
                    }
                    return this.pointers = builder.build();
                }
            }
        }
        return this.pointers;
    }
    
    @ApiStatus.OverrideOnly
    protected void contributePointers(final Pointers.Builder builder) {
    }
    
    public void close() {
        if (this.bossBars != null) {
            for (final BossBar bar : new LinkedList((Collection)this.bossBars.keySet())) {
                this.hideBossBar(bar);
            }
            this.bossBars.clear();
        }
        for (final V viewer : this.viewers) {
            this.removeViewer(viewer);
        }
        this.viewers.clear();
    }
    
    @Nullable
    private Object createMessage(@NotNull final Component original, final Facet.Message<V, Object> facet) {
        final Component message = this.provider.componentRenderer.render(original, this);
        final V viewer = this.viewer;
        return (viewer == null) ? null : facet.createMessage(viewer, message);
    }
}
