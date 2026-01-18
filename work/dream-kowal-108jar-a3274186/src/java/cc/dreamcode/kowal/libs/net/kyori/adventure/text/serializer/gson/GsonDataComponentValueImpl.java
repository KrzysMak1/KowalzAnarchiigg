package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.DataComponentValue;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonElement;

class GsonDataComponentValueImpl implements GsonDataComponentValue
{
    private final JsonElement element;
    
    GsonDataComponentValueImpl(@NotNull final JsonElement element) {
        this.element = element;
    }
    
    @NotNull
    @Override
    public JsonElement element() {
        return this.element;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("element", this.element));
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final GsonDataComponentValueImpl that = (GsonDataComponentValueImpl)other;
        return Objects.equals((Object)this.element, (Object)that.element);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode((Object)this.element);
    }
    
    static final class RemovedGsonComponentValueImpl extends GsonDataComponentValueImpl implements DataComponentValue.Removed
    {
        static final RemovedGsonComponentValueImpl INSTANCE;
        
        private RemovedGsonComponentValueImpl() {
            super((JsonElement)JsonNull.INSTANCE);
        }
        
        static {
            INSTANCE = new RemovedGsonComponentValueImpl();
        }
    }
}
