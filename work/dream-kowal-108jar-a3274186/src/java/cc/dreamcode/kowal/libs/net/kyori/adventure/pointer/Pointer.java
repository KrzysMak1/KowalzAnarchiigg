package cc.dreamcode.kowal.libs.net.kyori.adventure.pointer;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface Pointer<V> extends Examinable
{
    @NotNull
    default <V> Pointer<V> pointer(@NotNull final Class<V> type, @NotNull final Key key) {
        return new PointerImpl<V>(type, key);
    }
    
    @NotNull
    Class<V> type();
    
    @NotNull
    Key key();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("type", this.type()), ExaminableProperty.of("key", this.key()) });
    }
}
