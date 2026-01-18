package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ColorTagResolver implements TagResolver, SerializableResolver.Single
{
    private static final String COLOR_3 = "c";
    private static final String COLOR_2 = "colour";
    private static final String COLOR = "color";
    static final TagResolver INSTANCE;
    private static final StyleClaim<TextColor> STYLE;
    private static final Map<String, TextColor> COLOR_ALIASES;
    
    private static boolean isColorOrAbbreviation(final String name) {
        return name.equals((Object)"color") || name.equals((Object)"colour") || name.equals((Object)"c");
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
        if (!this.has(name)) {
            return null;
        }
        String colorName;
        if (isColorOrAbbreviation(name)) {
            colorName = args.popOr("Expected to find a color parameter: <name>|#RRGGBB").lowerValue();
        }
        else {
            colorName = name;
        }
        final TextColor color = resolveColor(colorName, ctx);
        return Tag.styling(color);
    }
    
    @NotNull
    static TextColor resolveColor(@NotNull final String colorName, @NotNull final Context ctx) throws ParsingException {
        TextColor color;
        if (ColorTagResolver.COLOR_ALIASES.containsKey((Object)colorName)) {
            color = (TextColor)ColorTagResolver.COLOR_ALIASES.get((Object)colorName);
        }
        else if (colorName.charAt(0) == '#') {
            color = TextColor.fromHexString(colorName);
        }
        else {
            color = NamedTextColor.NAMES.value(colorName);
        }
        if (color == null) {
            throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colours or hex (#RRGGBB) colors.", new Object[] { colorName }));
        }
        return color;
    }
    
    @Override
    public boolean has(@NotNull final String name) {
        return isColorOrAbbreviation(name) || TextColor.fromHexString(name) != null || NamedTextColor.NAMES.value(name) != null || ColorTagResolver.COLOR_ALIASES.containsKey((Object)name);
    }
    
    @Nullable
    @Override
    public StyleClaim<?> claimStyle() {
        return ColorTagResolver.STYLE;
    }
    
    static {
        INSTANCE = new ColorTagResolver();
        STYLE = StyleClaim.claim("color", (java.util.function.Function<Style, TextColor>)Style::color, (java.util.function.BiConsumer<TextColor, TokenEmitter>)((color, emitter) -> {
            if (color instanceof NamedTextColor) {
                emitter.tag(NamedTextColor.NAMES.key((NamedTextColor)color));
            }
            else {
                emitter.tag(color.asHexString());
            }
        }));
        (COLOR_ALIASES = (Map)new HashMap()).put((Object)"dark_grey", (Object)NamedTextColor.DARK_GRAY);
        ColorTagResolver.COLOR_ALIASES.put((Object)"grey", (Object)NamedTextColor.GRAY);
    }
}
