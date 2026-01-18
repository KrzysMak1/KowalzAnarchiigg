package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class MonkeyBars
{
    private MonkeyBars() {
    }
    
    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(final Class<E> type, final E... constants) {
        final Set<E> set = (Set<E>)EnumSet.noneOf((Class)type);
        Collections.addAll((Collection)set, (Object[])constants);
        return (Set<E>)Collections.unmodifiableSet((Set)set);
    }
    
    @NotNull
    public static <T> List<T> addOne(@NotNull final List<T> oldList, final T newElement) {
        if (oldList.isEmpty()) {
            return (List<T>)Collections.singletonList((Object)newElement);
        }
        final List<T> newList = (List<T>)new ArrayList(oldList.size() + 1);
        newList.addAll((Collection)oldList);
        newList.add((Object)newElement);
        return (List<T>)Collections.unmodifiableList((List)newList);
    }
    
    @SafeVarargs
    @NotNull
    public static <I, O> List<O> nonEmptyArrayToList(@NotNull final Function<I, O> mapper, @NotNull final I first, @NotNull final I... others) {
        final List<O> ret = (List<O>)new ArrayList(others.length + 1);
        ret.add(mapper.apply((Object)first));
        for (final I other : others) {
            ret.add(Objects.requireNonNull(mapper.apply(Objects.requireNonNull((Object)other, "source[?]")), "mapper(source[?])"));
        }
        return (List<O>)Collections.unmodifiableList((List)ret);
    }
    
    @NotNull
    public static <I, O> List<O> toUnmodifiableList(@NotNull final Function<I, O> mapper, @NotNull final Iterable<? extends I> source) {
        final ArrayList<O> ret = (ArrayList<O>)((source instanceof Collection) ? new ArrayList(((Collection)source).size()) : new ArrayList());
        for (final I el : source) {
            ret.add(Objects.requireNonNull(mapper.apply(Objects.requireNonNull((Object)el, "source[?]")), "mapper(source[?])"));
        }
        return (List<O>)Collections.unmodifiableList((List)ret);
    }
}
