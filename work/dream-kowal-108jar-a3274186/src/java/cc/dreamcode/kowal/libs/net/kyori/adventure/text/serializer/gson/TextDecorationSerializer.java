package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import com.google.gson.TypeAdapter;

final class TextDecorationSerializer
{
    static final TypeAdapter<TextDecoration> INSTANCE;
    
    private TextDecorationSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.strict("text decoration", TextDecoration.NAMES);
    }
}
