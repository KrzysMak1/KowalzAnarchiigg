package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import java.util.EnumMap;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import java.util.Objects;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;

final class StyleImpl implements Style
{
    static final StyleImpl EMPTY;
    @Nullable
    final Key font;
    @Nullable
    final TextColor color;
    @NotNull
    final DecorationMap decorations;
    @Nullable
    final ClickEvent clickEvent;
    @Nullable
    final HoverEvent<?> hoverEvent;
    @Nullable
    final String insertion;
    
    StyleImpl(@Nullable final Key font, @Nullable final TextColor color, @NotNull final Map<TextDecoration, TextDecoration.State> decorations, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent<?> hoverEvent, @Nullable final String insertion) {
        this.font = font;
        this.color = color;
        this.decorations = DecorationMap.fromMap(decorations);
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;
    }
    
    @Nullable
    @Override
    public Key font() {
        return this.font;
    }
    
    @NotNull
    @Override
    public Style font(@Nullable final Key font) {
        if (Objects.equals((Object)this.font, (Object)font)) {
            return this;
        }
        return new StyleImpl(font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public TextColor color() {
        return this.color;
    }
    
    @NotNull
    @Override
    public Style color(@Nullable final TextColor color) {
        if (Objects.equals((Object)this.color, (Object)color)) {
            return this;
        }
        return new StyleImpl(this.font, color, (Map<TextDecoration, TextDecoration.State>)this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @NotNull
    @Override
    public Style colorIfAbsent(@Nullable final TextColor color) {
        if (this.color == null) {
            return this.color(color);
        }
        return this;
    }
    
    @Override
    public TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
        final TextDecoration.State state = this.decorations.get(decoration);
        if (state != null) {
            return state;
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", new Object[] { decoration }));
    }
    
    @NotNull
    @Override
    public Style decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull((Object)state, "state");
        if (this.decoration(decoration) == state) {
            return this;
        }
        return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @NotNull
    @Override
    public Style decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull((Object)state, "state");
        final TextDecoration.State oldState = this.decorations.get(decoration);
        if (oldState == TextDecoration.State.NOT_SET) {
            return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (oldState != null) {
            return this;
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", new Object[] { decoration }));
    }
    
    @NotNull
    @Override
    public Map<TextDecoration, TextDecoration.State> decorations() {
        return (Map<TextDecoration, TextDecoration.State>)this.decorations;
    }
    
    @NotNull
    @Override
    public Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)DecorationMap.merge(decorations, (Map<TextDecoration, TextDecoration.State>)this.decorations), this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public ClickEvent clickEvent() {
        return this.clickEvent;
    }
    
    @NotNull
    @Override
    public Style clickEvent(@Nullable final ClickEvent event) {
        return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations, event, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public HoverEvent<?> hoverEvent() {
        return this.hoverEvent;
    }
    
    @NotNull
    @Override
    public Style hoverEvent(@Nullable final HoverEventSource<?> source) {
        return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
    }
    
    @Nullable
    @Override
    public String insertion() {
        return this.insertion;
    }
    
    @NotNull
    @Override
    public Style insertion(@Nullable final String insertion) {
        if (Objects.equals((Object)this.insertion, (Object)insertion)) {
            return this;
        }
        return new StyleImpl(this.font, this.color, (Map<TextDecoration, TextDecoration.State>)this.decorations, this.clickEvent, this.hoverEvent, insertion);
    }
    
    @NotNull
    @Override
    public Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
        if (nothingToMerge(that, strategy, merges)) {
            return this;
        }
        if (this.isEmpty() && Merge.hasAll(merges)) {
            return that;
        }
        final Builder builder = this.toBuilder();
        builder.merge(that, strategy, merges);
        return builder.build();
    }
    
    @NotNull
    @Override
    public Style unmerge(@NotNull final Style that) {
        if (this.isEmpty()) {
            return this;
        }
        final Builder builder = new BuilderImpl(this);
        if (Objects.equals((Object)this.font(), (Object)that.font())) {
            builder.font((Key)null);
        }
        if (Objects.equals((Object)this.color(), (Object)that.color())) {
            builder.color((TextColor)null);
        }
        for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = DecorationMap.DECORATIONS[i];
            if (this.decoration(decoration) == that.decoration(decoration)) {
                builder.decoration(decoration, TextDecoration.State.NOT_SET);
            }
        }
        if (Objects.equals((Object)this.clickEvent(), (Object)that.clickEvent())) {
            builder.clickEvent((ClickEvent)null);
        }
        if (Objects.equals((Object)this.hoverEvent(), (Object)that.hoverEvent())) {
            builder.hoverEvent((HoverEventSource<?>)null);
        }
        if (Objects.equals((Object)this.insertion(), (Object)that.insertion())) {
            builder.insertion((String)null);
        }
        return builder.build();
    }
    
    static boolean nothingToMerge(@NotNull final Style mergeFrom, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
        return strategy == Merge.Strategy.NEVER || mergeFrom.isEmpty() || merges.isEmpty();
    }
    
    @Override
    public boolean isEmpty() {
        return this == StyleImpl.EMPTY;
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat((Stream)this.decorations.examinableProperties(), Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("color", this.color), ExaminableProperty.of("clickEvent", this.clickEvent), ExaminableProperty.of("hoverEvent", this.hoverEvent), ExaminableProperty.of("insertion", this.insertion), ExaminableProperty.of("font", this.font) }));
    }
    
    @NotNull
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleImpl)) {
            return false;
        }
        final StyleImpl that = (StyleImpl)other;
        return Objects.equals((Object)this.color, (Object)that.color) && this.decorations.equals(that.decorations) && Objects.equals((Object)this.clickEvent, (Object)that.clickEvent) && Objects.equals((Object)this.hoverEvent, (Object)that.hoverEvent) && Objects.equals((Object)this.insertion, (Object)that.insertion) && Objects.equals((Object)this.font, (Object)that.font);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode((Object)this.color);
        result = 31 * result + this.decorations.hashCode();
        result = 31 * result + Objects.hashCode((Object)this.clickEvent);
        result = 31 * result + Objects.hashCode((Object)this.hoverEvent);
        result = 31 * result + Objects.hashCode((Object)this.insertion);
        result = 31 * result + Objects.hashCode((Object)this.font);
        return result;
    }
    
    static {
        EMPTY = new StyleImpl(null, null, (Map<TextDecoration, TextDecoration.State>)DecorationMap.EMPTY, null, null, null);
    }
    
    static final class BuilderImpl implements Builder
    {
        @Nullable
        Key font;
        @Nullable
        TextColor color;
        final Map<TextDecoration, TextDecoration.State> decorations;
        @Nullable
        ClickEvent clickEvent;
        @Nullable
        HoverEvent<?> hoverEvent;
        @Nullable
        String insertion;
        
        BuilderImpl() {
            this.decorations = (Map<TextDecoration, TextDecoration.State>)new EnumMap((Map)DecorationMap.EMPTY);
        }
        
        BuilderImpl(@NotNull final StyleImpl style) {
            this.color = style.color;
            this.decorations = (Map<TextDecoration, TextDecoration.State>)new EnumMap((Map)style.decorations);
            this.clickEvent = style.clickEvent;
            this.hoverEvent = style.hoverEvent;
            this.insertion = style.insertion;
            this.font = style.font;
        }
        
        @NotNull
        @Override
        public Builder font(@Nullable final Key font) {
            this.font = font;
            return this;
        }
        
        @NotNull
        @Override
        public Builder color(@Nullable final TextColor color) {
            this.color = color;
            return this;
        }
        
        @NotNull
        @Override
        public Builder colorIfAbsent(@Nullable final TextColor color) {
            if (this.color == null) {
                this.color = color;
            }
            return this;
        }
        
        @NotNull
        @Override
        public Builder decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull((Object)state, "state");
            Objects.requireNonNull((Object)decoration, "decoration");
            this.decorations.put((Object)decoration, (Object)state);
            return this;
        }
        
        @NotNull
        @Override
        public Builder decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull((Object)state, "state");
            final TextDecoration.State oldState = (TextDecoration.State)this.decorations.get((Object)decoration);
            if (oldState == TextDecoration.State.NOT_SET) {
                this.decorations.put((Object)decoration, (Object)state);
            }
            if (oldState != null) {
                return this;
            }
            throw new IllegalArgumentException(String.format("unknown decoration '%s'", new Object[] { decoration }));
        }
        
        @NotNull
        @Override
        public Builder clickEvent(@Nullable final ClickEvent event) {
            this.clickEvent = event;
            return this;
        }
        
        @NotNull
        @Override
        public Builder hoverEvent(@Nullable final HoverEventSource<?> source) {
            this.hoverEvent = HoverEventSource.unbox(source);
            return this;
        }
        
        @NotNull
        @Override
        public Builder insertion(@Nullable final String insertion) {
            this.insertion = insertion;
            return this;
        }
        
        @NotNull
        @Override
        public Builder merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
            Objects.requireNonNull((Object)that, "style");
            Objects.requireNonNull((Object)strategy, "strategy");
            Objects.requireNonNull((Object)merges, "merges");
            if (StyleImpl.nothingToMerge(that, strategy, merges)) {
                return this;
            }
            if (merges.contains((Object)Merge.COLOR)) {
                final TextColor color = that.color();
                if (color != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.color == null))) {
                    this.color(color);
                }
            }
            if (merges.contains((Object)Merge.DECORATIONS)) {
                for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; ++i) {
                    final TextDecoration decoration = DecorationMap.DECORATIONS[i];
                    final TextDecoration.State state = that.decoration(decoration);
                    if (state != TextDecoration.State.NOT_SET) {
                        if (strategy == Merge.Strategy.ALWAYS) {
                            this.decoration(decoration, state);
                        }
                        else if (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
                            this.decorationIfAbsent(decoration, state);
                        }
                    }
                }
            }
            if (merges.contains((Object)Merge.EVENTS)) {
                final ClickEvent clickEvent = that.clickEvent();
                if (clickEvent != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.clickEvent == null))) {
                    this.clickEvent(clickEvent);
                }
                final HoverEvent<?> hoverEvent = that.hoverEvent();
                if (hoverEvent != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.hoverEvent == null))) {
                    this.hoverEvent((HoverEventSource<?>)hoverEvent);
                }
            }
            if (merges.contains((Object)Merge.INSERTION)) {
                final String insertion = that.insertion();
                if (insertion != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.insertion == null))) {
                    this.insertion(insertion);
                }
            }
            if (merges.contains((Object)Merge.FONT)) {
                final Key font = that.font();
                if (font != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.font == null))) {
                    this.font(font);
                }
            }
            return this;
        }
        
        @NotNull
        @Override
        public StyleImpl build() {
            if (this.isEmpty()) {
                return StyleImpl.EMPTY;
            }
            return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
        }
        
        private boolean isEmpty() {
            return this.color == null && this.decorations.values().stream().allMatch(state -> state == TextDecoration.State.NOT_SET) && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null;
        }
    }
}
