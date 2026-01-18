package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ArgumentQueue
{
    Tag.Argument pop();
    
    Tag.Argument popOr(@NotNull final String errorMessage);
    
    Tag.Argument popOr(@NotNull final Supplier<String> errorMessage);
    
    Tag.Argument peek();
    
    boolean hasNext();
    
    void reset();
}
