package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component;

import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class RunnableResolver implements ComponentClassResolver<Runnable>
{
    private final DreamBukkitPlatform dreamBukkitPlatform;
    
    @Inject
    public RunnableResolver(final DreamBukkitPlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<Runnable> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Runnable.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "task";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        final Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("Runnable must have @Scheduler annotation.");
        }
        return (Map<String, Object>)new MapBuilder<String, Boolean>().put("async", scheduler.async()).put("start-time", (Boolean)(Object)Long.valueOf(scheduler.delay())).put("interval-time", (Boolean)(Object)Long.valueOf(scheduler.interval())).build();
    }
    
    @Override
    public Runnable resolve(@NonNull final Injector injector, @NonNull final Class<Runnable> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final Runnable runnable = injector.createInstance(type);
        final Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("Runnable must have @Scheduler annotation.");
        }
        if (scheduler.async()) {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this.dreamBukkitPlatform, runnable, scheduler.delay(), scheduler.interval());
        }
        else {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimer((Plugin)this.dreamBukkitPlatform, runnable, scheduler.delay(), scheduler.interval());
        }
        return runnable;
    }
}
