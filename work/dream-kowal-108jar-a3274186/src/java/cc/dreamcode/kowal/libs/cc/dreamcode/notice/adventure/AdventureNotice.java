package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure;

import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import net.md_5.bungee.api.ChatColor;
import java.util.Collection;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextReplacementConfig;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import java.util.stream.Collectors;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEventSource;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeType;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.regex.Pattern;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.Notice;

public class AdventureNotice<R extends Notice<R>> extends NoticeImpl<R>
{
    private static final char ALT_COLOR_CHAR = '&';
    private static final Pattern FIELD_PATTERN;
    private Component joiningComponent;
    private List<Component> component;
    
    public AdventureNotice(final NoticeType noticeType, final String... noticeText) {
        super(noticeType, noticeText);
        this.joiningComponent = null;
        this.component = null;
    }
    
    public R hoverEvent(@NonNull final HoverEventSource<?> source) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        this.joiningComponent = this.toJoiningComponent().hoverEvent(source);
        this.component = (List<Component>)this.toSplitComponents().stream().map(scan -> scan.hoverEvent((HoverEventSource<?>)source)).collect(Collectors.toList());
        return (R)this;
    }
    
    public R clickEvent(@NonNull final ClickEvent event) {
        if (event == null) {
            throw new NullPointerException("event is marked non-null but is null");
        }
        this.joiningComponent = this.toJoiningComponent().clickEvent(event);
        this.component = (List<Component>)this.toSplitComponents().stream().map(scan -> scan.clickEvent(event)).collect(Collectors.toList());
        return (R)this;
    }
    
    public TextReplacementConfig getPlaceholderConfig() {
        if (!this.getPlaceholderContext().isPresent()) {
            return null;
        }
        final PlaceholderContext placeholderContext = (PlaceholderContext)this.getPlaceholderContext().get();
        final Map<String, String> renderedFields = (Map<String, String>)placeholderContext.renderFields().entrySet().stream().collect(Collectors.toMap(entry -> ((MessageField)entry.getKey()).getRaw(), Map.Entry::getValue));
        return TextReplacementConfig.builder().match(AdventureNotice.FIELD_PATTERN).replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> {
            final String fieldValue = ChatColor.translateAlternateColorCodes('&', (String)renderedFields.get((Object)result.group(1)));
            return AdventureLegacy.component(fieldValue);
        })).build();
    }
    
    public List<Component> toSplitComponents() {
        if (this.component == null) {
            this.component = AdventureLegacy.splitDeserialize(this.getRaw(), this.getPlaceholderConfig());
        }
        return this.component;
    }
    
    public Component toJoiningComponent() {
        if (this.joiningComponent == null) {
            this.joiningComponent = AdventureLegacy.joiningDeserialize(this.getRaw(), this.getPlaceholderConfig());
        }
        return this.joiningComponent;
    }
    
    public List<Component> toComponents() {
        return new ListBuilder<Component>().add(this.toJoiningComponent()).addAll((java.util.Collection<? extends Component>)this.toSplitComponents()).build();
    }
    
    @Override
    public R clearRender() {
        final R respond = super.clearRender();
        this.component = null;
        this.joiningComponent = null;
        return respond;
    }
    
    static {
        FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");
    }
}
