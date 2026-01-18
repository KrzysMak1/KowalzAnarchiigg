package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.serializer;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.Collections;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.inventory.ItemFlag;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import java.util.Collection;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class ItemMetaSerializer implements ObjectSerializer<ItemMeta>
{
    @Override
    public boolean supports(@NonNull final Class<? super ItemMeta> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ItemMeta.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final ItemMeta itemMeta, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (itemMeta == null) {
            throw new NullPointerException("itemMeta is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (itemMeta.hasDisplayName()) {
            data.add("display-name", itemMeta.getDisplayName());
        }
        if (itemMeta.hasLore()) {
            data.addCollection("lore", (Collection<?>)itemMeta.getLore(), String.class);
        }
        if (!itemMeta.getEnchants().isEmpty()) {
            data.addAsMap("enchantments", (java.util.Map<Enchantment, Integer>)itemMeta.getEnchants(), Enchantment.class, Integer.class);
        }
        if (!itemMeta.getItemFlags().isEmpty()) {
            data.addCollection("item-flags", (Collection<?>)itemMeta.getItemFlags(), ItemFlag.class);
        }
        try {
            final Method methodHasCustomModelData = ItemMeta.class.getMethod("hasCustomModelData", (Class<?>[])new Class[0]);
            final boolean hasCustomModelData = (boolean)methodHasCustomModelData.invoke((Object)itemMeta, new Object[0]);
            if (hasCustomModelData) {
                final Method methodGetCustomModelData = ItemMeta.class.getMethod("getCustomModelData", (Class<?>[])new Class[0]);
                final int getCustomModelData = (int)methodGetCustomModelData.invoke((Object)itemMeta, new Object[0]);
                data.add("model-id", getCustomModelData, Integer.class);
            }
        }
        catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {}
    }
    
    @Override
    public ItemMeta deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final String displayName = data.get("display-name", String.class);
        final List<String> lore = (List<String>)(data.containsKey("lore") ? data.getAsList("lore", String.class) : Collections.emptyList());
        final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)(data.containsKey("enchantments") ? data.getAsMap("enchantments", Enchantment.class, Integer.class) : Collections.emptyMap());
        final List<ItemFlag> itemFlags = (List<ItemFlag>)(data.containsKey("item-flags") ? data.getAsList("item-flags", ItemFlag.class) : Collections.emptyList());
        final ItemMeta itemMeta = new ItemStack(Material.COBBLESTONE).getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot extract empty ItemMeta from COBBLESTONE");
        }
        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }
        itemMeta.setLore((List)lore);
        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, (int)level, true));
        itemMeta.addItemFlags((ItemFlag[])itemFlags.toArray((Object[])new ItemFlag[0]));
        if (data.containsKey("model-id")) {
            try {
                final int customModelData = data.get("model-id", Integer.class);
                final Method method = ItemMeta.class.getMethod("setCustomModelData", Integer.class);
                method.invoke((Object)itemMeta, new Object[] { customModelData });
            }
            catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {}
        }
        return itemMeta;
    }
}
