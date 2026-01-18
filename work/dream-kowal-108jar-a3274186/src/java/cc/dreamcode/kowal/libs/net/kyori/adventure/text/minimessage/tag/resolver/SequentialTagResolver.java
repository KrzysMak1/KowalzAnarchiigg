package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Arrays;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;

final class SequentialTagResolver implements TagResolver, SerializableResolver
{
    final TagResolver[] resolvers;
    
    SequentialTagResolver(@NotNull final TagResolver[] resolvers) {
        this.resolvers = resolvers;
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
        ParsingException thrown = null;
        for (final TagResolver resolver : this.resolvers) {
            try {
                final Tag placeholder = resolver.resolve(name, arguments, ctx);
                if (placeholder != null) {
                    return placeholder;
                }
            }
            catch (final ParsingException ex) {
                arguments.reset();
                if (thrown == null) {
                    thrown = ex;
                }
                else {
                    thrown.addSuppressed((Throwable)ex);
                }
            }
            catch (final Exception ex2) {
                arguments.reset();
                final ParsingException err = ctx.newException("Exception thrown while parsing <" + name + ">", (Throwable)ex2, arguments);
                if (thrown == null) {
                    thrown = err;
                }
                else {
                    thrown.addSuppressed((Throwable)err);
                }
            }
        }
        if (thrown != null) {
            throw thrown;
        }
        return null;
    }
    
    @Override
    public boolean has(@NotNull final String name) {
        for (final TagResolver resolver : this.resolvers) {
            if (resolver.has(name)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
        for (final TagResolver resolver : this.resolvers) {
            if (resolver instanceof SerializableResolver) {
                ((SerializableResolver)resolver).handle(serializable, consumer);
            }
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SequentialTagResolver)) {
            return false;
        }
        final SequentialTagResolver that = (SequentialTagResolver)other;
        return Arrays.equals((Object[])this.resolvers, (Object[])that.resolvers);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode((Object[])this.resolvers);
    }
}
