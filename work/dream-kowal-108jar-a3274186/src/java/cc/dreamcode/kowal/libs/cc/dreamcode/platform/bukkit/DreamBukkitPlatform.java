package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit;

import lombok.Generated;
import org.bukkit.plugin.Plugin;
import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentMethodResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.method.SchedulerMethodResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.RunnableResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.ListenerResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.OkaeriInjector;
import java.util.concurrent.atomic.AtomicBoolean;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DreamBukkitPlatform extends JavaPlugin implements DreamPlatform
{
    private Injector injector;
    private DreamLogger dreamLogger;
    private ComponentService componentService;
    private final AtomicBoolean pluginDisabled;
    
    public DreamBukkitPlatform() {
        this.pluginDisabled = new AtomicBoolean(false);
    }
    
    public void onLoad() {
        (this.injector = OkaeriInjector.create()).registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getServer());
        this.injector.registerInjectable(this.getServer().getScheduler());
        this.injector.registerInjectable(this.getServer().getPluginManager());
        this.dreamLogger = new DreamBukkitLogger(this.getLogger());
        this.injector.registerInjectable(this.dreamLogger);
        this.componentService = new ComponentService(this.injector);
        this.injector.registerInjectable(this.componentService);
        try {
            this.load(this.componentService);
        }
        catch (final Exception e) {
            this.getPluginDisabled().set(true);
            throw new PlatformException("An error was caught when plugin are loading...", (Throwable)e);
        }
    }
    
    public void onEnable() {
        if (this.getPluginDisabled().get()) {
            return;
        }
        this.componentService.registerResolver(ListenerResolver.class);
        this.componentService.registerResolver(RunnableResolver.class);
        this.componentService.registerMethodResolver(SchedulerMethodResolver.class);
        try {
            this.enable(this.componentService);
        }
        catch (final Exception e) {
            this.getPluginDisabled().set(true);
            throw new PlatformException("An error was caught when plugin are starting...", (Throwable)e);
        }
        this.dreamLogger.info(String.format("Active version: v%s - Author: %s", new Object[] { this.getDescription().getVersion(), this.getDescription().getAuthors() }));
    }
    
    public void onDisable() {
        if (this.getPluginDisabled().get()) {
            return;
        }
        try {
            this.disable();
        }
        catch (final Exception e) {
            throw new PlatformException("An error was caught when plugin are stopping...", (Throwable)e);
        }
        this.dreamLogger.info(String.format("Active version: v%s - Author: %s", new Object[] { this.getDescription().getVersion(), this.getDescription().getAuthors() }));
    }
    
    public abstract void load(@NonNull final ComponentService componentService);
    
    public <T> T registerInjectable(final Class<T> type) {
        final T t = (T)this.createInstance((Class<Object>)type);
        return this.registerInjectable(t);
    }
    
    public <T> T registerInjectable(@NonNull final String name, final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final T t = this.createInstance(type);
        return this.registerInjectable(name, t);
    }
    
    public <T> T registerInjectable(@NonNull final T t) {
        if (t == null) {
            throw new NullPointerException("t is marked non-null but is null");
        }
        this.injector.registerInjectable(t);
        if (this.componentService.isDebug()) {
            this.dreamLogger.info(new DreamLogger.Builder().type("Added object instance").name(t.getClass().getSimpleName()).build());
        }
        return t;
    }
    
    public <T> T registerInjectable(@NonNull final String name, @NonNull final T t) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (t == null) {
            throw new NullPointerException("t is marked non-null but is null");
        }
        this.injector.registerInjectable(name, t);
        if (this.componentService.isDebug()) {
            this.dreamLogger.info(new DreamLogger.Builder().type("Added object instance").name(t.getClass().getSimpleName()).meta("name", name).build());
        }
        return t;
    }
    
    public <T> T createInstance(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return this.injector.createInstance(type);
    }
    
    public <T> Optional<T> getInject(@NonNull final String name, @NonNull final Class<T> value) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return this.injector.get(name, value);
    }
    
    public <T> Optional<T> getInject(@NonNull final Class<T> value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return this.getInject("", value);
    }
    
    public void runAsync(@NonNull final Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (!this.isEnabled() || this.getPluginDisabled().get()) {
            return;
        }
        this.getServer().getScheduler().runTaskAsynchronously((Plugin)this, runnable);
    }
    
    @Generated
    public Injector getInjector() {
        return this.injector;
    }
    
    @Generated
    public DreamLogger getDreamLogger() {
        return this.dreamLogger;
    }
    
    @Generated
    public ComponentService getComponentService() {
        return this.componentService;
    }
    
    @Generated
    public AtomicBoolean getPluginDisabled() {
        return this.pluginDisabled;
    }
}
