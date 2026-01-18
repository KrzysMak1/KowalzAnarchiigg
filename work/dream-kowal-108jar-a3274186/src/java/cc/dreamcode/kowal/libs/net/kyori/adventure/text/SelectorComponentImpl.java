package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import java.util.function.Predicate;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

final class SelectorComponentImpl extends AbstractComponent implements SelectorComponent
{
    private final String pattern;
    @Nullable
    private final Component separator;
    
    static SelectorComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String pattern, @Nullable final ComponentLike separator) {
        return new SelectorComponentImpl(ComponentLike.asComponents(children, (Predicate<? super Component>)SelectorComponentImpl.IS_NOT_EMPTY), (Style)Objects.requireNonNull((Object)style, "style"), (String)Objects.requireNonNull((Object)pattern, "pattern"), ComponentLike.unbox(separator));
    }
    
    SelectorComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String pattern, @Nullable final Component separator) {
        super(children, style);
        this.pattern = pattern;
        this.separator = separator;
    }
    
    @NotNull
    @Override
    public String pattern() {
        return this.pattern;
    }
    
    @NotNull
    @Override
    public SelectorComponent pattern(@NotNull final String pattern) {
        if (Objects.equals((Object)this.pattern, (Object)pattern)) {
            return this;
        }
        return create(this.children, this.style, pattern, this.separator);
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @NotNull
    @Override
    public SelectorComponent separator(@Nullable final ComponentLike separator) {
        return create(this.children, this.style, this.pattern, separator);
    }
    
    @NotNull
    @Override
    public SelectorComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.pattern, this.separator);
    }
    
    @NotNull
    @Override
    public SelectorComponent style(@NotNull final Style style) {
        return create(this.children, style, this.pattern, this.separator);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SelectorComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final SelectorComponent that = (SelectorComponent)other;
        return Objects.equals((Object)this.pattern, (Object)that.pattern()) && Objects.equals((Object)this.separator, (Object)that.separator());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.pattern.hashCode();
        result = 31 * result + Objects.hashCode((Object)this.separator);
        return result;
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, SelectorComponent.Builder> implements SelectorComponent.Builder
    {
        @Nullable
        private String pattern;
        @Nullable
        private Component separator;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final SelectorComponent component) {
            super(component);
            this.pattern = component.pattern();
            this.separator = component.separator();
        }
        
        @NotNull
        @Override
        public SelectorComponent.Builder pattern(@NotNull final String pattern) {
            this.pattern = (String)Objects.requireNonNull((Object)pattern, "pattern");
            return this;
        }
        
        @NotNull
        @Override
        public SelectorComponent.Builder separator(@Nullable final ComponentLike separator) {
            this.separator = ComponentLike.unbox(separator);
            return this;
        }
        
        @NotNull
        @Override
        public SelectorComponent build() {
            if (this.pattern == null) {
                throw new IllegalStateException("pattern must be set");
            }
            return SelectorComponentImpl.create(this.children, this.buildStyle(), this.pattern, this.separator);
        }
    }
}
