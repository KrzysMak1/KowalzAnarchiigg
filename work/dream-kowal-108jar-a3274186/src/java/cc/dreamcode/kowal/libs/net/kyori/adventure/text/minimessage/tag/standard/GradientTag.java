package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.BiFunction;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.RGBLike;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class GradientTag extends AbstractColorChangingTag
{
    private static final String GRADIENT = "gradient";
    static final TagResolver RESOLVER;
    private int index;
    private double multiplier;
    private final TextColor[] colors;
    private double phase;
    
    static Tag create(final ArgumentQueue args, final Context ctx) {
        double phase = 0.0;
        List<TextColor> textColors;
        if (args.hasNext()) {
            textColors = (List<TextColor>)new ArrayList();
            while (args.hasNext()) {
                final Tag.Argument arg = args.pop();
                if (!args.hasNext()) {
                    final OptionalDouble possiblePhase = arg.asDouble();
                    if (possiblePhase.isPresent()) {
                        phase = possiblePhase.getAsDouble();
                        if (phase < -1.0 || phase > 1.0) {
                            throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", new Object[] { phase }), args);
                        }
                        break;
                    }
                }
                final TextColor parsedColor = ColorTagResolver.resolveColor(arg.value(), ctx);
                textColors.add((Object)parsedColor);
            }
            if (textColors.size() == 1) {
                throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
            }
        }
        else {
            textColors = (List<TextColor>)Collections.emptyList();
        }
        return new GradientTag(phase, textColors);
    }
    
    private GradientTag(final double phase, final List<TextColor> colors) {
        this.index = 0;
        this.multiplier = 1.0;
        if (colors.isEmpty()) {
            this.colors = new TextColor[] { TextColor.color(16777215), TextColor.color(0) };
        }
        else {
            this.colors = (TextColor[])colors.toArray((Object[])new TextColor[0]);
        }
        if (phase < 0.0) {
            this.phase = 1.0 + phase;
            Collections.reverse(Arrays.asList((Object[])this.colors));
        }
        else {
            this.phase = phase;
        }
    }
    
    @Override
    protected void init() {
        this.multiplier = ((this.size() == 1) ? 0.0 : ((this.colors.length - 1) / (double)(this.size() - 1)));
        this.phase *= this.colors.length - 1;
        this.index = 0;
    }
    
    @Override
    protected void advanceColor() {
        ++this.index;
    }
    
    @Override
    protected TextColor color() {
        final double position = this.index * this.multiplier + this.phase;
        final int lowUnclamped = (int)Math.floor(position);
        final int high = (int)Math.ceil(position) % this.colors.length;
        final int low = lowUnclamped % this.colors.length;
        return TextColor.lerp((float)position - lowUnclamped, this.colors[low], this.colors[high]);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", this.colors) });
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final GradientTag that = (GradientTag)other;
        return this.index == that.index && this.phase == that.phase && Arrays.equals((Object[])this.colors, (Object[])that.colors);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hash(new Object[] { this.index, this.phase });
        result = 31 * result + Arrays.hashCode((Object[])this.colors);
        return result;
    }
    
    static {
        RESOLVER = TagResolver.resolver("gradient", (BiFunction<ArgumentQueue, Context, Tag>)GradientTag::create);
    }
}
