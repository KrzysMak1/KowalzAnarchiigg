package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.nbt;

import java.util.Optional;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public interface ItemNbt
{
    Map<String, String> getValues(@NonNull final Plugin p0, @NonNull final ItemStack p1);
    
    Map<String, String> getValues(@NonNull final ItemStack p0);
    
    default Optional<String> getValue(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack, @NonNull final String key) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (Optional<String>)this.getValues(plugin, itemStack).entrySet().stream().filter(entry -> ((String)entry.getKey()).equals((Object)key) && !((String)entry.getValue()).isEmpty()).map(Map.Entry::getValue).findAny();
    }
    
    default Optional<String> getValue(@NonNull final ItemStack itemStack, @NonNull final String key) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (Optional<String>)this.getValues(itemStack).entrySet().stream().filter(entry -> ((String)entry.getKey()).equals((Object)key) && !((String)entry.getValue()).isEmpty()).map(Map.Entry::getValue).findAny();
    }
    
    ItemStack setValue(@NonNull final Plugin p0, @NonNull final ItemStack p1, @NonNull final String p2, @NonNull final String p3);
}
