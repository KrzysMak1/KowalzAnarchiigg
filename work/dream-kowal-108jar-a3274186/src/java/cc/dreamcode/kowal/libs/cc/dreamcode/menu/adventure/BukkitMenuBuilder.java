package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base.BukkitMenu;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.DreamMenuBuilder;

public class BukkitMenuBuilder extends DreamMenuBuilder<BukkitMenu, InventoryType, ItemStack>
{
    public BukkitMenuBuilder(@NonNull final String name, final int rows, final Map<Integer, ItemStack> items) {
        super(InventoryType.CHEST, name, rows, (java.util.Map<Integer, Object>)((items == null) ? new HashMap() : items));
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
    }
    
    public BukkitMenuBuilder(@NonNull final InventoryType inventoryType, @NonNull final String name, final Map<Integer, ItemStack> items) {
        super(inventoryType, name, inventoryType.getDefaultSize(), (java.util.Map<Integer, Object>)((items == null) ? new HashMap() : items));
        if (inventoryType == null) {
            throw new NullPointerException("inventoryType is marked non-null but is null");
        }
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
    }
    
    @Override
    public BukkitMenu buildEmpty() {
        if (((DreamMenuBuilder<B, InventoryType, I>)this).getInventoryType().equals((Object)InventoryType.CHEST)) {
            return new BukkitMenu(this.getName(), this.getRows(), 0);
        }
        return new BukkitMenu(((DreamMenuBuilder<B, InventoryType, I>)this).getInventoryType(), this.getName(), 0);
    }
    
    @Override
    public BukkitMenu buildEmpty(@NonNull final Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            throw new NullPointerException("replaceMap is marked non-null but is null");
        }
        if (((DreamMenuBuilder<B, InventoryType, I>)this).getInventoryType().equals((Object)InventoryType.CHEST)) {
            return new BukkitMenu(this.getName(), replaceMap, this.getRows(), 0);
        }
        return new BukkitMenu(((DreamMenuBuilder<B, InventoryType, I>)this).getInventoryType(), this.getName(), replaceMap, 0);
    }
    
    @Override
    public BukkitMenu buildWithItems() {
        final BukkitMenu bukkitMenu = this.buildEmpty();
        this.getItems().forEach((slot, item) -> bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors().toItemStack()));
        return bukkitMenu;
    }
    
    @Override
    public BukkitMenu buildWithItems(@NonNull final Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            throw new NullPointerException("replaceMap is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = this.buildEmpty(replaceMap);
        this.getItems().forEach((slot, item) -> bukkitMenu.setItem((int)slot, ItemBuilder.of(item).fixColors(replaceMap).toItemStack()));
        return bukkitMenu;
    }
}
