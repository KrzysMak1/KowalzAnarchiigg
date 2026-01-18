package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import com.google.gson.TypeAdapter;

final class ClickEventActionSerializer
{
    static final TypeAdapter<ClickEvent.Action> INSTANCE;
    
    private ClickEventActionSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.lenient("click action", ClickEvent.Action.NAMES);
    }
}
