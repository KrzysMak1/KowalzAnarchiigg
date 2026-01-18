package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.BiFunction;
import java.util.Objects;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.OptionalDouble;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.NamedTextColor;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Inserting;

public final class TransitionTag implements Inserting, Examinable
{
    public static final String TRANSITION = "transition";
    private final TextColor[] colors;
    private final float phase;
    private final boolean negativePhase;
    static final TagResolver RESOLVER;
    
    static Tag create(final ArgumentQueue args, final Context ctx) {
        float phase = 0.0f;
        List<TextColor> textColors;
        if (args.hasNext()) {
            textColors = (List<TextColor>)new ArrayList();
            while (args.hasNext()) {
                final Tag.Argument arg = args.pop();
                if (!args.hasNext()) {
                    final OptionalDouble possiblePhase = arg.asDouble();
                    if (possiblePhase.isPresent()) {
                        phase = (float)possiblePhase.getAsDouble();
                        if (phase < -1.0f || phase > 1.0f) {
                            throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", new Object[] { phase }), args);
                        }
                        break;
                    }
                }
                final String argValue = arg.value();
                TextColor parsedColor;
                if (argValue.charAt(0) == '#') {
                    parsedColor = TextColor.fromHexString(argValue);
                }
                else {
                    parsedColor = NamedTextColor.NAMES.value(arg.lowerValue());
                }
                if (parsedColor == null) {
                    throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", new Object[] { argValue }), args);
                }
                textColors.add((Object)parsedColor);
            }
            if (textColors.size() < 2) {
                throw ctx.newException("Invalid transition, not enough colors. Transitions must have at least two colors.", args);
            }
        }
        else {
            textColors = (List<TextColor>)Collections.emptyList();
        }
        return new TransitionTag(phase, textColors);
    }
    
    private TransitionTag(final float phase, final List<TextColor> colors) {
        if (phase < 0.0f) {
            this.negativePhase = true;
            this.phase = 1.0f + phase;
            Collections.reverse((List)colors);
        }
        else {
            this.negativePhase = false;
            this.phase = phase;
        }
        if (colors.isEmpty()) {
            this.colors = new TextColor[] { TextColor.color(16777215), TextColor.color(0) };
        }
        else {
            this.colors = (TextColor[])colors.toArray((Object[])new TextColor[0]);
        }
    }
    
    @NotNull
    @Override
    public Component value() {
        return Component.text("", this.color());
    }
    
    private TextColor color() {
        final float steps = 1.0f / (this.colors.length - 1);
        int colorIndex = 1;
        while (colorIndex < this.colors.length) {
            final float val = colorIndex * steps;
            if (val >= this.phase) {
                final float factor = 1.0f + (this.phase - val) * (this.colors.length - 1);
                if (this.negativePhase) {
                    return TextColor.lerp(1.0f - factor, this.colors[colorIndex], this.colors[colorIndex - 1]);
                }
                return TextColor.lerp(factor, this.colors[colorIndex - 1], this.colors[colorIndex]);
            }
            else {
                ++colorIndex;
            }
        }
        return this.colors[0];
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", this.colors) });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final TransitionTag that = (TransitionTag)other;
        return this.phase == that.phase && Arrays.equals((Object[])this.colors, (Object[])that.colors);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(new Object[] { this.phase });
        result = 31 * result + Arrays.hashCode((Object[])this.colors);
        return result;
    }
    
    static {
        RESOLVER = TagResolver.resolver("transition", (BiFunction<ArgumentQueue, Context, Tag>)TransitionTag::create);
    }
}
