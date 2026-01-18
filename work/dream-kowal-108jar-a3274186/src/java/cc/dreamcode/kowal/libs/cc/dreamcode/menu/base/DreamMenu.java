package cc.dreamcode.kowal.libs.cc.dreamcode.menu.base;

import java.util.function.Consumer;
import java.util.List;
import lombok.NonNull;

public interface DreamMenu<R, T, I, E, H, P>
{
    R addItem(@NonNull final I p0);
    
    R addItem(@NonNull final I p0, @NonNull final List<Integer> p1);
    
    R addItem(@NonNull final I p0, @NonNull final Consumer<E> p1);
    
    R addItem(@NonNull final I p0, @NonNull final List<Integer> p1, @NonNull final Consumer<E> p2);
    
    R setItem(final int p0, @NonNull final I p1);
    
    R setItem(final int p0, @NonNull final I p1, @NonNull final Consumer<E> p2);
    
    R setItem(final int p0, final int p1, @NonNull final I p2);
    
    R setItem(final int p0, final int p1, @NonNull final I p2, @NonNull final Consumer<E> p3);
    
    R setItem(final int[] p0, @NonNull final I p1);
    
    R setItem(final int[] p0, @NonNull final I p1, @NonNull final Consumer<E> p2);
    
    R fillInventoryWith(@NonNull final I p0);
    
    R fillInventoryWith(@NonNull final I p0, @NonNull final Consumer<E> p1);
    
    H getHolder();
    
    R open(@NonNull final P p0);
    
    T toPaginated();
    
    R dispose();
}
