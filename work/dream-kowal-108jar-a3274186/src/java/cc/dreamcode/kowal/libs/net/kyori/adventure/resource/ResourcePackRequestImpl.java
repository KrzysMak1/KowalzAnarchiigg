package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.MonkeyBars;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.List;

final class ResourcePackRequestImpl implements ResourcePackRequest
{
    private final List<ResourcePackInfo> packs;
    private final ResourcePackCallback cb;
    private final boolean replace;
    private final boolean required;
    @Nullable
    private final Component prompt;
    
    ResourcePackRequestImpl(final List<ResourcePackInfo> packs, final ResourcePackCallback cb, final boolean replace, final boolean required, @Nullable final Component prompt) {
        this.packs = packs;
        this.cb = cb;
        this.replace = replace;
        this.required = required;
        this.prompt = prompt;
    }
    
    @NotNull
    @Override
    public List<ResourcePackInfo> packs() {
        return this.packs;
    }
    
    @NotNull
    @Override
    public ResourcePackRequest packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs) {
        if (this.packs.equals((Object)packs)) {
            return this;
        }
        return new ResourcePackRequestImpl(MonkeyBars.toUnmodifiableList((java.util.function.Function<Object, ResourcePackInfo>)ResourcePackInfoLike::asResourcePackInfo, packs), this.cb, this.replace, this.required, this.prompt);
    }
    
    @NotNull
    @Override
    public ResourcePackCallback callback() {
        return this.cb;
    }
    
    @NotNull
    @Override
    public ResourcePackRequest callback(@NotNull final ResourcePackCallback cb) {
        if (cb == this.cb) {
            return this;
        }
        return new ResourcePackRequestImpl(this.packs, (ResourcePackCallback)Objects.requireNonNull((Object)cb, "cb"), this.replace, this.required, this.prompt);
    }
    
    @Override
    public boolean replace() {
        return this.replace;
    }
    
    @Override
    public boolean required() {
        return this.required;
    }
    
    @Nullable
    @Override
    public Component prompt() {
        return this.prompt;
    }
    
    @NotNull
    @Override
    public ResourcePackRequest replace(final boolean replace) {
        if (replace == this.replace) {
            return this;
        }
        return new ResourcePackRequestImpl(this.packs, this.cb, replace, this.required, this.prompt);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final ResourcePackRequestImpl that = (ResourcePackRequestImpl)other;
        return this.replace == that.replace && Objects.equals((Object)this.packs, (Object)that.packs) && Objects.equals((Object)this.cb, (Object)that.cb) && this.required == that.required && Objects.equals((Object)this.prompt, (Object)that.prompt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.packs, this.cb, this.replace, this.required, this.prompt });
    }
    
    @NotNull
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("packs", this.packs), ExaminableProperty.of("callback", this.cb), ExaminableProperty.of("replace", this.replace), ExaminableProperty.of("required", this.required), ExaminableProperty.of("prompt", this.prompt) });
    }
    
    static final class BuilderImpl implements Builder
    {
        private List<ResourcePackInfo> packs;
        private ResourcePackCallback cb;
        private boolean replace;
        private boolean required;
        @Nullable
        private Component prompt;
        
        BuilderImpl() {
            this.packs = (List<ResourcePackInfo>)Collections.emptyList();
            this.cb = ResourcePackCallback.noOp();
            this.replace = false;
        }
        
        BuilderImpl(@NotNull final ResourcePackRequest req) {
            this.packs = req.packs();
            this.cb = req.callback();
            this.replace = req.replace();
            this.required = req.required();
            this.prompt = req.prompt();
        }
        
        @NotNull
        @Override
        public Builder packs(@NotNull final ResourcePackInfoLike first, @NotNull final ResourcePackInfoLike... others) {
            this.packs = MonkeyBars.nonEmptyArrayToList((java.util.function.Function<ResourcePackInfoLike, ResourcePackInfo>)ResourcePackInfoLike::asResourcePackInfo, first, others);
            return this;
        }
        
        @NotNull
        @Override
        public Builder packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs) {
            this.packs = MonkeyBars.toUnmodifiableList((java.util.function.Function<Object, ResourcePackInfo>)ResourcePackInfoLike::asResourcePackInfo, packs);
            return this;
        }
        
        @NotNull
        @Override
        public Builder callback(@NotNull final ResourcePackCallback cb) {
            this.cb = (ResourcePackCallback)Objects.requireNonNull((Object)cb, "cb");
            return this;
        }
        
        @NotNull
        @Override
        public Builder replace(final boolean replace) {
            this.replace = replace;
            return this;
        }
        
        @NotNull
        @Override
        public Builder required(final boolean required) {
            this.required = required;
            return this;
        }
        
        @NotNull
        @Override
        public Builder prompt(@Nullable final Component prompt) {
            this.prompt = prompt;
            return this;
        }
        
        @NotNull
        @Override
        public ResourcePackRequest build() {
            return new ResourcePackRequestImpl(this.packs, this.cb, this.replace, this.required, this.prompt);
        }
    }
}
