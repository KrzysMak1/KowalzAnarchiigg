package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleSetter;
import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.function.Function;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.Collection;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.List;

abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B>
{
    protected List<Component> children;
    @Nullable
    private Style style;
    private Style.Builder styleBuilder;
    
    protected AbstractComponentBuilder() {
        this.children = (List<Component>)Collections.emptyList();
    }
    
    protected AbstractComponentBuilder(@NotNull final C component) {
        this.children = (List<Component>)Collections.emptyList();
        final List<Component> children = component.children();
        if (!children.isEmpty()) {
            this.children = (List<Component>)new ArrayList((Collection)children);
        }
        if (component.hasStyling()) {
            this.style = component.style();
        }
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Component component) {
        if (component == Component.empty()) {
            return (B)this;
        }
        this.prepareChildren();
        this.children.add((Object)Objects.requireNonNull((Object)component, "component"));
        return (B)this;
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Component... components) {
        return this.append((ComponentLike[])components);
    }
    
    @NotNull
    @Override
    public B append(@NotNull final ComponentLike... components) {
        Objects.requireNonNull((Object)components, "components");
        boolean prepared = false;
        for (int i = 0, length = components.length; i < length; ++i) {
            final Component component = ((ComponentLike)Objects.requireNonNull((Object)components[i], "components[?]")).asComponent();
            if (component != Component.empty()) {
                if (!prepared) {
                    this.prepareChildren();
                    prepared = true;
                }
                this.children.add((Object)Objects.requireNonNull((Object)component, "components[?]"));
            }
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Iterable<? extends ComponentLike> components) {
        Objects.requireNonNull((Object)components, "components");
        boolean prepared = false;
        for (final ComponentLike like : components) {
            final Component component = ((ComponentLike)Objects.requireNonNull((Object)like, "components[?]")).asComponent();
            if (component != Component.empty()) {
                if (!prepared) {
                    this.prepareChildren();
                    prepared = true;
                }
                this.children.add((Object)Objects.requireNonNull((Object)component, "components[?]"));
            }
        }
        return (B)this;
    }
    
    private void prepareChildren() {
        if (this.children == Collections.emptyList()) {
            this.children = (List<Component>)new ArrayList();
        }
    }
    
    @NotNull
    @Override
    public B applyDeep(@NotNull final Consumer<? super ComponentBuilder<?, ?>> consumer) {
        this.apply(consumer);
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = (ListIterator<Component>)this.children.listIterator();
        while (it.hasNext()) {
            final Component child = (Component)it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final ComponentBuilder<?, ?> childBuilder = ((BuildableComponent)child).toBuilder();
            childBuilder.applyDeep(consumer);
            it.set((Object)childBuilder.build());
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mapChildren(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = (ListIterator<Component>)this.children.listIterator();
        while (it.hasNext()) {
            final Component child = (Component)it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final BuildableComponent<?, ?> mappedChild = (BuildableComponent<?, ?>)Objects.requireNonNull((Object)function.apply((Object)child), "mappedChild");
            if (child == mappedChild) {
                continue;
            }
            it.set((Object)mappedChild);
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mapChildrenDeep(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = (ListIterator<Component>)this.children.listIterator();
        while (it.hasNext()) {
            final Component child = (Component)it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final BuildableComponent<?, ?> mappedChild = (BuildableComponent<?, ?>)Objects.requireNonNull((Object)function.apply((Object)child), "mappedChild");
            if (mappedChild.children().isEmpty()) {
                if (child == mappedChild) {
                    continue;
                }
                it.set((Object)mappedChild);
            }
            else {
                final ComponentBuilder<?, ?> builder = (ComponentBuilder<?, ?>)mappedChild.toBuilder();
                builder.mapChildrenDeep(function);
                it.set((Object)builder.build());
            }
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public List<Component> children() {
        return (List<Component>)Collections.unmodifiableList((List)this.children);
    }
    
    @NotNull
    @Override
    public B style(@NotNull final Style style) {
        this.style = style;
        this.styleBuilder = null;
        return (B)this;
    }
    
    @NotNull
    @Override
    public B style(@NotNull final Consumer<Style.Builder> consumer) {
        consumer.accept((Object)this.styleBuilder());
        return (B)this;
    }
    
    @NotNull
    @Override
    public B font(@Nullable final Key font) {
        this.styleBuilder().font(font);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B color(@Nullable final TextColor color) {
        this.styleBuilder().color(color);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B colorIfAbsent(@Nullable final TextColor color) {
        this.styleBuilder().colorIfAbsent(color);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        this.styleBuilder().decoration(decoration, state);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        this.styleBuilder().decorationIfAbsent(decoration, state);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B clickEvent(@Nullable final ClickEvent event) {
        this.styleBuilder().clickEvent(event);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B hoverEvent(@Nullable final HoverEventSource<?> source) {
        this.styleBuilder().hoverEvent(source);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B insertion(@Nullable final String insertion) {
        this.styleBuilder().insertion(insertion);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        this.styleBuilder().merge(((Component)Objects.requireNonNull((Object)that, "component")).style(), merges);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B resetStyle() {
        this.style = null;
        this.styleBuilder = null;
        return (B)this;
    }
    
    private Style.Builder styleBuilder() {
        if (this.styleBuilder == null) {
            if (this.style != null) {
                this.styleBuilder = this.style.toBuilder();
                this.style = null;
            }
            else {
                this.styleBuilder = Style.style();
            }
        }
        return this.styleBuilder;
    }
    
    protected final boolean hasStyle() {
        return this.styleBuilder != null || this.style != null;
    }
    
    @NotNull
    protected Style buildStyle() {
        if (this.styleBuilder != null) {
            return this.styleBuilder.build();
        }
        if (this.style != null) {
            return this.style;
        }
        return Style.empty();
    }
}
