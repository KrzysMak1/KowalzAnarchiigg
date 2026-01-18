package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BlockNBTComponent;
import com.google.gson.TypeAdapter;

final class BlockNBTComponentPosSerializer extends TypeAdapter<BlockNBTComponent.Pos>
{
    static final TypeAdapter<BlockNBTComponent.Pos> INSTANCE;
    
    private BlockNBTComponentPosSerializer() {
    }
    
    public BlockNBTComponent.Pos read(final JsonReader in) throws IOException {
        final String string = in.nextString();
        try {
            return BlockNBTComponent.Pos.fromString(string);
        }
        catch (final IllegalArgumentException ex) {
            throw new JsonParseException("Don't know how to turn " + string + " into a Position");
        }
    }
    
    public void write(final JsonWriter out, final BlockNBTComponent.Pos value) throws IOException {
        out.value(value.asString());
    }
    
    static {
        INSTANCE = new BlockNBTComponentPosSerializer().nullSafe();
    }
}
