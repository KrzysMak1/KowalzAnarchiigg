package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import java.util.Objects;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"CompoundBinaryTag[length=\" + this.tags.size() + \"]\"", childrenArray = "this.tags.entrySet().toArray()", hasChildren = "!this.tags.isEmpty()")
final class CompoundBinaryTagImpl extends AbstractBinaryTag implements CompoundBinaryTag
{
    static final CompoundBinaryTag EMPTY;
    private final Map<String, BinaryTag> tags;
    private final int hashCode;
    
    CompoundBinaryTagImpl(final Map<String, BinaryTag> tags) {
        this.tags = (Map<String, BinaryTag>)Collections.unmodifiableMap((Map)tags);
        this.hashCode = tags.hashCode();
    }
    
    public boolean contains(@NotNull final String key, @NotNull final BinaryTagType<?> type) {
        final BinaryTag tag = (BinaryTag)this.tags.get((Object)key);
        return tag != null && type.test(tag.type());
    }
    
    @NotNull
    @Override
    public Set<String> keySet() {
        return (Set<String>)Collections.unmodifiableSet(this.tags.keySet());
    }
    
    @Nullable
    @Override
    public BinaryTag get(final String key) {
        return (BinaryTag)this.tags.get((Object)key);
    }
    
    @Override
    public int size() {
        return this.tags.size();
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final String key, @NotNull final BinaryTag tag) {
        return this.edit((Consumer<Map<String, BinaryTag>>)(map -> map.put((Object)key, (Object)tag)));
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final CompoundBinaryTag tag) {
        return this.edit((Consumer<Map<String, BinaryTag>>)(map -> {
            for (final String key : tag.keySet()) {
                map.put((Object)key, (Object)tag.get(key));
            }
        }));
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag put(@NotNull final Map<String, ? extends BinaryTag> tags) {
        return this.edit((Consumer<Map<String, BinaryTag>>)(map -> map.putAll(tags)));
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag remove(@NotNull final String key, @Nullable final Consumer<? super BinaryTag> removed) {
        if (!this.tags.containsKey((Object)key)) {
            return this;
        }
        return this.edit((Consumer<Map<String, BinaryTag>>)(map -> {
            final BinaryTag tag = (BinaryTag)map.remove((Object)key);
            if (removed != null) {
                removed.accept((Object)tag);
            }
        }));
    }
    
    @Override
    public byte getByte(@NotNull final String key, final byte defaultValue) {
        if (this.contains(key, BinaryTagTypes.BYTE)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).byteValue();
        }
        return defaultValue;
    }
    
    @Override
    public short getShort(@NotNull final String key, final short defaultValue) {
        if (this.contains(key, BinaryTagTypes.SHORT)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).shortValue();
        }
        return defaultValue;
    }
    
    @Override
    public int getInt(@NotNull final String key, final int defaultValue) {
        if (this.contains(key, BinaryTagTypes.INT)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).intValue();
        }
        return defaultValue;
    }
    
    @Override
    public long getLong(@NotNull final String key, final long defaultValue) {
        if (this.contains(key, BinaryTagTypes.LONG)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).longValue();
        }
        return defaultValue;
    }
    
    @Override
    public float getFloat(@NotNull final String key, final float defaultValue) {
        if (this.contains(key, BinaryTagTypes.FLOAT)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).floatValue();
        }
        return defaultValue;
    }
    
    @Override
    public double getDouble(@NotNull final String key, final double defaultValue) {
        if (this.contains(key, BinaryTagTypes.DOUBLE)) {
            return ((NumberBinaryTag)this.tags.get((Object)key)).doubleValue();
        }
        return defaultValue;
    }
    
    @Override
    public byte[] getByteArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
            return ((ByteArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return new byte[0];
    }
    
    @Override
    public byte[] getByteArray(@NotNull final String key, final byte[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
            return ((ByteArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public String getString(@NotNull final String key, @NotNull final String defaultValue) {
        if (this.contains(key, BinaryTagTypes.STRING)) {
            return ((StringBinaryTag)this.tags.get((Object)key)).value();
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public ListBinaryTag getList(@NotNull final String key, @NotNull final ListBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.LIST)) {
            return (ListBinaryTag)this.tags.get((Object)key);
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType, @NotNull final ListBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.LIST)) {
            final ListBinaryTag tag = (ListBinaryTag)this.tags.get((Object)key);
            if (expectedType.test(tag.elementType())) {
                return tag;
            }
        }
        return defaultValue;
    }
    
    @NotNull
    @Override
    public CompoundBinaryTag getCompound(@NotNull final String key, @NotNull final CompoundBinaryTag defaultValue) {
        if (this.contains(key, BinaryTagTypes.COMPOUND)) {
            return (CompoundBinaryTag)this.tags.get((Object)key);
        }
        return defaultValue;
    }
    
    @Override
    public int[] getIntArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
            return ((IntArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return new int[0];
    }
    
    @Override
    public int[] getIntArray(@NotNull final String key, final int[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
            return ((IntArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return defaultValue;
    }
    
    @Override
    public long[] getLongArray(@NotNull final String key) {
        if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
            return ((LongArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return new long[0];
    }
    
    @Override
    public long[] getLongArray(@NotNull final String key, final long[] defaultValue) {
        if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
            return ((LongArrayBinaryTag)this.tags.get((Object)key)).value();
        }
        return defaultValue;
    }
    
    private CompoundBinaryTag edit(final Consumer<Map<String, BinaryTag>> consumer) {
        final Map<String, BinaryTag> tags = (Map<String, BinaryTag>)new HashMap((Map)this.tags);
        consumer.accept((Object)tags);
        return new CompoundBinaryTagImpl((Map<String, BinaryTag>)new HashMap((Map)tags));
    }
    
    @Override
    public boolean equals(final Object that) {
        return this == that || (that instanceof CompoundBinaryTagImpl && this.tags.equals((Object)((CompoundBinaryTagImpl)that).tags));
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("tags", this.tags));
    }
    
    @NotNull
    public Iterator<Map.Entry<String, ? extends BinaryTag>> iterator() {
        return (Iterator<Map.Entry<String, ? extends BinaryTag>>)this.tags.entrySet().iterator();
    }
    
    public void forEach(@NotNull final Consumer<? super Map.Entry<String, ? extends BinaryTag>> action) {
        this.tags.entrySet().forEach((Consumer)Objects.requireNonNull((Object)action, "action"));
    }
    
    static {
        EMPTY = new CompoundBinaryTagImpl((Map<String, BinaryTag>)Collections.emptyMap());
    }
}
