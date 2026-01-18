package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import java.util.ArrayList;
import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent
{
    private final String key;
    @Nullable
    private final String fallback;
    private final List<TranslationArgument> args;
    
    static TranslatableComponent create(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final ComponentLike[] args) {
        Objects.requireNonNull((Object)args, "args");
        return create(children, style, key, fallback, (List<? extends ComponentLike>)Arrays.asList((Object[])args));
    }
    
    static TranslatableComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(ComponentLike.asComponents(children, (Predicate<? super Component>)TranslatableComponentImpl.IS_NOT_EMPTY), (Style)Objects.requireNonNull((Object)style, "style"), (String)Objects.requireNonNull((Object)key, "key"), fallback, asArguments(args));
    }
    
    TranslatableComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final List<TranslationArgument> args) {
        super(children, style);
        this.key = key;
        this.fallback = fallback;
        this.args = args;
    }
    
    @NotNull
    @Override
    public String key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public TranslatableComponent key(@NotNull final String key) {
        if (Objects.equals((Object)this.key, (Object)key)) {
            return this;
        }
        return create(this.children, this.style, key, this.fallback, this.args);
    }
    
    @Deprecated
    @NotNull
    @Override
    public List<Component> args() {
        return ComponentLike.asComponents(this.args);
    }
    
    @NotNull
    @Override
    public List<TranslationArgument> arguments() {
        return this.args;
    }
    
    @NotNull
    @Override
    public TranslatableComponent arguments(@NotNull final ComponentLike... args) {
        return create(this.children, this.style, this.key, this.fallback, args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent arguments(@NotNull final List<? extends ComponentLike> args) {
        return create(this.children, this.style, this.key, this.fallback, args);
    }
    
    @Nullable
    @Override
    public String fallback() {
        return this.fallback;
    }
    
    @NotNull
    @Override
    public TranslatableComponent fallback(@Nullable final String fallback) {
        return create(this.children, this.style, this.key, fallback, this.args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.key, this.fallback, this.args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent style(@NotNull final Style style) {
        return create(this.children, style, this.key, this.fallback, this.args);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslatableComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final TranslatableComponent that = (TranslatableComponent)other;
        return Objects.equals((Object)this.key, (Object)that.key()) && Objects.equals((Object)this.fallback, (Object)that.fallback()) && Objects.equals((Object)this.args, (Object)that.arguments());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.key.hashCode();
        result = 31 * result + Objects.hashCode((Object)this.fallback);
        result = 31 * result + this.args.hashCode();
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
    
    static List<TranslationArgument> asArguments(@NotNull final List<? extends ComponentLike> likes) {
        if (likes.isEmpty()) {
            return (List<TranslationArgument>)Collections.emptyList();
        }
        final List<TranslationArgument> ret = (List<TranslationArgument>)new ArrayList(likes.size());
        for (int i = 0; i < likes.size(); ++i) {
            final ComponentLike like = (ComponentLike)likes.get(i);
            if (like == null) {
                throw new NullPointerException("likes[" + i + "]");
            }
            if (like instanceof TranslationArgument) {
                ret.add((Object)like);
            }
            else if (like instanceof TranslationArgumentLike) {
                ret.add((Object)Objects.requireNonNull((Object)((TranslationArgumentLike)like).asTranslationArgument(), "likes[" + i + "].asTranslationArgument()"));
            }
            else {
                ret.add((Object)TranslationArgument.component(like));
            }
        }
        return (List<TranslationArgument>)Collections.unmodifiableList((List)ret);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder
    {
        @Nullable
        private String key;
        @Nullable
        private String fallback;
        private List<TranslationArgument> args;
        
        BuilderImpl() {
            this.args = (List<TranslationArgument>)Collections.emptyList();
        }
        
        BuilderImpl(@NotNull final TranslatableComponent component) {
            super(component);
            this.args = (List<TranslationArgument>)Collections.emptyList();
            this.key = component.key();
            this.args = component.arguments();
            this.fallback = component.fallback();
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder key(@NotNull final String key) {
            this.key = key;
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder arguments(@NotNull final ComponentLike... args) {
            Objects.requireNonNull((Object)args, "args");
            if (args.length == 0) {
                return this.arguments((List<? extends ComponentLike>)Collections.emptyList());
            }
            return this.arguments((List<? extends ComponentLike>)Arrays.asList((Object[])args));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder arguments(@NotNull final List<? extends ComponentLike> args) {
            this.args = TranslatableComponentImpl.asArguments((List<? extends ComponentLike>)Objects.requireNonNull((Object)args, "args"));
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder fallback(@Nullable final String fallback) {
            this.fallback = fallback;
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent build() {
            if (this.key == null) {
                throw new IllegalStateException("key must be set");
            }
            return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
        }
    }
}
