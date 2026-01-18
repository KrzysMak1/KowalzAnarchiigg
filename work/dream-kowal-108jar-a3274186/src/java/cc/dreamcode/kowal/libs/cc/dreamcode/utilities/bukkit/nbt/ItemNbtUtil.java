package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.nbt;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.VersionUtil;
import lombok.Generated;
import java.util.Optional;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public final class ItemNbtUtil
{
    private static final ItemNbt ITEM_NBT;
    private static Plugin plugin;
    
    public static Map<String, String> getValuesByPlugin(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValues(plugin, itemStack);
    }
    
    public static Map<String, String> getValuesByPlugin(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValues(ItemNbtUtil.plugin, itemStack);
    }
    
    public static Map<String, String> getValues(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValues(itemStack);
    }
    
    public static Optional<String> getValueByPlugin(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack, @NonNull final String key) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValue(plugin, itemStack, key);
    }
    
    public static Optional<String> getValueByPlugin(@NonNull final ItemStack itemStack, @NonNull final String key) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValue(ItemNbtUtil.plugin, itemStack, key);
    }
    
    public static Optional<String> getValue(@NonNull final ItemStack itemStack, @NonNull final String key) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.getValue(itemStack, key);
    }
    
    public static ItemStack setValue(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack, @NonNull final String key, @NonNull final String value) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.setValue(plugin, itemStack, key, value);
    }
    
    public static ItemStack setValue(@NonNull final ItemStack itemStack, @NonNull final String key, @NonNull final String value) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return ItemNbtUtil.ITEM_NBT.setValue(ItemNbtUtil.plugin, itemStack, key, value);
    }
    
    public static void setPlugin(final Plugin plugin) {
        ItemNbtUtil.plugin = plugin;
    }
    
    @Generated
    private ItemNbtUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        ITEM_NBT = (VersionUtil.isSupported(14) ? new ItemNbtNewer() : new ItemNbtLegacy());
    }
}
