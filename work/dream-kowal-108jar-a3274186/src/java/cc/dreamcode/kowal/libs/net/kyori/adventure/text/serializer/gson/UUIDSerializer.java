package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import cc.dreamcode.kowal.libs.net.kyori.option.OptionState;
import java.util.UUID;
import com.google.gson.TypeAdapter;

final class UUIDSerializer extends TypeAdapter<UUID>
{
    private final boolean emitIntArray;
    
    static TypeAdapter<UUID> uuidSerializer(final OptionState features) {
        return (TypeAdapter<UUID>)new UUIDSerializer(features.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY)).nullSafe();
    }
    
    private UUIDSerializer(final boolean emitIntArray) {
        this.emitIntArray = emitIntArray;
    }
    
    public void write(final JsonWriter out, final UUID value) throws IOException {
        if (this.emitIntArray) {
            final int msb0 = (int)(value.getMostSignificantBits() >> 32);
            final int msb2 = (int)(value.getMostSignificantBits() & 0xFFFFFFFFL);
            final int lsb0 = (int)(value.getLeastSignificantBits() >> 32);
            final int lsb2 = (int)(value.getLeastSignificantBits() & 0xFFFFFFFFL);
            out.beginArray().value((long)msb0).value((long)msb2).value((long)lsb0).value((long)lsb2).endArray();
        }
        else {
            out.value(value.toString());
        }
    }
    
    public UUID read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            final int msb0 = in.nextInt();
            final int msb2 = in.nextInt();
            final int lsb0 = in.nextInt();
            final int lsb2 = in.nextInt();
            in.endArray();
            return new UUID((long)msb0 << 32 | ((long)msb2 & 0xFFFFFFFFL), (long)lsb0 << 32 | ((long)lsb2 & 0xFFFFFFFFL));
        }
        return UUID.fromString(in.nextString());
    }
}
