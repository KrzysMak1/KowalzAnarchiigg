package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;

interface MappableResolver
{
    boolean contributeToMap(@NotNull final Map<String, Tag> map);
}
