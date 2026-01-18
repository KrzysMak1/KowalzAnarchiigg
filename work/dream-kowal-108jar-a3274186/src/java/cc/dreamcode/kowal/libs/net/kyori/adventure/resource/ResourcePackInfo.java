package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Contract;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import java.net.URI;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface ResourcePackInfo extends Examinable, ResourcePackInfoLike
{
    @NotNull
    default ResourcePackInfo resourcePackInfo(@NotNull final UUID id, @NotNull final URI uri, @NotNull final String hash) {
        return new ResourcePackInfoImpl(id, uri, hash);
    }
    
    @NotNull
    default Builder resourcePackInfo() {
        return new ResourcePackInfoImpl.BuilderImpl();
    }
    
    @NotNull
    UUID id();
    
    @NotNull
    URI uri();
    
    @NotNull
    String hash();
    
    @NotNull
    default ResourcePackInfo asResourcePackInfo() {
        return this;
    }
    
    public interface Builder extends AbstractBuilder<ResourcePackInfo>, ResourcePackInfoLike
    {
        @Contract("_ -> this")
        @NotNull
        Builder id(@NotNull final UUID id);
        
        @Contract("_ -> this")
        @NotNull
        Builder uri(@NotNull final URI uri);
        
        @Contract("_ -> this")
        @NotNull
        Builder hash(@NotNull final String hash);
        
        @NotNull
        ResourcePackInfo build();
        
        @NotNull
        default CompletableFuture<ResourcePackInfo> computeHashAndBuild() {
            return this.computeHashAndBuild((Executor)ForkJoinPool.commonPool());
        }
        
        @NotNull
        CompletableFuture<ResourcePackInfo> computeHashAndBuild(@NotNull final Executor executor);
        
        @NotNull
        default ResourcePackInfo asResourcePackInfo() {
            return this.build();
        }
    }
}
