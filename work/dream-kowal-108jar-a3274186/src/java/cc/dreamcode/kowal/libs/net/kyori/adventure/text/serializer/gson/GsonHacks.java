package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.Nullable;
import com.google.gson.JsonElement;

final class GsonHacks
{
    private GsonHacks() {
    }
    
    static boolean isNullOrEmpty(@Nullable final JsonElement element) {
        return element == null || element.isJsonNull() || (element.isJsonArray() && element.getAsJsonArray().size() == 0) || (element.isJsonObject() && element.getAsJsonObject().entrySet().isEmpty());
    }
    
    static boolean readBoolean(final JsonReader in) throws IOException {
        final JsonToken peek = in.peek();
        if (peek == JsonToken.BOOLEAN) {
            return in.nextBoolean();
        }
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return Boolean.parseBoolean(in.nextString());
        }
        throw new JsonParseException("Token of type " + (Object)peek + " cannot be interpreted as a boolean");
    }
    
    static String readString(final JsonReader in) throws IOException {
        final JsonToken peek = in.peek();
        if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
            return in.nextString();
        }
        if (peek == JsonToken.BOOLEAN) {
            return String.valueOf(in.nextBoolean());
        }
        throw new JsonParseException("Token of type " + (Object)peek + " cannot be interpreted as a string");
    }
}
