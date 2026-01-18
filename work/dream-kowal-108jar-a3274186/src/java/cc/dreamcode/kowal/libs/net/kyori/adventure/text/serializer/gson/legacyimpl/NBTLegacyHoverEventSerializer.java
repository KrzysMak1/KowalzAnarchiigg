package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.legacyimpl;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;

public interface NBTLegacyHoverEventSerializer extends LegacyHoverEventSerializer
{
    @NotNull
    default LegacyHoverEventSerializer get() {
        return NBTLegacyHoverEventSerializerImpl.INSTANCE;
    }
}
