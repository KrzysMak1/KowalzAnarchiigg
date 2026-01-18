package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.util.Optional;

public final class ClassUtil
{
    public static boolean hasClass(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (final ClassNotFoundException e) {
            return false;
        }
    }
    
    public static Optional<Class<?>> getClass(final String className) {
        try {
            return (Optional<Class<?>>)Optional.of((Object)Class.forName(className));
        }
        catch (final ClassNotFoundException e) {
            return (Optional<Class<?>>)Optional.empty();
        }
    }
    
    @Generated
    private ClassUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
