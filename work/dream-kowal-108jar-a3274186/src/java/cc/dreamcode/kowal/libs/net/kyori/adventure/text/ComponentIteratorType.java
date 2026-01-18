package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import java.util.Set;
import java.util.Deque;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
@ApiStatus.NonExtendable
public interface ComponentIteratorType
{
    public static final ComponentIteratorType DEPTH_FIRST = (component, deque, flags) -> {
        if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
            final TranslatableComponent translatable = (TranslatableComponent)component;
            final List<TranslationArgument> args = translatable.arguments();
            for (int i = args.size() - 1; i >= 0; --i) {
                deque.addFirst((Object)((ComponentLike)args.get(i)).asComponent());
            }
        }
        final HoverEvent<?> hoverEvent = component.hoverEvent();
        if (hoverEvent != null) {
            final HoverEvent.Action<?> action = hoverEvent.action();
            if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
                deque.addFirst((Object)((HoverEvent.ShowEntity)hoverEvent.value()).name());
            }
            else if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
                deque.addFirst((Object)hoverEvent.value());
            }
        }
        final List<Component> children = component.children();
        for (int j = children.size() - 1; j >= 0; --j) {
            deque.addFirst((Object)children.get(j));
        }
        return;
    };
    public static final ComponentIteratorType BREADTH_FIRST = (component, deque, flags) -> {
        if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
            ((TranslatableComponent)component).arguments().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final TranslationArgument argument = (TranslationArgument)iterator.next();
                deque.add((Object)argument.asComponent());
            }
        }
        final HoverEvent<?> hoverEvent2 = component.hoverEvent();
        if (hoverEvent2 != null) {
            final HoverEvent.Action<?> action2 = hoverEvent2.action();
            if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action2 == HoverEvent.Action.SHOW_ENTITY) {
                deque.addLast((Object)((HoverEvent.ShowEntity)hoverEvent2.value()).name());
            }
            else if (flags.contains((Object)ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action2 == HoverEvent.Action.SHOW_TEXT) {
                deque.addLast((Object)hoverEvent2.value());
            }
        }
        deque.addAll((Collection)component.children());
    };
    
    void populate(@NotNull final Component component, @NotNull final Deque<Component> deque, @NotNull final Set<ComponentIteratorFlag> flags);
}
