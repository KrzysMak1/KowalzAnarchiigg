package cc.dreamcode.kowal.libs.cc.dreamcode.menu.adventure.base;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.option.Option;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.utilities.MenuUtil;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.HashMap;
import lombok.NonNull;
import java.util.function.BiConsumer;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.base.DreamMenuPaginated;

public class BukkitMenuPaginated implements DreamMenuPaginated<BukkitMenuPaginated, BukkitMenu, ItemStack, InventoryClickEvent, HumanEntity>
{
    private final BukkitMenu menuPlatform;
    private final List<Integer> storageItemSlots;
    private final Map<Integer, BukkitMenu> bukkitMenuMap;
    private final Map<UUID, Integer> cacheSlotPlayerViewing;
    private BiConsumer<HumanEntity, Integer> nextPagePreEvent;
    private BiConsumer<HumanEntity, Integer> nextPageEvent;
    private BiConsumer<HumanEntity, Integer> nextPagePostEvent;
    private BiConsumer<HumanEntity, Integer> previousPagePreEvent;
    private BiConsumer<HumanEntity, Integer> previousPageEvent;
    private BiConsumer<HumanEntity, Integer> previousPagePostEvent;
    
    public BukkitMenuPaginated(@NonNull final BukkitMenu menuPlatform) {
        this.bukkitMenuMap = (Map<Integer, BukkitMenu>)new HashMap();
        this.cacheSlotPlayerViewing = (Map<UUID, Integer>)new HashMap();
        if (menuPlatform == null) {
            throw new NullPointerException("menuPlatform is marked non-null but is null");
        }
        this.menuPlatform = menuPlatform;
        this.storageItemSlots = (List<Integer>)IntStream.rangeClosed(0, menuPlatform.getSize()).boxed().collect(Collectors.toList());
    }
    
    @Override
    public BukkitMenu getMenuPlatform() {
        return this.menuPlatform;
    }
    
    @Override
    public List<Integer> getStorageItemSlots() {
        return this.storageItemSlots;
    }
    
    @Override
    public Optional<BukkitMenu> getMenuByPage(final int page) {
        return (Optional<BukkitMenu>)Optional.ofNullable((Object)this.bukkitMenuMap.get((Object)page));
    }
    
    @Override
    public Map<Integer, BukkitMenu> getMenuPages() {
        return (Map<Integer, BukkitMenu>)Collections.unmodifiableMap((Map)this.bukkitMenuMap);
    }
    
    @Override
    public int getSize() {
        return this.bukkitMenuMap.size();
    }
    
    @Override
    public List<HumanEntity> getViewers() {
        final List<HumanEntity> viewers = (List<HumanEntity>)new ArrayList();
        final Stream map = this.bukkitMenuMap.values().stream().map(bukkitMenu -> bukkitMenu.getInventory().getViewers());
        final List<HumanEntity> list = viewers;
        Objects.requireNonNull((Object)list);
        map.forEach(list::addAll);
        return viewers;
    }
    
    @Override
    public int getPlayerPage(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        return (int)this.cacheSlotPlayerViewing.getOrDefault((Object)humanEntity.getUniqueId(), (Object)0);
    }
    
    @Override
    public BukkitMenuPaginated setNextPageSlot(final int slot, @NonNull final Consumer<HumanEntity> nextPageReach) {
        if (nextPageReach == null) {
            throw new NullPointerException("nextPageReach is marked non-null but is null");
        }
        this.menuPlatform.getHolder().setActionOnSlot(slot, (Consumer<InventoryClickEvent>)(e -> {
            e.setCancelled(true);
            final HumanEntity humanEntity = e.getWhoClicked();
            final int nextPage = this.getPlayerPage(humanEntity) + 1;
            if (this.nextPagePreEvent != null) {
                this.nextPagePreEvent.accept((Object)humanEntity, (Object)nextPage);
            }
            if (this.getSize() <= nextPage) {
                nextPageReach.accept((Object)humanEntity);
                return;
            }
            if (this.nextPageEvent != null) {
                this.nextPageEvent.accept((Object)humanEntity, (Object)nextPage);
            }
            this.open(nextPage, humanEntity);
            if (this.nextPagePostEvent != null) {
                this.nextPagePostEvent.accept((Object)humanEntity, (Object)nextPage);
            }
        }));
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setNextPageSlot(final int slot, @NonNull final ItemStack itemStack, @NonNull final Consumer<HumanEntity> nextPageReach) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (nextPageReach == null) {
            throw new NullPointerException("nextPageReach is marked non-null but is null");
        }
        this.setNextPageSlot(slot, nextPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setNextPageSlot(final int x, final int z, @NonNull final Consumer<HumanEntity> lastPageReach) {
        if (lastPageReach == null) {
            throw new NullPointerException("lastPageReach is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, lastPageReach);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setNextPageSlot(final int x, final int z, @NonNull final ItemStack itemStack, @NonNull final Consumer<HumanEntity> lastPageReach) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (lastPageReach == null) {
            throw new NullPointerException("lastPageReach is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setNextPageSlot(slot, itemStack, lastPageReach);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setPreviousPageSlot(final int slot, @NonNull final Consumer<HumanEntity> firstPageReach) {
        if (firstPageReach == null) {
            throw new NullPointerException("firstPageReach is marked non-null but is null");
        }
        this.menuPlatform.getHolder().setActionOnSlot(slot, (Consumer<InventoryClickEvent>)(e -> {
            e.setCancelled(true);
            final HumanEntity humanEntity = e.getWhoClicked();
            final int previousPage = this.getPlayerPage(humanEntity) - 1;
            if (this.previousPagePreEvent != null) {
                this.previousPagePreEvent.accept((Object)humanEntity, (Object)previousPage);
            }
            if (0 > previousPage) {
                firstPageReach.accept((Object)humanEntity);
                return;
            }
            if (this.previousPageEvent != null) {
                this.previousPageEvent.accept((Object)humanEntity, (Object)previousPage);
            }
            this.open(previousPage, humanEntity);
            if (this.previousPagePostEvent != null) {
                this.previousPagePostEvent.accept((Object)humanEntity, (Object)previousPage);
            }
        }));
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setPreviousPageSlot(final int slot, @NonNull final ItemStack itemStack, @NonNull final Consumer<HumanEntity> firstPageReach) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (firstPageReach == null) {
            throw new NullPointerException("firstPageReach is marked non-null but is null");
        }
        this.setPreviousPageSlot(slot, firstPageReach);
        this.getMenuPlatform().setItem(slot, itemStack);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setPreviousPageSlot(final int x, final int z, @NonNull final Consumer<HumanEntity> firstPageReach) {
        if (firstPageReach == null) {
            throw new NullPointerException("firstPageReach is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, firstPageReach);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated setPreviousPageSlot(final int x, final int z, @NonNull final ItemStack itemStack, @NonNull final Consumer<HumanEntity> firstPageReach) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (firstPageReach == null) {
            throw new NullPointerException("firstPageReach is marked non-null but is null");
        }
        final int slot = MenuUtil.countSlot(x, z);
        this.setPreviousPageSlot(slot, itemStack, firstPageReach);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull final BukkitMenu bukkitMenu, final int page, @NonNull final ItemStack itemStack, final Consumer<InventoryClickEvent> event) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final int slot = this.addItem(bukkitMenu, itemStack, this.storageItemSlots);
        if (slot != -1) {
            if (event != null) {
                bukkitMenu.getHolder().setActionOnSlot(slot, (Consumer<InventoryClickEvent>)event.andThen(e -> {
                    if (e.isCancelled()) {
                        return;
                    }
                    bukkitMenu.getHolder().removeActionOnSlot(slot);
                }));
            }
            return this;
        }
        final AtomicReference<BukkitMenu> nextMenuRef = (AtomicReference<BukkitMenu>)new AtomicReference((Object)this.bukkitMenuMap.get((Object)(page + 1)));
        if (nextMenuRef.get() == null) {
            nextMenuRef.set((Object)this.getMenuPlatform().cloneMenu(page + 2));
            this.bukkitMenuMap.put((Object)(page + 1), (Object)nextMenuRef.get());
        }
        final BukkitMenu nextMenu = (BukkitMenu)nextMenuRef.get();
        final int nextMenuSlot = this.addItem(nextMenu, itemStack, this.storageItemSlots);
        if (nextMenuSlot != -1 && event != null) {
            nextMenu.getHolder().setActionOnSlot(nextMenuSlot, (Consumer<InventoryClickEvent>)event.andThen(e -> {
                if (e.isCancelled()) {
                    return;
                }
                nextMenu.getHolder().removeActionOnSlot(nextMenuSlot);
            }));
        }
        return this;
    }
    
    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull final ItemStack itemStack, final Consumer<InventoryClickEvent> event) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        final Optional<Map.Entry<Integer, BukkitMenu>> optionalMenu = (Optional<Map.Entry<Integer, BukkitMenu>>)this.bukkitMenuMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getKey));
        if (optionalMenu.isPresent()) {
            final int page = (int)((Map.Entry)optionalMenu.get()).getKey();
            final BukkitMenu bukkitMenu = (BukkitMenu)((Map.Entry)optionalMenu.get()).getValue();
            this.addStorageItem(bukkitMenu, page, itemStack, event);
        }
        else {
            final BukkitMenu bukkitMenu2 = this.getMenuPlatform().cloneMenu(1);
            this.bukkitMenuMap.put((Object)0, (Object)bukkitMenu2);
            this.addStorageItem(bukkitMenu2, 0, itemStack, event);
        }
        return this;
    }
    
    @Override
    public BukkitMenuPaginated addStorageItem(@NonNull final ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        this.addStorageItem(itemStack, (Consumer<InventoryClickEvent>)null);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated addStorageItems(@NonNull final List<ItemStack> list, final Consumer<InventoryClickEvent> event) {
        if (list == null) {
            throw new NullPointerException("list is marked non-null but is null");
        }
        list.forEach(itemStack -> this.addStorageItem(itemStack, (Consumer<InventoryClickEvent>)event));
        return this;
    }
    
    @Override
    public BukkitMenuPaginated addStorageItems(@NonNull final List<ItemStack> list) {
        if (list == null) {
            throw new NullPointerException("list is marked non-null but is null");
        }
        this.addStorageItems(list, (Consumer<InventoryClickEvent>)null);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated open(final int page, @NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = (BukkitMenu)this.bukkitMenuMap.get((Object)page);
        if (bukkitMenu == null) {
            this.openFirstPage(humanEntity);
            return this;
        }
        this.cacheSlotPlayerViewing.put((Object)humanEntity.getUniqueId(), (Object)page);
        bukkitMenu.getHolder().open(humanEntity);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated openPage(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        this.open(this.getPlayerPage(humanEntity), humanEntity);
        return this;
    }
    
    @Override
    public BukkitMenuPaginated openFirstPage(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        Option.ofOptional((java.util.Optional<Object>)this.bukkitMenuMap.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getKey)).ifPresentOrElse((java.util.function.Consumer<? super Object>)(page -> this.open((int)page, humanEntity)), () -> {
            this.bukkitMenuMap.put((Object)0, (Object)this.getMenuPlatform().cloneMenu(1));
            this.openFirstPage(humanEntity);
        });
        return this;
    }
    
    @Override
    public BukkitMenuPaginated openLastPage(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        this.bukkitMenuMap.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getKey).ifPresent(page -> this.open((int)page, humanEntity));
        return this;
    }
    
    @Override
    public BukkitMenuPaginated dispose() {
        this.bukkitMenuMap.values().forEach(BukkitMenu::dispose);
        this.bukkitMenuMap.clear();
        this.cacheSlotPlayerViewing.clear();
        return this;
    }
    
    private int addItem(@NonNull final BukkitMenu bukkitMenu, @NonNull final ItemStack itemStack, @NonNull final List<Integer> applySlots) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        if (applySlots == null) {
            throw new NullPointerException("applySlots is marked non-null but is null");
        }
        for (int slot = 0; slot < bukkitMenu.getSize(); ++slot) {
            if (applySlots.contains((Object)slot)) {
                if (bukkitMenu.getInventory().getItem(slot) == null) {
                    bukkitMenu.setItem(slot, itemStack);
                    return slot;
                }
            }
        }
        return -1;
    }
    
    @Generated
    public BukkitMenuPaginated(final BukkitMenu menuPlatform, final List<Integer> storageItemSlots) {
        this.bukkitMenuMap = (Map<Integer, BukkitMenu>)new HashMap();
        this.cacheSlotPlayerViewing = (Map<UUID, Integer>)new HashMap();
        this.menuPlatform = menuPlatform;
        this.storageItemSlots = storageItemSlots;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getNextPagePreEvent() {
        return this.nextPagePreEvent;
    }
    
    @Generated
    public void setNextPagePreEvent(final BiConsumer<HumanEntity, Integer> nextPagePreEvent) {
        this.nextPagePreEvent = nextPagePreEvent;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getNextPageEvent() {
        return this.nextPageEvent;
    }
    
    @Generated
    public void setNextPageEvent(final BiConsumer<HumanEntity, Integer> nextPageEvent) {
        this.nextPageEvent = nextPageEvent;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getNextPagePostEvent() {
        return this.nextPagePostEvent;
    }
    
    @Generated
    public void setNextPagePostEvent(final BiConsumer<HumanEntity, Integer> nextPagePostEvent) {
        this.nextPagePostEvent = nextPagePostEvent;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getPreviousPagePreEvent() {
        return this.previousPagePreEvent;
    }
    
    @Generated
    public void setPreviousPagePreEvent(final BiConsumer<HumanEntity, Integer> previousPagePreEvent) {
        this.previousPagePreEvent = previousPagePreEvent;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getPreviousPageEvent() {
        return this.previousPageEvent;
    }
    
    @Generated
    public void setPreviousPageEvent(final BiConsumer<HumanEntity, Integer> previousPageEvent) {
        this.previousPageEvent = previousPageEvent;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Integer> getPreviousPagePostEvent() {
        return this.previousPagePostEvent;
    }
    
    @Generated
    public void setPreviousPagePostEvent(final BiConsumer<HumanEntity, Integer> previousPagePostEvent) {
        this.previousPagePostEvent = previousPagePostEvent;
    }
}
