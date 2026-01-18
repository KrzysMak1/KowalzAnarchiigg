package cc.dreamcode.kowal.libs.net.kyori.adventure.audience;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackInfo;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackRequestLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackRequest;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackInfoLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.inventory.Book;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.SoundStop;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import cc.dreamcode.kowal.libs.net.kyori.adventure.title.TitlePart;
import cc.dreamcode.kowal.libs.net.kyori.adventure.title.Title;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.SignedMessage;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.ChatType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identified;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;

public interface Audience extends Pointered
{
    @NotNull
    default Audience empty() {
        return EmptyAudience.INSTANCE;
    }
    
    @NotNull
    default Audience audience(@NotNull final Audience... audiences) {
        final int length = audiences.length;
        if (length == 0) {
            return empty();
        }
        if (length == 1) {
            return audiences[0];
        }
        return audience(Arrays.asList(audiences));
    }
    
    @NotNull
    default ForwardingAudience audience(@NotNull final Iterable<? extends Audience> audiences) {
        return () -> audiences;
    }
    
    @NotNull
    default Collector<? super Audience, ?, ForwardingAudience> toAudience() {
        return Audiences.COLLECTOR;
    }
    
    @NotNull
    default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        return filter.test(this) ? this : empty();
    }
    
    default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
        action.accept(this);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final ComponentLike message) {
        this.sendMessage(message.asComponent());
    }
    
    default void sendMessage(@NotNull final Component message) {
        this.sendMessage(message, MessageType.SYSTEM);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(message.asComponent(), type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Component message, @NotNull final MessageType type) {
        this.sendMessage(Identity.nil(), message, type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message) {
        this.sendMessage(source, message, MessageType.CHAT);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message) {
        this.sendMessage(source, message, MessageType.CHAT);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(source, message.asComponent(), type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(source, message.asComponent(), type);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
        this.sendMessage(source.identity(), message, type);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
    }
    
    default void sendMessage(@NotNull final Component message, final ChatType.Bound boundChatType) {
        this.sendMessage(message, MessageType.CHAT);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final ComponentLike message, final ChatType.Bound boundChatType) {
        this.sendMessage(message.asComponent(), boundChatType);
    }
    
    default void sendMessage(@NotNull final SignedMessage signedMessage, final ChatType.Bound boundChatType) {
        final Component content = (signedMessage.unsignedContent() != null) ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
        if (signedMessage.isSystem()) {
            this.sendMessage(content);
        }
        else {
            this.sendMessage(signedMessage.identity(), content, MessageType.CHAT);
        }
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void deleteMessage(@NotNull final SignedMessage signedMessage) {
        if (signedMessage.canDelete()) {
            this.deleteMessage((SignedMessage.Signature)Objects.requireNonNull((Object)signedMessage.signature()));
        }
    }
    
    default void deleteMessage(final SignedMessage.Signature signature) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendActionBar(@NotNull final ComponentLike message) {
        this.sendActionBar(message.asComponent());
    }
    
    default void sendActionBar(@NotNull final Component message) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListHeader(@NotNull final ComponentLike header) {
        this.sendPlayerListHeader(header.asComponent());
    }
    
    default void sendPlayerListHeader(@NotNull final Component header) {
        this.sendPlayerListHeaderAndFooter(header, Component.empty());
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListFooter(@NotNull final ComponentLike footer) {
        this.sendPlayerListFooter(footer.asComponent());
    }
    
    default void sendPlayerListFooter(@NotNull final Component footer) {
        this.sendPlayerListHeaderAndFooter(Component.empty(), footer);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListHeaderAndFooter(@NotNull final ComponentLike header, @NotNull final ComponentLike footer) {
        this.sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
    }
    
    default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void showTitle(@NotNull final Title title) {
        final Title.Times times = title.times();
        if (times != null) {
            this.sendTitlePart(TitlePart.TIMES, times);
        }
        this.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
        this.sendTitlePart(TitlePart.TITLE, title.title());
    }
    
    default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
    }
    
    default void clearTitle() {
    }
    
    default void resetTitle() {
    }
    
    default void showBossBar(@NotNull final BossBar bar) {
    }
    
    default void hideBossBar(@NotNull final BossBar bar) {
    }
    
    default void playSound(@NotNull final Sound sound) {
    }
    
    default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
    }
    
    default void playSound(@NotNull final Sound sound, final Sound.Emitter emitter) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void stopSound(@NotNull final Sound sound) {
        this.stopSound(((Sound)Objects.requireNonNull((Object)sound, "sound")).asStop());
    }
    
    default void stopSound(@NotNull final SoundStop stop) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void openBook(final Book.Builder book) {
        this.openBook(book.build());
    }
    
    default void openBook(@NotNull final Book book) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendResourcePacks(@NotNull final ResourcePackInfoLike first, @NotNull final ResourcePackInfoLike... others) {
        this.sendResourcePacks(ResourcePackRequest.addingRequest(first, others));
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendResourcePacks(@NotNull final ResourcePackRequestLike request) {
        this.sendResourcePacks(request.asResourcePackRequest());
    }
    
    default void sendResourcePacks(@NotNull final ResourcePackRequest request) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void removeResourcePacks(@NotNull final ResourcePackRequestLike request) {
        this.removeResourcePacks(request.asResourcePackRequest());
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void removeResourcePacks(@NotNull final ResourcePackRequest request) {
        final List<ResourcePackInfo> infos = request.packs();
        if (infos.size() == 1) {
            this.removeResourcePacks(((ResourcePackInfo)infos.get(0)).id(), new UUID[0]);
        }
        else if (infos.isEmpty()) {
            return;
        }
        final UUID[] otherReqs = new UUID[infos.size() - 1];
        for (int i = 0; i < otherReqs.length; ++i) {
            otherReqs[i] = ((ResourcePackInfo)infos.get(i + 1)).id();
        }
        this.removeResourcePacks(((ResourcePackInfo)infos.get(0)).id(), otherReqs);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void removeResourcePacks(@NotNull final ResourcePackInfoLike request, @NotNull final ResourcePackInfoLike... others) {
        final UUID[] otherReqs = new UUID[others.length];
        for (int i = 0; i < others.length; ++i) {
            otherReqs[i] = others[i].asResourcePackInfo().id();
        }
        this.removeResourcePacks(request.asResourcePackInfo().id(), otherReqs);
    }
    
    default void removeResourcePacks(@NotNull final Iterable<UUID> ids) {
        final Iterator<UUID> it = (Iterator<UUID>)ids.iterator();
        if (!it.hasNext()) {
            return;
        }
        final UUID id = (UUID)it.next();
        UUID[] others;
        if (!it.hasNext()) {
            others = new UUID[0];
        }
        else if (ids instanceof Collection) {
            others = new UUID[((Collection)ids).size() - 1];
            for (int i = 0; i < others.length; ++i) {
                others[i] = (UUID)it.next();
            }
        }
        else {
            final List<UUID> othersList = (List<UUID>)new ArrayList();
            while (it.hasNext()) {
                othersList.add((Object)it.next());
            }
            others = (UUID[])othersList.toArray((Object[])new UUID[0]);
        }
        this.removeResourcePacks(id, others);
    }
    
    default void removeResourcePacks(@NotNull final UUID id, @NotNull final UUID... others) {
    }
    
    default void clearResourcePacks() {
    }
}
