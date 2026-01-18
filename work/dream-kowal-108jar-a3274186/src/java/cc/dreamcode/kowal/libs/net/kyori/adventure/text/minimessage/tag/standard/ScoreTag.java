package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ScoreComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ScoreTag
{
    public static final String SCORE = "score";
    static final TagResolver RESOLVER;
    
    private ScoreTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String name = args.popOr("A scoreboard member name is required").value();
        final String objective = args.popOr("An objective name is required").value();
        return Tag.inserting(Component.score(name, objective));
    }
    
    @Nullable
    static Emitable emit(final Component component) {
        if (!(component instanceof ScoreComponent)) {
            return null;
        }
        final ScoreComponent score = (ScoreComponent)component;
        return emit -> emit.tag("score").argument(score.name()).argument(score.objective());
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent("score", (BiFunction<ArgumentQueue, Context, Tag>)ScoreTag::create, (Function<Component, Emitable>)ScoreTag::emit);
    }
}
