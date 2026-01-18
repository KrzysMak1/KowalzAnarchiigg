package cc.dreamcode.kowal.libs.cc.dreamcode.menu.utilities;

import lombok.Generated;

public final class MenuUtil
{
    public static int countSlot(final int x, final int z) {
        return (x - 1) * 9 + (z - 1);
    }
    
    @Generated
    private MenuUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
