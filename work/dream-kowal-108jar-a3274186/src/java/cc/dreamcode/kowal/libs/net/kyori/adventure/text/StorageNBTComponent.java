package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;

public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, StorageNBTComponent.Builder>, ScopedComponent<StorageNBTComponent>
{
    @NotNull
    Key storage();
    
    @Contract(pure = true)
    @NotNull
    StorageNBTComponent storage(@NotNull final Key storage);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object)ExaminableProperty.of("storage", this.storage())), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends NBTComponentBuilder<StorageNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder storage(@NotNull final Key storage);
    }
}
