package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.plugin.PluginManager;
import java.lang.reflect.Field;
import org.bukkit.plugin.PluginDescriptionFile;
import com.google.common.graph.MutableGraph;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.GlobalTranslator;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.EventException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetAudience;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.Nullable;
import java.lang.invoke.MethodHandle;
import org.bukkit.event.player.PlayerEvent;
import java.util.Locale;
import java.util.function.BiConsumer;
import org.bukkit.event.Event;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import java.util.Iterator;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.function.Consumer;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jetbrains.annotations.NotNull;
import org.bukkit.plugin.Plugin;
import java.util.Map;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetAudienceProvider;

final class BukkitAudiencesImpl extends FacetAudienceProvider<CommandSender, BukkitAudience> implements BukkitAudiences, Listener
{
    private static final Map<String, BukkitAudiences> INSTANCES;
    private final Plugin plugin;
    
    static Builder builder(@NotNull final Plugin plugin) {
        return new Builder(plugin);
    }
    
    static BukkitAudiences instanceFor(@NotNull final Plugin plugin) {
        return builder(plugin).build();
    }
    
    BukkitAudiencesImpl(@NotNull final Plugin plugin, @NotNull final ComponentRenderer<Pointered> componentRenderer) {
        super(componentRenderer);
        this.plugin = (Plugin)Objects.requireNonNull((Object)plugin, "plugin");
        final CommandSender console = (CommandSender)this.plugin.getServer().getConsoleSender();
        ((FacetAudienceProvider<CommandSender, A>)this).addViewer(console);
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            ((FacetAudienceProvider<CommandSender, A>)this).addViewer((CommandSender)player);
        }
        this.registerEvent(PlayerJoinEvent.class, EventPriority.LOWEST, (java.util.function.Consumer<PlayerJoinEvent>)(event -> ((FacetAudienceProvider<CommandSender, A>)this).addViewer((CommandSender)event.getPlayer())));
        this.registerEvent(PlayerQuitEvent.class, EventPriority.MONITOR, (java.util.function.Consumer<PlayerQuitEvent>)(event -> ((FacetAudienceProvider<CommandSender, A>)this).removeViewer((CommandSender)event.getPlayer())));
    }
    
    @NotNull
    @Override
    public Audience sender(@NotNull final CommandSender sender) {
        if (sender instanceof Player) {
            return this.player((Player)sender);
        }
        if (sender instanceof ConsoleCommandSender) {
            return this.console();
        }
        if (sender instanceof ProxiedCommandSender) {
            return this.sender(((ProxiedCommandSender)sender).getCallee());
        }
        if (sender instanceof Entity || sender instanceof Block) {
            return Audience.empty();
        }
        return this.createAudience((Collection<CommandSender>)Collections.singletonList((Object)sender));
    }
    
    @NotNull
    @Override
    public Audience player(@NotNull final Player player) {
        return super.player(player.getUniqueId());
    }
    
    @NotNull
    @Override
    protected BukkitAudience createAudience(@NotNull final Collection<CommandSender> viewers) {
        return new BukkitAudience(this.plugin, this, viewers);
    }
    
    @Override
    public void close() {
        BukkitAudiencesImpl.INSTANCES.remove((Object)this.plugin.getName());
        super.close();
    }
    
    @NotNull
    public ComponentFlattener flattener() {
        return BukkitComponentSerializer.FLATTENER;
    }
    
    private <T extends Event> void registerEvent(@NotNull final Class<T> type, @NotNull final EventPriority priority, @NotNull final Consumer<T> callback) {
        Objects.requireNonNull((Object)callback, "callback");
        this.plugin.getServer().getPluginManager().registerEvent((Class)type, (Listener)this, priority, (listener, event) -> callback.accept((Object)event), this.plugin, true);
    }
    
    private void registerLocaleEvent(final EventPriority priority, @NotNull final BiConsumer<Player, Locale> callback) {
        Class<?> eventClass = MinecraftReflection.findClass("org.bukkit.event.player.PlayerLocaleChangeEvent");
        if (eventClass == null) {
            eventClass = MinecraftReflection.findClass("com.destroystokyo.paper.event.player.PlayerLocaleChangeEvent");
        }
        MethodHandle getMethod = MinecraftReflection.findMethod(eventClass, "getLocale", String.class, (Class<?>[])new Class[0]);
        if (getMethod == null) {
            getMethod = MinecraftReflection.findMethod(eventClass, "getNewLocale", String.class, (Class<?>[])new Class[0]);
        }
        if (getMethod != null && PlayerEvent.class.isAssignableFrom(eventClass)) {
            final Class<? extends PlayerEvent> localeEvent = (Class<? extends PlayerEvent>)eventClass;
            final MethodHandle getLocale = getMethod;
            this.registerEvent(localeEvent, priority, (java.util.function.Consumer<? extends PlayerEvent>)(event -> {
                final Player player = event.getPlayer();
                String locale;
                try {
                    locale = getLocale.invoke(event);
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to accept %s: %s", localeEvent.getName(), player);
                    return;
                }
                callback.accept((Object)player, (Object)toLocale(locale));
            }));
        }
    }
    
    @NotNull
    private static Locale toLocale(@Nullable final String string) {
        if (string != null) {
            final Locale locale = Translator.parseLocale(string);
            if (locale != null) {
                return locale;
            }
        }
        return Locale.US;
    }
    
    static {
        Knob.OUT = (Consumer<String>)(message -> Bukkit.getLogger().log(Level.INFO, message));
        Knob.ERR = (BiConsumer<String, Throwable>)((message, error) -> Bukkit.getLogger().log(Level.WARNING, message, error));
        INSTANCES = Collections.synchronizedMap((Map)new HashMap(4));
    }
    
    static final class Builder implements BukkitAudiences.Builder
    {
        @NotNull
        private final Plugin plugin;
        private ComponentRenderer<Pointered> componentRenderer;
        
        Builder(@NotNull final Plugin plugin) {
            this.plugin = (Plugin)Objects.requireNonNull((Object)plugin, "plugin");
            ((AudienceProvider.Builder<AudienceProvider, AudienceProvider.Builder>)this).componentRenderer((java.util.function.Function<Pointered, Locale>)(ptr -> ptr.getOrDefault(Identity.LOCALE, BukkitAudiencesImpl.DEFAULT_LOCALE)), GlobalTranslator.renderer());
        }
        
        @NotNull
        @Override
        public Builder componentRenderer(@NotNull final ComponentRenderer<Pointered> componentRenderer) {
            this.componentRenderer = (ComponentRenderer)Objects.requireNonNull((Object)componentRenderer, "component renderer");
            return this;
        }
        
        @Override
        public BukkitAudiences.Builder partition(@NotNull final Function<Pointered, ?> partitionFunction) {
            Objects.requireNonNull((Object)partitionFunction, "partitionFunction");
            return this;
        }
        
        @NotNull
        @Override
        public BukkitAudiences build() {
            return (BukkitAudiences)BukkitAudiencesImpl.INSTANCES.computeIfAbsent((Object)this.plugin.getName(), name -> {
                this.softDepend("ViaVersion");
                return new BukkitAudiencesImpl(this.plugin, this.componentRenderer);
            });
        }
        
        private void softDepend(@NotNull final String pluginName) {
            final PluginDescriptionFile file = this.plugin.getDescription();
            if (file.getName().equals((Object)pluginName)) {
                return;
            }
            try {
                final Field softDepend = MinecraftReflection.needField(file.getClass(), "softDepend");
                final List<String> dependencies = (List<String>)softDepend.get((Object)file);
                if (!dependencies.contains((Object)pluginName)) {
                    final List<String> newList = (List<String>)ImmutableList.builder().addAll((Iterable)dependencies).add((Object)pluginName).build();
                    softDepend.set((Object)file, (Object)newList);
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to inject softDepend in plugin.yml: %s %s", this.plugin, pluginName);
            }
            try {
                final PluginManager manager = this.plugin.getServer().getPluginManager();
                final Field dependencyGraphField = MinecraftReflection.needField(manager.getClass(), "dependencyGraph");
                final MutableGraph<String> graph = (MutableGraph<String>)dependencyGraphField.get((Object)manager);
                graph.putEdge((Object)file.getName(), (Object)pluginName);
            }
            catch (final Throwable t) {}
        }
    }
}
