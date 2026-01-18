package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import org.bukkit.ChatColor;
import java.util.stream.Collectors;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.Collections;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.inventory.ItemFlag;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import java.util.Collection;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class ItemMetaSerializer implements ObjectSerializer<ItemMeta>
{
    private static final char COLOR_CHAR = '§';
    private static final char ALT_COLOR_CHAR = '&';
    
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
            data.add("display", this.decolor(itemMeta.getDisplayName()));
        }
        if (itemMeta.hasLore()) {
            data.addCollection("lore", (Collection<?>)this.decolor((List<String>)itemMeta.getLore()), String.class);
        }
        if (!itemMeta.getEnchants().isEmpty()) {
            data.addAsMap("enchantments", (java.util.Map<Enchantment, Integer>)itemMeta.getEnchants(), Enchantment.class, Integer.class);
        }
        if (!itemMeta.getItemFlags().isEmpty()) {
            data.addCollection("flags", (Collection<?>)itemMeta.getItemFlags(), ItemFlag.class);
        }
    }
    
    @Override
    public ItemMeta deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        String displayName = data.get("display", String.class);
        if (displayName == null) {
            displayName = data.get("display-name", String.class);
        }
        final List<String> lore = (List<String>)(data.containsKey("lore") ? data.getAsList("lore", String.class) : Collections.emptyList());
        final Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>)(data.containsKey("enchantments") ? data.getAsMap("enchantments", Enchantment.class, Integer.class) : Collections.emptyMap());
        final List<ItemFlag> itemFlags = (List<ItemFlag>)new ArrayList((Collection)(data.containsKey("flags") ? data.getAsList("flags", ItemFlag.class) : Collections.emptyList()));
        if (data.containsKey("item-flags")) {
            itemFlags.addAll((Collection)data.getAsList("item-flags", ItemFlag.class));
        }
        final ItemMeta itemMeta = new ItemStack(Material.COBBLESTONE).getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot extract empty ItemMeta from COBBLESTONE");
        }
        if (displayName != null) {
            itemMeta.setDisplayName(this.color(displayName));
        }
        itemMeta.setLore((List)this.color(lore));
        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, (int)level, true));
        itemMeta.addItemFlags((ItemFlag[])itemFlags.toArray((Object[])new ItemFlag[0]));
        return itemMeta;
    }
    
    private List<String> color(final List<String> text) {
        return (List<String>)text.stream().map(this::color).collect(Collectors.toList());
    }
    
    private String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    private List<String> decolor(final List<String> text) {
        return (List<String>)text.stream().map(this::decolor).collect(Collectors.toList());
    }
    
    private String decolor(final String text) {
        return text.replace((CharSequence)"§", (CharSequence)"&");
    }
}
