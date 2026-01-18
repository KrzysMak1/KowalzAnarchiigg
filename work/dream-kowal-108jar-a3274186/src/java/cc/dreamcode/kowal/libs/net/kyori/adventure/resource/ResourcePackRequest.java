package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import org.jetbrains.annotations.Contract;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface ResourcePackRequest extends Examinable, ResourcePackRequestLike
{
    @NotNull
    default ResourcePackRequest addingRequest(@NotNull final ResourcePackInfoLike first, @NotNull final ResourcePackInfoLike... others) {
        return resourcePackRequest().packs(first, others).replace(false).build();
    }
    
    @NotNull
    default Builder resourcePackRequest() {
        return new ResourcePackRequestImpl.BuilderImpl();
    }
    
    @NotNull
    default Builder resourcePackRequest(@NotNull final ResourcePackRequest existing) {
        return new ResourcePackRequestImpl.BuilderImpl((ResourcePackRequest)Objects.requireNonNull((Object)existing, "existing"));
    }
    
    @NotNull
    List<ResourcePackInfo> packs();
    
    @NotNull
    ResourcePackRequest packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs);
    
    @NotNull
    ResourcePackCallback callback();
    
    @NotNull
    ResourcePackRequest callback(@NotNull final ResourcePackCallback cb);
    
    boolean replace();
    
    @NotNull
    ResourcePackRequest replace(final boolean replace);
    
    boolean required();
    
    @Nullable
    Component prompt();
    
    @NotNull
    default ResourcePackRequest asResourcePackRequest() {
        return this;
    }
    
    public interface Builder extends AbstractBuilder<ResourcePackRequest>, ResourcePackRequestLike
    {
        @Contract("_, _ -> this")
        @NotNull
        Builder packs(@NotNull final ResourcePackInfoLike first, @NotNull final ResourcePackInfoLike... others);
        
        @Contract("_ -> this")
        @NotNull
        Builder packs(@NotNull final Iterable<? extends ResourcePackInfoLike> packs);
        
        @Contract("_ -> this")
        @NotNull
        Builder callback(@NotNull final ResourcePackCallback cb);
        
        @Contract("_ -> this")
        @NotNull
        Builder replace(final boolean replace);
        
        @Contract("_ -> this")
        @NotNull
        Builder required(final boolean required);
        
        @Contract("_ -> this")
        @NotNull
        Builder prompt(@Nullable final Component prompt);
        
        @NotNull
        default ResourcePackRequest asResourcePackRequest() {
            return this.build();
        }
    }
}
