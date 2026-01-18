package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.adventure;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextReplacementConfig;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.UnaryOperator;

public class AdventureLegacyColor implements UnaryOperator<Component>
{
    public Component apply(final Component component) {
        return component.replaceText((Consumer<TextReplacementConfig.Builder>)(builder -> builder.match(Pattern.compile(".*")).replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((matchResult, builder1) -> AdventureLegacy.component(matchResult.group())))));
    }
}
