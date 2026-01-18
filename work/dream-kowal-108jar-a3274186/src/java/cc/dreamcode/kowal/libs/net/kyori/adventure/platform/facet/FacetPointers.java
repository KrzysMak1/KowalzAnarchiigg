package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FacetPointers
{
    private static final String NAMESPACE = "adventure_platform";
    public static final Pointer<String> SERVER;
    public static final Pointer<Key> WORLD;
    public static final Pointer<Type> TYPE;
    
    private FacetPointers() {
    }
    
    static {
        SERVER = Pointer.pointer(String.class, Key.key("adventure_platform", "server"));
        WORLD = Pointer.pointer(Key.class, Key.key("adventure_platform", "world"));
        TYPE = Pointer.pointer(Type.class, Key.key("adventure_platform", "type"));
    }
    
    public enum Type
    {
        PLAYER, 
        CONSOLE, 
        OTHER;
    }
}
