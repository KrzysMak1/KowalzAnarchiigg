package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface TokenEmitter
{
    @NotNull
    TokenEmitter tag(@NotNull final String token);
    
    @NotNull
    TokenEmitter selfClosingTag(@NotNull final String token);
    
    @NotNull
    default TokenEmitter arguments(@NotNull final String... args) {
        for (final String arg : args) {
            this.argument(arg);
        }
        return this;
    }
    
    @NotNull
    TokenEmitter argument(@NotNull final String arg);
    
    @NotNull
    TokenEmitter argument(@NotNull final String arg, @NotNull final QuotingOverride quotingPreference);
    
    @NotNull
    TokenEmitter argument(@NotNull final Component arg);
    
    @NotNull
    TokenEmitter text(@NotNull final String text);
    
    @NotNull
    TokenEmitter pop();
}
