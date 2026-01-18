package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface EntityNBTComponent extends NBTComponent<EntityNBTComponent, EntityNBTComponent.Builder>, ScopedComponent<EntityNBTComponent>
{
    @NotNull
    String selector();
    
    @Contract(pure = true)
    @NotNull
    EntityNBTComponent selector(@NotNull final String selector);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object)ExaminableProperty.of("selector", this.selector())), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends NBTComponentBuilder<EntityNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder selector(@NotNull final String selector);
    }
}
