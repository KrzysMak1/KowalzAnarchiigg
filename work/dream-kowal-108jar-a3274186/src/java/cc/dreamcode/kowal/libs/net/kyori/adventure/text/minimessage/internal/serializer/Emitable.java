package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Emitable
{
    void emit(@NotNull final TokenEmitter emitter);
}
