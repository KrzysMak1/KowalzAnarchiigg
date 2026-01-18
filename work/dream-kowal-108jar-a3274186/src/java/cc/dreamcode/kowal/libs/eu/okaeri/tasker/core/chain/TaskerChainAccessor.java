package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain;

import java.time.Duration;
import java.util.function.Supplier;
import lombok.NonNull;

public class TaskerChainAccessor
{
    private final TaskerChain<?> chain;
    
    public void abort(final boolean abort) {
        this.chain.abort.set(abort);
    }
    
    public boolean abort() {
        return this.chain.abort.get();
    }
    
    public void data(final Object newValue) {
        this.chain.data.set(newValue);
    }
    
    public <D> D data() {
        return (D)this.chain.data.get();
    }
    
    public <D> D dataOr(final D other) {
        final D data = this.data();
        return (data == null) ? other : data;
    }
    
    public boolean lastAsync() {
        return this.chain.lastAsync.get();
    }
    
    public void taskInsert(@NonNull final Runnable runnable, @NonNull final Supplier<Boolean> async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (async == null) {
            throw new NullPointerException("async is marked non-null but is null");
        }
        this.chain.tasks.add(this.chain.currentTaskIndex.get() + 1, (Object)new ChainTask(runnable, Duration.ZERO, async, false));
    }
    
    public void task(@NonNull final Runnable runnable, @NonNull final Supplier<Boolean> async) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (async == null) {
            throw new NullPointerException("async is marked non-null but is null");
        }
        if (this.chain.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        this.chain.tasks.add((Object)new ChainTask(runnable, Duration.ZERO, async, false));
    }
    
    public void taskInsert(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.taskInsert(runnable, (Supplier<Boolean>)this::lastAsync);
    }
    
    public void task(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.task(runnable, (Supplier<Boolean>)this::lastAsync);
    }
    
    public void syncInsert(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.taskInsert(runnable, (Supplier<Boolean>)(() -> false));
    }
    
    public void sync(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.task(runnable, (Supplier<Boolean>)(() -> false));
    }
    
    public void asyncInsert(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.taskInsert(runnable, (Supplier<Boolean>)(() -> true));
    }
    
    public void async(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        this.task(runnable, (Supplier<Boolean>)(() -> true));
    }
    
    public void delayInsert(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        this.chain.tasks.add(this.chain.currentTaskIndex.get() + 1, (Object)new ChainTask(TaskerChain.NOOP_RUNNABLE, duration, (Supplier<Boolean>)this::lastAsync, false));
    }
    
    public void delay(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        if (this.chain.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        this.chain.tasks.add((Object)new ChainTask(TaskerChain.NOOP_RUNNABLE, duration, (Supplier<Boolean>)this::lastAsync, false));
    }
    
    protected TaskerChainAccessor(final TaskerChain<?> chain) {
        this.chain = chain;
    }
}
