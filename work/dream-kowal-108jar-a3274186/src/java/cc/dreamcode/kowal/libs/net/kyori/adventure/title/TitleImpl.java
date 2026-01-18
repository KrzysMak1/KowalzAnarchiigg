package cc.dreamcode.kowal.libs.net.kyori.adventure.title;

import java.time.Duration;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

final class TitleImpl implements Title
{
    private final Component title;
    private final Component subtitle;
    @Nullable
    private final Times times;
    
    TitleImpl(@NotNull final Component title, @NotNull final Component subtitle, @Nullable final Times times) {
        this.title = (Component)Objects.requireNonNull((Object)title, "title");
        this.subtitle = (Component)Objects.requireNonNull((Object)subtitle, "subtitle");
        this.times = times;
    }
    
    @NotNull
    @Override
    public Component title() {
        return this.title;
    }
    
    @NotNull
    @Override
    public Component subtitle() {
        return this.subtitle;
    }
    
    @Nullable
    @Override
    public Times times() {
        return this.times;
    }
    
    @Override
    public <T> T part(@NotNull final TitlePart<T> part) {
        Objects.requireNonNull((Object)part, "part");
        if (part == TitlePart.TITLE) {
            return (T)this.title;
        }
        if (part == TitlePart.SUBTITLE) {
            return (T)this.subtitle;
        }
        if (part == TitlePart.TIMES) {
            return (T)this.times;
        }
        throw new IllegalArgumentException("Don't know what " + (Object)part + " is.");
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final TitleImpl that = (TitleImpl)other;
        return this.title.equals(that.title) && this.subtitle.equals(that.subtitle) && Objects.equals((Object)this.times, (Object)that.times);
    }
    
    @Override
    public int hashCode() {
        int result = this.title.hashCode();
        result = 31 * result + this.subtitle.hashCode();
        result = 31 * result + Objects.hashCode((Object)this.times);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("title", this.title), ExaminableProperty.of("subtitle", this.subtitle), ExaminableProperty.of("times", this.times) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static class TimesImpl implements Times
    {
        private final Duration fadeIn;
        private final Duration stay;
        private final Duration fadeOut;
        
        TimesImpl(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
            this.fadeIn = (Duration)Objects.requireNonNull((Object)fadeIn, "fadeIn");
            this.stay = (Duration)Objects.requireNonNull((Object)stay, "stay");
            this.fadeOut = (Duration)Objects.requireNonNull((Object)fadeOut, "fadeOut");
        }
        
        @NotNull
        @Override
        public Duration fadeIn() {
            return this.fadeIn;
        }
        
        @NotNull
        @Override
        public Duration stay() {
            return this.stay;
        }
        
        @NotNull
        @Override
        public Duration fadeOut() {
            return this.fadeOut;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TimesImpl)) {
                return false;
            }
            final TimesImpl that = (TimesImpl)other;
            return this.fadeIn.equals((Object)that.fadeIn) && this.stay.equals((Object)that.stay) && this.fadeOut.equals((Object)that.fadeOut);
        }
        
        @Override
        public int hashCode() {
            int result = this.fadeIn.hashCode();
            result = 31 * result + this.stay.hashCode();
            result = 31 * result + this.fadeOut.hashCode();
            return result;
        }
        
        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("fadeIn", this.fadeIn), ExaminableProperty.of("stay", this.stay), ExaminableProperty.of("fadeOut", this.fadeOut) });
        }
        
        @Override
        public String toString() {
            return Internals.toString(this);
        }
    }
}
