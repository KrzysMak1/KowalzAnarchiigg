package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.chain;

import java.time.temporal.Temporal;
import java.util.concurrent.TimeoutException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerFuture;
import java.util.concurrent.Future;
import java.util.function.BooleanSupplier;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.ArrayList;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerExecutor;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskerChain<T>
{
    protected static final Runnable NOOP_RUNNABLE;
    protected final AtomicBoolean abort;
    protected final AtomicBoolean lastAsync;
    protected final AtomicBoolean executed;
    protected final AtomicBoolean done;
    protected final AtomicBoolean cancelled;
    protected final AtomicReference<Object> data;
    protected final AtomicReference<Exception> exception;
    protected final AtomicReference<Exception> trace;
    protected final AtomicReference<Object> currentTask;
    protected final AtomicInteger currentTaskIndex;
    protected final List<ChainTask> tasks;
    protected final TaskerExecutor<Object> executor;
    private final TaskerChainAccessor accessor;
    
    public TaskerChain(@NonNull final TaskerExecutor<?> executor) {
        this.abort = new AtomicBoolean(false);
        this.lastAsync = new AtomicBoolean(false);
        this.executed = new AtomicBoolean(false);
        this.done = new AtomicBoolean(false);
        this.cancelled = new AtomicBoolean(false);
        this.data = (AtomicReference<Object>)new AtomicReference();
        this.exception = (AtomicReference<Exception>)new AtomicReference();
        this.trace = (AtomicReference<Exception>)new AtomicReference();
        this.currentTask = (AtomicReference<Object>)new AtomicReference();
        this.currentTaskIndex = new AtomicInteger(0);
        this.tasks = (List<ChainTask>)new ArrayList();
        this.accessor = new TaskerChainAccessor(this);
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        this.executor = (TaskerExecutor<Object>)executor;
    }
    
    @Deprecated
    public <N> TaskerChain<N> unsafe(@NonNull final Consumer<TaskerChainAccessor> extension) {
        if (extension == null) {
            throw new NullPointerException("extension is marked non-null but is null");
        }
        extension.accept((Object)this.accessor);
        return (TaskerChain<N>)this;
    }
    
    @Deprecated
    public <D> D unsafeGet(@NonNull final Function<TaskerChainAccessor, D> extension) {
        if (extension == null) {
            throw new NullPointerException("extension is marked non-null but is null");
        }
        return (D)extension.apply((Object)this.accessor);
    }
    
    public TaskerChain<T> sync(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        this.tasks.add((Object)new ChainTask(runnable, Duration.ZERO, (Supplier<Boolean>)(() -> false), false));
        return this;
    }
    
    public <N> TaskerChain<N> supplySync(@NonNull final Supplier<N> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this.sync(() -> this.data.set(supplier.get()));
    }
    
    public TaskerChain<T> acceptSync(@NonNull final Consumer<T> data) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        return this.sync(() -> data.accept(this.data.get()));
    }
    
    public <R> TaskerChain<R> transformSync(@NonNull final Function<T, R> function) {
        if (function == null) {
            throw new NullPointerException("function is marked non-null but is null");
        }
        return this.supplySync((java.util.function.Supplier<R>)(() -> function.apply(this.data.get())));
    }
    
    public TaskerChain<T> async(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        this.tasks.add((Object)new ChainTask(runnable, Duration.ZERO, (Supplier<Boolean>)(() -> true), false));
        return this;
    }
    
    public <N> TaskerChain<N> supplyAsync(@NonNull final Supplier<N> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this.async(() -> this.data.set(supplier.get()));
    }
    
    public TaskerChain<T> acceptAsync(@NonNull final Consumer<T> data) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        return this.async(() -> data.accept(this.data.get()));
    }
    
    public <R> TaskerChain<R> transformAsync(@NonNull final Function<T, R> function) {
        if (function == null) {
            throw new NullPointerException("function is marked non-null but is null");
        }
        return this.supplyAsync((java.util.function.Supplier<R>)(() -> function.apply(this.data.get())));
    }
    
    protected TaskerChain<T> _abortIf(@NonNull final Predicate<T> predicate, @NonNull final Supplier<Boolean> async) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (async == null) {
            throw new NullPointerException("async is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        final Runnable runnable = () -> {
            if (predicate.test(this.data.get())) {
                this.abort.set(true);
            }
        };
        this.tasks.add((Object)new ChainTask(runnable, Duration.ZERO, async, false));
        return this;
    }
    
    public TaskerChain<T> abortIf(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._abortIf(predicate, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfSync(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return this._abortIf(predicate, (Supplier<Boolean>)(() -> false));
    }
    
    public TaskerChain<T> abortIfAsync(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return this._abortIf(predicate, (Supplier<Boolean>)(() -> true));
    }
    
    public TaskerChain<T> abortIfNot(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        final Predicate predicate2 = data -> !predicate.test(data);
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._abortIf((Predicate<T>)predicate2, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfSyncNot(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(data -> !predicate.test(data)), (Supplier<Boolean>)(() -> false));
    }
    
    public TaskerChain<T> abortIfAsyncNot(@NonNull final Predicate<T> predicate) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(data -> !predicate.test(data)), (Supplier<Boolean>)(() -> true));
    }
    
    public TaskerChain<T> abortIfThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThenOrElse(predicate, whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfSyncThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse(predicate, whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfAsyncThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse(predicate, whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfNotThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThen((Predicate<T>)(data -> !predicate.test(data)), whenAbort);
    }
    
    public TaskerChain<T> abortIfSyncNotThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfSyncThen((Predicate<T>)(data -> !predicate.test(data)), whenAbort);
    }
    
    public TaskerChain<T> abortIfAsyncNotThen(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfAsyncThen((Predicate<T>)(data -> !predicate.test(data)), whenAbort);
    }
    
    public TaskerChain<T> abortIfOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse(predicate, TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse(predicate, TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse(predicate, TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfNotOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfOrElse((Predicate<T>)(data -> !predicate.test(data)), whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncNotOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncOrElse((Predicate<T>)(data -> !predicate.test(data)), whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncNotOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncOrElse((Predicate<T>)(data -> !predicate.test(data)), whenContinue);
    }
    
    private TaskerChain<T> _abortIfThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue, @NonNull final Supplier<Boolean> async) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        if (async == null) {
            throw new NullPointerException("async is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(data -> {
            if (predicate.test(data)) {
                whenAbort.run();
                return true;
            }
            whenContinue.run();
            return false;
        }), async);
    }
    
    public TaskerChain<T> abortIfThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._abortIfThenOrElse(predicate, whenAbort, whenContinue, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfSyncThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this._abortIfThenOrElse(predicate, whenAbort, whenContinue, (Supplier<Boolean>)(() -> false));
    }
    
    public TaskerChain<T> abortIfAsyncThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this._abortIfThenOrElse(predicate, whenAbort, whenContinue, (Supplier<Boolean>)(() -> true));
    }
    
    public TaskerChain<T> abortIfNotThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Predicate<T>)(data -> !predicate.test(data)), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncNotThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse((Predicate<T>)(data -> !predicate.test(data)), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncNotThenOrElse(@NonNull final Predicate<T> predicate, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (predicate == null) {
            throw new NullPointerException("predicate is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse((Predicate<T>)(data -> !predicate.test(data)), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIf(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        final Predicate predicate = unused -> supplier.getAsBoolean();
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._abortIf((Predicate<T>)predicate, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfSync(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(unused -> supplier.getAsBoolean()), (Supplier<Boolean>)(() -> false));
    }
    
    public TaskerChain<T> abortIfAsync(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(unused -> supplier.getAsBoolean()), (Supplier<Boolean>)(() -> true));
    }
    
    public TaskerChain<T> abortIfNot(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        final Predicate predicate = unused -> !supplier.getAsBoolean();
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._abortIf((Predicate<T>)predicate, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfSyncNot(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(unused -> !supplier.getAsBoolean()), (Supplier<Boolean>)(() -> false));
    }
    
    public TaskerChain<T> abortIfAsyncNot(@NonNull final BooleanSupplier supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this._abortIf((Predicate<T>)(unused -> !supplier.getAsBoolean()), (Supplier<Boolean>)(() -> true));
    }
    
    public TaskerChain<T> abortIfThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfSyncThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfAsyncThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, TaskerChain.NOOP_RUNNABLE);
    }
    
    public TaskerChain<T> abortIfNotThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThen((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort);
    }
    
    public TaskerChain<T> abortIfSyncNotThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfSyncThen((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort);
    }
    
    public TaskerChain<T> abortIfAsyncNotThen(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfAsyncThen((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort);
    }
    
    public TaskerChain<T> abortIfOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), TaskerChain.NOOP_RUNNABLE, whenContinue);
    }
    
    public TaskerChain<T> abortIfNotOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncNotOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncNotOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenContinue);
    }
    
    public TaskerChain<T> abortIfThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse((Predicate<T>)(unused -> supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfNotThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfSyncNotThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfSyncThenOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfAsyncNotThenOrElse(@NonNull final BooleanSupplier supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfAsyncThenOrElse((Predicate<T>)(unused -> !supplier.getAsBoolean()), whenAbort, whenContinue);
    }
    
    public TaskerChain<T> abortIfNull() {
        return this.abortIf((Predicate<T>)Objects::isNull);
    }
    
    public TaskerChain<T> delay(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        final List<ChainTask> tasks = this.tasks;
        final Runnable noop_RUNNABLE = TaskerChain.NOOP_RUNNABLE;
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        tasks.add((Object)new ChainTask(noop_RUNNABLE, duration, (Supplier<Boolean>)lastAsync::get, false));
        return this;
    }
    
    protected <E extends Exception> TaskerChain<T> _handleException(@NonNull final Function<E, T> handler, @NonNull final Supplier<Boolean> async) {
        if (handler == null) {
            throw new NullPointerException("handler is marked non-null but is null");
        }
        if (async == null) {
            throw new NullPointerException("async is marked non-null but is null");
        }
        if (this.executed.get()) {
            throw new RuntimeException("Cannot modify already executed chain");
        }
        final Runnable task = () -> {
            final Exception exception = (Exception)this.exception.get();
            if (exception == null) {
                return;
            }
            this.data.set(handler.apply((Object)exception));
            this.exception.set((Object)null);
        };
        this.tasks.add((Object)new ChainTask(task, Duration.ZERO, async, true));
        return this;
    }
    
    public <E extends Exception> TaskerChain<T> handleExceptionSync(@NonNull final Function<E, T> handler) {
        if (handler == null) {
            throw new NullPointerException("handler is marked non-null but is null");
        }
        return this._handleException(handler, (Supplier<Boolean>)(() -> false));
    }
    
    public <E extends Exception> TaskerChain<T> handleExceptionAsync(@NonNull final Function<E, T> handler) {
        if (handler == null) {
            throw new NullPointerException("handler is marked non-null but is null");
        }
        return this._handleException(handler, (Supplier<Boolean>)(() -> true));
    }
    
    public <E extends Exception> TaskerChain<T> abortIfException(@NonNull final Consumer<E> handler) {
        if (handler == null) {
            throw new NullPointerException("handler is marked non-null but is null");
        }
        final Function handler2 = exception -> {
            handler.accept((Object)exception);
            this.abort.set(true);
            return null;
        };
        final AtomicBoolean lastAsync = this.lastAsync;
        Objects.requireNonNull((Object)lastAsync);
        return this._handleException((java.util.function.Function<Exception, T>)handler2, (Supplier<Boolean>)lastAsync::get);
    }
    
    public TaskerChain<T> abortIfException() {
        final Function identity = Function.identity();
        Objects.requireNonNull((Object)identity);
        return this.abortIfException((java.util.function.Consumer<Exception>)identity::apply);
    }
    
    protected void _execute(final Consumer<T> consumer, final Consumer<Exception> unhandledExceptionConsumer) {
        if (this.executed.get()) {
            throw new RuntimeException("Cannot execute already executed chain");
        }
        this.trace.set((Object)new RuntimeException("Chain trace point"));
        final Runnable abortCallback = () -> {
            final Exception unhandled = (Exception)this.exception.get();
            if (unhandled != null) {
                if (unhandledExceptionConsumer == null) {
                    final Throwable throwable = ((Exception)this.trace.get()).initCause((Throwable)unhandled);
                    throw new RuntimeException("Unhandled chain exception", throwable);
                }
                unhandledExceptionConsumer.accept((Object)unhandled);
            }
            if (consumer != null) {
                consumer.accept(this.data.get());
            }
            this.done.set(true);
        };
        this._executeTask(0, abortCallback, unhandledExceptionConsumer);
    }
    
    protected void _executeTask(final int index, final Runnable abortCallback, final Consumer<Exception> unhandledExceptionConsumer) {
        if (index >= this.tasks.size()) {
            abortCallback.run();
            return;
        }
        if (this.abort.get()) {
            abortCallback.run();
            return;
        }
        final ChainTask task = (ChainTask)this.tasks.get(index);
        final Exception unhandled = (Exception)this.exception.get();
        if (unhandled != null && !task.isExceptionHandler()) {
            if (unhandledExceptionConsumer == null) {
                final Throwable throwable = ((Exception)this.trace.get()).initCause((Throwable)unhandled);
                throw new RuntimeException("Unhandled chain exception", throwable);
            }
            unhandledExceptionConsumer.accept((Object)unhandled);
            if (this.abort.get()) {
                abortCallback.run();
                return;
            }
        }
        final boolean async = (boolean)task.getAsync().get();
        final Runnable callback = () -> {
            this.lastAsync.set(async);
            this._executeTask(index + 1, abortCallback, unhandledExceptionConsumer);
        };
        final Runnable runnable = () -> {
            try {
                task.getRunnable().run();
            }
            catch (final Exception exception) {
                this.exception.set((Object)exception);
            }
            callback.run();
        };
        this.currentTaskIndex.set(index);
        this.currentTask.set(this.executor.runLater(runnable, task.getDelay(), async));
    }
    
    public void execute(@NonNull final Consumer<T> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        this._execute(consumer, null);
    }
    
    public void execute() {
        this._execute(null, null);
    }
    
    public Future<T> executeFuture() {
        return (Future<T>)new TaskerFuture((TaskerChain<Object>)this);
    }
    
    public T await() {
        return this.await(-1L, null);
    }
    
    public T await(final long timeout, final TimeUnit unit) {
        try {
            if (this.executor.isMain()) {
                throw new IllegalStateException("cannot await synchronously");
            }
            final Instant start = (unit == null) ? null : Instant.now();
            final AtomicReference<T> resource = (AtomicReference<T>)new AtomicReference();
            final AtomicReference<Exception> exception = (AtomicReference<Exception>)new AtomicReference();
            final AtomicReference<T> atomicReference = resource;
            Objects.requireNonNull((Object)atomicReference);
            this._execute((Consumer<T>)atomicReference::set, (Consumer<Exception>)(unhandledException -> {
                this.abort.set(true);
                this.cancelled.set(true);
                exception.set((Object)unhandledException);
            }));
            while (!this.isDone()) {
                if (this.isCancelled()) {
                    throw new TimeoutException("Task was cancelled");
                }
                if (unit != null) {
                    final Duration waitDuration = Duration.between((Temporal)start, (Temporal)Instant.now());
                    if (waitDuration.toNanos() >= unit.toNanos(timeout)) {
                        this.cancel();
                        throw new TimeoutException("No result after " + (Object)waitDuration);
                    }
                }
                Thread.sleep(1L);
            }
            final Exception unhandledException = (Exception)exception.get();
            if (unhandledException != null) {
                throw unhandledException;
            }
            return (T)resource.get();
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public boolean cancel() {
        if (this.abort.get() || this.isCancelled()) {
            return false;
        }
        this.abort.set(true);
        this.cancelled.set(true);
        final Object currentTask = this.currentTask.get();
        if (currentTask != null) {
            this.executor.cancel(currentTask);
        }
        return true;
    }
    
    public boolean isDone() {
        return this.done.get();
    }
    
    public boolean isCancelled() {
        return this.cancelled.get();
    }
    
    static {
        NOOP_RUNNABLE = (() -> {});
    }
}
