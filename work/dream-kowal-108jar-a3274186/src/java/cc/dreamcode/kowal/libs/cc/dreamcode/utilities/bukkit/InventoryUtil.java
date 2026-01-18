package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit;

import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.Validation;
import org.bukkit.World;
import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import java.util.Objects;
import org.bukkit.Location;
import java.util.List;
import org.bukkit.inventory.Inventory;
import java.util.Collections;
import org.bukkit.inventory.ItemStack;
import lombok.NonNull;
import org.bukkit.entity.Player;

public final class InventoryUtil
{
    public static void giveItem(@NonNull final Player player, @NonNull final ItemStack itemStack) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        giveItems((Inventory)player.getInventory(), player.getLocation(), (List<ItemStack>)Collections.singletonList((Object)itemStack));
    }
    
    public static void giveItems(@NonNull final Player player, @NonNull final List<ItemStack> itemStacks) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (itemStacks == null) {
            throw new NullPointerException("itemStacks is marked non-null but is null");
        }
        giveItems((Inventory)player.getInventory(), player.getLocation(), itemStacks);
    }
    
    public static void giveItem(@NonNull final Player player, @NonNull final Location location, @NonNull final ItemStack itemStack) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        giveItems((Inventory)player.getInventory(), location, (List<ItemStack>)Collections.singletonList((Object)itemStack));
    }
    
    public static void giveItem(@NonNull final Inventory inventory, @NonNull final Location location, @NonNull final ItemStack itemStack) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        giveItems(inventory, location, (List<ItemStack>)Collections.singletonList((Object)itemStack));
    }
    
    public static void giveItems(@NonNull final Inventory inventory, @NonNull final Location location, @NonNull final List<ItemStack> itemStacks) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (itemStacks == null) {
            throw new NullPointerException("itemStacks is marked non-null but is null");
        }
        itemStacks.forEach(itemStack -> inventory.addItem(new ItemStack[] { itemStack }).values().forEach(noAdded -> Validation.nonNull(location.getWorld(), (java.util.function.Consumer<World>)(world -> world.dropItem(location, noAdded)))));
    }
    
    public static void dropItem(@NonNull final ItemStack itemStack, @NonNull final Location location) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        dropItems((List<ItemStack>)Collections.singletonList((Object)itemStack), location);
    }
    
    public static void dropItems(@NonNull final List<ItemStack> itemStacks, @NonNull final Location location) {
        if (itemStacks == null) {
            throw new NullPointerException("itemStacks is marked non-null but is null");
        }
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        itemStacks.forEach(itemStack -> ((World)Objects.requireNonNull((Object)location.getWorld())).dropItem(location, itemStack));
    }
    
    public static void addItem(@NonNull final ItemStack itemStack, @NonNull final Inventory inventory) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        addItems((List<ItemStack>)Collections.singletonList((Object)itemStack), inventory);
    }
    
    public static void addItems(@NonNull final List<ItemStack> itemStacks, @NonNull final Inventory inventory) {
        if (itemStacks == null) {
            throw new NullPointerException("itemStacks is marked non-null but is null");
        }
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        Objects.requireNonNull((Object)inventory);
        itemStacks.forEach(xva$0 -> inventory.addItem(new ItemStack[] { xva$0 }));
    }
    
    public static boolean hasSpace(@NonNull final Inventory inventory, @NonNull final ItemStack itemStack) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final Inventory newInv = Bukkit.createInventory((InventoryHolder)null, inventory.getSize());
        newInv.setContents(inventory.getContents());
        return newInv.addItem(new ItemStack[] { itemStack.clone() }).isEmpty();
    }
    
    public static boolean hasSpace(@NonNull final Inventory inventory, @NonNull final List<ItemStack> itemStacks) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (itemStacks == null) {
            throw new NullPointerException("itemStacks is marked non-null but is null");
        }
        final Inventory newInv = Bukkit.createInventory((InventoryHolder)null, inventory.getSize());
        newInv.setContents(inventory.getContents());
        return itemStacks.stream().map(itemStack -> newInv.addItem(new ItemStack[] { itemStack }).values()).allMatch(Collection::isEmpty);
    }
    
    public static List<ItemStack> hasItem(@NonNull final Inventory inventory, @NonNull final ItemStack itemStack) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return (List<ItemStack>)Arrays.stream((Object[])inventory.getContents()).filter(scan -> scan.isSimilar(itemStack)).collect(Collectors.toList());
    }
    
    public static int countItems(@NonNull final Inventory inventory, @NonNull final ItemStack itemStack) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return Arrays.stream((Object[])inventory.getContents()).filter(Objects::nonNull).filter(scan -> scan.isSimilar(itemStack)).mapToInt(ItemStack::getAmount).sum();
    }
    
    public static int countColorizedItems(@NonNull final Inventory inventory, @NonNull final ItemStack itemStack) {
        if (inventory == null) {
            throw new NullPointerException("inventory is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return countItems(inventory, ItemBuilder.of(itemStack).fixColors().toItemStack());
    }
    
    @Generated
    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
