package cc.dreamcode.kowal.libs.cc.dreamcode.menu;

import java.util.HashMap;
import lombok.Generated;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.cc.dreamcode.menu.utilities.MenuUtil;
import lombok.NonNull;
import java.util.Map;

public abstract class DreamMenuBuilder<B, T, I>
{
    private final T inventoryType;
    private final String name;
    private final int rows;
    private Map<Integer, I> items;
    
    public DreamMenuBuilder<B, T, I> setItem(final int x, final int z, @NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        this.items.put((Object)MenuUtil.countSlot(x, z), (Object)i);
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> setItem(final int slot, @NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        this.items.put((Object)slot, (Object)i);
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> fillBackground(@NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        for (int slot = 0; slot < this.rows * 9; ++slot) {
            if (!this.items.containsKey((Object)slot)) {
                this.items.put((Object)slot, (Object)i);
            }
        }
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> fillTop(@NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        Stream.of((Object[])new Integer[] { MenuUtil.countSlot(1, 1), MenuUtil.countSlot(1, 2), MenuUtil.countSlot(1, 3), MenuUtil.countSlot(1, 4), MenuUtil.countSlot(1, 5), MenuUtil.countSlot(1, 6), MenuUtil.countSlot(1, 7), MenuUtil.countSlot(1, 8), MenuUtil.countSlot(1, 9) }).forEach(slot -> {
            if (!this.items.containsKey((Object)slot)) {
                this.items.put((Object)slot, i);
            }
        });
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> fillBottom(@NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        Stream.of((Object[])new Integer[] { MenuUtil.countSlot(this.rows, 1), MenuUtil.countSlot(this.rows, 2), MenuUtil.countSlot(this.rows, 3), MenuUtil.countSlot(this.rows, 4), MenuUtil.countSlot(this.rows, 5), MenuUtil.countSlot(this.rows, 6), MenuUtil.countSlot(this.rows, 7), MenuUtil.countSlot(this.rows, 8), MenuUtil.countSlot(this.rows, 9) }).forEach(slot -> {
            if (!this.items.containsKey((Object)slot)) {
                this.items.put((Object)slot, i);
            }
        });
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> fillTopAndBottom(@NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        this.fillTop(i);
        this.fillBottom(i);
        return this;
    }
    
    public DreamMenuBuilder<B, T, I> fillMargin(@NonNull final I i) {
        if (i == null) {
            throw new NullPointerException("i is marked non-null but is null");
        }
        this.fillTop(i);
        this.fillBottom(i);
        for (int row = 2; row < this.rows; ++row) {
            Stream.of((Object[])new Integer[] { MenuUtil.countSlot(row, 1), MenuUtil.countSlot(row, 9) }).forEach(slot -> {
                if (!this.items.containsKey((Object)slot)) {
                    this.items.put((Object)slot, i);
                }
            });
        }
        return this;
    }
    
    public abstract B buildEmpty();
    
    public abstract B buildEmpty(@NonNull final Map<String, Object> p0);
    
    public abstract B buildWithItems();
    
    public abstract B buildWithItems(@NonNull final Map<String, Object> p0);
    
    @Generated
    public T getInventoryType() {
        return this.inventoryType;
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public int getRows() {
        return this.rows;
    }
    
    @Generated
    public Map<Integer, I> getItems() {
        return this.items;
    }
    
    @Generated
    public DreamMenuBuilder(final T inventoryType, final String name, final int rows) {
        this.items = (Map<Integer, I>)new HashMap();
        this.inventoryType = inventoryType;
        this.name = name;
        this.rows = rows;
    }
    
    @Generated
    public DreamMenuBuilder(final T inventoryType, final String name, final int rows, final Map<Integer, I> items) {
        this.items = (Map<Integer, I>)new HashMap();
        this.inventoryType = inventoryType;
        this.name = name;
        this.rows = rows;
        this.items = items;
    }
}
