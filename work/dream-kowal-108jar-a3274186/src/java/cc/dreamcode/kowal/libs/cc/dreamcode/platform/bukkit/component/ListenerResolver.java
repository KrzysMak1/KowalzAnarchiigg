package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.stream.Collectors;
import java.lang.reflect.Method;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import org.bukkit.event.Listener;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class ListenerResolver implements ComponentClassResolver<Listener>
{
    private final DreamBukkitPlatform dreamBukkitPlatform;
    
    @Inject
    public ListenerResolver(final DreamBukkitPlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<Listener> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Listener.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "listener (events)";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final Listener listener) {
        if (listener == null) {
            throw new NullPointerException("listener is marked non-null but is null");
        }
        return new MapBuilder<String, Object>().put("handlers", Arrays.stream((Object[])listener.getClass().getDeclaredMethods()).filter(method -> method.getAnnotation((Class)EventHandler.class) != null).map(Method::getName).collect(Collectors.joining((CharSequence)", "))).build();
    }
    
    @Override
    public Listener resolve(@NonNull final Injector injector, @NonNull final Class<Listener> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final Listener listener = injector.createInstance(type);
        final PluginManager pluginManager = this.dreamBukkitPlatform.getServer().getPluginManager();
        pluginManager.registerEvents(listener, (Plugin)this.dreamBukkitPlatform);
        return listener;
    }
}
