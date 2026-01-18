package cc.dreamcode.kowal.libs.cc.dreamcode.menu.holder;

import lombok.NonNull;

public interface DreamMenuHolder<P>
{
    void open(@NonNull final P p0);
    
    void dispose();
}
