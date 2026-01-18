package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.holder;

import lombok.Generated;
import java.util.HashMap;
import org.bukkit.event.inventory.InventoryInteractEvent;
import lombok.NonNull;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.bukkit.inventory.Inventory;
import java.util.Map;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base.BukkitMenu;

public final class DefaultMenuHolder implements BukkitMenuHolder
{
    private final BukkitMenu bukkitMenu;
    private boolean cancelInventoryClick;
    private boolean disposeWhenClose;
    private Consumer<InventoryClickEvent> inventoryClickEvent;
    private Consumer<InventoryClickEvent> postInventoryClickEvent;
    private Consumer<InventoryCloseEvent> inventoryCloseEvent;
    private Consumer<InventoryDragEvent> inventoryDragEvent;
    private final Map<Integer, Consumer<InventoryClickEvent>> inventoryActions;
    
    @NotNull
    public Inventory getInventory() {
        return this.bukkitMenu.getInventory();
    }
    
    @Override
    public void open(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        humanEntity.openInventory(this.bukkitMenu.getInventory());
    }
    
    @Override
    public Map<Integer, Consumer<InventoryClickEvent>> getSlotActions() {
        return this.inventoryActions;
    }
    
    @Override
    public void setActionOnSlot(final int slot, @NonNull final Consumer<InventoryClickEvent> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        this.inventoryActions.put((Object)slot, (Object)consumer);
    }
    
    @Override
    public void removeActionOnSlot(final int slot) {
        this.inventoryActions.remove((Object)slot);
    }
    
    @Override
    public void handleClick(@NonNull final InventoryInteractEvent event) {
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        if (event instanceof InventoryClickEvent) {
            final InventoryClickEvent inventoryClickEvent = (InventoryClickEvent)event;
            if (this.inventoryClickEvent != null) {
                this.inventoryClickEvent.accept((Object)inventoryClickEvent);
            }
            ((Consumer)this.inventoryActions.getOrDefault((Object)inventoryClickEvent.getRawSlot(), (Object)(e -> {
                if (this.cancelInventoryClick) {
                    e.setCancelled(true);
                }
            }))).accept((Object)inventoryClickEvent);
            if (this.postInventoryClickEvent != null) {
                this.postInventoryClickEvent.accept((Object)inventoryClickEvent);
            }
        }
        if (this.cancelInventoryClick) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void handleClose(@NonNull final InventoryCloseEvent event) {
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        if (this.inventoryCloseEvent != null) {
            this.inventoryCloseEvent.accept((Object)event);
        }
        if (this.disposeWhenClose) {
            this.dispose();
        }
    }
    
    @Override
    public void handleDrag(@NonNull final InventoryDragEvent event) {
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        if (this.inventoryDragEvent != null) {
            this.inventoryDragEvent.accept((Object)event);
        }
    }
    
    @Override
    public void dispose() {
        this.getInventory().clear();
        this.inventoryActions.clear();
    }
    
    @Generated
    public DefaultMenuHolder(final BukkitMenu bukkitMenu) {
        this.cancelInventoryClick = true;
        this.disposeWhenClose = false;
        this.inventoryActions = (Map<Integer, Consumer<InventoryClickEvent>>)new HashMap();
        this.bukkitMenu = bukkitMenu;
    }
    
    @Generated
    public boolean isCancelInventoryClick() {
        return this.cancelInventoryClick;
    }
    
    @Generated
    public void setCancelInventoryClick(final boolean cancelInventoryClick) {
        this.cancelInventoryClick = cancelInventoryClick;
    }
    
    @Generated
    public boolean isDisposeWhenClose() {
        return this.disposeWhenClose;
    }
    
    @Generated
    public void setDisposeWhenClose(final boolean disposeWhenClose) {
        this.disposeWhenClose = disposeWhenClose;
    }
    
    @Generated
    public Consumer<InventoryClickEvent> getInventoryClickEvent() {
        return this.inventoryClickEvent;
    }
    
    @Generated
    public void setInventoryClickEvent(final Consumer<InventoryClickEvent> inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
    }
    
    @Generated
    public Consumer<InventoryClickEvent> getPostInventoryClickEvent() {
        return this.postInventoryClickEvent;
    }
    
    @Generated
    public void setPostInventoryClickEvent(final Consumer<InventoryClickEvent> postInventoryClickEvent) {
        this.postInventoryClickEvent = postInventoryClickEvent;
    }
    
    @Generated
    public Consumer<InventoryCloseEvent> getInventoryCloseEvent() {
        return this.inventoryCloseEvent;
    }
    
    @Generated
    public void setInventoryCloseEvent(final Consumer<InventoryCloseEvent> inventoryCloseEvent) {
        this.inventoryCloseEvent = inventoryCloseEvent;
    }
    
    @Generated
    public Consumer<InventoryDragEvent> getInventoryDragEvent() {
        return this.inventoryDragEvent;
    }
    
    @Generated
    public void setInventoryDragEvent(final Consumer<InventoryDragEvent> inventoryDragEvent) {
        this.inventoryDragEvent = inventoryDragEvent;
    }
}
