package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.NonNull;

public interface CommandScheduler
{
    default void sync(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        runnable.run();
    }
    
    void async(@NonNull final Runnable runnable);
}
