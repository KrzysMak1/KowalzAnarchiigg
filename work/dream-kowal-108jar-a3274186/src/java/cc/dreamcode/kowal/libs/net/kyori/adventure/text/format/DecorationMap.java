package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import java.util.Spliterators;
import java.util.AbstractCollection;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Set;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import org.jetbrains.annotations.Unmodifiable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import java.util.AbstractMap;

@Unmodifiable
final class DecorationMap extends AbstractMap<TextDecoration, TextDecoration.State> implements Examinable
{
    static final TextDecoration[] DECORATIONS;
    private static final TextDecoration.State[] STATES;
    private static final int MAP_SIZE;
    private static final TextDecoration.State[] EMPTY_STATE_ARRAY;
    static final DecorationMap EMPTY;
    private static final KeySet KEY_SET;
    private final int bitSet;
    private volatile EntrySet entrySet;
    private volatile Values values;
    
    static DecorationMap fromMap(final Map<TextDecoration, TextDecoration.State> decorationMap) {
        if (decorationMap instanceof DecorationMap) {
            return (DecorationMap)decorationMap;
        }
        int bitSet = 0;
        for (final TextDecoration decoration : DecorationMap.DECORATIONS) {
            bitSet |= ((TextDecoration.State)decorationMap.getOrDefault((Object)decoration, (Object)TextDecoration.State.NOT_SET)).ordinal() * offset(decoration);
        }
        return withBitSet(bitSet);
    }
    
    static DecorationMap merge(final Map<TextDecoration, TextDecoration.State> first, final Map<TextDecoration, TextDecoration.State> second) {
        int bitSet = 0;
        for (final TextDecoration decoration : DecorationMap.DECORATIONS) {
            bitSet |= ((TextDecoration.State)first.getOrDefault((Object)decoration, (Object)second.getOrDefault((Object)decoration, (Object)TextDecoration.State.NOT_SET))).ordinal() * offset(decoration);
        }
        return withBitSet(bitSet);
    }
    
    private static DecorationMap withBitSet(final int bitSet) {
        return (bitSet == 0) ? DecorationMap.EMPTY : new DecorationMap(bitSet);
    }
    
    private static int offset(final TextDecoration decoration) {
        return 1 << decoration.ordinal() * 2;
    }
    
    private DecorationMap(final int bitSet) {
        this.entrySet = null;
        this.values = null;
        this.bitSet = bitSet;
    }
    
    @NotNull
    public DecorationMap with(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull((Object)state, "state");
        Objects.requireNonNull((Object)decoration, "decoration");
        final int offset = offset(decoration);
        return withBitSet((this.bitSet & ~(3 * offset)) | state.ordinal() * offset);
    }
    
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Arrays.stream((Object[])DecorationMap.DECORATIONS).map(decoration -> ExaminableProperty.of(decoration.toString(), this.get(decoration)));
    }
    
    public TextDecoration.State get(final Object o) {
        if (o instanceof TextDecoration) {
            return DecorationMap.STATES[this.bitSet >> ((TextDecoration)o).ordinal() * 2 & 0x3];
        }
        return null;
    }
    
    public boolean containsKey(final Object key) {
        return key instanceof TextDecoration;
    }
    
    public int size() {
        return DecorationMap.MAP_SIZE;
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    @NotNull
    public Set<Map.Entry<TextDecoration, TextDecoration.State>> entrySet() {
        if (this.entrySet == null) {
            synchronized (this) {
                if (this.entrySet == null) {
                    this.entrySet = new EntrySet();
                }
            }
        }
        return (Set<Map.Entry<TextDecoration, TextDecoration.State>>)this.entrySet;
    }
    
    @NotNull
    public Set<TextDecoration> keySet() {
        return (Set<TextDecoration>)DecorationMap.KEY_SET;
    }
    
    @NotNull
    public Collection<TextDecoration.State> values() {
        if (this.values == null) {
            synchronized (this) {
                if (this.values == null) {
                    this.values = new Values();
                }
            }
        }
        return (Collection<TextDecoration.State>)this.values;
    }
    
    public boolean equals(final Object other) {
        return other == this || (other != null && other.getClass() == DecorationMap.class && this.bitSet == ((DecorationMap)other).bitSet);
    }
    
    public int hashCode() {
        return this.bitSet;
    }
    
    static {
        DECORATIONS = TextDecoration.values();
        STATES = TextDecoration.State.values();
        MAP_SIZE = DecorationMap.DECORATIONS.length;
        EMPTY_STATE_ARRAY = new TextDecoration.State[0];
        EMPTY = new DecorationMap(0);
        KEY_SET = new KeySet();
    }
    
    final class EntrySet extends AbstractSet<Map.Entry<TextDecoration, TextDecoration.State>>
    {
        @NotNull
        public Iterator<Map.Entry<TextDecoration, TextDecoration.State>> iterator() {
            return (Iterator<Map.Entry<TextDecoration, TextDecoration.State>>)new Iterator<Map.Entry<TextDecoration, TextDecoration.State>>() {
                private final Iterator<TextDecoration> decorations = DecorationMap.KEY_SET.iterator();
                private final Iterator<TextDecoration.State> states = DecorationMap.this.values().iterator();
                
                public boolean hasNext() {
                    return this.decorations.hasNext() && this.states.hasNext();
                }
                
                public Map.Entry<TextDecoration, TextDecoration.State> next() {
                    if (this.hasNext()) {
                        return (Map.Entry<TextDecoration, TextDecoration.State>)new AbstractMap.SimpleImmutableEntry((Object)this.decorations.next(), (Object)this.states.next());
                    }
                    throw new NoSuchElementException();
                }
            };
        }
        
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
    
    final class Values extends AbstractCollection<TextDecoration.State>
    {
        @NotNull
        public Iterator<TextDecoration.State> iterator() {
            return (Iterator<TextDecoration.State>)Spliterators.iterator(Arrays.spliterator((Object[])this.toArray(DecorationMap.EMPTY_STATE_ARRAY)));
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Object[] toArray() {
            final Object[] states = new Object[DecorationMap.MAP_SIZE];
            for (int i = 0; i < DecorationMap.MAP_SIZE; ++i) {
                states[i] = DecorationMap.this.get(DecorationMap.DECORATIONS[i]);
            }
            return states;
        }
        
        public <T> T[] toArray(final T[] dest) {
            if (dest.length < DecorationMap.MAP_SIZE) {
                return (T[])Arrays.copyOf(this.toArray(), DecorationMap.MAP_SIZE, (Class)dest.getClass());
            }
            System.arraycopy((Object)this.toArray(), 0, (Object)dest, 0, DecorationMap.MAP_SIZE);
            if (dest.length > DecorationMap.MAP_SIZE) {
                dest[DecorationMap.MAP_SIZE] = null;
            }
            return dest;
        }
        
        public boolean contains(final Object o) {
            return o instanceof TextDecoration.State && super.contains(o);
        }
        
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
    
    static final class KeySet extends AbstractSet<TextDecoration>
    {
        public boolean contains(final Object o) {
            return o instanceof TextDecoration;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Object[] toArray() {
            return Arrays.copyOf((Object[])DecorationMap.DECORATIONS, DecorationMap.MAP_SIZE, (Class)Object[].class);
        }
        
        public <T> T[] toArray(final T[] dest) {
            if (dest.length < DecorationMap.MAP_SIZE) {
                return (T[])Arrays.copyOf((Object[])DecorationMap.DECORATIONS, DecorationMap.MAP_SIZE, (Class)dest.getClass());
            }
            System.arraycopy((Object)DecorationMap.DECORATIONS, 0, (Object)dest, 0, DecorationMap.MAP_SIZE);
            if (dest.length > DecorationMap.MAP_SIZE) {
                dest[DecorationMap.MAP_SIZE] = null;
            }
            return dest;
        }
        
        @NotNull
        public Iterator<TextDecoration> iterator() {
            return (Iterator<TextDecoration>)Spliterators.iterator(Arrays.spliterator((Object[])DecorationMap.DECORATIONS));
        }
        
        public int size() {
            return DecorationMap.MAP_SIZE;
        }
    }
}
