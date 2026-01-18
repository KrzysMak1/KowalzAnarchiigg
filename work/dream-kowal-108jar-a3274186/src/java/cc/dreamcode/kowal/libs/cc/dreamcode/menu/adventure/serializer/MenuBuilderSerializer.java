package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.serializer;

import cc.dreamcode.kowal.libs.cc.dreamcode.menu.DreamMenuBuilder;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.BukkitMenuBuilder;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class MenuBuilderSerializer implements ObjectSerializer<BukkitMenuBuilder>
{
    @Override
    public boolean supports(@NonNull final Class<? super BukkitMenuBuilder> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BukkitMenuBuilder.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final BukkitMenuBuilder object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (!((DreamMenuBuilder<B, InventoryType, I>)object).getInventoryType().equals((Object)InventoryType.CHEST)) {
            data.add("type", ((DreamMenuBuilder<B, Object, I>)object).getInventoryType());
        }
        data.add("name", object.getName());
        if (((DreamMenuBuilder<B, InventoryType, I>)object).getInventoryType().equals((Object)InventoryType.CHEST)) {
            data.add("rows", object.getRows());
        }
        data.addAsMap("items", ((DreamMenuBuilder<B, T, ItemStack>)object).getItems(), Integer.class, ItemStack.class);
    }
    
    @Override
    public BukkitMenuBuilder deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        if (data.containsKey("type")) {
            return new BukkitMenuBuilder(data.get("type", InventoryType.class), data.get("name", String.class), data.getAsMap("items", Integer.class, ItemStack.class));
        }
        return new BukkitMenuBuilder(data.get("name", String.class), data.get("rows", Integer.class), data.getAsMap("items", Integer.class, ItemStack.class));
    }
}
