package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@FunctionalInterface
public interface ComponentLike
{
    @NotNull
    default List<Component> asComponents(@NotNull final List<? extends ComponentLike> likes) {
        return asComponents(likes, null);
    }
    
    @NotNull
    default List<Component> asComponents(@NotNull final List<? extends ComponentLike> likes, @Nullable final Predicate<? super Component> filter) {
        Objects.requireNonNull((Object)likes, "likes");
        final int size = likes.size();
        if (size == 0) {
            return (List<Component>)Collections.emptyList();
        }
        ArrayList<Component> components = null;
        for (int i = 0; i < size; ++i) {
            final ComponentLike like = (ComponentLike)likes.get(i);
            if (like == null) {
                throw new NullPointerException("likes[" + i + "]");
            }
            final Component component = like.asComponent();
            if (filter == null || filter.test((Object)component)) {
                if (components == null) {
                    components = (ArrayList<Component>)new ArrayList(size);
                }
                components.add((Object)component);
            }
        }
        if (components == null) {
            return (List<Component>)Collections.emptyList();
        }
        components.trimToSize();
        return (List<Component>)Collections.unmodifiableList((List)components);
    }
    
    @Nullable
    default Component unbox(@Nullable final ComponentLike like) {
        return (like != null) ? like.asComponent() : null;
    }
    
    @Contract(pure = true)
    @NotNull
    Component asComponent();
}
