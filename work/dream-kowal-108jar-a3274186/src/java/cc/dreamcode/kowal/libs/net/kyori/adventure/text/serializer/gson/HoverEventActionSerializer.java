package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import com.google.gson.TypeAdapter;

final class HoverEventActionSerializer
{
    static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;
    
    private HoverEventActionSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.lenient("hover action", HoverEvent.Action.NAMES);
    }
}
