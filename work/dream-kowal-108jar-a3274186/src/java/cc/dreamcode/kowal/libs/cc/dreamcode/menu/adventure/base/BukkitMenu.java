package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.utilities.MenuUtil;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import java.util.Locale;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.BukkitMenuProvider;
import java.util.HashMap;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import java.util.function.Consumer;
import org.bukkit.inventory.Inventory;
import java.util.Map;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.holder.DefaultMenuHolder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.base.DreamMenu;

public final class BukkitMenu implements DreamMenu<BukkitMenu, BukkitMenuPaginated, ItemStack, InventoryClickEvent, DefaultMenuHolder, HumanEntity>
{
    private final InventoryType inventoryType;
    private final String title;
    private final int size;
    private final Map<String, Object> placeholders;
    private final Inventory inventory;
    private boolean cancelInventoryClick;
    private boolean disposeWhenClose;
    private Consumer<InventoryClickEvent> inventoryClickEvent;
    private Consumer<InventoryClickEvent> postInventoryClickEvent;
    private Consumer<InventoryDragEvent> inventoryDragEvent;
    private Consumer<InventoryCloseEvent> inventoryCloseEvent;
    private final DefaultMenuHolder defaultMenuHolder;
    
    public BukkitMenu(@NonNull final InventoryType inventoryType, @NonNull final String title, final int page) {
        this.cancelInventoryClick = true;
        this.disposeWhenClose = false;
        if (inventoryType == null) {
            throw new NullPointerException("inventoryType is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = (Map<String, Object>)new HashMap();
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        final Locale locale = BukkitMenuProvider.getInstance().getLocale();
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, title);
        final PlaceholderContext placeholderContext = BukkitMenuProvider.getInstance().getPlaceholders().contextOf(compiledMessage).with("page", page);
        this.inventory = Bukkit.createInventory((InventoryHolder)this.defaultMenuHolder, this.inventoryType, StringColorUtil.fixColor(placeholderContext.apply()));
    }
    
    public BukkitMenu(@NonNull final InventoryType inventoryType, @NonNull final String title, @NonNull final Map<String, Object> placeholders, final int page) {
        this.cancelInventoryClick = true;
        this.disposeWhenClose = false;
        if (inventoryType == null) {
            throw new NullPointerException("inventoryType is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        this.inventoryType = inventoryType;
        this.title = title;
        this.size = inventoryType.getDefaultSize();
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        final Locale locale = BukkitMenuProvider.getInstance().getLocale();
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, title);
        final PlaceholderContext placeholderContext = BukkitMenuProvider.getInstance().getPlaceholders().contextOf(compiledMessage).with("page", page).with(placeholders);
        this.inventory = Bukkit.createInventory((InventoryHolder)this.defaultMenuHolder, this.inventoryType, StringColorUtil.fixColor(placeholderContext.apply()));
    }
    
    public BukkitMenu(@NonNull final String title, final int rows, final int page) {
        this.cancelInventoryClick = true;
        this.disposeWhenClose = false;
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        this.inventoryType = InventoryType.CHEST;
        this.title = title;
        this.size = ((rows > 6) ? 54 : (rows * 9));
        this.placeholders = (Map<String, Object>)new HashMap();
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        final Locale locale = BukkitMenuProvider.getInstance().getLocale();
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, title);
        final PlaceholderContext placeholderContext = BukkitMenuProvider.getInstance().getPlaceholders().contextOf(compiledMessage).with("page", page);
        this.inventory = Bukkit.createInventory((InventoryHolder)this.defaultMenuHolder, this.size, StringColorUtil.fixColor(placeholderContext.apply()));
    }
    
    public BukkitMenu(@NonNull final String title, @NonNull final Map<String, Object> placeholders, final int rows, final int page) {
        this.cancelInventoryClick = true;
        this.disposeWhenClose = false;
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        this.inventoryType = InventoryType.CHEST;
        this.title = title;
        this.size = ((rows > 6) ? 54 : (rows * 9));
        this.placeholders = placeholders;
        this.defaultMenuHolder = new DefaultMenuHolder(this);
        final Locale locale = BukkitMenuProvider.getInstance().getLocale();
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, title);
        final PlaceholderContext placeholderContext = BukkitMenuProvider.getInstance().getPlaceholders().contextOf(compiledMessage).with("page", page).with(placeholders);
        this.inventory = Bukkit.createInventory((InventoryHolder)this.defaultMenuHolder, this.size, StringColorUtil.fixColor(placeholderContext.apply()));
    }
    
    @Override
    public BukkitMenu addItem(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }
        return this;
    }
    
    @Override
    public BukkitMenu addItem(@NonNull final ItemStack itemStack, @NonNull final List<Integer> applySlots) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (applySlots == null) {
            throw new NullPointerException("applySlots is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (applySlots.contains((Object)slot)) {
                if (this.inventory.getItem(slot) == null) {
                    this.inventory.setItem(slot, itemStack);
                    return this;
                }
            }
        }
        return this;
    }
    
    @Override
    public BukkitMenu addItem(@NonNull final ItemStack itemStack, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (this.inventory.getItem(slot) == null) {
                this.defaultMenuHolder.setActionOnSlot(slot, event);
                this.inventory.setItem(slot, itemStack);
                return this;
            }
        }
        return this;
    }
    
    @Override
    public BukkitMenu addItem(@NonNull final ItemStack itemStack, @NonNull final List<Integer> applySlots, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (applySlots == null) {
            throw new NullPointerException("applySlots is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (applySlots.contains((Object)slot)) {
                if (this.inventory.getItem(slot) == null) {
                    this.defaultMenuHolder.setActionOnSlot(slot, event);
                    this.inventory.setItem(slot, itemStack);
                    return this;
                }
            }
        }
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int slot, @NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        this.inventory.setItem(slot, itemStack);
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int slot, @NonNull final ItemStack itemStack, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        this.defaultMenuHolder.setActionOnSlot(slot, event);
        this.inventory.setItem(slot, itemStack);
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int x, final int z, @NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setItem(slot, itemStack);
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int x, final int z, @NonNull final ItemStack itemStack, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setItem(slot, itemStack, event);
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int[] slots, @NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        for (final int slot : slots) {
            this.setItem(slot, itemStack);
        }
        return this;
    }
    
    @Override
    public BukkitMenu setItem(final int[] slots, @NonNull final ItemStack itemStack, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        for (final int slot : slots) {
            this.setItem(slot, itemStack, event);
        }
        return this;
    }
    
    @Override
    public BukkitMenu fillInventoryWith(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
            }
        }
        return this;
    }
    
    @Override
    public BukkitMenu fillInventoryWith(@NonNull final ItemStack itemStack, @NonNull final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        for (int slot = 0; slot < this.size; ++slot) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, itemStack);
                this.defaultMenuHolder.setActionOnSlot(slot, event);
            }
        }
        return this;
    }
    
    @Override
    public DefaultMenuHolder getHolder() {
        return this.defaultMenuHolder;
    }
    
    @Override
    public BukkitMenu open(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        this.getHolder().open(humanEntity);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated toPaginated() {
        return new BukkitMenuPaginated(this);
    }
    
    @Override
    public BukkitMenu dispose() {
        this.getHolder().dispose();
        return this;
    }
    
    public BukkitMenu cloneMenu(final int slot) {
        BukkitMenu bukkitMenu;
        if (this.inventoryType.equals((Object)InventoryType.CHEST)) {
            bukkitMenu = new BukkitMenu(this.title, this.placeholders, this.size / 9, slot);
        }
        else {
            bukkitMenu = new BukkitMenu(this.inventoryType, this.title, this.placeholders, slot);
        }
        bukkitMenu.setCancelInventoryClick(this.cancelInventoryClick);
        bukkitMenu.setDisposeWhenClose(this.disposeWhenClose);
        bukkitMenu.getInventory().setContents(this.inventory.getContents());
        this.getHolder().getSlotActions().forEach((integer, inventoryClickEventConsumer) -> bukkitMenu.getHolder().setActionOnSlot(integer, (Consumer<InventoryClickEvent>)inventoryClickEventConsumer));
        return bukkitMenu;
    }
    
    public BukkitMenu setCancelInventoryClick(final boolean cancelInventoryClick) {
        this.cancelInventoryClick = cancelInventoryClick;
        this.defaultMenuHolder.setCancelInventoryClick(cancelInventoryClick);
        return this;
    }
    
    public BukkitMenu setDisposeWhenClose(final boolean disposeWhenClose) {
        this.disposeWhenClose = disposeWhenClose;
        this.defaultMenuHolder.setDisposeWhenClose(disposeWhenClose);
        return this;
    }
    
    public BukkitMenu setInventoryCloseEvent(final Consumer<InventoryCloseEvent> inventoryCloseEvent) {
        this.inventoryCloseEvent = inventoryCloseEvent;
        this.defaultMenuHolder.setInventoryCloseEvent(inventoryCloseEvent);
        return this;
    }
    
    public BukkitMenu setInventoryClickEvent(final Consumer<InventoryClickEvent> inventoryClickEvent) {
        this.inventoryClickEvent = inventoryClickEvent;
        this.defaultMenuHolder.setInventoryClickEvent(inventoryClickEvent);
        return this;
    }
    
    public BukkitMenu setPostInventoryClickEvent(final Consumer<InventoryClickEvent> postInventoryClickEvent) {
        this.postInventoryClickEvent = postInventoryClickEvent;
        this.defaultMenuHolder.setPostInventoryClickEvent(postInventoryClickEvent);
        return this;
    }
    
    public BukkitMenu setInventoryDragEvent(final Consumer<InventoryDragEvent> inventoryDragEvent) {
        this.inventoryDragEvent = inventoryDragEvent;
        this.defaultMenuHolder.setInventoryDragEvent(inventoryDragEvent);
        return this;
    }
    
    @Generated
    public InventoryType getInventoryType() {
        return this.inventoryType;
    }
    
    @Generated
    public String getTitle() {
        return this.title;
    }
    
    @Generated
    public int getSize() {
        return this.size;
    }
    
    @Generated
    public Map<String, Object> getPlaceholders() {
        return this.placeholders;
    }
    
    @Generated
    public Inventory getInventory() {
        return this.inventory;
    }
    
    @Generated
    public boolean isCancelInventoryClick() {
        return this.cancelInventoryClick;
    }
    
    @Generated
    public boolean isDisposeWhenClose() {
        return this.disposeWhenClose;
    }
    
    @Generated
    public Consumer<InventoryClickEvent> getInventoryClickEvent() {
        return this.inventoryClickEvent;
    }
    
    @Generated
    public Consumer<InventoryClickEvent> getPostInventoryClickEvent() {
        return this.postInventoryClickEvent;
    }
    
    @Generated
    public Consumer<InventoryDragEvent> getInventoryDragEvent() {
        return this.inventoryDragEvent;
    }
    
    @Generated
    public Consumer<InventoryCloseEvent> getInventoryCloseEvent() {
        return this.inventoryCloseEvent;
    }
}
