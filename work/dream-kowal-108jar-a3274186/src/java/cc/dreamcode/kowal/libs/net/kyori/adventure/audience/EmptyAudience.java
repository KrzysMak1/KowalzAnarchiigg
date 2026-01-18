package cc.dreamcode.kowal.libs.net.kyori.adventure.audience;

import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackRequest;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackInfoLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.inventory.Book;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.SignedMessage;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.ChatType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identified;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointer;

final class EmptyAudience implements Audience
{
    static final EmptyAudience INSTANCE;
    
    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        return (Optional<T>)Optional.empty();
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    @Nullable
    @Override
    public <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
        return defaultValue;
    }
    
    @Override
    public <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
        return (T)defaultValue.get();
    }
    
    @NotNull
    @Override
    public Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        return this;
    }
    
    @Override
    public void forEachAudience(@NotNull final Consumer<? super Audience> action) {
    }
    
    @Override
    public void sendMessage(@NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendMessage(@NotNull final Component message) {
    }
    
    @Deprecated
    @Override
    public void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
    }
    
    @Deprecated
    @Override
    public void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
    }
    
    @Override
    public void sendMessage(@NotNull final Component message, final ChatType.Bound boundChatType) {
    }
    
    @Override
    public void sendMessage(@NotNull final SignedMessage signedMessage, final ChatType.Bound boundChatType) {
    }
    
    @Override
    public void deleteMessage(final SignedMessage.Signature signature) {
    }
    
    @Override
    public void sendActionBar(@NotNull final ComponentLike message) {
    }
    
    @Override
    public void sendPlayerListHeader(@NotNull final ComponentLike header) {
    }
    
    @Override
    public void sendPlayerListFooter(@NotNull final ComponentLike footer) {
    }
    
    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull final ComponentLike header, @NotNull final ComponentLike footer) {
    }
    
    @Override
    public void openBook(final Book.Builder book) {
    }
    
    @Override
    public void sendResourcePacks(@NotNull final ResourcePackInfoLike request, @NotNull final ResourcePackInfoLike... others) {
    }
    
    @Override
    public void removeResourcePacks(@NotNull final ResourcePackRequest request) {
    }
    
    @Override
    public void removeResourcePacks(@NotNull final ResourcePackInfoLike request, @NotNull final ResourcePackInfoLike... others) {
    }
    
    @Override
    public boolean equals(final Object that) {
        return this == that;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public String toString() {
        return "EmptyAudience";
    }
    
    static {
        INSTANCE = new EmptyAudience();
    }
}
