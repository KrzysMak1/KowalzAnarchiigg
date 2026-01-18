package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.NamedTextColor;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface CharacterAndFormat extends Examinable
{
    public static final CharacterAndFormat BLACK = characterAndFormat('0', NamedTextColor.BLACK, true);
    public static final CharacterAndFormat DARK_BLUE = characterAndFormat('1', NamedTextColor.DARK_BLUE, true);
    public static final CharacterAndFormat DARK_GREEN = characterAndFormat('2', NamedTextColor.DARK_GREEN, true);
    public static final CharacterAndFormat DARK_AQUA = characterAndFormat('3', NamedTextColor.DARK_AQUA, true);
    public static final CharacterAndFormat DARK_RED = characterAndFormat('4', NamedTextColor.DARK_RED, true);
    public static final CharacterAndFormat DARK_PURPLE = characterAndFormat('5', NamedTextColor.DARK_PURPLE, true);
    public static final CharacterAndFormat GOLD = characterAndFormat('6', NamedTextColor.GOLD, true);
    public static final CharacterAndFormat GRAY = characterAndFormat('7', NamedTextColor.GRAY, true);
    public static final CharacterAndFormat DARK_GRAY = characterAndFormat('8', NamedTextColor.DARK_GRAY, true);
    public static final CharacterAndFormat BLUE = characterAndFormat('9', NamedTextColor.BLUE, true);
    public static final CharacterAndFormat GREEN = characterAndFormat('a', NamedTextColor.GREEN, true);
    public static final CharacterAndFormat AQUA = characterAndFormat('b', NamedTextColor.AQUA, true);
    public static final CharacterAndFormat RED = characterAndFormat('c', NamedTextColor.RED, true);
    public static final CharacterAndFormat LIGHT_PURPLE = characterAndFormat('d', NamedTextColor.LIGHT_PURPLE, true);
    public static final CharacterAndFormat YELLOW = characterAndFormat('e', NamedTextColor.YELLOW, true);
    public static final CharacterAndFormat WHITE = characterAndFormat('f', NamedTextColor.WHITE, true);
    public static final CharacterAndFormat OBFUSCATED = characterAndFormat('k', TextDecoration.OBFUSCATED, true);
    public static final CharacterAndFormat BOLD = characterAndFormat('l', TextDecoration.BOLD, true);
    public static final CharacterAndFormat STRIKETHROUGH = characterAndFormat('m', TextDecoration.STRIKETHROUGH, true);
    public static final CharacterAndFormat UNDERLINED = characterAndFormat('n', TextDecoration.UNDERLINED, true);
    public static final CharacterAndFormat ITALIC = characterAndFormat('o', TextDecoration.ITALIC, true);
    public static final CharacterAndFormat RESET = characterAndFormat('r', Reset.INSTANCE, true);
    
    @NotNull
    default CharacterAndFormat characterAndFormat(final char character, @NotNull final TextFormat format) {
        return characterAndFormat(character, format, false);
    }
    
    @NotNull
    default CharacterAndFormat characterAndFormat(final char character, @NotNull final TextFormat format, final boolean caseInsensitive) {
        return new CharacterAndFormatImpl(character, format, caseInsensitive);
    }
    
    @NotNull
    default List<CharacterAndFormat> defaults() {
        return CharacterAndFormatImpl.Defaults.DEFAULTS;
    }
    
    char character();
    
    @NotNull
    TextFormat format();
    
    boolean caseInsensitive();
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("character", this.character()), ExaminableProperty.of("format", this.format()), ExaminableProperty.of("caseInsensitive", this.caseInsensitive()) });
    }
}
