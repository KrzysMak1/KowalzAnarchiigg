package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core;

import java.time.Duration;
import lombok.NonNull;

public interface TaskerExecutor<T>
{
    default boolean hasMain() {
        return true;
    }
    
    boolean isMain();
    
    T schedule(@NonNull final Runnable runnable, final boolean async);
    
    T schedule(@NonNull final Runnable runnable, @NonNull final Duration delay, @NonNull final Duration rate, final boolean async);
    
    default T schedule(@NonNull final Runnable runnable, @NonNull final Duration rate, final boolean async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (rate == null) {
            throw new NullPointerException("rate is marked non-null but is null");
        }
        return this.schedule(runnable, rate, rate, async);
    }
    
    T run(@NonNull final Runnable runnable, final boolean async);
    
    T runLater(@NonNull final Runnable runnable, @NonNull final Duration delay, final boolean async);
    
    void cancel(@NonNull final T task);
}
