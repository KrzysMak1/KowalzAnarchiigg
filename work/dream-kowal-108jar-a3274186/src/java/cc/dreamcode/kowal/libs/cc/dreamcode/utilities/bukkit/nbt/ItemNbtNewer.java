package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.nbt;

import java.util.Locale;
import lombok.Generated;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class ItemNbtNewer implements ItemNbt
{
    @Override
    public Map<String, String> getValues(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final MapBuilder<String, String> mapBuilder = new MapBuilder<String, String>();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return mapBuilder.build();
        }
        itemMeta.getPersistentDataContainer().getKeys().stream().filter(namespacedKey -> namespacedKey.getNamespace().equals((Object)plugin.getName().toLowerCase(Locale.ROOT))).forEach(namespacedKey -> mapBuilder.put(namespacedKey.getKey(), itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING)));
        return mapBuilder.build();
    }
    
    @Override
    public Map<String, String> getValues(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final MapBuilder<String, String> mapBuilder = new MapBuilder<String, String>();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return mapBuilder.build();
        }
        itemMeta.getPersistentDataContainer().getKeys().forEach(namespacedKey -> mapBuilder.put(namespacedKey.getKey(), itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING)));
        return mapBuilder.build();
    }
    
    @Override
    public ItemStack setValue(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack, @NonNull final String key, @NonNull final String value) {
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
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, (Object)value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    @Generated
    public ItemNbtNewer() {
    }
}
