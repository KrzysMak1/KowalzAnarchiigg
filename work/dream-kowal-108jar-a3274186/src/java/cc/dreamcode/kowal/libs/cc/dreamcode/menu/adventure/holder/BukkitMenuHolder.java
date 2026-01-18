package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.holder;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.function.Consumer;
import java.util.Map;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.holder.DreamMenuHolder;

public interface BukkitMenuHolder extends DreamMenuHolder<HumanEntity>, InventoryHolder
{
    Map<Integer, Consumer<InventoryClickEvent>> getSlotActions();
    
    void setActionOnSlot(final int p0, @NonNull final Consumer<InventoryClickEvent> p1);
    
    void removeActionOnSlot(final int p0);
    
    void handleClick(@NonNull final InventoryInteractEvent p0);
    
    void handleClose(@NonNull final InventoryCloseEvent p0);
    
    void handleDrag(@NonNull final InventoryDragEvent p0);
}
