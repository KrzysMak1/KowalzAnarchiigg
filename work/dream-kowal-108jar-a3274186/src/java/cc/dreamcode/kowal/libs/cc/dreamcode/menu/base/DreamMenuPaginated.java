package cc.dreamcode.kowal.libs.cc.dreamcode.menu.base;

import java.util.function.Consumer;
import lombok.NonNull;
import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface DreamMenuPaginated<R, M, I, E, H>
{
    M getMenuPlatform();
    
    List<Integer> getStorageItemSlots();
    
    Optional<M> getMenuByPage(final int p0);
    
    Map<Integer, M> getMenuPages();
    
    int getSize();
    
    List<H> getViewers();
    
    int getPlayerPage(@NonNull final H p0);
    
    R setNextPageSlot(final int p0, @NonNull final Consumer<H> p1);
    
    R setNextPageSlot(final int p0, @NonNull final I p1, @NonNull final Consumer<H> p2);
    
    R setNextPageSlot(final int p0, final int p1, @NonNull final Consumer<H> p2);
    
    R setNextPageSlot(final int p0, final int p1, @NonNull final I p2, @NonNull final Consumer<H> p3);
    
    R setPreviousPageSlot(final int p0, @NonNull final Consumer<H> p1);
    
    R setPreviousPageSlot(final int p0, @NonNull final I p1, @NonNull final Consumer<H> p2);
    
    R setPreviousPageSlot(final int p0, final int p1, @NonNull final Consumer<H> p2);
    
    R setPreviousPageSlot(final int p0, final int p1, @NonNull final I p2, @NonNull final Consumer<H> p3);
    
    R addStorageItem(@NonNull final M p0, final int p1, @NonNull final I p2, final Consumer<E> p3);
    
    R addStorageItem(@NonNull final I p0, final Consumer<E> p1);
    
    R addStorageItem(@NonNull final I p0);
    
    R addStorageItems(@NonNull final List<I> p0, final Consumer<E> p1);
    
    R addStorageItems(@NonNull final List<I> p0);
    
    R open(final int p0, @NonNull final H p1);
    
    R openPage(@NonNull final H p0);
    
    R openFirstPage(@NonNull final H p0);
    
    R openLastPage(@NonNull final H p0);
    
    R dispose();
}
