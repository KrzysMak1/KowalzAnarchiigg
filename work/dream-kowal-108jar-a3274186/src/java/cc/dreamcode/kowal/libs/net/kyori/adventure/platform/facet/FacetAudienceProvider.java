package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.permission.PermissionChecker;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.Collection;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointers;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.ForwardingAudience;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.AudienceProvider;

@ApiStatus.Internal
public abstract class FacetAudienceProvider<V, A extends FacetAudience<V>> implements AudienceProvider, ForwardingAudience
{
    protected static final Locale DEFAULT_LOCALE;
    protected final ComponentRenderer<Pointered> componentRenderer;
    private final Audience console;
    private final Audience player;
    protected final Map<V, A> viewers;
    private final Map<UUID, A> players;
    private final Set<A> consoles;
    private A empty;
    private volatile boolean closed;
    
    protected FacetAudienceProvider(@NotNull final ComponentRenderer<Pointered> componentRenderer) {
        this.componentRenderer = (ComponentRenderer)Objects.requireNonNull((Object)componentRenderer, "component renderer");
        this.viewers = (Map<V, A>)new ConcurrentHashMap();
        this.players = (Map<UUID, A>)new ConcurrentHashMap();
        this.consoles = (Set<A>)new CopyOnWriteArraySet();
        this.console = new ForwardingAudience() {
            @NotNull
            @Override
            public Iterable<? extends Audience> audiences() {
                return (Iterable<? extends Audience>)FacetAudienceProvider.this.consoles;
            }
            
            @NotNull
            @Override
            public Pointers pointers() {
                if (FacetAudienceProvider.this.consoles.size() == 1) {
                    return ((FacetAudience)FacetAudienceProvider.this.consoles.iterator().next()).pointers();
                }
                return Pointers.empty();
            }
        };
        this.player = Audience.audience((Iterable<? extends Audience>)this.players.values());
        this.closed = false;
    }
    
    public void addViewer(@NotNull final V viewer) {
        if (this.closed) {
            return;
        }
        final A audience = (A)this.viewers.computeIfAbsent(Objects.requireNonNull((Object)viewer, "viewer"), v -> this.createAudience((Collection<V>)Collections.singletonList(v)));
        final FacetPointers.Type type = audience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
        if (type == FacetPointers.Type.PLAYER) {
            final UUID id = audience.getOrDefault(Identity.UUID, null);
            if (id != null) {
                this.players.putIfAbsent((Object)id, (Object)audience);
            }
        }
        else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.add((Object)audience);
        }
    }
    
    public void removeViewer(@NotNull final V viewer) {
        final A audience = (A)this.viewers.remove((Object)viewer);
        if (audience == null) {
            return;
        }
        final FacetPointers.Type type = audience.getOrDefault(FacetPointers.TYPE, FacetPointers.Type.OTHER);
        if (type == FacetPointers.Type.PLAYER) {
            final UUID id = audience.getOrDefault(Identity.UUID, null);
            if (id != null) {
                this.players.remove((Object)id);
            }
        }
        else if (type == FacetPointers.Type.CONSOLE) {
            this.consoles.remove((Object)audience);
        }
        audience.close();
    }
    
    public void refreshViewer(@NotNull final V viewer) {
        final A audience = (A)this.viewers.get((Object)viewer);
        if (audience != null) {
            audience.refresh();
        }
    }
    
    @NotNull
    protected abstract A createAudience(@NotNull final Collection<V> viewers);
    
    @NotNull
    @Override
    public Iterable<? extends Audience> audiences() {
        return (Iterable<? extends Audience>)this.viewers.values();
    }
    
    @NotNull
    @Override
    public Audience all() {
        return this;
    }
    
    @NotNull
    @Override
    public Audience console() {
        return this.console;
    }
    
    @NotNull
    @Override
    public Audience players() {
        return this.player;
    }
    
    @NotNull
    @Override
    public Audience player(@NotNull final UUID playerId) {
        return (Audience)this.players.getOrDefault((Object)playerId, this.empty());
    }
    
    @NotNull
    private A empty() {
        if (this.empty == null) {
            this.empty = this.createAudience((Collection<V>)Collections.emptyList());
        }
        return this.empty;
    }
    
    @NotNull
    public Audience filter(@NotNull final Predicate<V> predicate) {
        return Audience.audience((Iterable<? extends Audience>)filter((java.lang.Iterable<Object>)this.viewers.entrySet(), (java.util.function.Predicate<Object>)(entry -> predicate.test(entry.getKey())), (java.util.function.Function<Object, Object>)Map.Entry::getValue));
    }
    
    @NotNull
    private Audience filterPointers(@NotNull final Predicate<Pointered> predicate) {
        return Audience.audience((Iterable<? extends Audience>)filter((java.lang.Iterable<Object>)this.viewers.entrySet(), (java.util.function.Predicate<Object>)(entry -> predicate.test((Object)entry.getValue())), (java.util.function.Function<Object, Object>)Map.Entry::getValue));
    }
    
    @NotNull
    @Override
    public Audience permission(@NotNull final String permission) {
        return this.filterPointers((Predicate<Pointered>)(pointers -> ((PermissionChecker)pointers.get(PermissionChecker.POINTER).orElse((Object)PermissionChecker.always(TriState.FALSE))).test(permission)));
    }
    
    @NotNull
    @Override
    public Audience world(@NotNull final Key world) {
        return this.filterPointers((Predicate<Pointered>)(pointers -> world.equals(pointers.getOrDefault(FacetPointers.WORLD, null))));
    }
    
    @NotNull
    @Override
    public Audience server(@NotNull final String serverName) {
        return this.filterPointers((Predicate<Pointered>)(pointers -> serverName.equals((Object)pointers.getOrDefault(FacetPointers.SERVER, null))));
    }
    
    @Override
    public void close() {
        this.closed = true;
        for (final V viewer : this.viewers.keySet()) {
            this.removeViewer(viewer);
        }
    }
    
    @NotNull
    private static <T, V> Iterable<V> filter(@NotNull final Iterable<T> input, @NotNull final Predicate<T> filter, @NotNull final Function<T, V> transformer) {
        return (Iterable<V>)new Iterable<V>() {
            @NotNull
            public Iterator<V> iterator() {
                return (Iterator<V>)new Iterator<V>() {
                    private final Iterator<T> parent = input.iterator();
                    private V next;
                    
                    {
                        this.populate();
                    }
                    
                    private void populate() {
                        this.next = null;
                        while (this.parent.hasNext()) {
                            final T next = (T)this.parent.next();
                            if (filter.test((Object)next)) {
                                this.next = (V)transformer.apply((Object)next);
                            }
                        }
                    }
                    
                    public boolean hasNext() {
                        return this.next != null;
                    }
                    
                    public V next() {
                        if (this.next == null) {
                            throw new NoSuchElementException();
                        }
                        final V next = this.next;
                        this.populate();
                        return next;
                    }
                };
            }
            
            public void forEach(final Consumer<? super V> action) {
                for (final T each : input) {
                    if (filter.test((Object)each)) {
                        action.accept(transformer.apply((Object)each));
                    }
                }
            }
        };
    }
    
    static {
        DEFAULT_LOCALE = Locale.US;
    }
}
