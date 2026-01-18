package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook;

import org.bukkit.Bukkit;
import java.lang.annotation.Annotation;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamPlatform;

public class PluginHookManager
{
    private final DreamPlatform dreamPlatform;
    private final DreamLogger dreamLogger;
    private final List<PluginHook> pluginHooks;
    
    @Inject
    public PluginHookManager(final DreamPlatform dreamPlatform, final DreamLogger dreamLogger) {
        this.pluginHooks = (List<PluginHook>)new ArrayList();
        this.dreamPlatform = dreamPlatform;
        this.dreamLogger = dreamLogger;
    }
    
    public <T extends PluginHook> Optional<T> get(@NonNull final Class<T> pluginHookClass) {
        if (pluginHookClass == null) {
            throw new NullPointerException("pluginHookClass is marked non-null but is null");
        }
        return (Optional<T>)this.pluginHooks.stream().filter(hook -> hook.getClass().isAssignableFrom(pluginHookClass)).findAny();
    }
    
    public void registerHook(@NonNull final Class<? extends PluginHook> pluginHookClass) {
        if (pluginHookClass == null) {
            throw new NullPointerException("pluginHookClass is marked non-null but is null");
        }
        final long time = System.currentTimeMillis();
        if (!pluginHookClass.isAnnotationPresent((Class<? extends Annotation>)Hook.class)) {
            throw new RuntimeException("Hook annotation not found");
        }
        final Hook hook = pluginHookClass.getAnnotation(Hook.class);
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(hook.name())) {
            this.dreamLogger.warning(hook.name() + " not found! Some things may be unavailable.");
            return;
        }
        final PluginHook pluginHook = this.dreamPlatform.createInstance(pluginHookClass);
        pluginHook.onInit();
        this.pluginHooks.add((Object)pluginHook);
        this.dreamLogger.info(new DreamLogger.Builder().type("Added hook").name(hook.name()).took(System.currentTimeMillis() - time).build());
    }
}
