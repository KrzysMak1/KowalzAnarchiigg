package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;

final class TextColorWrapper
{
    @Nullable
    final TextColor color;
    @Nullable
    final TextDecoration decoration;
    final boolean reset;
    
    TextColorWrapper(@Nullable final TextColor color, @Nullable final TextDecoration decoration, final boolean reset) {
        this.color = color;
        this.decoration = decoration;
        this.reset = reset;
    }
    
    static final class Serializer extends TypeAdapter<TextColorWrapper>
    {
        static final Serializer INSTANCE;
        
        private Serializer() {
        }
        
        public void write(final JsonWriter out, final TextColorWrapper value) {
            throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
        }
        
        public TextColorWrapper read(final JsonReader in) throws IOException {
            final String input = in.nextString();
            final TextColor color = TextColorSerializer.fromString(input);
            final TextDecoration decoration = TextDecoration.NAMES.value(input);
            final boolean reset = decoration == null && input.equals((Object)"reset");
            if (color == null && decoration == null && !reset) {
                throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
            }
            return new TextColorWrapper(color, decoration, reset);
        }
        
        static {
            INSTANCE = new Serializer();
        }
    }
}
