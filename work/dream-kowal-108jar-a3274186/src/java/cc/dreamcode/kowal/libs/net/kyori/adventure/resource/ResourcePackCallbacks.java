package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import java.util.UUID;

final class ResourcePackCallbacks
{
    static final ResourcePackCallback NO_OP;
    
    private ResourcePackCallbacks() {
    }
    
    static {
        NO_OP = ((uuid, status, audience) -> {});
    }
}
