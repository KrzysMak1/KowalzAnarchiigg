package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.function.BiFunction;

final class DataComponentValueConversionImpl<I, O> implements DataComponentValueConverterRegistry.Conversion<I, O>
{
    private final Class<I> source;
    private final Class<O> destination;
    private final BiFunction<Key, I, O> conversion;
    
    DataComponentValueConversionImpl(@NotNull final Class<I> source, @NotNull final Class<O> destination, @NotNull final BiFunction<Key, I, O> conversion) {
        this.source = source;
        this.destination = destination;
        this.conversion = conversion;
    }
    
    @NotNull
    @Override
    public Class<I> source() {
        return this.source;
    }
    
    @NotNull
    @Override
    public Class<O> destination() {
        return this.destination;
    }
    
    @NotNull
    @Override
    public O convert(@NotNull final Key key, @NotNull final I input) {
        return (O)this.conversion.apply((Object)Objects.requireNonNull((Object)key, "key"), Objects.requireNonNull((Object)input, "input"));
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("source", this.source), ExaminableProperty.of("destination", this.destination), ExaminableProperty.of("conversion", this.conversion) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final DataComponentValueConversionImpl<?, ?> that = (DataComponentValueConversionImpl<?, ?>)other;
        return Objects.equals((Object)this.source, (Object)that.source) && Objects.equals((Object)this.destination, (Object)that.destination) && Objects.equals((Object)this.conversion, (Object)that.conversion);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.source, this.destination, this.conversion });
    }
}
