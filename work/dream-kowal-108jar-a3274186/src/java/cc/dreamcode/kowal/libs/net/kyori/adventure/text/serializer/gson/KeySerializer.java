package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import com.google.gson.TypeAdapter;

final class KeySerializer extends TypeAdapter<Key>
{
    static final TypeAdapter<Key> INSTANCE;
    
    private KeySerializer() {
    }
    
    public void write(final JsonWriter out, final Key value) throws IOException {
        out.value(value.asString());
    }
    
    public Key read(final JsonReader in) throws IOException {
        return Key.key(in.nextString());
    }
    
    static {
        INSTANCE = new KeySerializer().nullSafe();
    }
}
