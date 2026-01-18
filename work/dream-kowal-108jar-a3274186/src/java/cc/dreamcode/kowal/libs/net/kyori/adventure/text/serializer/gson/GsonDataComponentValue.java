package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import java.util.Objects;
import com.google.gson.JsonNull;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.DataComponentValue;

@ApiStatus.NonExtendable
public interface GsonDataComponentValue extends DataComponentValue
{
    default GsonDataComponentValue gsonDataComponentValue(@NotNull final JsonElement data) {
        if (data instanceof JsonNull) {
            return GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE;
        }
        return new GsonDataComponentValueImpl((JsonElement)Objects.requireNonNull((Object)data, "data"));
    }
    
    @NotNull
    JsonElement element();
}
