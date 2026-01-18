package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

final class ComponentCompaction
{
    @VisibleForTesting
    static final boolean SIMPLIFY_STYLE_FOR_BLANK_COMPONENTS = false;
    
    private ComponentCompaction() {
    }
    
    static Component compact(@NotNull final Component self, @Nullable final Style parentStyle) {
        final List<Component> children = self.children();
        Component optimized = self.children((List<? extends ComponentLike>)Collections.emptyList());
        if (parentStyle != null) {
            optimized = optimized.style(self.style().unmerge(parentStyle));
        }
        final int childrenSize = children.size();
        if (childrenSize == 0) {
            if (isBlank(optimized)) {
                optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
            }
            return optimized;
        }
        if (childrenSize == 1 && optimized instanceof TextComponent) {
            final TextComponent textComponent = (TextComponent)optimized;
            if (textComponent.content().isEmpty()) {
                final Component child = (Component)children.get(0);
                return child.style(child.style().merge(optimized.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET)).compact();
            }
        }
        Style childParentStyle = optimized.style();
        if (parentStyle != null) {
            childParentStyle = childParentStyle.merge(parentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
        }
        final List<Component> childrenToAppend = (List<Component>)new ArrayList(children.size());
        for (int i = 0; i < children.size(); ++i) {
            Component child2 = (Component)children.get(i);
            child2 = compact(child2, childParentStyle);
            if (child2.children().isEmpty() && child2 instanceof TextComponent) {
                final TextComponent textComponent2 = (TextComponent)child2;
                if (textComponent2.content().isEmpty()) {
                    continue;
                }
            }
            childrenToAppend.add((Object)child2);
        }
        if (optimized instanceof TextComponent) {
            while (!childrenToAppend.isEmpty()) {
                final Component child3 = (Component)childrenToAppend.get(0);
                final Style childStyle = child3.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                if (!(child3 instanceof TextComponent) || !Objects.equals((Object)childStyle, (Object)childParentStyle)) {
                    break;
                }
                optimized = joinText((TextComponent)optimized, (TextComponent)child3);
                childrenToAppend.remove(0);
                childrenToAppend.addAll(0, (Collection)child3.children());
            }
        }
        int i = 0;
        while (i + 1 < childrenToAppend.size()) {
            final Component child2 = (Component)childrenToAppend.get(i);
            final Component neighbor = (Component)childrenToAppend.get(i + 1);
            if (child2.children().isEmpty() && child2 instanceof TextComponent && neighbor instanceof TextComponent) {
                final Style childStyle2 = child2.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                final Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                if (childStyle2.equals(neighborStyle)) {
                    final Component combined = joinText((TextComponent)child2, (TextComponent)neighbor);
                    childrenToAppend.set(i, (Object)combined);
                    childrenToAppend.remove(i + 1);
                    continue;
                }
            }
            ++i;
        }
        if (childrenToAppend.isEmpty() && isBlank(optimized)) {
            optimized = optimized.style(simplifyStyleForBlank(optimized.style(), parentStyle));
        }
        return optimized.children(childrenToAppend);
    }
    
    private static boolean isBlank(final Component component) {
        if (component instanceof TextComponent) {
            final TextComponent textComponent = (TextComponent)component;
            final String content = textComponent.content();
            for (int i = 0; i < content.length(); ++i) {
                final char c = content.charAt(i);
                if (c != ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @NotNull
    private static Style simplifyStyleForBlank(@NotNull final Style style, @Nullable final Style parentStyle) {
        return style;
    }
    
    private static TextComponent joinText(final TextComponent one, final TextComponent two) {
        return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
    }
}
