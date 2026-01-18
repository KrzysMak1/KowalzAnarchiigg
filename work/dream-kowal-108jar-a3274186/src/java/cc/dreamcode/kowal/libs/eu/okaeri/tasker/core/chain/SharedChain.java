package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain;

import java.util.function.Consumer;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;

public class SharedChain<T> extends TaskerChain<T>
{
    protected final Queue<Runnable> queue;
    protected final AtomicBoolean executed;
    
    public SharedChain(@NonNull final TaskerExecutor<?> executor, @NonNull final Queue<Runnable> queue) {
        super(executor);
        this.executed = new AtomicBoolean(false);
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        if (queue == null) {
            throw new NullPointerException("queue is marked non-null but is null");
        }
        this.queue = queue;
    }
    
    @Override
    public void execute() {
        if (this.executed.get()) {
            throw new RuntimeException("Cannot execute already executed chain");
        }
        this.executed.set(true);
        this.queue.add((Object)(() -> super.await()));
    }
    
    @Override
    public void execute(@NonNull final Consumer<T> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot execute already executed chain");
        }
        this.executed.set(true);
        this.queue.add((Object)(() -> consumer.accept(super.await())));
    }
}
