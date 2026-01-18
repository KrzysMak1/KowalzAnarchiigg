package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextFormat;

final class CharacterAndFormatImpl implements CharacterAndFormat
{
    private final char character;
    private final TextFormat format;
    private final boolean caseInsensitive;
    
    CharacterAndFormatImpl(final char character, @NotNull final TextFormat format, final boolean caseInsensitive) {
        this.character = character;
        this.format = (TextFormat)Objects.requireNonNull((Object)format, "format");
        this.caseInsensitive = caseInsensitive;
    }
    
    @Override
    public char character() {
        return this.character;
    }
    
    @NotNull
    @Override
    public TextFormat format() {
        return this.format;
    }
    
    @Override
    public boolean caseInsensitive() {
        return this.caseInsensitive;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CharacterAndFormatImpl)) {
            return false;
        }
        final CharacterAndFormatImpl that = (CharacterAndFormatImpl)other;
        return this.character == that.character && this.format.equals(that.format) && this.caseInsensitive == that.caseInsensitive;
    }
    
    @Override
    public int hashCode() {
        int result = this.character;
        result = 31 * result + this.format.hashCode();
        result = 31 * result + Boolean.hashCode(this.caseInsensitive);
        return result;
    }
    
    @NotNull
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static final class Defaults
    {
        static final List<CharacterAndFormat> DEFAULTS;
        
        private Defaults() {
        }
        
        static List<CharacterAndFormat> createDefaults() {
            final List<CharacterAndFormat> formats = (List<CharacterAndFormat>)new ArrayList(22);
            formats.add((Object)CharacterAndFormat.BLACK);
            formats.add((Object)CharacterAndFormat.DARK_BLUE);
            formats.add((Object)CharacterAndFormat.DARK_GREEN);
            formats.add((Object)CharacterAndFormat.DARK_AQUA);
            formats.add((Object)CharacterAndFormat.DARK_RED);
            formats.add((Object)CharacterAndFormat.DARK_PURPLE);
            formats.add((Object)CharacterAndFormat.GOLD);
            formats.add((Object)CharacterAndFormat.GRAY);
            formats.add((Object)CharacterAndFormat.DARK_GRAY);
            formats.add((Object)CharacterAndFormat.BLUE);
            formats.add((Object)CharacterAndFormat.GREEN);
            formats.add((Object)CharacterAndFormat.AQUA);
            formats.add((Object)CharacterAndFormat.RED);
            formats.add((Object)CharacterAndFormat.LIGHT_PURPLE);
            formats.add((Object)CharacterAndFormat.YELLOW);
            formats.add((Object)CharacterAndFormat.WHITE);
            formats.add((Object)CharacterAndFormat.OBFUSCATED);
            formats.add((Object)CharacterAndFormat.BOLD);
            formats.add((Object)CharacterAndFormat.STRIKETHROUGH);
            formats.add((Object)CharacterAndFormat.UNDERLINED);
            formats.add((Object)CharacterAndFormat.ITALIC);
            formats.add((Object)CharacterAndFormat.RESET);
            return (List<CharacterAndFormat>)Collections.unmodifiableList((List)formats);
        }
        
        static {
            DEFAULTS = createDefaults();
        }
    }
}
