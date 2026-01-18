package cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.delayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.time.temporal.Temporal;
import lombok.NonNull;
import java.time.Duration;
import cc.dreamcode.kowal.libs.eu.okaeri.tasker.core.TaskerExecutor;
import java.util.function.Supplier;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class Delayer
{
    private static final Runnable NOOP_RUNNABLE;
    protected final AtomicReference<Object> task;
    protected final AtomicReference<Instant> started;
    protected final AtomicBoolean abort;
    protected final List<Supplier<Boolean>> abortWhen;
    protected final List<Supplier<Boolean>> forceWhen;
    protected final List<Runnable> actions;
    protected final List<Runnable> forcedActions;
    protected final TaskerExecutor<Object> executor;
    protected final Duration duration;
    protected final Duration checkRate;
    
    public static Delayer of(@NonNull final TaskerExecutor<?> executor, @NonNull final Duration duration) {
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return of(executor, duration, duration.dividedBy(10L));
    }
    
    public static Delayer of(@NonNull final TaskerExecutor<?> executor, @NonNull final Duration duration, @NonNull final Duration checkRate) {
        if (executor == null) {
            throw new NullPointerException("executor is marked non-null but is null");
        }
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        if (checkRate == null) {
            throw new NullPointerException("checkRate is marked non-null but is null");
        }
        return new Delayer((TaskerExecutor<Object>)executor, duration, checkRate);
    }
    
    public Delayer abortIf(@NonNull final Supplier<Boolean> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        this.abortWhen.add((Object)supplier);
        return this;
    }
    
    public Delayer abortIfNot(@NonNull final Supplier<Boolean> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this.abortIf((Supplier<Boolean>)(() -> !(boolean)supplier.get()));
    }
    
    public Delayer abortIfThen(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThenOrElse(supplier, whenAbort, Delayer.NOOP_RUNNABLE);
    }
    
    public Delayer abortIfNotThen(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenAbort) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        return this.abortIfThen((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenAbort);
    }
    
    public Delayer abortIfOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse(supplier, Delayer.NOOP_RUNNABLE, whenContinue);
    }
    
    public Delayer abortIfNotOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfOrElse((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenContinue);
    }
    
    public Delayer abortIfThenOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIf((Supplier<Boolean>)(() -> {
            if (supplier.get()) {
                whenAbort.run();
                return true;
            }
            whenContinue.run();
            return false;
        }));
    }
    
    public Delayer abortIfNotThenOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenAbort, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenAbort == null) {
            throw new NullPointerException("whenAbort is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.abortIfThenOrElse((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenAbort, whenContinue);
    }
    
    public Delayer forceIf(@NonNull final Supplier<Boolean> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        this.forceWhen.add((Object)supplier);
        return this;
    }
    
    public Delayer forceIfNot(@NonNull final Supplier<Boolean> supplier) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        return this.forceIf((Supplier<Boolean>)(() -> !(boolean)supplier.get()));
    }
    
    public Delayer forceIfThen(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenForce) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenForce == null) {
            throw new NullPointerException("whenForce is marked non-null but is null");
        }
        return this.forceIfThenOrElse(supplier, whenForce, Delayer.NOOP_RUNNABLE);
    }
    
    public Delayer forceIfNotThen(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenForce) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenForce == null) {
            throw new NullPointerException("whenForce is marked non-null but is null");
        }
        return this.forceIfThen((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenForce);
    }
    
    public Delayer forceIfOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.forceIfThenOrElse(supplier, Delayer.NOOP_RUNNABLE, whenContinue);
    }
    
    public Delayer forceIfNotOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.forceIfOrElse((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenContinue);
    }
    
    public Delayer forceIfThenOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenForce, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenForce == null) {
            throw new NullPointerException("whenForce is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.forceIf((Supplier<Boolean>)(() -> {
            if (supplier.get()) {
                whenForce.run();
                return true;
            }
            whenContinue.run();
            return false;
        }));
    }
    
    public Delayer forceIfNotThenOrElse(@NonNull final Supplier<Boolean> supplier, @NonNull final Runnable whenForce, @NonNull final Runnable whenContinue) {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (whenForce == null) {
            throw new NullPointerException("whenForce is marked non-null but is null");
        }
        if (whenContinue == null) {
            throw new NullPointerException("whenContinue is marked non-null but is null");
        }
        return this.forceIfThenOrElse((Supplier<Boolean>)(() -> !(boolean)supplier.get()), whenForce, whenContinue);
    }
    
    public Delayer delayed(@NonNull final Runnable action) {
        if (action == null) {
            throw new NullPointerException("action is marked non-null but is null");
        }
        this.actions.add((Object)action);
        return this;
    }
    
    public Delayer forced(@NonNull final Runnable action) {
        if (action == null) {
            throw new NullPointerException("action is marked non-null but is null");
        }
        this.forcedActions.add((Object)action);
        return this;
    }
    
    public Delayer executeSync() {
        return this.execute(false);
    }
    
    public Delayer executeAsync() {
        return this.execute(true);
    }
    
    public Delayer execute(final boolean async) {
        if (this.started.get() != null) {
            throw new RuntimeException("Cannot execute already executed chain");
        }
        this.started.set((Object)Instant.now());
        this.task.set(this.executor.schedule(this::run, this.checkRate, async));
        return this;
    }
    
    protected void run() {
        if (this.abort.get()) {
            return;
        }
        for (final Supplier<Boolean> abortWhenSupplier : this.abortWhen) {
            try {
                if (!(boolean)abortWhenSupplier.get()) {
                    continue;
                }
            }
            catch (final Throwable throwable) {
                this.cancel();
                throw throwable;
            }
            this.cancel();
            return;
        }
        for (final Supplier<Boolean> forceWhenSupplier : this.forceWhen) {
            try {
                if (!(boolean)forceWhenSupplier.get()) {
                    continue;
                }
            }
            catch (final Throwable throwable) {
                this.cancel();
                throw throwable;
            }
            this.cancel();
            this.forcedActions.forEach(Runnable::run);
            this.actions.forEach(Runnable::run);
            return;
        }
        if (Duration.between((Temporal)this.started.get(), (Temporal)Instant.now()).compareTo(this.duration) < 0) {
            return;
        }
        this.cancel();
        this.actions.forEach(Runnable::run);
    }
    
    public boolean cancel() {
        if (this.abort.get()) {
            return false;
        }
        this.abort.set(true);
        this.executor.cancel(this.task.get());
        return true;
    }
    
    protected Delayer(final TaskerExecutor<Object> executor, final Duration duration, final Duration checkRate) {
        this.task = (AtomicReference<Object>)new AtomicReference((Object)null);
        this.started = (AtomicReference<Instant>)new AtomicReference((Object)null);
        this.abort = new AtomicBoolean(false);
        this.abortWhen = (List<Supplier<Boolean>>)new ArrayList();
        this.forceWhen = (List<Supplier<Boolean>>)new ArrayList();
        this.actions = (List<Runnable>)new ArrayList();
        this.forcedActions = (List<Runnable>)new ArrayList();
        this.executor = executor;
        this.duration = duration;
        this.checkRate = checkRate;
    }
    
    static {
        NOOP_RUNNABLE = (() -> {});
    }
}
