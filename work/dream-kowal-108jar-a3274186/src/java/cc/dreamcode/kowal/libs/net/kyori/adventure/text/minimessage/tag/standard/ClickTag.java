package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import java.util.function.BiConsumer;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import java.util.function.Supplier;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ClickTag
{
    private static final String CLICK = "click";
    static final TagResolver RESOLVER;
    
    private ClickTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String actionName = args.popOr((Supplier<String>)(() -> "A click tag requires an action of one of " + (Object)ClickEvent.Action.NAMES.keys())).lowerValue();
        final ClickEvent.Action action = ClickEvent.Action.NAMES.value(actionName);
        if (action == null) {
            throw ctx.newException("Unknown click event action '" + actionName + "'", args);
        }
        final String value = args.popOr("Click event actions require a value").value();
        return Tag.styling(ClickEvent.clickEvent(action, value));
    }
    
    static {
        RESOLVER = SerializableResolver.claimingStyle("click", (BiFunction<ArgumentQueue, Context, Tag>)ClickTag::create, StyleClaim.claim("click", (java.util.function.Function<Style, Object>)Style::clickEvent, (java.util.function.BiConsumer<Object, TokenEmitter>)((event, emitter) -> emitter.tag("click").argument(ClickEvent.Action.NAMES.key(event.action())).argument(event.value(), QuotingOverride.QUOTED))));
    }
}
