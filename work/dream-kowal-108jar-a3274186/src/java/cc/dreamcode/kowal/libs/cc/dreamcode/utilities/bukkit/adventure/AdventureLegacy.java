package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.adventure;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import java.util.function.UnaryOperator;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextReplacementConfig;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.MiniMessage;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.regex.Pattern;

public final class AdventureLegacy
{
    private static final Pattern FIELD_PATTERN;
    private static final LegacyComponentSerializer SECTION_SERIALIZER;
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER;
    private static final MiniMessage MINI_MESSAGE;
    
    public static Component component(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return AdventureLegacy.AMPERSAND_SERIALIZER.deserialize(text);
    }
    
    public static Component deserialize(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return deserialize(text, null);
    }
    
    public static Component deserialize(@NonNull final String text, final TextReplacementConfig textReplacementConfig) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        Component component = ((ComponentSerializer<I, Component, String>)AdventureLegacy.MINI_MESSAGE).deserialize(text);
        if (textReplacementConfig != null) {
            component = component.replaceText(textReplacementConfig);
        }
        return component;
    }
    
    public static TextReplacementConfig getPlaceholderConfig(@NonNull final PlaceholderContext placeholderContext) {
        if (placeholderContext == null) {
            throw new NullPointerException("placeholderContext is marked non-null but is null");
        }
        final Map<String, String> renderedFields = (Map<String, String>)placeholderContext.renderFields().entrySet().stream().collect(Collectors.toMap(entry -> ((MessageField)entry.getKey()).getRaw(), Map.Entry::getValue));
        return TextReplacementConfig.builder().match(AdventureLegacy.FIELD_PATTERN).replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> {
            final String fieldValue = StringColorUtil.legacyFixColor((String)renderedFields.get((Object)result.group(1)));
            return component(fieldValue);
        })).build();
    }
    
    public static String serialize(@NonNull final Component component) {
        if (component == null) {
            throw new NullPointerException("component is marked non-null but is null");
        }
        return AdventureLegacy.SECTION_SERIALIZER.serialize(component);
    }
    
    static {
        FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");
        SECTION_SERIALIZER = LegacyComponentSerializer.legacySection();
        AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder().character('&').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
        MINI_MESSAGE = MiniMessage.builder().postProcessor((UnaryOperator<Component>)new AdventureLegacyColor()).build();
    }
}
