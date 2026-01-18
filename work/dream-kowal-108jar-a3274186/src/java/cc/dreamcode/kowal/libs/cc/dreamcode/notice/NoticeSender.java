package cc.dreamcode.kowal.libs.cc.dreamcode.notice;

import java.util.Collection;
import java.util.Map;
import lombok.NonNull;

public interface NoticeSender<T>
{
    void send(@NonNull final T p0);
    
    void send(@NonNull final T p0, @NonNull final Map<String, Object> p1);
    
    void send(@NonNull final Collection<T> p0);
    
    void send(@NonNull final Collection<T> p0, @NonNull final Map<String, Object> p1);
}
