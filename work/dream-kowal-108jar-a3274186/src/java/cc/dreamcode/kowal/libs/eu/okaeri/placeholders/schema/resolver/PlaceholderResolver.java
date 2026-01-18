package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver;

import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageFieldAccessor;
import lombok.NonNull;

public interface PlaceholderResolver<T>
{
    Object resolve(@NonNull final T from, @NonNull final MessageFieldAccessor field, @Nullable final PlaceholderContext context);
}
