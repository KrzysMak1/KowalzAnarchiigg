package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.listener;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryInteractEvent;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.holder.BukkitMenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public final class BukkitMenuListener implements Listener
{
    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }
        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder)inventoryHolder;
            bukkitMenuHolder.handleClick((InventoryInteractEvent)event);
        }
    }
    
    @EventHandler
    private void onInventoryInteract(final InventoryInteractEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }
        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder)inventoryHolder;
            bukkitMenuHolder.handleClick(event);
        }
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }
        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder)inventoryHolder;
            bukkitMenuHolder.handleClose(event);
        }
    }
    
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getHolder() == null) {
            return;
        }
        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventoryHolder instanceof BukkitMenuHolder) {
            final BukkitMenuHolder bukkitMenuHolder = (BukkitMenuHolder)inventoryHolder;
            bukkitMenuHolder.handleDrag(event);
        }
    }
}
