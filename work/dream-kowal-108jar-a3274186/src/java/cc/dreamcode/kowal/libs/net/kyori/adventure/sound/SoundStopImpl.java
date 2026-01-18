package cc.dreamcode.kowal.libs.net.kyori.adventure.sound;

import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl implements SoundStop
{
    static final SoundStop ALL;
    private final Sound.Source source;
    
    SoundStopImpl(final Sound.Source source) {
        this.source = source;
    }
    
    @Override
    public Sound.Source source() {
        return this.source;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SoundStopImpl)) {
            return false;
        }
        final SoundStopImpl that = (SoundStopImpl)other;
        return Objects.equals((Object)this.sound(), (Object)that.sound()) && Objects.equals((Object)this.source, (Object)that.source);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode((Object)this.sound());
        result = 31 * result + Objects.hashCode((Object)this.source);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("name", this.sound()), ExaminableProperty.of("source", this.source) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static {
        ALL = new SoundStopImpl() {
            @Nullable
            @Override
            public Key sound() {
                return null;
            }
        };
    }
}
