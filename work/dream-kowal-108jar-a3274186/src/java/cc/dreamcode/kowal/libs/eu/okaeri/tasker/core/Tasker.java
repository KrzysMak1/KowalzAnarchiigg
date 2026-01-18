package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain.SharedChain;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain.TaskerChain;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.delayer.Delayer;
import java.time.Duration;
import lombok.NonNull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;
import java.util.Map;

public class Tasker
{
    protected final Map<String, Queue<Runnable>> sharedChains;
    protected final Map<String, Queue<Runnable>> sharedChainsPriority;
    protected final Map<String, Object> sharedChainsTasks;
    protected final Map<String, AtomicBoolean> sharedChainsLocks;
    protected final TaskerExecutor<?> executor;
    
    public static Tasker newPool(@NonNull final TaskerExecutor<?> executor) {
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        return new Tasker(executor);
    }
    
    public Delayer newDelayer(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return Delayer.of(this.executor, duration);
    }
    
    public Delayer newDelayer(@NonNull final Duration duration, @NonNull final Duration checkRate) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        if (checkRate == null) {
            throw new NullPointerException("checkRate is marked non-null but is null");
        }
        return Delayer.of(this.executor, duration, checkRate);
    }
    
    public TaskerChain<Object> newChain() {
        return new TaskerChain<Object>(this.executor);
    }
    
    public TaskerChain<Object> newSharedChain(@NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        return this.newSharedChain(name, false);
    }
    
    public TaskerChain<Object> newSharedChain(@NonNull final String name, final boolean priority) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (!this.sharedChainsTasks.containsKey((Object)name)) {
            final Object task = this.executor.schedule(() -> this.execSharedChainQueue(name), true);
            this.sharedChainsTasks.put((Object)name, task);
        }
        return new SharedChain<Object>(this.executor, this.getSharedChainQueue(name, priority));
    }
    
    protected Queue<Runnable> getSharedChainQueue(@NonNull final String name, final boolean priority) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final Map<String, Queue<Runnable>> queueMap = priority ? this.sharedChainsPriority : this.sharedChains;
        return (Queue<Runnable>)queueMap.computeIfAbsent((Object)name, n -> new ConcurrentLinkedQueue());
    }
    
    protected void execSharedChainQueue(@NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final AtomicBoolean lock = (AtomicBoolean)this.sharedChainsLocks.computeIfAbsent((Object)name, n -> new AtomicBoolean(false));
        if (lock.get()) {
            return;
        }
        lock.set(true);
        final Queue<Runnable> queue = this.getSharedChainQueue(name, false);
        final Queue<Runnable> priorityQueue = this.getSharedChainQueue(name, true);
        try {
            while (!priorityQueue.isEmpty()) {
                ((Runnable)priorityQueue.poll()).run();
            }
            for (int tasksDone = 0; !queue.isEmpty() && (tasksDone <= 1 || priorityQueue.isEmpty()); ++tasksDone) {
                ((Runnable)queue.poll()).run();
            }
        }
        finally {
            lock.set(false);
        }
    }
    
    protected Tasker(final TaskerExecutor<?> executor) {
        this.sharedChains = (Map<String, Queue<Runnable>>)new ConcurrentHashMap();
        this.sharedChainsPriority = (Map<String, Queue<Runnable>>)new ConcurrentHashMap();
        this.sharedChainsTasks = (Map<String, Object>)new ConcurrentHashMap();
        this.sharedChainsLocks = (Map<String, AtomicBoolean>)new ConcurrentHashMap();
        this.executor = executor;
    }
}
