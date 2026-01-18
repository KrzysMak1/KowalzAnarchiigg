package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.stream.Collector;
import java.util.function.Predicate;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Function;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import java.util.Set;
import java.util.AbstractMap;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.function.BiConsumer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import java.util.Map;

final class DecorationTag
{
    private static final String B = "b";
    private static final String I = "i";
    private static final String EM = "em";
    private static final String OBF = "obf";
    private static final String ST = "st";
    private static final String U = "u";
    public static final String REVERT = "!";
    static final Map<TextDecoration, TagResolver> RESOLVERS;
    static final TagResolver RESOLVER;
    
    static Map.Entry<TextDecoration, Stream<TagResolver>> resolvers(final TextDecoration decoration, @Nullable final String shortName, @NotNull final String... secondaryAliases) {
        final String canonicalName = TextDecoration.NAMES.key(decoration);
        final Set<String> names = (Set<String>)new HashSet();
        names.add((Object)canonicalName);
        if (shortName != null) {
            names.add((Object)shortName);
        }
        Collections.addAll((Collection)names, (Object[])secondaryAliases);
        return (Map.Entry<TextDecoration, Stream<TagResolver>>)new AbstractMap.SimpleImmutableEntry((Object)decoration, (Object)Stream.concat(Stream.of((Object)SerializableResolver.claimingStyle(names, (BiFunction<ArgumentQueue, Context, Tag>)((args, ctx) -> create(decoration, args, ctx)), claim(decoration, (BiConsumer<TextDecoration.State, TokenEmitter>)((state, emitter) -> emit(canonicalName, (shortName == null) ? canonicalName : shortName, state, emitter))))), names.stream().map(name -> TagResolver.resolver("!" + name, createNegated(decoration)))));
    }
    
    private DecorationTag() {
    }
    
    static Tag create(final TextDecoration toApply, final ArgumentQueue args, final Context ctx) {
        final boolean flag = !args.hasNext() || !args.pop().isFalse();
        return Tag.styling(toApply.withState(flag));
    }
    
    static Tag createNegated(final TextDecoration toApply) {
        return Tag.styling(toApply.withState(false));
    }
    
    @NotNull
    static StyleClaim<TextDecoration.State> claim(@NotNull final TextDecoration decoration, @NotNull final BiConsumer<TextDecoration.State, TokenEmitter> emitable) {
        Objects.requireNonNull((Object)decoration, "decoration");
        return StyleClaim.claim("decoration_" + (String)TextDecoration.NAMES.key(decoration), (java.util.function.Function<Style, TextDecoration.State>)(style -> style.decoration(decoration)), (java.util.function.Predicate<TextDecoration.State>)(state -> state != TextDecoration.State.NOT_SET), emitable);
    }
    
    static void emit(@NotNull final String longName, @NotNull final String shortName, final TextDecoration.State state, @NotNull final TokenEmitter emitter) {
        if (state == TextDecoration.State.FALSE) {
            emitter.tag("!" + longName);
        }
        else {
            emitter.tag(longName);
        }
    }
    
    static {
        RESOLVERS = (Map)Stream.of((Object[])new Map.Entry[] { resolvers(TextDecoration.OBFUSCATED, "obf", new String[0]), resolvers(TextDecoration.BOLD, "b", new String[0]), resolvers(TextDecoration.STRIKETHROUGH, "st", new String[0]), resolvers(TextDecoration.UNDERLINED, "u", new String[0]), resolvers(TextDecoration.ITALIC, "em", "i") }).collect(Collectors.toMap(Map.Entry::getKey, ent -> (TagResolver)((Stream)ent.getValue()).collect((Collector)TagResolver.toTagResolver()), (l, r) -> TagResolver.builder().resolver(l).resolver(r).build(), LinkedHashMap::new));
        RESOLVER = TagResolver.resolver((Iterable<? extends TagResolver>)DecorationTag.RESOLVERS.values());
    }
}
