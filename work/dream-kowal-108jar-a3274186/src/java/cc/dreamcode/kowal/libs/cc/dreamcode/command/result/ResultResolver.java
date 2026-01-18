package cc.dreamcode.kowal.libs.cc.dreamcode.command.result;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public interface ResultResolver
{
    boolean isAssignableFrom(@NonNull final Class<?> type);
    
    void resolveResult(@NonNull final DreamSender<?> sender, @NonNull final Class<?> type, @NonNull final Object object);
}
