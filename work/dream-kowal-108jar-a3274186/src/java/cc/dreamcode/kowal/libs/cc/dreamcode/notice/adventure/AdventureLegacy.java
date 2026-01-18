package cc.dreamcode.kowal.libs.cc.dreamcode.notice.adventure;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import java.util.function.UnaryOperator;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextReplacementConfig;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.MiniMessage;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class AdventureLegacy
{
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER;
    private static final MiniMessage MINI_MESSAGE;
    
    public static Component component(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return AdventureLegacy.AMPERSAND_SERIALIZER.deserialize(text);
    }
    
    public static List<Component> splitDeserialize(@NonNull final String rawText) {
        if (rawText == null) {
            throw new NullPointerException("rawText is marked non-null but is null");
        }
        return splitDeserialize(rawText, null);
    }
    
    public static List<Component> splitDeserialize(@NonNull final String rawText, final TextReplacementConfig textReplacementConfig) {
        if (rawText == null) {
            throw new NullPointerException("rawText is marked non-null but is null");
        }
        final ListBuilder<Component> listBuilder = new ListBuilder<Component>();
        final String[] split = rawText.split(NoticeImpl.lineSeparator());
        Arrays.stream((Object[])split).forEach(text -> listBuilder.add(deserialize(text, textReplacementConfig)));
        return listBuilder.build();
    }
    
    public static Component joiningDeserialize(@NonNull final String rawText) {
        if (rawText == null) {
            throw new NullPointerException("rawText is marked non-null but is null");
        }
        return joiningDeserialize(rawText, null);
    }
    
    public static Component joiningDeserialize(@NonNull final String rawText, final TextReplacementConfig textReplacementConfig) {
        if (rawText == null) {
            throw new NullPointerException("rawText is marked non-null but is null");
        }
        final String joiningText = StringUtil.join(rawText.split(NoticeImpl.lineSeparator()), " ");
        return deserialize(joiningText, textReplacementConfig);
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
    
    static {
        AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder().character('&').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
        MINI_MESSAGE = MiniMessage.builder().postProcessor((UnaryOperator<Component>)new AdventureLegacyColor()).build();
    }
}
