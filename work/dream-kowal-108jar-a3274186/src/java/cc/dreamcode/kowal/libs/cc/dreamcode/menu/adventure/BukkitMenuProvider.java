package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure;

import lombok.Generated;
import java.util.function.Consumer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.listener.BukkitMenuListener;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import java.util.Locale;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.Placeholders;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base.BukkitMenuPaginated;
import org.bukkit.event.inventory.InventoryType;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base.BukkitMenu;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.DreamMenuProvider;

public final class BukkitMenuProvider implements DreamMenuProvider<BukkitMenu, InventoryType, BukkitMenuPaginated>
{
    private static BukkitMenuProvider INSTANCE;
    private final Placeholders placeholders;
    private final Locale locale;
    
    public BukkitMenuProvider(final Placeholders placeholders, final Locale locale) {
        BukkitMenuProvider.INSTANCE = this;
        this.placeholders = placeholders;
        this.locale = locale;
    }
    
    public static BukkitMenuProvider create(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return create(plugin, Locale.forLanguageTag("pl"));
    }
    
    public static BukkitMenuProvider create(@NonNull final Plugin plugin, @NonNull final Locale locale) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents((Listener)new BukkitMenuListener(), plugin);
        return new BukkitMenuProvider(Placeholders.create(true), locale);
    }
    
    public static BukkitMenuProvider create(@NonNull final Plugin plugin, @NonNull final Locale locale, @NonNull final Placeholders placeholders) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents((Listener)new BukkitMenuListener(), plugin);
        return new BukkitMenuProvider(placeholders, locale);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final String title, final int rows) {
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        return new BukkitMenu(title, rows, 0);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final String title, final int rows, @NonNull final Consumer<BukkitMenu> consumer) {
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = new BukkitMenu(title, rows, 0);
        consumer.accept((Object)bukkitMenu);
        return bukkitMenu;
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final InventoryType type, @NonNull final String title) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        return new BukkitMenu(type, title, 0);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final InventoryType type, @NonNull final String title, @NonNull final Consumer<BukkitMenu> consumer) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = new BukkitMenu(type, title, 0);
        consumer.accept((Object)bukkitMenu);
        return bukkitMenu;
    }
    
    @Override
    public BukkitMenuPaginated createPaginated(@NonNull final BukkitMenu bukkitMenu) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        return new BukkitMenuPaginated(bukkitMenu);
    }
    
    @Override
    public BukkitMenuPaginated createPaginated(@NonNull final BukkitMenu bukkitMenu, @NonNull final Consumer<BukkitMenu> consumer) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        consumer.accept((Object)bukkitMenu);
        return new BukkitMenuPaginated(bukkitMenu);
    }
    
    public static BukkitMenuProvider getInstance() {
        if (BukkitMenuProvider.INSTANCE == null) {
            throw new RuntimeException("BukkitMenuProvider not found, make sure it is registered");
        }
        return BukkitMenuProvider.INSTANCE;
    }
    
    @Generated
    public Placeholders getPlaceholders() {
        return this.placeholders;
    }
    
    @Generated
    public Locale getLocale() {
        return this.locale;
    }
}
