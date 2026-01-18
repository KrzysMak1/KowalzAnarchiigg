package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.BiFunction;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.HSVLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class RainbowTag extends AbstractColorChangingTag
{
    private static final String REVERSE = "!";
    private static final String RAINBOW = "rainbow";
    static final TagResolver RESOLVER;
    private final boolean reversed;
    private final double dividedPhase;
    private int colorIndex;
    
    static Tag create(final ArgumentQueue args, final Context ctx) {
        boolean reversed = false;
        int phase = 0;
        if (args.hasNext()) {
            String value = args.pop().value();
            if (value.startsWith("!")) {
                reversed = true;
                value = value.substring("!".length());
            }
            if (value.length() > 0) {
                try {
                    phase = Integer.parseInt(value);
                }
                catch (final NumberFormatException ex) {
                    throw ctx.newException("Expected phase, got " + value);
                }
            }
        }
        return new RainbowTag(reversed, phase);
    }
    
    private RainbowTag(final boolean reversed, final int phase) {
        this.colorIndex = 0;
        this.reversed = reversed;
        this.dividedPhase = phase / 10.0;
    }
    
    @Override
    protected void init() {
        if (this.reversed) {
            this.colorIndex = this.size() - 1;
        }
    }
    
    @Override
    protected void advanceColor() {
        if (this.reversed) {
            if (this.colorIndex == 0) {
                this.colorIndex = this.size() - 1;
            }
            else {
                --this.colorIndex;
            }
        }
        else {
            ++this.colorIndex;
        }
    }
    
    @Override
    protected TextColor color() {
        final float index = (float)this.colorIndex;
        final float hue = (float)((index / this.size() + this.dividedPhase) % 1.0);
        return TextColor.color(HSVLike.hsvLike(hue, 1.0f, 1.0f));
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("phase", this.dividedPhase));
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final RainbowTag that = (RainbowTag)other;
        return this.colorIndex == that.colorIndex && this.dividedPhase == that.dividedPhase;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.colorIndex, this.dividedPhase });
    }
    
    static {
        RESOLVER = TagResolver.resolver("rainbow", (BiFunction<ArgumentQueue, Context, Tag>)RainbowTag::create);
    }
}
