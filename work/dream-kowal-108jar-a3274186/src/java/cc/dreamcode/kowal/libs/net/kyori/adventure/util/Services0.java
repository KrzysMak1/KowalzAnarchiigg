package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import java.util.ServiceLoader;

final class Services0
{
    private Services0() {
    }
    
    static <S> ServiceLoader<S> loader(final Class<S> type) {
        return (ServiceLoader<S>)ServiceLoader.load((Class)type, type.getClassLoader());
    }
}
