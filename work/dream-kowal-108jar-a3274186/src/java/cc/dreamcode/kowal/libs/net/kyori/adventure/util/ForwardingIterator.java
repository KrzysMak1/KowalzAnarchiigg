package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.function.Supplier;

public final class ForwardingIterator<T> implements Iterable<T>
{
    private final Supplier<Iterator<T>> iterator;
    private final Supplier<Spliterator<T>> spliterator;
    
    public ForwardingIterator(@NotNull final Supplier<Iterator<T>> iterator, @NotNull final Supplier<Spliterator<T>> spliterator) {
        this.iterator = (Supplier<Iterator<T>>)Objects.requireNonNull((Object)iterator, "iterator");
        this.spliterator = (Supplier<Spliterator<T>>)Objects.requireNonNull((Object)spliterator, "spliterator");
    }
    
    @NotNull
    public Iterator<T> iterator() {
        return (Iterator<T>)this.iterator.get();
    }
    
    @NotNull
    public Spliterator<T> spliterator() {
        return (Spliterator<T>)this.spliterator.get();
    }
}
