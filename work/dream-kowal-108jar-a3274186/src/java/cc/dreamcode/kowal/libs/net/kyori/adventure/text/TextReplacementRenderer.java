package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import java.util.function.BiFunction;
import java.util.regex.Pattern;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import java.util.regex.Matcher;
import java.util.function.Consumer;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import java.util.regex.MatchResult;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.ComponentRenderer;

final class TextReplacementRenderer implements ComponentRenderer<TextReplacementRenderer.State>
{
    static final TextReplacementRenderer INSTANCE;
    
    private TextReplacementRenderer() {
    }
    
    @NotNull
    @Override
    public Component render(@NotNull final Component component, @NotNull final State state) {
        if (!state.running) {
            return component;
        }
        final boolean prevFirstMatch = state.firstMatch;
        state.firstMatch = true;
        final List<Component> oldChildren = component.children();
        final int oldChildrenSize = oldChildren.size();
        Style oldStyle = component.style();
        List<Component> children = null;
        Component modified = component;
        if (component instanceof TextComponent) {
            final String content = ((TextComponent)component).content();
            final Matcher matcher = state.pattern.matcher((CharSequence)content);
            int replacedUntil = 0;
            while (matcher.find()) {
                final PatternReplacementResult result = state.continuer.shouldReplace((MatchResult)matcher, ++state.matchCount, state.replaceCount);
                if (result == PatternReplacementResult.CONTINUE) {
                    continue;
                }
                if (result == PatternReplacementResult.STOP) {
                    state.running = false;
                    break;
                }
                if (matcher.start() == 0) {
                    if (matcher.end() == content.length()) {
                        final ComponentLike replacement = (ComponentLike)state.replacement.apply((Object)matcher, (Object)((ComponentBuilder<C, TextComponent.Builder>)Component.text().content(matcher.group())).style(component.style()));
                        modified = ((replacement == null) ? Component.empty() : replacement.asComponent());
                        if (modified.style().hoverEvent() != null) {
                            oldStyle = oldStyle.hoverEvent((HoverEventSource<?>)null);
                        }
                        modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                        if (children == null) {
                            children = (List<Component>)new ArrayList(oldChildrenSize + modified.children().size());
                            children.addAll((Collection)modified.children());
                        }
                    }
                    else {
                        modified = Component.text("", component.style());
                        final ComponentLike child = (ComponentLike)state.replacement.apply((Object)matcher, (Object)Component.text().content(matcher.group()));
                        if (child != null) {
                            if (children == null) {
                                children = (List<Component>)new ArrayList(oldChildrenSize + 1);
                            }
                            children.add((Object)child.asComponent());
                        }
                    }
                }
                else {
                    if (children == null) {
                        children = (List<Component>)new ArrayList(oldChildrenSize + 2);
                    }
                    if (state.firstMatch) {
                        modified = ((TextComponent)component).content(content.substring(0, matcher.start()));
                    }
                    else if (replacedUntil < matcher.start()) {
                        children.add((Object)Component.text(content.substring(replacedUntil, matcher.start())));
                    }
                    final ComponentLike builder = (ComponentLike)state.replacement.apply((Object)matcher, (Object)Component.text().content(matcher.group()));
                    if (builder != null) {
                        children.add((Object)builder.asComponent());
                    }
                }
                ++state.replaceCount;
                state.firstMatch = false;
                replacedUntil = matcher.end();
            }
            if (replacedUntil < content.length() && replacedUntil > 0) {
                if (children == null) {
                    children = (List<Component>)new ArrayList(oldChildrenSize);
                }
                children.add((Object)Component.text(content.substring(replacedUntil)));
            }
        }
        else if (modified instanceof TranslatableComponent) {
            final List<TranslationArgument> args = ((TranslatableComponent)modified).arguments();
            List<TranslationArgument> newArgs = null;
            for (int i = 0, size = args.size(); i < size; ++i) {
                final TranslationArgument original = (TranslationArgument)args.get(i);
                final TranslationArgument replaced = (original.value() instanceof Component) ? TranslationArgument.component(this.render((Component)original.value(), state)) : original;
                if (replaced != original && newArgs == null) {
                    newArgs = (List<TranslationArgument>)new ArrayList(size);
                    if (i > 0) {
                        newArgs.addAll((Collection)args.subList(0, i));
                    }
                }
                if (newArgs != null) {
                    newArgs.add((Object)replaced);
                }
            }
            if (newArgs != null) {
                modified = ((TranslatableComponent)modified).arguments(newArgs);
            }
        }
        if (state.running) {
            final HoverEvent<?> event = oldStyle.hoverEvent();
            if (event != null) {
                final HoverEvent<?> rendered = event.withRenderedValue(this, state);
                if (event != rendered) {
                    modified = modified.style((Consumer<Style.Builder>)(s -> s.hoverEvent((HoverEventSource<?>)rendered)));
                }
            }
            boolean first = true;
            for (int i = 0; i < oldChildrenSize; ++i) {
                final Component child2 = (Component)oldChildren.get(i);
                final Component replaced2 = this.render(child2, state);
                if (replaced2 != child2) {
                    if (children == null) {
                        children = (List<Component>)new ArrayList(oldChildrenSize);
                    }
                    if (first) {
                        children.addAll((Collection)oldChildren.subList(0, i));
                    }
                    first = false;
                }
                if (children != null) {
                    children.add((Object)replaced2);
                    first = false;
                }
            }
        }
        else if (children != null) {
            children.addAll((Collection)oldChildren);
        }
        state.firstMatch = prevFirstMatch;
        if (children != null) {
            return modified.children(children);
        }
        return modified;
    }
    
    static {
        INSTANCE = new TextReplacementRenderer();
    }
    
    static final class State
    {
        final Pattern pattern;
        final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
        final TextReplacementConfig.Condition continuer;
        boolean running;
        int matchCount;
        int replaceCount;
        boolean firstMatch;
        
        State(@NotNull final Pattern pattern, @NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement, final TextReplacementConfig.Condition continuer) {
            this.running = true;
            this.matchCount = 0;
            this.replaceCount = 0;
            this.firstMatch = true;
            this.pattern = pattern;
            this.replacement = replacement;
            this.continuer = continuer;
        }
    }
}
