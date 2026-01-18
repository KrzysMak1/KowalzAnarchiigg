package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentMethodResolver;

public class SchedulerMethodResolver implements ComponentMethodResolver<Scheduler>
{
    private final DreamBukkitPlatform dreamBukkitPlatform;
    
    @Inject
    public SchedulerMethodResolver(final DreamBukkitPlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<Scheduler> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Scheduler.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "scheduler";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final Scheduler scheduler) {
        if (scheduler == null) {
            throw new NullPointerException("scheduler is marked non-null but is null");
        }
        return (Map<String, Object>)new MapBuilder<String, Boolean>().put("async", scheduler.async()).put("start-time", (Boolean)(Object)Long.valueOf(scheduler.delay())).put("interval-time", (Boolean)(Object)Long.valueOf(scheduler.interval())).build();
    }
    
    @Override
    public void apply(@NonNull final Injector injector, @NonNull final Scheduler scheduler, @NonNull final Method method, @NonNull final Object instance) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (scheduler == null) {
            throw new NullPointerException("scheduler is marked non-null but is null");
        }
        if (method == null) {
            throw new NullPointerException("method is marked non-null but is null");
        }
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        final Runnable runnable = () -> {
            try {
                method.invoke(instance, new Object[0]);
            }
            catch (final IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke scheduler method", (Throwable)e);
            }
        };
        if (scheduler.async()) {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.dreamBukkitPlatform, runnable, scheduler.delay(), scheduler.interval());
        }
        else {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimer((Plugin)this.dreamBukkitPlatform, runnable, scheduler.delay(), scheduler.interval());
        }
    }
}
