package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.gson.stream.JsonReader;
import java.util.Locale;
import java.io.IOException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.NamedTextColor;
import com.google.gson.stream.JsonWriter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import com.google.gson.TypeAdapter;

final class TextColorSerializer extends TypeAdapter<TextColor>
{
    static final TypeAdapter<TextColor> INSTANCE;
    static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR;
    private final boolean downsampleColor;
    
    private TextColorSerializer(final boolean downsampleColor) {
        this.downsampleColor = downsampleColor;
    }
    
    public void write(final JsonWriter out, final TextColor value) throws IOException {
        if (value instanceof NamedTextColor) {
            out.value((String)NamedTextColor.NAMES.key((NamedTextColor)value));
        }
        else if (this.downsampleColor) {
            out.value((String)NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
        }
        else {
            out.value(asUpperCaseHexString(value));
        }
    }
    
    private static String asUpperCaseHexString(final TextColor color) {
        return String.format(Locale.ROOT, "%c%06X", new Object[] { '#', color.value() });
    }
    
    @Nullable
    public TextColor read(final JsonReader in) throws IOException {
        final TextColor color = fromString(in.nextString());
        if (color == null) {
            return null;
        }
        return this.downsampleColor ? NamedTextColor.nearestTo(color) : color;
    }
    
    @Nullable
    static TextColor fromString(@NotNull final String value) {
        if (value.startsWith("#")) {
            return TextColor.fromHexString(value);
        }
        return NamedTextColor.NAMES.value(value);
    }
    
    static {
        INSTANCE = new TextColorSerializer(false).nullSafe();
        DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();
    }
}
