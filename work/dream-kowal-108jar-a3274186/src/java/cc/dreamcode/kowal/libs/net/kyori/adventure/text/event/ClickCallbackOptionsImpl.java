package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import java.util.Objects;
import java.time.temporal.TemporalAmount;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;

final class ClickCallbackOptionsImpl implements ClickCallback.Options
{
    static final ClickCallback.Options DEFAULT;
    private final int uses;
    private final Duration lifetime;
    
    ClickCallbackOptionsImpl(final int uses, final Duration lifetime) {
        this.uses = uses;
        this.lifetime = lifetime;
    }
    
    @Override
    public int uses() {
        return this.uses;
    }
    
    @NotNull
    @Override
    public Duration lifetime() {
        return this.lifetime;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("uses", this.uses), ExaminableProperty.of("expiration", this.lifetime) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static {
        DEFAULT = new BuilderImpl().build();
    }
    
    static final class BuilderImpl implements Builder
    {
        private static final int DEFAULT_USES = 1;
        private int uses;
        private Duration lifetime;
        
        BuilderImpl() {
            this.uses = 1;
            this.lifetime = ClickCallback.DEFAULT_LIFETIME;
        }
        
        BuilderImpl(final ClickCallback.Options existing) {
            this.uses = existing.uses();
            this.lifetime = existing.lifetime();
        }
        
        @Override
        public ClickCallback.Options build() {
            return new ClickCallbackOptionsImpl(this.uses, this.lifetime);
        }
        
        @NotNull
        @Override
        public Builder uses(final int uses) {
            this.uses = uses;
            return this;
        }
        
        @NotNull
        @Override
        public Builder lifetime(@NotNull final TemporalAmount lifetime) {
            this.lifetime = ((lifetime instanceof Duration) ? lifetime : Duration.from((TemporalAmount)Objects.requireNonNull((Object)lifetime, "lifetime")));
            return this;
        }
    }
}
