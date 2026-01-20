package cc.dreamcode.kowal.hook;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketEventsSupport {
    private static final int PLAYER_INVENTORY_WINDOW_ID = 0;
    private static final int HOTBAR_SLOT_OFFSET = 36;
    private static final int ARMOR_SLOT_HELMET = 5;
    private static final int ARMOR_SLOT_CHESTPLATE = 6;
    private static final int ARMOR_SLOT_LEGGINGS = 7;
    private static final int ARMOR_SLOT_BOOTS = 8;
    private static final int OFFHAND_SLOT = 45;
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;

    public boolean isEnabled() {
        final PluginConfig.PacketEventsSyncSettings packetSettings = this.pluginConfig.integrations != null
                ? this.pluginConfig.integrations.packetEventsSync
                : null;
        if (packetSettings == null || !packetSettings.enabled) {
            return false;
        }
        return this.isPacketEventsAvailable();
    }

    public void sendInventoryResync(final Player player) {
        if (player == null || !this.isEnabled()) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        final int heldSlot = inventory.getHeldItemSlot();
        this.sendSlotResync(player, HOTBAR_SLOT_OFFSET + heldSlot, inventory.getItem(heldSlot));
        this.sendSlotResync(player, ARMOR_SLOT_HELMET, inventory.getHelmet());
        this.sendSlotResync(player, ARMOR_SLOT_CHESTPLATE, inventory.getChestplate());
        this.sendSlotResync(player, ARMOR_SLOT_LEGGINGS, inventory.getLeggings());
        this.sendSlotResync(player, ARMOR_SLOT_BOOTS, inventory.getBoots());
        this.sendSlotResync(player, OFFHAND_SLOT, inventory.getItemInOffHand());
    }

    public void sendSlotResync(final Player player, final int slot, final ItemStack stack) {
        this.sendSlotResync(player, PLAYER_INVENTORY_WINDOW_ID, slot, stack);
    }

    public void sendSlotResync(final Player player, final int windowId, final int slot, final ItemStack stack) {
        if (player == null || !this.isEnabled()) {
            return;
        }
        try {
            this.ensureInitialized();
            final Object packet = this.createSetSlotPacket(windowId, this.resolveStateId(player), slot, stack);
            if (packet == null) {
                return;
            }
            final Object api = this.getPacketEventsApi();
            final Object playerManager = api.getClass().getMethod("getPlayerManager").invoke(api);
            playerManager.getClass().getMethod("sendPacket", Player.class, Object.class).invoke(playerManager, player, packet);
        } catch (final Exception exception) {
            this.logDebug("PacketEvents: failed to send slot resync (" + exception.getClass().getSimpleName() + ").");
        }
    }

    public Integer getOpenWindowId(final Player player) {
        if (player == null) {
            return null;
        }
        final Object containerMenu = this.getContainerMenu(player);
        if (containerMenu == null) {
            return null;
        }
        final Integer containerId = this.getIntField(containerMenu, "containerId");
        return containerId != null ? containerId : null;
    }

    private boolean isPacketEventsAvailable() {
        final Plugin packetEventsPlugin = this.plugin.getServer().getPluginManager().getPlugin("PacketEvents");
        return packetEventsPlugin != null && packetEventsPlugin.isEnabled();
    }

    private void ensureInitialized() {
        try {
            final Object api = this.getPacketEventsApi();
            final Boolean loaded = this.invokeBoolean(api, "isLoaded");
            if (loaded == null || !loaded) {
                this.invokeVoid(api, "load");
            }
            final Boolean initialized = this.invokeBoolean(api, "isInitialized");
            if (initialized == null || !initialized) {
                this.invokeVoid(api, "init");
            }
        } catch (final Exception exception) {
            this.logDebug("PacketEvents: failed to initialize (" + exception.getClass().getSimpleName() + ").");
        }
    }

    private Object createSetSlotPacket(final int windowId, final int stateId, final int slot, final ItemStack stack) throws Exception {
        final Class<?> wrapperClass = Class.forName("com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot");
        final Object packetItem = this.toPacketEventsItem(stack);
        for (final Constructor<?> constructor : wrapperClass.getConstructors()) {
            final Class<?>[] params = constructor.getParameterTypes();
            if (params.length == 4 && params[0] == int.class && params[1] == int.class && params[2] == int.class) {
                return constructor.newInstance(windowId, stateId, slot, packetItem);
            }
        }
        return null;
    }

    private Object toPacketEventsItem(final ItemStack stack) throws Exception {
        final Class<?> conversionUtil = Class.forName("com.github.retrooper.packetevents.util.SpigotConversionUtil");
        final Method convert = conversionUtil.getMethod("fromBukkitItemStack", ItemStack.class);
        final ItemStack safeStack = stack == null ? new ItemStack(Material.AIR) : stack;
        return convert.invoke(null, safeStack);
    }

    private int resolveStateId(final Player player) {
        final Object containerMenu = this.getContainerMenu(player);
        if (containerMenu == null) {
            return 0;
        }
        final Integer stateId = this.invokeInt(containerMenu, "getStateId");
        if (stateId != null) {
            return stateId;
        }
        final Integer stateField = this.getIntField(containerMenu, "stateId");
        return stateField != null ? stateField : 0;
    }

    private Object getContainerMenu(final Player player) {
        final Object handle = this.getHandle(player);
        if (handle == null) {
            return null;
        }
        return this.getField(handle, "containerMenu");
    }

    private Object getHandle(final Player player) {
        try {
            final Method getHandle = player.getClass().getMethod("getHandle");
            return getHandle.invoke(player);
        } catch (final Exception exception) {
            return null;
        }
    }

    private Object getField(final Object target, final String name) {
        if (target == null) {
            return null;
        }
        try {
            final Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (final Exception exception) {
            return null;
        }
    }

    private Integer getIntField(final Object target, final String name) {
        final Object value = this.getField(target, name);
        if (value instanceof Integer intValue) {
            return intValue;
        }
        return null;
    }

    private Integer invokeInt(final Object target, final String name) {
        if (target == null) {
            return null;
        }
        try {
            final Method method = target.getClass().getMethod(name);
            final Object value = method.invoke(target);
            if (value instanceof Integer intValue) {
                return intValue;
            }
        } catch (final Exception exception) {
            return null;
        }
        return null;
    }

    private Boolean invokeBoolean(final Object target, final String name) {
        if (target == null) {
            return null;
        }
        try {
            final Method method = target.getClass().getMethod(name);
            final Object value = method.invoke(target);
            if (value instanceof Boolean boolValue) {
                return boolValue;
            }
        } catch (final Exception exception) {
            return null;
        }
        return null;
    }

    private void invokeVoid(final Object target, final String name) {
        if (target == null) {
            return;
        }
        try {
            final Method method = target.getClass().getMethod(name);
            method.invoke(target);
        } catch (final Exception exception) {
            return;
        }
    }

    private Object getPacketEventsApi() throws Exception {
        final Class<?> packetEventsClass = Class.forName("com.github.retrooper.packetevents.PacketEvents");
        final Method getApi = packetEventsClass.getMethod("getAPI");
        return getApi.invoke(null);
    }

    private void logDebug(final String message) {
        final PluginConfig.PacketEventsSyncSettings packetSettings = this.pluginConfig.integrations != null
                ? this.pluginConfig.integrations.packetEventsSync
                : null;
        if (packetSettings != null && packetSettings.debug) {
            this.plugin.getLogger().info(message);
        }
    }

    @Inject
    @Generated
    public PacketEventsSupport(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }
}
