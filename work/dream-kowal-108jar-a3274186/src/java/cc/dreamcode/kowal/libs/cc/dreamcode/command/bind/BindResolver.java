package cc.dreamcode.kowal.libs.cc.dreamcode.command.bind;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface BindResolver<T>
{
    boolean isAssignableFrom(@NonNull final Class<?> type);
    
    @NonNull
    T resolveBind(@NonNull final DreamSender<?> sender);
}
