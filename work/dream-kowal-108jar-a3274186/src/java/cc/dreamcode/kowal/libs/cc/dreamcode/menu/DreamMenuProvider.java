package cc.dreamcode.kowal.libs.cc.dreamcode.menu;

import java.util.function.Consumer;
import lombok.NonNull;

public interface DreamMenuProvider<D, T, P>
{
    D createDefault(@NonNull final String p0, final int p1);
    
    D createDefault(@NonNull final String p0, final int p1, @NonNull final Consumer<D> p2);
    
    D createDefault(@NonNull final T p0, @NonNull final String p1);
    
    D createDefault(@NonNull final T p0, @NonNull final String p1, @NonNull final Consumer<D> p2);
    
    P createPaginated(@NonNull final D p0);
    
    P createPaginated(@NonNull final D p0, @NonNull final Consumer<D> p1);
}
