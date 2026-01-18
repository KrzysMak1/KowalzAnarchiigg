package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.nbt;

import java.util.Iterator;
import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import java.util.Set;
import java.util.Map;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ClassUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.VersionUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ItemNbtLegacy implements ItemNbt
{
    private final Method asNMSCopyMethod;
    private final Method asBukkitCopyMethod;
    private final Method hasItemTagMethod;
    private final Method getItemTagMethod;
    private final Method setItemTagMethod;
    private final Method getItemKeySet;
    private final Method getItemStringMethod;
    private final Method setItemStringMethod;
    private final Constructor<?> nbtCompoundConstructor;
    
    public ItemNbtLegacy() {
        final String version = (String)VersionUtil.getStringVersion().orElseThrow(() -> new RuntimeException("Cannot resolve nms version"));
        final Class<?> craftItemStackClass = (Class<?>)ClassUtil.getClass("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack").orElseThrow(() -> new RuntimeException("Cannot resolve craftbukkit CraftItemStack class"));
        final Class<?> itemStackClass = (Class<?>)ClassUtil.getClass("net.minecraft.server." + version + ".ItemStack").orElseThrow(() -> new RuntimeException("Cannot resolve minecraft-server ItemStack class"));
        final Class<?> nbtTagCompoundClass = (Class<?>)ClassUtil.getClass("net.minecraft.server." + version + ".NBTTagCompound").orElseThrow(() -> new RuntimeException("Cannot resolve minecraft-server NBTTagCompound class"));
        try {
            this.asNMSCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            this.asBukkitCopyMethod = craftItemStackClass.getMethod("asBukkitCopy", itemStackClass);
            this.hasItemTagMethod = itemStackClass.getMethod("hasTag", (Class<?>[])new Class[0]);
            this.getItemTagMethod = itemStackClass.getMethod("getTag", (Class<?>[])new Class[0]);
            this.setItemTagMethod = itemStackClass.getMethod("setTag", nbtTagCompoundClass);
            this.getItemKeySet = nbtTagCompoundClass.getMethod("c", (Class<?>[])new Class[0]);
            this.getItemStringMethod = nbtTagCompoundClass.getMethod("getString", String.class);
            this.setItemStringMethod = nbtTagCompoundClass.getMethod("setString", String.class, String.class);
            this.nbtCompoundConstructor = nbtTagCompoundClass.getDeclaredConstructor((Class<?>[])new Class[0]);
        }
        catch (final NoSuchMethodException e) {
            throw new RuntimeException("Cannot find method/constuctor", (Throwable)e);
        }
    }
    
    @Override
    public Map<String, String> getValues(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (itemStack == null) {
            throw new NullPointerException("itemStack is marked non-null but is null");
        }
        return this.getValues(itemStack);
    }
    
    @Override
    public Map<String, String> getValues(@NonNull final ItemStack itemStack) {
        try {
            if (itemStack == null) {
                throw new NullPointerException("itemStack is marked non-null but is null");
            }
            final Object nmsCopy = this.asNMSCopyMethod.invoke((Object)null, new Object[] { itemStack });
            final Object itemCompound = this.getItemCompound(nmsCopy);
            final Set<String> keys = (Set<String>)this.getItemKeySet.invoke(itemCompound, new Object[0]);
            final MapBuilder<String, String> mapBuilder = new MapBuilder<String, String>();
            for (final String key : keys) {
                Optional.ofNullable((Object)this.getItemStringMethod.invoke(itemCompound, new Object[] { key })).ifPresent(s -> mapBuilder.put(key, s));
            }
            return mapBuilder.build();
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public ItemStack setValue(@NonNull final Plugin plugin, @NonNull final ItemStack itemStack, @NonNull final String key, @NonNull final String value) {
        try {
            if (plugin == null) {
                throw new NullPointerException("plugin is marked non-null but is null");
            }
            if (itemStack == null) {
                throw new NullPointerException("itemStack is marked non-null but is null");
            }
            if (key == null) {
                throw new NullPointerException("key is marked non-null but is null");
            }
            if (value == null) {
                throw new NullPointerException("value is marked non-null but is null");
            }
            final Object nmsCopy = this.asNMSCopyMethod.invoke((Object)null, new Object[] { itemStack });
            final Object itemCompound = this.getItemCompound(nmsCopy);
            this.setItemStringMethod.invoke(itemCompound, new Object[] { key, value });
            this.setItemTagMethod.invoke(nmsCopy, new Object[] { itemCompound });
            return (ItemStack)this.asBukkitCopyMethod.invoke((Object)null, new Object[] { nmsCopy });
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private Object getItemCompound(@NonNull final Object nmsCopy) {
        try {
            if (nmsCopy == null) {
                throw new NullPointerException("nmsCopy is marked non-null but is null");
            }
            return ((boolean)this.hasItemTagMethod.invoke(nmsCopy, new Object[0])) ? this.getItemTagMethod.invoke(nmsCopy, new Object[0]) : this.nbtCompoundConstructor.newInstance(new Object[0]);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
}
