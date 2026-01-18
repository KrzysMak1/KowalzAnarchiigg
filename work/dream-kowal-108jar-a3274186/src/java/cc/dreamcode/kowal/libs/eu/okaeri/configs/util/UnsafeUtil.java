package cc.dreamcode.kowal.libs.eu.okaeri.configs.util;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import lombok.NonNull;

@Deprecated
public final class UnsafeUtil
{
    public static <T> T allocateInstance(@NonNull final Class<T> clazz) throws OkaeriException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        T instance;
        try {
            instance = clazz.newInstance();
        }
        catch (final InstantiationException | IllegalAccessException exception) {
            try {
                instance = (T)allocateInstanceUnsafe(clazz);
            }
            catch (final Exception exception2) {
                throw new OkaeriException("failed to create " + (Object)clazz + " instance, neither default constructor available, nor unsafe succeeded");
            }
        }
        return instance;
    }
    
    private static Object allocateInstanceUnsafe(@NonNull final Class<?> clazz) throws Exception {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final Class<?> unsafeClazz = Class.forName("sun.misc.Unsafe");
        final Field theUnsafeField = unsafeClazz.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        final Object unsafeInstance = theUnsafeField.get((Object)null);
        final Method allocateInstance = unsafeClazz.getDeclaredMethod("allocateInstance", Class.class);
        return allocateInstance.invoke(unsafeInstance, new Object[] { clazz });
    }
}
