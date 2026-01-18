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

final class ScoreComponentImpl extends AbstractComponent implements ScoreComponent
{
    private final String name;
    private final String objective;
    @Deprecated
    @Nullable
    private final String value;
    
    static ScoreComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        return new ScoreComponentImpl(ComponentLike.asComponents(children, (Predicate<? super Component>)ScoreComponentImpl.IS_NOT_EMPTY), (Style)Objects.requireNonNull((Object)style, "style"), (String)Objects.requireNonNull((Object)name, "name"), (String)Objects.requireNonNull((Object)objective, "objective"), value);
    }
    
    ScoreComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        super(children, style);
        this.name = name;
        this.objective = objective;
        this.value = value;
    }
    
    @NotNull
    @Override
    public String name() {
        return this.name;
    }
    
    @NotNull
    @Override
    public ScoreComponent name(@NotNull final String name) {
        if (Objects.equals((Object)this.name, (Object)name)) {
            return this;
        }
        return create(this.children, this.style, name, this.objective, this.value);
    }
    
    @NotNull
    @Override
    public String objective() {
        return this.objective;
    }
    
    @NotNull
    @Override
    public ScoreComponent objective(@NotNull final String objective) {
        if (Objects.equals((Object)this.objective, (Object)objective)) {
            return this;
        }
        return create(this.children, this.style, this.name, objective, this.value);
    }
    
    @Deprecated
    @Nullable
    @Override
    public String value() {
        return this.value;
    }
    
    @Deprecated
    @NotNull
    @Override
    public ScoreComponent value(@Nullable final String value) {
        if (Objects.equals((Object)this.value, (Object)value)) {
            return this;
        }
        return create(this.children, this.style, this.name, this.objective, value);
    }
    
    @NotNull
    @Override
    public ScoreComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.name, this.objective, this.value);
    }
    
    @NotNull
    @Override
    public ScoreComponent style(@NotNull final Style style) {
        return create(this.children, style, this.name, this.objective, this.value);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ScoreComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final ScoreComponent that = (ScoreComponent)other;
        return Objects.equals((Object)this.name, (Object)that.name()) && Objects.equals((Object)this.objective, (Object)that.objective()) && Objects.equals((Object)this.value, (Object)that.value());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.objective.hashCode();
        result = 31 * result + Objects.hashCode((Object)this.value);
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
    
    static final class BuilderImpl extends AbstractComponentBuilder<ScoreComponent, ScoreComponent.Builder> implements ScoreComponent.Builder
    {
        @Nullable
        private String name;
        @Nullable
        private String objective;
        @Nullable
        private String value;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final ScoreComponent component) {
            super(component);
            this.name = component.name();
            this.objective = component.objective();
            this.value = component.value();
        }
        
        @NotNull
        @Override
        public ScoreComponent.Builder name(@NotNull final String name) {
            this.name = (String)Objects.requireNonNull((Object)name, "name");
            return this;
        }
        
        @NotNull
        @Override
        public ScoreComponent.Builder objective(@NotNull final String objective) {
            this.objective = (String)Objects.requireNonNull((Object)objective, "objective");
            return this;
        }
        
        @Deprecated
        @NotNull
        @Override
        public ScoreComponent.Builder value(@Nullable final String value) {
            this.value = value;
            return this;
        }
        
        @NotNull
        @Override
        public ScoreComponent build() {
            if (this.name == null) {
                throw new IllegalStateException("name must be set");
            }
            if (this.objective == null) {
                throw new IllegalStateException("objective must be set");
            }
            return ScoreComponentImpl.create(this.children, this.buildStyle(), this.name, this.objective, this.value);
        }
    }
}
