package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import java.util.Iterator;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.Spliterator;
import java.util.PrimitiveIterator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface LongArrayBinaryTag extends ArrayBinaryTag, Iterable<Long>
{
    @NotNull
    default LongArrayBinaryTag longArrayBinaryTag(final long... value) {
        return new LongArrayBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default LongArrayBinaryTag of(final long... value) {
        return new LongArrayBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<LongArrayBinaryTag> type() {
        return BinaryTagTypes.LONG_ARRAY;
    }
    
    long[] value();
    
    int size();
    
    long get(final int index);
    
    PrimitiveIterator.OfLong iterator();
    
    Spliterator.OfLong spliterator();
    
    @NotNull
    LongStream stream();
    
    void forEachLong(@NotNull final LongConsumer action);
}
