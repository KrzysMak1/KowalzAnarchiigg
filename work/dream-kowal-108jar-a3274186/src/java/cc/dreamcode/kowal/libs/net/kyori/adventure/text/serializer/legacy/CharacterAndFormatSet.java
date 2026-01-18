package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy;

import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextFormat;
import java.util.List;

final class CharacterAndFormatSet
{
    static final CharacterAndFormatSet DEFAULT;
    final List<TextFormat> formats;
    final List<TextColor> colors;
    final String characters;
    
    static CharacterAndFormatSet of(final List<CharacterAndFormat> pairs) {
        final int size = pairs.size();
        final List<TextColor> colors = (List<TextColor>)new ArrayList();
        final List<TextFormat> formats = (List<TextFormat>)new ArrayList(size);
        final StringBuilder characters = new StringBuilder(size);
        for (int i = 0; i < size; ++i) {
            final CharacterAndFormat pair = (CharacterAndFormat)pairs.get(i);
            final char character = pair.character();
            final TextFormat format = pair.format();
            final boolean formatIsTextColor = format instanceof TextColor;
            characters.append(character);
            formats.add((Object)format);
            if (formatIsTextColor) {
                colors.add((Object)format);
            }
            if (pair.caseInsensitive()) {
                boolean added = false;
                if (Character.isUpperCase(character)) {
                    characters.append(Character.toLowerCase(character));
                    added = true;
                }
                else if (Character.isLowerCase(character)) {
                    characters.append(Character.toUpperCase(character));
                    added = true;
                }
                if (added) {
                    formats.add((Object)format);
                    if (formatIsTextColor) {
                        colors.add((Object)format);
                    }
                }
            }
        }
        if (formats.size() != characters.length()) {
            throw new IllegalStateException("formats length differs from characters length");
        }
        return new CharacterAndFormatSet((List<TextFormat>)Collections.unmodifiableList((List)formats), (List<TextColor>)Collections.unmodifiableList((List)colors), characters.toString());
    }
    
    CharacterAndFormatSet(final List<TextFormat> formats, final List<TextColor> colors, final String characters) {
        this.formats = formats;
        this.colors = colors;
        this.characters = characters;
    }
    
    static {
        DEFAULT = of(CharacterAndFormat.defaults());
    }
}
