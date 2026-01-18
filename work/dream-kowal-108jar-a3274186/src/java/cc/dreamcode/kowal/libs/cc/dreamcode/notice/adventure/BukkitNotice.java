package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.kowal.libs.net.kyori.adventure.title.Title;
import java.time.Duration;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit.BukkitAudiences;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import java.util.Objects;
import java.util.Collection;
import java.util.Map;
import org.bukkit.command.CommandSender;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeType;

public class BukkitNotice extends AdventureNotice<BukkitNotice> implements BukkitSender
{
    public BukkitNotice(@NonNull final NoticeType noticeType, @NonNull final String... noticeText) {
        super(noticeType, noticeText);
        if (noticeType == null) {
            throw new NullPointerException("noticeType is marked non-null but is null");
        }
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
    }
    
    public static BukkitNotice of(@NonNull final NoticeType noticeType, @NonNull final String... noticeText) {
        if (noticeType == null) {
            throw new NullPointerException("noticeType is marked non-null but is null");
        }
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(noticeType, noticeText);
    }
    
    public static BukkitNotice chat(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.CHAT, noticeText);
    }
    
    public static BukkitNotice actionBar(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.ACTION_BAR, noticeText);
    }
    
    public static BukkitNotice title(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.TITLE, noticeText);
    }
    
    public static BukkitNotice subtitle(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.SUBTITLE, noticeText);
    }
    
    public static BukkitNotice titleSubtitle(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }
    
    @Override
    public void send(@NonNull final CommandSender target) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.wrapAndSend(target);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final CommandSender target, @NonNull final Map<String, Object> mapReplacer) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer).wrapAndSend(target);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final Collection<CommandSender> targets) {
        if (targets == null) {
            throw new NullPointerException("targets is marked non-null but is null");
        }
        targets.forEach(this::wrapAndSend);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final Collection<CommandSender> targets, @NonNull final Map<String, Object> mapReplacer) {
        if (targets == null) {
            throw new NullPointerException("targets is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        final BukkitNotice bukkitNotice;
        final BukkitNotice notice = bukkitNotice = this.with(mapReplacer);
        Objects.requireNonNull((Object)bukkitNotice);
        targets.forEach(bukkitNotice::wrapAndSend);
        notice.clearRender();
    }
    
    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }
    
    @Override
    public void sendAll(@NonNull final Map<String, Object> mapReplacer) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        final BukkitNotice notice = this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(notice::wrapAndSend);
        notice.clearRender();
    }
    
    @Override
    public void sendBroadcast() {
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.wrapAndSend((CommandSender)Bukkit.getConsoleSender());
        this.clearRender();
    }
    
    @Override
    public void sendBroadcast(@NonNull final Map<String, Object> mapReplacer) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        final BukkitNotice notice = this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(notice::wrapAndSend);
        notice.wrapAndSend((CommandSender)Bukkit.getConsoleSender());
        notice.clearRender();
    }
    
    @Override
    public void sendPermitted(@NonNull final String permission) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission(permission)).forEach(this::wrapAndSend);
        this.clearRender();
    }
    
    @Override
    public void sendPermitted(@NonNull final String permission, @NonNull final Map<String, Object> mapReplacer) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        final BukkitNotice notice = this.with(mapReplacer);
        final Stream<CommandSender> filter = Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission(permission)).map(target -> (CommandSender)target);
        filter.forEach(notice::wrapAndSend);
        notice.clearRender();
    }
    
    private void wrapAndSend(@NonNull final CommandSender target) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        final BukkitAudiences bukkitAudiences = BukkitNoticeProvider.getInstance().getBukkitAudiences();
        this.sendFormatted(target, bukkitAudiences.sender(target));
    }
    
    private void sendFormatted(@NonNull final CommandSender sender, @NonNull final Audience target) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (!(sender instanceof Player)) {
            final List<Component> splitComponents = this.toSplitComponents();
            Objects.requireNonNull((Object)target);
            splitComponents.forEach(target::sendMessage);
            return;
        }
        final NoticeType noticeType = (NoticeType)this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                final List<Component> splitComponents2 = this.toSplitComponents();
                Objects.requireNonNull((Object)target);
                splitComponents2.forEach(target::sendMessage);
                break;
            }
            case ACTION_BAR: {
                target.sendActionBar(this.toJoiningComponent());
                break;
            }
            case TITLE: {
                final Component component = this.toJoiningComponent();
                final Component emptyComponent = AdventureLegacy.deserialize(" ");
                final Title titleBuilder = Title.title(component, emptyComponent, Title.Times.times(Duration.ofMillis(this.getTitleFadeIn() * 50L), Duration.ofMillis(this.getTitleStay() * 50L), Duration.ofMillis(this.getTitleFadeOut() * 50L)));
                target.showTitle(titleBuilder);
                break;
            }
            case SUBTITLE: {
                final Component component = AdventureLegacy.deserialize(" ");
                final Component emptyComponent = this.toJoiningComponent();
                final Title titleBuilder = Title.title(component, emptyComponent, Title.Times.times(Duration.ofMillis(this.getTitleFadeIn() * 50L), Duration.ofMillis(this.getTitleStay() * 50L), Duration.ofMillis(this.getTitleFadeOut() * 50L)));
                target.showTitle(titleBuilder);
                break;
            }
            case TITLE_SUBTITLE: {
                final String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + NoticeImpl.lineSeparator() + ") to separate two messages.");
                }
                final Component title = AdventureLegacy.deserialize(split[0]);
                final Component subTitle = AdventureLegacy.deserialize(split[1]);
                final Title titleBuilder2 = Title.title(title, subTitle, Title.Times.times(Duration.ofMillis(this.getTitleFadeIn() * 50L), Duration.ofMillis(this.getTitleStay() * 50L), Duration.ofMillis(this.getTitleFadeOut() * 50L)));
                target.showTitle(titleBuilder2);
                break;
            }
            default: {
                this.clearRender();
                throw new RuntimeException("Cannot resolve notice-type. (" + (Object)this.getNoticeType() + ")");
            }
        }
    }
}
