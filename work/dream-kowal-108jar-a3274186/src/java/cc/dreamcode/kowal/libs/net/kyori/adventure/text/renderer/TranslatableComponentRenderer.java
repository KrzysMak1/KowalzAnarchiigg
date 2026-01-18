package cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BuildableComponent;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslationArgument;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.SelectorComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ScoreComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.KeybindComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponentBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.StorageNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.EntityNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BlockNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.Nullable;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.Set;

public abstract class TranslatableComponentRenderer<C> extends AbstractComponentRenderer<C>
{
    private static final Set<Style.Merge> MERGES;
    
    @NotNull
    public static TranslatableComponentRenderer<Locale> usingTranslationSource(@NotNull final Translator source) {
        Objects.requireNonNull((Object)source, "source");
        return new TranslatableComponentRenderer<Locale>() {
            @Nullable
            @Override
            protected MessageFormat translate(@NotNull final String key, @NotNull final Locale context) {
                return source.translate(key, context);
            }
            
            @NotNull
            @Override
            protected Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final Locale context) {
                final TriState anyTranslations = source.hasAnyTranslations();
                if (anyTranslations != TriState.TRUE && anyTranslations != TriState.NOT_SET) {
                    return component;
                }
                final Component translated = source.translate(component, context);
                if (translated != null) {
                    return translated;
                }
                return super.renderTranslatable(component, context);
            }
        };
    }
    
    @Nullable
    protected MessageFormat translate(@NotNull final String key, @NotNull final C context) {
        return null;
    }
    
    @Nullable
    protected MessageFormat translate(@NotNull final String key, @Nullable final String fallback, @NotNull final C context) {
        return this.translate(key, context);
    }
    
    @NotNull
    @Override
    protected Component renderBlockNbt(@NotNull final BlockNBTComponent component, @NotNull final C context) {
        final BlockNBTComponent.Builder builder = this.nbt(context, Component.blockNBT(), component).pos(component.pos());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderEntityNbt(@NotNull final EntityNBTComponent component, @NotNull final C context) {
        final EntityNBTComponent.Builder builder = this.nbt(context, Component.entityNBT(), component).selector(component.selector());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderStorageNbt(@NotNull final StorageNBTComponent component, @NotNull final C context) {
        final StorageNBTComponent.Builder builder = this.nbt(context, Component.storageNBT(), component).storage(component.storage());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    protected <O extends NBTComponent<O, B>, B extends NBTComponentBuilder<O, B>> B nbt(@NotNull final C context, final B builder, final O oldComponent) {
        ((NBTComponentBuilder<C, NBTComponentBuilder<O, B>>)builder).nbtPath(oldComponent.nbtPath()).interpret(oldComponent.interpret());
        final Component separator = oldComponent.separator();
        if (separator != null) {
            builder.separator(this.render(separator, context));
        }
        return builder;
    }
    
    @NotNull
    @Override
    protected Component renderKeybind(@NotNull final KeybindComponent component, @NotNull final C context) {
        final KeybindComponent.Builder builder = Component.keybind().keybind(component.keybind());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderScore(@NotNull final ScoreComponent component, @NotNull final C context) {
        final ScoreComponent.Builder builder = Component.score().name(component.name()).objective(component.objective()).value(component.value());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderSelector(@NotNull final SelectorComponent component, @NotNull final C context) {
        final SelectorComponent.Builder builder = Component.selector().pattern(component.pattern());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderText(@NotNull final TextComponent component, @NotNull final C context) {
        final TextComponent.Builder builder = Component.text().content(component.content());
        return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }
    
    @NotNull
    @Override
    protected Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final C context) {
        final MessageFormat format = this.translate(component.key(), component.fallback(), context);
        if (format == null) {
            final TranslatableComponent.Builder builder = Component.translatable().key(component.key()).fallback(component.fallback());
            if (!component.arguments().isEmpty()) {
                final List<TranslationArgument> args = (List<TranslationArgument>)new ArrayList((Collection)component.arguments());
                for (int i = 0, size = args.size(); i < size; ++i) {
                    final TranslationArgument arg = (TranslationArgument)args.get(i);
                    if (arg.value() instanceof Component) {
                        args.set(i, (Object)TranslationArgument.component(this.render((Component)arg.value(), context)));
                    }
                }
                builder.arguments(args);
            }
            return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
        }
        final List<TranslationArgument> args2 = component.arguments();
        final TextComponent.Builder builder2 = Component.text();
        this.mergeStyle(component, builder2, context);
        if (args2.isEmpty()) {
            builder2.content(format.format((Object[])null, new StringBuffer(), (FieldPosition)null).toString());
            return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder2, context);
        }
        final Object[] nulls = new Object[args2.size()];
        final StringBuffer sb = format.format(nulls, new StringBuffer(), (FieldPosition)null);
        final AttributedCharacterIterator it = format.formatToCharacterIterator((Object)nulls);
        while (it.getIndex() < it.getEndIndex()) {
            final int end = it.getRunLimit();
            final Integer index = (Integer)it.getAttribute((AttributedCharacterIterator.Attribute)MessageFormat.Field.ARGUMENT);
            if (index != null) {
                final TranslationArgument arg2 = (TranslationArgument)args2.get((int)index);
                if (arg2.value() instanceof Component) {
                    builder2.append(this.render(arg2.asComponent(), context));
                }
                else {
                    builder2.append(arg2.asComponent());
                }
            }
            else {
                builder2.append(Component.text(sb.substring(it.getIndex(), end)));
            }
            it.setIndex(end);
        }
        return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder2, context);
    }
    
    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(final Component component, final B builder, final C context) {
        this.mergeStyle(component, builder, context);
        return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
    }
    
    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(final List<Component> children, final B builder, final C context) {
        if (!children.isEmpty()) {
            children.forEach(child -> builder.append(this.render(child, context)));
        }
        return ((ComponentBuilder<O, B>)builder).build();
    }
    
    protected <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
        builder.mergeStyle(component, TranslatableComponentRenderer.MERGES);
        builder.clickEvent(component.clickEvent());
        final HoverEvent<?> hoverEvent = component.hoverEvent();
        if (hoverEvent != null) {
            builder.hoverEvent((HoverEventSource<?>)hoverEvent.withRenderedValue(this, context));
        }
    }
    
    static {
        MERGES = Style.Merge.merges(Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT);
    }
}
