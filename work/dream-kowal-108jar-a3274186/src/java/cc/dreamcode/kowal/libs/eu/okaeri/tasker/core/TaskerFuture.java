package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core;

import java.util.concurrent.TimeUnit;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain.TaskerChain;
import java.util.concurrent.Future;

public class TaskerFuture<T> implements Future<T>
{
    protected final TaskerChain<T> chain;
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.chain.cancel();
    }
    
    public boolean isCancelled() {
        return this.chain.isCancelled();
    }
    
    public boolean isDone() {
        return this.chain.isDone();
    }
    
    public T get() {
        return this.chain.await();
    }
    
    public T get(final long timeout, final TimeUnit unit) {
        return this.chain.await(timeout, unit);
    }
    
    public TaskerFuture(final TaskerChain<T> chain) {
        this.chain = chain;
    }
}
